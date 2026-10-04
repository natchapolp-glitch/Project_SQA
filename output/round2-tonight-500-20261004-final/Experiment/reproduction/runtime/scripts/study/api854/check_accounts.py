"""Inspect local credential configuration without HTTP, keys in output, or writes."""
from __future__ import annotations

import argparse
import json
import os
from pathlib import Path
import re

DEFAULT_ALIASES = tuple(f'a{number:02d}' for number in range(1, 11))


def inspect_accounts(document, aliases=DEFAULT_ALIASES, environ=None):
    environment = os.environ if environ is None else environ
    expected = tuple(aliases)
    if (not expected
            or any(not isinstance(alias, str) or not re.fullmatch(r'a[0-9]{2,}', alias) for alias in expected)
            or len(set(expected)) != len(expected)):
        return {'state': 'blocked', 'errors': ['invalid_expected_aliases'], 'accounts': [],
                'authenticated': False, 'quota_verified': False, 'kku_requests': 0, 'queue_mutations': 0}
    errors, rows = [], {}
    if not isinstance(document, dict) or not isinstance(document.get('accounts'), list):
        errors.append('invalid_account_document')
    else:
        for row in document['accounts']:
            if (not isinstance(row, dict) or not isinstance(row.get('alias'), str)
                    or not re.fullmatch(r'a[0-9]{2,}', row['alias'])):
                errors.append('invalid_account_row')
                continue
            rows.setdefault(row['alias'], []).append(row)
    duplicates = sorted(alias for alias, entries in rows.items() if len(entries) > 1)
    if duplicates:
        errors.append('duplicate_account_aliases')
    accounts, credentials = [], {}
    placeholders = {'none', 'null', 'your_api_key', 'paste_key_here', 'changeme'}
    for alias in expected:
        key = environment.get(f'KKU_API_KEY_{alias.upper()}')
        source = 'environment' if key else 'file'
        entries = rows.get(alias, [])
        if not key:
            key = entries[0].get('api_key') if len(entries) == 1 else None
        if alias in duplicates:
            status = 'ambiguous_alias'
        elif key is None or key == '':
            status = 'missing_credential'
        elif (not isinstance(key, str) or not key.strip()
                or any(character.isspace() for character in key.strip())
                or key.strip().lower() in placeholders):
            status = 'invalid_credential_value'
        else:
            status = 'credential_configured'
            credentials[alias] = key.strip()
        accounts.append({'alias': alias, 'status': status,
                         'effective_source': source if status == 'credential_configured' else None})
    by_key = {}
    for alias, key in credentials.items():
        by_key.setdefault(key, []).append(alias)
    shared_keys = sorted(sorted(group) for group in by_key.values() if len(group) > 1)
    if shared_keys:
        errors.append('same_credential_configured_for_multiple_aliases')
    if any(account['status'] == 'invalid_credential_value' for account in accounts):
        errors.append('invalid_credential_values')
    configured = len(credentials)
    return {'schema_version': 1,
            'state': 'blocked' if errors else 'ready_for_authenticated_preflight' if configured == len(expected) else 'incomplete',
            'required_accounts': len(expected), 'configured_accounts': configured,
            'missing_or_invalid_accounts': len(expected) - configured,
            'accounts': accounts, 'duplicate_aliases': duplicates, 'shared_credential_alias_groups': shared_keys,
            'errors': sorted(set(errors)), 'authenticated': False, 'quota_verified': False,
            'distinct_quota_buckets_verified': False, 'pilot_enabled': False,
            'kku_requests': 0, 'queue_mutations': 0}


def main(argv=None):
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--secrets', type=Path, default=Path('.local/api854/accounts.json'))
    parser.add_argument('--accounts', nargs='+', default=DEFAULT_ALIASES)
    args = parser.parse_args(argv)
    try:
        document = json.loads(args.secrets.read_text(encoding='utf-8-sig'))
    except FileNotFoundError:
        document = {'accounts': []}
    except (OSError, UnicodeError, ValueError):
        # Parser errors or exception details can contain private file contents.
        print(json.dumps({'state': 'blocked', 'errors': ['account_file_unreadable_or_invalid_json'],
                          'authenticated': False, 'quota_verified': False, 'kku_requests': 0, 'queue_mutations': 0}))
        return 2
    report = inspect_accounts(document, args.accounts)
    print(json.dumps(report, ensure_ascii=True))
    return 0 if report['state'] == 'ready_for_authenticated_preflight' else 2


if __name__ == '__main__':
    raise SystemExit(main())
