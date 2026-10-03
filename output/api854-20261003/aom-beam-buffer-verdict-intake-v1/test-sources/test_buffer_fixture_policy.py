import unittest

from scripts.study.api854.common import ROOT, read_json
from scripts.study.api854.fixture_policy import BUFFER_SIGNATURES, POLICY_V5, POLICY_V6_BUFFER, select


class BufferFixturePolicyTests(unittest.TestCase):
    def test_twenty_bug_inventory_adds_exactly_eight_declared_signatures(self):
        root = ROOT / 'docs/api854/evidence/beam-aom-review-20261003'
        inventory = []
        for row in read_json(root / 'index.json')['bugs']:
            inventory.extend(read_json(root / 'declarations' / f"{row['project']}-{row['bug_id']}" / 'targets.json')['targets'])
        old, old_excluded = select(inventory, POLICY_V5)
        new, new_excluded = select(inventory, POLICY_V6_BUFFER)
        key = lambda target: (target['class'], target['method'], target['parameter_types'])
        self.assertEqual(len(inventory), 691)
        self.assertEqual((len(old), len(old_excluded)), (377, 314))
        self.assertEqual((len(new), len(new_excluded)), (385, 306))
        self.assertEqual({key(t) for t in new} - {key(t) for t in old}, BUFFER_SIGNATURES)
        self.assertTrue(all(t in new for t in old))
        self.assertTrue(all(row['reason'] for row in new_excluded))

    def test_empty_enum_and_constructor_domains_stay_pending(self):
        root = ROOT / 'docs/api854/evidence/beam-aom-review-20261003/declarations/JacksonXml-1/targets.json'
        inventory = read_json(root)['targets']
        new, excluded = select(inventory, POLICY_V6_BUFFER)
        enum_methods = {'configure', 'disable', 'enable', 'isEnabled'}
        empty_domain = [t for t in inventory if t['method'] in enum_methods
                        and 'FromXmlParser$Feature' in t['parameter_types']]
        self.assertEqual(len(empty_domain), 4)
        self.assertTrue(all(t not in new for t in empty_domain))
        self.assertTrue(all(t in [row['target'] for row in excluded] for t in empty_domain))
        self.assertTrue(all(t['method'] not in {'<init>', 'hashCode'} for t in new))
