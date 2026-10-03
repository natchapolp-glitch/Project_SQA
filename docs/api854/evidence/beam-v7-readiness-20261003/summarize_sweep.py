"""Retain a prioritized diagnostic worklist; never changes approved capability scope."""
import json
from collections import Counter
from pathlib import Path
import sys

ROOT = Path(__file__).resolve().parents[4]
sys.path.insert(0, str(ROOT))
from scripts.study.api854.common import sha256, write_json


def summarize():
    sweep = ROOT / 'docs/api854/evidence/beam-v7-fixed-sweep-20261003'
    index = json.loads((sweep / 'index.json').read_text(encoding='utf-8'))
    categories = Counter()
    rows, worklist = [], []
    for record in index['records']:
        counts = Counter()
        for case in record['cases']:
            first = case['first']
            if first['status'] != 'ok':
                category = first['status']
            elif not case['repeat_equal']:
                category = 'repeat_unstable'
            elif first.get('target_invoked') is not True:
                category = 'invocation_unverified'
            elif first.get('outcome', '').startswith('exception:'):
                category = 'target_exception_review_needed'
            else:
                category = 'stable_normal_observation_oracle_review_needed'
            counts[category] += 1
            categories[category] += 1
            if case['v7_capability'] == 'unsupported':
                worklist.append({'project': record['project'], 'bug_id': record['bug_id'],
                    'owner': record['owner'], 'target': case['target'], 'category': category,
                    'reason': first.get('reason'), 'outcome': first.get('outcome'),
                    'existing_exclusion': case['exclusion_reason'], 'oracle_approved': False})
        rows.append({'project': record['project'], 'bug_id': record['bug_id'],
            'status': record['status'], 'attempted_declarations': len(record['cases']),
            'categories': dict(counts), 'error': record.get('error')})
    total = sum(row['attempted_declarations'] for row in rows)
    result = {'primary': False, 'semantic_approved': False, 'capability_changes': 0,
        'required_common_declarations': 691, 'attempted_declarations': total,
        'complete_sweep': total == 691 and len(rows) == 20 and all(r['status'] == 'fixed_sweep_complete' for r in rows),
        'sweep_index_sha256': sha256(sweep / 'index.json'), 'summarizer_sha256': sha256(__file__),
        'category_counts': dict(categories), 'bugs': rows, 'unsupported_worklist': worklist,
        'real_kku_requests': 0, 'queue_mutations': 0,
        'limitations': ['Two observations at one midpoint vector do not cover the input domain.',
                       'Stable output does not establish a meaningful or correct oracle.',
                       'Diagnostic runs are not JUnit fixed-twice/buggy/coverage experiments.',
                       'Current aomsin host does not certify beam-pc1 acceptance.']}
    write_json(Path(__file__).parent / 'sweep-summary.json', result)
    priorities = Counter(r['category'] for r in worklist)
    lines = ['# บีม: ตรวจ v7 และ fixed-only diagnostic sweep', '',
        'ตรวจวันที่ 3 ต.ค. 2569 บน WSL ของ aomsin; เก็บงานใน branch `codex/beam-readiness` แยกจาก checkout เดิม', '',
        '## ผลที่ทำแล้ว', '',
        '- รวม Aom `c25faa5e` ซึ่งมี Beam `3ae6f2fb` และตรวจ runtime/preparation v7 ที่ได้รับ',
        '- ตรวจ source/recipe/prompt/targets partitions ครบ 20 bugs: selected 377 + unsupported 314 = 691',
        '- ตรวจ CPU/API consumers อ่าน inputs เดียวกัน; Gate A ยังคง false ตามข้อค้างจริง',
        f'- Fixed-only sweep ทดลอง probe {total}/691 declarations รายการละสองครั้งด้วย midpoint vector; ไม่ใช้ buggy outcomes เลือก scope',
        '- Fixture errors ไม่ยืนยัน target invocation; ไม่ถือว่าทุก declaration เข้า target สำเร็จ',
        '- ตรวจ fixed Java source bytes ของ checkout ใหม่กับ retained v7 ก่อน observations; เก็บ setup failures และ raw observations',
        '- ไม่เปลี่ยน capability policy, ไม่เพิ่ม usable count และไม่เปิด live API/queue', '',
        '## ผลวินิจฉัยรายกลุ่ม', '']
    for category, count in categories.items():
        lines.append(f'- `{category}`: {count} declarations')
    lines.extend(['', '## ลำดับงานต่อสำหรับ 314 unsupported targets', ''])
    for category, count in priorities.items():
        lines.append(f'- `{category}`: {count} รายการใน worklist เดิม')
    lines.extend(['',
        'เริ่มจาก stable normal observations เพื่อ review receiver/arguments/state oracle จาก fixed source ก่อนเพิ่ม recipe',
        'target exceptions ต้องแยก valid boundary จาก invalid preconditions; fixture_error ต้องสร้าง concrete object graph',
        'constructor/hashCode ต้องตรวจ structural/identity oracle โดยไม่ถือว่าผลซ้ำครั้งเดียวรับรองแล้ว', '',
        '## สิ่งที่ยังต้องตรวจร่วม', '',
        '- Requirement ครบ 691 declarations รวม 4 empty-enum targets ยังไม่ผ่าน',
        '- ข้อเสนอ enum ดู [BEAM_V7_ENUM_DECISION_TH.md](BEAM_V7_ENUM_DECISION_TH.md)',
        '- Meaningful oracle, final host acceptance, provider settings/limits/reserve/quota และ three-owner review',
        '- รอบนี้ไม่มี new primary results; diagnostics ไม่แทน JUnit/buggy/coverage evidence', '',
        '## หลักฐาน', '',
        '- [Readiness receipt](evidence/beam-v7-readiness-20261003/readiness.json)',
        '- [Gate A receipt](evidence/beam-v7-readiness-20261003/gate-a.json)',
        '- [Validation log](evidence/beam-v7-readiness-20261003/validation.log)',
        '- [Prioritized diagnostic worklist](evidence/beam-v7-readiness-20261003/sweep-summary.json)',
        '- [Raw fixed sweep](evidence/beam-v7-fixed-sweep-20261003/index.json)',
        '- [Sweep checksums](evidence/beam-v7-fixed-sweep-20261003/checksums.json)', ''])
    path = ROOT / 'docs/api854/BEAM_V7_READINESS_WORK_TH.md'
    with path.open('x', encoding='utf-8', newline='\n') as stream:
        stream.write('\n'.join(lines))
    print(json.dumps({'attempted': total, 'categories': dict(categories), 'unsupported_categories': dict(priorities)}))


if __name__ == '__main__':
    summarize()
