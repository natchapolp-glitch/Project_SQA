#!/usr/bin/env python3
"""Aggregate real Defects4J study evidence. Python 3.10+, standard library only."""
from __future__ import annotations

import argparse
import csv
import hashlib
import json
import math
import re
from collections import Counter, defaultdict
from datetime import datetime, timezone
from pathlib import Path
from statistics import mean, median

METHODS = ("cmaes", "fscs-art", "claude", "intellisphere")
IDENTITY = ("project", "bug_id", "generator", "seed", "budget")
FIELDS = ("run_id", *IDENTITY, "status", "test_count", "compile_status", "fault_detected",
          "line_covered", "line_total", "branch_covered", "branch_total", "generation_seconds", "duration_seconds", "total_seconds", "workflow_seconds",
          "artifact_path", "failed_stage", "error", "interrupted_reason", "source_sha256", "record_path",
          "record_sha256", "valid", "issues")
TEAM = [("นายธนินธร อันทรบุตร", "673380043-6"), ("นายศุภกร กรมรินทร์", "673380061-4"),
        ("นายณัชพล เพ็งพล", "673380267-4"), ("นายณัฐกรณ์ อินธิสาร", "673380268-2")]


def key(row):
    return tuple(str(row.get(k, "")) if row.get(k) is not None else "" for k in IDENTITY)


def number(value):
    return isinstance(value, (int, float)) and not isinstance(value, bool) and math.isfinite(value)


def relative(path, root):
    try:
        return path.resolve().relative_to(root.resolve()).as_posix()
    except ValueError:
        return str(path.resolve())


def normalize(raw, path, root):
    row = dict(raw)
    errors = []
    row["record_path"] = relative(path, root)
    row["record_sha256"] = hashlib.sha256(path.read_bytes()).hexdigest()
    workflow = row.get('workflow_seconds', row.get('total_seconds'))
    if row.get('superseded_environment_failure') and number(workflow):
        previous = json.loads((root / row['superseded_environment_failure']).read_text())
        if number(previous.get('duration_seconds')):
            workflow += previous['duration_seconds']
    row['workflow_seconds'] = workflow
    row["bug_id"] = str(row.get("bug_id", ""))
    for field in ("run_id", "project", "bug_id", "generator", "status"):
        if not isinstance(row.get(field), str) or not row[field].strip():
            errors.append(f"missing_or_invalid_{field}")
    if row.get("generator") not in METHODS:
        errors.append("unknown_generator")
    # These are counts, not rates. A missing total must not become zero.
    for field in ("test_count", "line_covered", "line_total", "branch_covered", "branch_total"):
        value = row.get(field)
        if value is not None and (not isinstance(value, int) or isinstance(value, bool) or value < 0):
            errors.append(f"invalid_{field}")
            row[field] = None
    for metric in ("line", "branch"):
        covered, total = row.get(metric + "_covered"), row.get(metric + "_total")
        if covered is not None and total is not None and covered > total:
            errors.append(f"{metric}_covered_exceeds_total")
            row[metric + "_covered"] = row[metric + "_total"] = None
    if row.get("fault_detected") is not None and not isinstance(row["fault_detected"], bool):
        errors.append("invalid_fault_detected_boolean")
        row["fault_detected"] = None
    for field in ('duration_seconds', 'generation_seconds', 'total_seconds', 'workflow_seconds'):
        if row.get(field) is not None and (not number(row[field]) or row[field] < 0):
            errors.append('invalid_' + field)
            row[field] = None
    compile_value = row.get("compile_status")
    if isinstance(compile_value, bool):
        row["compile_status"] = "passed" if compile_value else "failed"
    elif compile_value in ("pass", "success", "ok", "compiled"):
        row["compile_status"] = "passed"
    elif compile_value in ("fail", "error", "compile_failed"):
        row["compile_status"] = "failed"
    if row.get("fault_detected") is True and row.get("compile_status") != "passed":
        errors.append("fault_claim_without_compile_pass")
    row["valid"] = not errors
    row["issues"] = ";".join(errors)
    return row


def load_records(folder, root):
    rows, issues, seen = [], [], set()
    for path in sorted(folder.rglob("record.json")) if folder.exists() else []:
        try:
            raw = json.loads(path.read_text(encoding="utf-8-sig"))
            if not isinstance(raw, dict):
                raise ValueError("record must be an object")
            row = normalize(raw, path, root)
            rid = (row.get("run_id"), key(row))
            if rid in seen:
                row["valid"] = False
                row["issues"] = ";".join(filter(None, (row["issues"], "duplicate_run_identity")))
            seen.add(rid)
            rows.append(row)
            if row["issues"]:
                issues.append({"record_path": row["record_path"], "issue": row["issues"]})
        except (ValueError, OSError, TypeError) as exc:
            issues.append({"record_path": relative(path, root), "issue": str(exc)})
    return rows, issues


def completed(row):
    return row.get("valid") and row.get("status") in ("complete", "completed", "success", "ok")


def summarize(rows):
    done = [r for r in rows if completed(r)]
    compiled = [r for r in rows if r.get("valid") and r.get("compile_status") in ("passed", "failed")]
    eligible = [r for r in done if r.get("compile_status") == "passed" and isinstance(r.get("fault_detected"), bool)]
    durations = [r["duration_seconds"] for r in done if number(r.get("duration_seconds"))]
    out = {"observed_runs": len(rows), "completed_runs": len(done),
           "incomplete_runs": sum(not completed(r) for r in rows),
           "invalid_records": sum(not r.get("valid") for r in rows),
           "compile_passed": sum(r["compile_status"] == "passed" for r in compiled),
           "compile_evaluated": len(compiled), "fault_detected_runs": sum(r["fault_detected"] for r in eligible),
           "fault_evaluated_runs": len(eligible),
           "fault_detection_run_rate": sum(r["fault_detected"] for r in eligible) / len(eligible) if eligible else None,
           "fault_evaluated_bugs": len({(r["project"], r["bug_id"]) for r in eligible}),
           "fault_detected_bugs": len({(r["project"], r["bug_id"]) for r in eligible if r["fault_detected"]}),
           "duration_observations": len(durations), "duration_mean_seconds": mean(durations) if durations else None,
           "duration_median_seconds": median(durations) if durations else None,
           "status_counts": dict(Counter(str(r.get("status")) for r in rows))}
    out["fault_detection_rate"] = out["fault_detected_bugs"] / out["fault_evaluated_bugs"] if out["fault_evaluated_bugs"] else None
    for metric in ('generation_seconds', 'total_seconds', 'workflow_seconds'):
        measured = [row[metric] for row in done if number(row.get(metric)) and row[metric] >= 0]
        out[metric + '_observations'] = len(measured)
        out[metric + '_mean'] = mean(measured) if measured else None
        out[metric + '_median'] = median(measured) if measured else None
    for metric in ("line", "branch"):
        observed = [r for r in done if r.get("compile_status") == "passed" and
                    isinstance(r.get(metric + "_covered"), int) and isinstance(r.get(metric + "_total"), int)
                    and r[metric + "_total"] > 0]
        covered = sum(r[metric + "_covered"] for r in observed)
        total = sum(r[metric + "_total"] for r in observed)
        out.update({metric + "_coverage_observations": len(observed), metric + "_covered_sum": covered if observed else None,
                    metric + "_total_sum": total if observed else None,
                    metric + "_coverage_micro": covered / total if total else None,
                    metric + "_coverage_macro": mean(r[metric + "_covered"] / r[metric + "_total"] for r in observed) if observed else None})
    return out


def load_manifest(path):
    if path is None:
        return None
    if path.suffix.lower() == ".json":
        raw = json.loads(path.read_text(encoding="utf-8-sig"))
        if isinstance(raw, dict):
            raw = raw.get("runs", raw.get("rows", raw.get("cases")))
        if not isinstance(raw, list):
            raise ValueError("manifest JSON must be a list or contain runs/rows/cases")
        return raw
    with path.open(encoding="utf-8-sig", newline="") as stream:
        return list(csv.DictReader(stream))


def load_pilot(root):
    rows = []
    base = root / "results/raw/Lang/1/differential-v3"
    for path in sorted(base.glob("*/*/summary.json")):
        data = json.loads(path.read_text(encoding="utf-8-sig"))
        env_path = path.parent / "regression-test/validation.env"
        env = dict(line.split("=", 1) for line in env_path.read_text().splitlines() if "=" in line) if env_path.exists() else {}
        row = {"evidence_type": "legacy_pilot", "project": "Lang", "bug_id": "1",
               "generator": "fscs-art" if path.parent.name.startswith("fscs-art") else "cmaes",
               "run_id": path.parent.parent.name, "seed": 2026, "budget": 30,
               **data, "generated_tests": int(env["test_count"]) if "test_count" in env else None,
               "fixed_passed": env.get("fixed_exit") == "0" if "fixed_exit" in env else None,
               "buggy_failed": env.get("buggy_exit") != "0" if "buggy_exit" in env else None,
               "source_path": relative(path, root)}
        rows.append(row)
    return rows


def write_csv(path, rows, fields=None):
    fields = fields or list(dict.fromkeys(k for row in rows for k in row)) or ["no_records"]
    with path.open("w", encoding="utf-8-sig", newline="") as stream:
        writer = csv.DictWriter(stream, fieldnames=fields, extrasaction="ignore")
        writer.writeheader()
        for row in rows:
            writer.writerow({k: json.dumps(v, ensure_ascii=False) if isinstance(v, (dict, list)) else v for k, v in row.items()})


def pct(value):
    return "ยังไม่มีข้อมูล" if value is None else f"{value * 100:.2f}%"


def paired_comparison(rows):
    """Descriptive differences for unambiguous completed algorithm pairs."""
    groups = defaultdict(lambda: defaultdict(list))
    for row in rows:
        if completed(row):
            group = tuple(str(row.get(k)) for k in ('project', 'bug_id', 'seed', 'budget'))
            groups[group][row['generator']].append(row)
    differences, pairs = defaultdict(list), []
    for identity, methods in sorted(groups.items()):
        if len(methods['cmaes']) != 1 or len(methods['fscs-art']) != 1:
            continue
        a, b = methods['cmaes'][0], methods['fscs-art'][0]
        entry = dict(zip(('project', 'bug_id', 'seed', 'budget'), identity))
        for metric in ('line', 'branch'):
            if a.get(metric + '_total', 0) and b.get(metric + '_total', 0):
                delta = a[metric + '_covered'] / a[metric + '_total'] - b[metric + '_covered'] / b[metric + '_total']
                entry[metric + '_difference'] = delta
                differences[metric].append(delta)
        for metric in ('total_seconds', 'workflow_seconds'):
            if number(a.get(metric)) and number(b.get(metric)):
                entry[metric + '_difference'] = a[metric] - b[metric]
                differences[metric].append(entry[metric + '_difference'])
        pairs.append(entry)
    return {'direction': 'cmaes minus fscs-art', 'matched_pairs': len(pairs),
            'distinct_bugs': len({(p['project'], p['bug_id']) for p in pairs}), 'pairs': pairs,
            'metrics': {metric: {'n': len(values), 'mean_difference': mean(values),
                                'median_difference': median(values)} for metric, values in differences.items()}}


def report_sources(root, results, rows):
    """Bounded excerpts copied from real evidence and configuration."""
    config_path = results / 'config.json'
    config = json.loads(config_path.read_text()) if config_path.is_file() else None
    example = None
    for row in sorted(rows, key=lambda r: (not bool(r.get('fault_detected')), str(r.get('record_path')))):
        if not completed(row):
            continue
        record_path = root / row['record_path']
        java = record_path.parent.parent / 'generation/GeneratedStudyTest.java'
        if not java.is_file():
            continue
        content = java.read_text()
        triggering = row.get('triggering_tests') or []
        method = triggering[0].split('::')[-1] if triggering else 'generated1'
        match = re.search(r'  @Test[^\n]*\n  public void ' + re.escape(method) + r'\(\) \{\n.*?\n  \}', content, re.S)
        excerpt = match.group() if match else '\n'.join(content.splitlines()[:12])
        if len(excerpt) > 2200:
            continue
        example = {'source_path': relative(java, root), 'record_path': row['record_path'],
                   'java_excerpt': excerpt,
                   'record_excerpt': {k: row.get(k) for k in ('run_id', 'test_count', 'compile_status',
                        'fixed_validation', 'fault_detected', 'triggering_tests', 'line_covered', 'line_total',
                        'branch_covered', 'branch_total', 'generation_seconds', 'duration_seconds', 'total_seconds')}}
        break
    prompt_path = root / 'prompts/round2-unit-test.md'
    return {'configuration_path': relative(config_path, root), 'configuration': config,
            'example': example, 'prompt': prompt_path.read_text() if prompt_path.is_file() else None,
            'environment': json.loads((results / 'environment.json').read_text()) if (results / 'environment.json').is_file() else None}


def generation_quality(root, rows):
    quality = []
    for method in ('cmaes', 'fscs-art'):
        counters = Counter()
        for row in rows:
            if not completed(row) or row['generator'] != method:
                continue
            path = (root / row['record_path']).parent.parent / 'generation/observations.json'
            if not path.is_file():
                continue
            observations = json.loads(path.read_text())
            counters['runs'] += 1
            counters['proposed_inputs'] += len(observations)
            for observation in observations:
                counters['retained_tests'] += bool(observation['retained'])
                if observation['retained']:
                    outcome = observation['fixed_first']['outcome']
                    counters['exception_oracles'] += outcome.startswith('exception:')
                    counters['value_oracles'] += not outcome.startswith('exception:')
                else:
                    counters['excluded_inputs'] += 1
        quality.append({'generator': method, **dict(counters)})
    return quality


def test_case_counts(rows):
    result = []
    for method in METHODS:
        selected = [r for r in rows if r['generator'] == method]
        finished = [r for r in selected if completed(r)]
        result.append({'generator': method, 'completed_runs': len(finished),
            'retained_methods_completed_suites': sum(int(r.get('test_count') or 0) for r in finished),
            'retained_methods_incomplete_suites': sum(int(r.get('test_count') or 0) for r in selected if not completed(r)),
            'count_scope': 'Sum of retained declared test methods across primary runs; repeats across run indices are counted; excludes validation histories and pilot. Not unique semantic scenarios or individual runner executions.'})
    return result


def make_report(data):
    meta, pilot, summary = data["metadata"], data["pilot"], data["overall"]
    lines = ["# รายงานผลการทดลองรอบที่ 2", "", "## สถานะของหลักฐาน", "",
             f"รายงานนี้สร้างจากไฟล์ผลจริงเมื่อ {meta['generated_at_utc']} เพื่อประกอบงานข้อ 2.2 ของรายวิชา CP353201",
             "สถานะฉบับปัจจุบัน: รายงานระหว่างดำเนินการ ยังยืนยันความครบถ้วนของงานทั้งชุดไม่ได้",
             f"พบผลการทดลองในรูปแบบมาตรฐาน {summary['observed_runs']} run เสร็จสมบูรณ์ {summary['completed_runs']} run และยังไม่สมบูรณ์หรือข้อมูลไม่ผ่านการตรวจสอบ {summary['incomplete_runs']} run",
             "", "## ผู้จัดทำ", "", *[f"- {name} {student}" for name, student in TEAM], "",
             "เสนอ ผศ. ดร.ชิตสุธา สุ่มเล็ก มหาวิทยาลัยขอนแก่น ภาคการศึกษาที่ 1 ปีการศึกษา 2569",
             "ชื่อและรหัสนักศึกษาอ้างอิงเอกสารรายงานรอบที่ 1 ที่เก็บไว้ใน docs/reference", "",
             "## วัตถุประสงค์และคำถามวิจัย", "",
             "เปรียบเทียบ CMA-ES, FSCS-ART, Claude และ IntelliSphere สำหรับการสร้างชุดทดสอบ Java บน Defects4J โดยใช้ Line coverage, Branch coverage, การตรวจพบบั๊กจริง และต้นทุนการทดลอง",
             "RQ1 แต่ละวิธีครอบคลุมโค้ดได้เท่าใดภายใต้ขอบเขตและงบประมาณเดียวกัน",
             "RQ2 ชุดทดสอบผ่านบน fixed revision และล้มเหลวบน buggy revision หรือไม่",
             "RQ3 การคอมไพล์ เวลา และการปรับพรอมพ์มีข้อจำกัดอย่างไร", "",
             "## วิธีการและความเป็นธรรม", "",
             "ปรับ pipeline จากผลตรวจสอบช่วงพัฒนา แล้วบันทึก SHA-256 ของ source/config ก่อนเก็บผล v4 กำหนด Defects4J, project/bug, คลาสเป้าหมาย, seed และงบประมาณไว้ใน manifest เก็บ log และชุดทดสอบจริงสำหรับแต่ละรอบ ไม่อ้างว่าเป็นการลงทะเบียน protocol ล่วงหน้า",
             "การตรวจพบบั๊กต้องมีชุดทดสอบที่คอมไพล์ผ่านและผ่าน fixed revision ก่อนพิจารณาความล้มเหลวบน buggy revision ต้องแยก timeout และความผิดพลาดของ harness ออกจาก fault detection",
             "สำหรับ AI บันทึกผู้ให้บริการและรุ่นโมเดล พรอมพ์ คำตอบดิบ จำนวนรอบแก้ไข เวลาและ token ถ้ามี ใช้บริบทเดียวกันและขอบเขตชุดทดสอบเดียวกัน ไม่ตีความจำนวน prompt เป็นจำนวนการประเมินโปรแกรม",
             "เปรียบเทียบหลักโดยจับคู่ project, bug, seed และ budget ระหว่างวิธี รายงานระดับรวมเป็นเพียงข้อมูลเชิงพรรณนาเมื่อจำนวนตัวอย่างหรือขอบเขตต่างกัน", "",
             "AI ในชุดนี้เป็น AI-assisted tests ที่ผ่านการประมวลผลในเครื่องตามกติกาที่เปิดเผย: เก็บคำตอบเดิม ตัดจำนวน test methods ตามลำดับ source ให้ไม่เกิน 30 และตัดเฉพาะ methods ที่ไม่ผ่าน fixed validation ได้ไม่เกินสองครั้ง ไม่แก้ expected values จากผล buggy กติกานี้เพิ่มระหว่างการรวมผล AI ไม่ได้ลงทะเบียนล่วงหน้า",
             "Source processing v2/v3 ตัด filename hint บรรทัดแรกในกรอบ Java เฉพาะเมื่อชื่อตรงกับ package/class ที่ประกาศไว้ โดย v3 รองรับคำนำหน้า test source roots ที่ระบุใน policy ไม่ใช่การแก้ assertions; เก็บผล parse failure ก่อนหน้า และ raw source พร้อม before/after hash chain ผลที่สำเร็จอยู่แล้วคงเดิม รายงานรุ่น driver/hash ต่อ record ไว้ครบ กติกานี้เพิ่มหลังพบ ingestion error จึงไม่ได้ลงทะเบียนล่วงหน้า",
             "Source processing v4/v5 เพิ่มการแก้ compatibility ในเครื่องจาก fixed compile diagnostics และ fixed API source เช่นชื่อเมธอดและ nested enum, null overload cast และ assertion แบบ try/catch สำหรับ JUnit รุ่นเก่า เก็บ raw response, ผลก่อนแก้, policy, driver snapshots และ before/after hashes ไม่ส่ง log ให้โมเดลและไม่เปลี่ยน expected values จากผล buggy; JacksonXml 101 เหลือเพียง 1 test หลัง fixed-only pruning จึงตีความ coverage ต่ำตาม suite ที่เหลือจริง",
             "Source processing v6-v29 กู้เฉพาะเมธอด Java ที่สมบูรณ์ก่อนจุดตัดข้อความของ UI, เลือกคลาสชื่อซ้ำตัวแรกตามลำดับคำตอบ, แก้ syntax/import ที่ parser หรือ fixed compiler ระบุ และตัด methods ที่เรียก API ใช้ไม่ได้ พร้อมบันทึก source hashes และผลที่ล้มเหลวก่อนหน้า; v10/v11 ตัดเฉพาะเมธอดที่ fixed compiler ระบุและ cast null เพื่อเลือก overload; v14/v15 ใช้ Option.addValue ตาม fixed API แทน addValueForProcessing ใน Claude Cli โดยไม่เปลี่ยน arguments/assertions; v16-v19 แก้ constructor, XML START_ELEMENT fixture และ configured serializer provider ตาม fixed API; v20-v29 แยก init/parse/normalize/process ของ Closure และใช้ reflection เรียก private method เดิม รวมทั้งแก้ metadata/type/codec/helper ของ Jackson และ Mockito โดยเก็บ assertion เดิม; การแก้ fixture เป็น local AI-assisted processing ที่เปิดเผย มิใช่ output ดิบของโมเดล; count หลัง salvage ไม่ใช่ count ของ test ที่โมเดลตั้งใจเขียนทั้งหมด",
             "Budget 30 หมายถึง proposed input vectors สำหรับอัลกอริทึม และจำนวน JUnit test methods สูงสุดสำหรับ AI หนึ่ง method ของ AI อาจมีหลาย calls/assertions และ stateful fixtures ขณะที่ reflection probe ของอัลกอริทึมสร้าง object graph ได้จำกัด จึงไม่ใช่ input space, จำนวน program calls หรือ compute budget ที่เท่ากันทั้งหมด แม้ใช้ fixed source และ eligible API inventory ร่วมกัน",
             "IntelSphere ใช้ Auto Router และ agent Gemini/OpenAI ที่เลือกเอง ซึ่งเลือกโมเดลได้ต่างกันในแต่ละคำตอบ ผลนี้จึงเป็นการเปรียบเทียบแพลตฟอร์มตามการตั้งค่าที่ใช้ ส่วนข้อความ Java ที่ export จากหน้าเว็บอาจมี suffix (undefined) หลัง array index จึงลบรูปแบบดังกล่าวอย่างจำกัด พร้อมเก็บ HTML และข้อความก่อนแก้ ไม่อ้างว่าเป็น raw API response ที่ตรวจสอบแล้ว",
             "generation_seconds ของ AI รวมเวลาจากกดส่งจนสังเกตว่าคำตอบเสร็จและเวลา processing ในเครื่อง จึงเป็นขอบเขตบนของเวลาที่สังเกตผ่าน UI ซึ่งอาจรวมเวลารอผู้ปฏิบัติงาน ไม่ใช่ model compute time และไม่ใช้จัดอันดับความเร็วกับอัลกอริทึม Token และ model random seed ที่เว็บไม่แสดงเก็บเป็นไม่ทราบ ผู้ใช้อนุญาตเฉพาะ prompt เดิม จึงไม่ได้ส่ง compile/fixed failure logs ไปซ่อมกับโมเดล", "",
             "## นิยามตัวชี้วัดและตัวหาร", "",
             "Fault detection rate = จำนวน project/bug ที่ตรวจพบอย่างน้อยหนึ่งรอบ / จำนวน project/bug ที่ประเมินได้ โดยนับแต่ละ bug ครั้งเดียวแม้ทดลองหลาย seed ส่วน fault_detection_run_rate รายงานอัตรารอบที่ตรวจพบแยกต่างหาก",
             'fault_detected เป็นนิยามเชิงปฏิบัติ: assertion ผ่าน fixed สองครั้งและล้มเหลวบน buggy โดยไม่ใช่ harness/timeout error บาง assertion ของ opaque object ตรวจเพียง runtime type จึงอาจผูกกับรายละเอียด implementation การนับนี้ไม่ได้ยืนยันว่าแต่ละ assertion ตรวจ semantic defect ของ public API อย่างเป็นอิสระ ต้องอ่าน triggering test ประกอบ',
             "Compile rate ใช้จำนวน run ที่มีผลการคอมไพล์ชัดเจนเป็นตัวหาร Coverage macro คือค่าเฉลี่ยสัดส่วนต่อ run ส่วน micro คือผลรวม covered / ผลรวม total ของ run ที่วัดได้ ตัวเลข micro มีการนับโค้ดซ้ำเมื่อทดลองหลาย seed จึงไม่ใช่ coverage ของ union ทั้งโครงการ",
             "ค่าที่ไม่วัดใช้ null หรือช่องว่าง ไม่แทนด้วยศูนย์ Coverage ที่ total เป็นศูนย์ไม่รวมในตัวหาร รายงานเวลาของ run ที่เสร็จและวัดได้เท่านั้น", "",
             'รัน project workers พร้อมกันบน Windows/WSL เครื่องเดียวและใช้ build caches ร่วมกัน เวลาจึงได้รับผลจาก JVM/build overhead, cache warming และ host load ไม่ใช่การวัด CPU ของอัลกอริทึมแบบแยกเครื่อง และไม่ใช้ยืนยันสาเหตุว่าตัวสร้างใดเร็วกว่า', '',
             "## ผลการทดลองมาตรฐาน", ""]
    for row in data["method_summary"]:
        lines.extend([f"### {row['generator']}", "",
                      f"พบ {row['observed_runs']} run เสร็จ {row['completed_runs']} run คอมไพล์ผ่าน {row['compile_passed']}/{row['compile_evaluated']} run ที่ทราบผล",
                      f"ตรวจพบ {row['fault_detected_bugs']}/{row['fault_evaluated_bugs']} bug ที่ประเมินได้ อัตรา {pct(row['fault_detection_rate'])}; ผลระดับ run คือ {row['fault_detected_runs']}/{row['fault_evaluated_runs']}",
                      f"Line coverage macro {pct(row['line_coverage_macro'])} จาก {row['line_coverage_observations']} run และ Branch/condition coverage (Cobertura) macro {pct(row['branch_coverage_macro'])} จาก {row['branch_coverage_observations']} run",
                      "เวลาเฉลี่ยเฉพาะ run ที่เสร็จ: สร้างชุดทดสอบ " + (f"{row['generation_seconds_mean']:.2f} วินาที" if row['generation_seconds_mean'] is not None else "ยังไม่มีข้อมูล") +
                      "; ประเมินชุดทดสอบ " + (f"{row['duration_mean_seconds']:.2f} วินาที" if row['duration_mean_seconds'] is not None else "ยังไม่มีข้อมูล") +
                      "; รวม " + (f"{row['total_seconds_mean']:.2f} วินาที" if row['total_seconds_mean'] is not None else "ยังไม่มีข้อมูล"), ""])
    ai = data['ai_provider_evidence']
    lines += ['## หลักฐานและผลล้มเหลวของบริการ AI', '',
              'จำนวนคำตอบด้านล่างรวม response ที่สร้าง tests ไม่ได้ ส่วน completed หมายถึงผ่าน pipeline ประเมินครบ การมีคำตอบครบ project จึงไม่เท่ากับ coverage ครบทุก project', '']
    for item in ai['summary']:
        lines += [f"- {item['tool']}: บันทึก {item['captured_runs']} original-prompt attempts พร้อมคำตอบหรือข้อความสถานะ ครอบคลุม {item['captured_projects']}/17 projects; ประเมินครบ {item['completed_runs']} รอบ; ผลที่ไม่สมบูรณ์ {item['incomplete_runs']} รอบ; โมเดลที่แสดง: {', '.join(item['models']) or 'ยังไม่มี'}"]
    for item in ai['provider_status']:
        lines += [f"- สถานะบริการ {item.get('tool')}: {item.get('status')}; {item.get('message')}"]
    lines += [f"ลองซ้ำด้วย prompt เดิมหลัง server busy {len(ai['service_retry_history'])} ครั้ง เก็บข้อความและ record ก่อนหน้าพร้อม hashes ใน results/validation/ai-provider-service-retry เวลา UI ของ service attempts เดิมรายงานแยก ไม่รวมเป็น model compute time หรือ successful-run workflow", '']
    lines += ['คำตอบดิบ ภาพหน้าจอ metadata และ source-processing ledger อยู่ใน ai-tests/provider-captures ผล parser/compile/fixed failures อยู่ใน incomplete-runs.csv และผลซ้ำเดิมอยู่ใน results/validation การล้มเหลวจาก parser หรือ UI export ไม่ถูกสรุปว่าเป็นข้อผิดพลาดของโมเดลเพียงอย่างเดียว',
              'การลอง prompt เดิมผ่าน Gemini และ OpenAI ที่เลือกเองบน KKU วันที่ 1 ตุลาคม เก็บ raw response และผลเดิมใน kku-original-prompt-retries และ original-prompt-retries ไม่นับ retry เป็นรอบอิสระเพิ่ม; Claude Chart s102 รัน archive เดิมซ้ำหลัง OS PermissionError โดยเก็บผลก่อนหน้าใน os-permission-retries และไม่แก้ assertions; รายงานรอบแรกที่ส่งจริงแนบใน docs/reference/SQA_Round1_กลุ่ม14.pdf', '']
    lines += ['ก่อน cohort ที่ใช้ original prompts เท่านั้น มี Claude Csv budget-correction response s101-i2 ที่ยังไม่ผ่าน fixed เก็บไว้เป็นหลักฐานนอก primary cohort ไม่นับแทนรอบ original-prompt และไม่ส่ง compile/fixed failure logs ให้โมเดล', '']
    four = data['four_method_comparison']
    lines += ['## เปรียบเทียบสี่วิธีบนกลุ่มที่ประเมินครบ', '',
              f"มี {four['matched_groups']} กลุ่ม project/bug/run-index/budget ที่ทั้งสี่วิธีประเมินครบ จาก {four['distinct_bugs']} project/bug จึงยังไม่ใช้ค่าเฉลี่ยจากคนละ sample จัดอันดับทั้งสี่วิธี", '',
              '| วิธี | n | Line macro | Condition macro | ตรวจพบระดับ run |',
              '|---|---:|---:|---:|---:|']
    for item in four['methods']:
        lines.append(f"| {item['generator']} | {item['completed_runs']} | {pct(item['line_coverage_macro'])} | {pct(item['branch_coverage_macro'])} | {item['fault_detected_runs']}/{item['fault_evaluated_runs']} |")
    lines += ['', 'ใช้เฉพาะกลุ่มเดียวกันในตารางนี้ ตัวอย่างน้อยและเป็นหนึ่ง bug ต่อ project ผลเป็นเชิงพรรณนา ไม่ใช่หลักฐานนัยสำคัญหรือข้อสรุปว่าโมเดลใดดีที่สุด ตัวชี้วัดจาก runs ที่สำเร็จเป็นผลแบบมีเงื่อนไข ต้องอ่านอัตราล้มเหลวประกอบ', '']
    repaired = [r for r in data['runs'] if r.get('postprocessing')]
    env_repaired = [r for r in data['runs'] if r.get('environment_repair')]
    lines += ['', '## การซ่อมและต้นทุน workflow', '', f"ประเมิน archive เดิมซ้ำหลังแก้ dependency {len(env_repaired)} run; ตัด assertion จาก fixed validation แล้วประเมินซ้ำ {len(repaired)} run ผล initial attempts เก็บใน results/validation ไม่ใช่ผลสำเร็จตั้งแต่ครั้งแรก",
              'workflow_seconds รวม generation และการประเมินที่สำเร็จ รวมทั้ง initial failed evaluation และ repackaging เมื่อมี ส่วน total_seconds รวม generation กับ final evaluation เท่านั้น ไม่รวม checkout/setup หรือเวลาพัฒนา', '']
    for row in data['method_summary']:
        if row.get('workflow_seconds_mean') is not None:
            lines.append(f"- {row['generator']}: workflow เฉลี่ย {row['workflow_seconds_mean']:.2f} วินาที จาก {row['workflow_seconds_observations']} run")
    lines += ['', '## แผนภาพขั้นตอนการทดลอง', '', '![Experiment pipeline](experiment-flow.svg)', '',
              'ตรวจเฉพาะ declaration signatures เพื่อกำหนด API ที่มีทั้งสอง revision ส่วนการสร้างอินพุตและ expected outcomes ใช้ fixed revision ผลล้มเหลวและ timeout คงอยู่ในหลักฐาน', '',
              '## Coverage รายโปรเจกต์', '', '![Coverage by project](coverage-by-project.png)', '',
              'กราฟใช้ค่าเฉลี่ยของ run ที่เสร็จ โดย n คือจำนวน observations ต่อแถบ ช่อง n/a ยังไม่มีผลที่วัดได้', '']
    if any(r['generator'] in ('claude','intellisphere') and r['completed_runs'] for r in data['method_summary']):
        lines += ['### Coverage ของ AI-assisted tests', '', '![AI coverage by project](coverage-ai-by-project.png)', '',
                  'กราฟ AI ใช้เฉพาะ runs ที่ประเมินครบ ไม่รวมคำตอบที่ parse/compile/fixed validation ไม่ผ่าน Sample และจำนวนรอบต่อ project อาจต่างจากอัลกอริทึม อ่านอัตราล้มเหลวและตาราง matched comparison ประกอบ', '']
    paired = data.get('paired_comparison', {})
    lines += ['## เปรียบเทียบอัลกอริทึมบนคู่ทดลองเดียวกัน', '',
              f"จับคู่ได้ {paired.get('matched_pairs', 0)} คู่ จาก {paired.get('distinct_bugs', 0)} project/bug โดยใช้ seed และ budget ตรงกัน"]
    for metric, result in paired.get('metrics', {}).items():
        delta = result['mean_difference']
        label = f'{delta * 100:+.2f} percentage points' if metric in ('line', 'branch') else f'{delta:+.2f} วินาที'
        lines.append(f"- {metric}: ค่าเฉลี่ย CMA-ES ลบ FSCS-ART = {label} จาก {result['n']} คู่")
    lines += ['ผลนี้เป็นสถิติเชิงพรรณนาของ sample ปัจจุบัน seed หลายตัวของ bug เดียวกันไม่ใช่ bug อิสระ และไม่มีการอ้างนัยสำคัญทางสถิติ', '']
    detected = defaultdict(set)
    for row in data['runs']:
        if completed(row) and row.get('fault_detected'):
            detected[(row['project'], str(row['bug_id']))].add(row['generator'])
    if detected:
        lines += ['## Project/bug ที่มีหลักฐานตรวจพบ fault', '']
        lines += [f"- {project}-{bug}: {', '.join(sorted(methods))}" for (project, bug), methods in sorted(detected.items())]
        lines += ['รายการนี้ต้องมี fixed ผ่านสองครั้งและ buggy failure ของ assertion ที่ประเมินได้ อ้างอิง triggering_tests และ buggy/failing_tests ของแต่ละ record ไม่ใช้ผลจาก harness error', '']
    expected = data.get('planned_runs') or [*data["runs"], *data["missing_runs"]]
    expected_counts = defaultdict(Counter)
    project_bugs = defaultdict(set)
    completed_counts = defaultdict(Counter)
    for row in expected:
        project = str(row.get("project", ""))
        method = str(row.get("generator", ""))
        expected_counts[project][method] += 1
        project_bugs[project].add(str(row.get("bug_id", "")))
    for row in data["runs"]:
        if completed(row):
            completed_counts[str(row.get("project", ""))][str(row.get("generator", ""))] += 1
    lines += ["## สถานะรายโปรเจกต์", "",
              "จำนวน run ที่เสร็จเทียบกับจำนวนที่วางแผนใน manifest (แสดงเป็น เสร็จ/ตามแผน); ค่า 0 หมายถึงยังไม่มี run เสร็จ ไม่ได้แปลว่าทดลองแล้วได้ศูนย์ coverage", "",
              "| Project | Bug | CMA-ES | FSCS-ART | Claude | IntelliSphere |",
              "|---|---:|---:|---:|---:|---:|"]
    for project in sorted(expected_counts):
        bug_label = ", ".join(sorted(project_bugs[project]))
        cells = [str(completed_counts[project][method]) + "/" + str(expected_counts[project][method])
                 for method in METHODS]
        lines.append("| " + " | ".join([project, bug_label, *cells]) + " |")
    lines.append("")
    lines += ["## หลักฐาน pilot เดิม Lang 1", "",
              "หลักฐานส่วนนี้มาจาก differential-v3 แยกจากการทดลองมาตรฐาน เพื่อไม่ให้จำนวนอินพุตที่ให้พฤติกรรมต่างกันถูกนับเป็นจำนวนบั๊กอิสระ", ""]
    for row in pilot:
        lines += [f"- {row['generator']}: {row.get('cases')} อินพุต พบความแตกต่างไม่ซ้ำ {row.get('unique_differences')} รายการ สร้าง JUnit {row.get('generated_tests')} test; fixed ผ่าน={row['fixed_passed']}, buggy ล้มเหลว={row['buggy_failed']}. หลักฐาน: {row['source_path']}"]
    if not pilot:
        lines.append("ไม่พบหลักฐาน pilot ในตำแหน่งที่กำหนด")
    lines += ["", "หลักฐาน pilot ใช้เพียง Lang-1, seed 2026 และ budget 30 จึงแสดงการทำงานของ pipeline ในกรณีนี้เท่านั้น จำนวนความแตกต่างไม่ซ้ำไม่ใช่อัตราตรวจพบบั๊กทั่ว Defects4J และไม่ใช่ coverage", "",
              "## ความครบถ้วนและข้อผิดพลาด", ""]
    if meta["expected_runs"] is None:
        lines.append("ยังไม่มี manifest ของ run ที่คาดหวัง จึงไม่สามารถสรุปจำนวนงานที่ขาดหรือยืนยันความครอบคลุมทุก project ได้")
    else:
        lines.append(f"manifest ระบุ {meta['expected_runs']} run ไม่พบ record {len(data['missing_runs'])} run และพบ run ที่ยังไม่สมบูรณ์ {summary['incomplete_runs']} run")
    if summary["status_counts"].get("interrupted", 0):
        lines.append("มี run ที่ถูกทำเครื่องหมาย interrupted เพราะการประเมินยังไม่จบ เหตุผลที่ทราบเก็บไว้ใน record และ incomplete-runs.csv ผลส่วนนี้ไม่ได้รวมเป็นผลตรวจพบ fault")
    lines += [f"พบปัญหาข้อมูล {len(data['issues'])} รายการ โปรดดู issues.csv และ incomplete-runs.csv", "",
              "## อภิปรายผลและข้อจำกัด", "",
              "ผลจาก sample ที่ต่างกันหรือเพียง seed เดียวไม่เพียงพอต่อการจัดอันดับทั้งสี่วิธี ต้องรายงานผลต่อ project และต่อ bug คู่กัน รวมทั้งผลล้มเหลวและเวลา timeout",
              "CMA-ES ขึ้นกับ input representation และ fitness ที่ใช้ ส่วน FSCS-ART ขึ้นกับระยะห่างและขอบเขตอินพุต ความแตกต่างระหว่างวิธีจึงต้องวิเคราะห์ร่วมกับ configuration จริง การอ้างว่าตัวสร้างทดสอบทั่วไปครอบคลุมทุก API ต้องมีหลักฐานรองรับ",
              "Claude และ IntelliSphere ต้องมีคำตอบจริงจากบริการดังกล่าวและบันทึกรุ่นโมเดลก่อนเปรียบเทียบ ผลที่สร้างโดยเครื่องมืออื่นไม่สามารถใช้แทนผลของสองบริการนี้ได้",
              "ยังไม่สรุปว่าวิธีใดดีที่สุด เพราะผลที่จับคู่ครบทั้งสี่วิธียังเป็นเพียงบางส่วนของแผนทดลอง การประเมินครบทุก project identity ใช้หนึ่ง active bug ต่อโปรเจกต์ จึงต้องจำกัดข้อสรุปให้อยู่ใน sample นี้", "",
              "## สิ่งที่ต้องเสร็จก่อนส่ง", "",
              "- ตรวจจำนวน algorithm runs กับ manifest และอธิบายการเลือกหนึ่ง bug ต่อ project รวมถึงขอบเขต API",
              "- เก็บชุดทดสอบจริง พรอมพ์และคำตอบของ Claude กับ IntelliSphere พร้อมผลรัน",
              "- ประเมินชุด AI ด้วย evaluator และขอบเขต coverage เดียวกับอัลกอริทึม เพื่อให้การเปรียบเทียบครบสี่วิธี",
              "- สร้างรายงานและสไลด์ใหม่หลังเพิ่มผล AI ตรวจ submission checklist ส่ง GitHub/Classroom และซ้อมนำเสนอ demo", "",
              "## แหล่งข้อมูลและการทำซ้ำ", "",
              "- เกณฑ์งาน: docs/reference/SQA_Project_2026.pdf ข้อ 2.2",
              "- ข้อมูลผู้จัดทำและแนวทางรอบแรก: docs/reference/SQA_Round1_กลุ่ม14.pdf",
              "- ผลมาตรฐาน: results/study/**/record.json และ artifact_path ในแต่ละ record",
              "- ผล pilot: results/raw/Lang/1/differential-v3/20260919T200905Z",
              "- รายละเอียดตัวหารและคำสั่งสร้างใหม่: docs/reporting.md", ""]
    lines += ['## เอกสารอ้างอิงแนวคิด', '',
              '- Hansen (2016), The CMA Evolution Strategy: A Tutorial. https://arxiv.org/abs/1604.00772',
              '- Chen, Leung and Mak (2004), Adaptive Random Testing. https://link.springer.com/chapter/10.1007/978-3-540-30502-6_23',
              '- Defects4J release 3.0.1 และ framework documentation. https://github.com/rjust/defects4j/tree/v3.0.1',
              'อ้างอิงแนวคิดของอัลกอริทึม ส่วน implementation, representation, fixed-outcome fitness และตัวเลขของโครงการนี้ต้องอ่านจาก source/config/หลักฐานที่แนบ ไม่ใช้ผลประสิทธิภาพจากงานวิจัยอื่นแทนผลทดลองของโครงการ', '']
    lines += ['## อุปสรรคและบทเรียนจากหลักฐานจริง', '',
              'การทดลอง v3 พบ target ที่มีเฉพาะ fixed revision และ assertion ของ char array ที่ยาวเกิน Java constant limit จึงปรับ v4 ให้ใช้ signature intersection และ snapshot SHA-256 ผล v3 เก็บใน results/validation ไม่รวมในสถิติ v4',
              'Cli พบ NoClassDefFoundError ของ Hamcrest ก่อนเริ่ม test จึงปรับ framework dependency ให้ใช้ JUnit/Hamcrest jar ที่ Defects4J มีอยู่ และประเมิน archive เดิมซ้ำ เก็บ log เดิมและ before/after configuration ไว้ ไม่เปลี่ยน production code หรือ assertion',
              'Coverage ต่ำหรือไม่ตรวจพบ fault ยังเป็นผลที่รายงานได้ ตัวสร้างที่ใช้ reflection แบบ single call และ opaque object oracle มีขอบเขตจำกัด รายละเอียดและเส้นทาง log อยู่ใน docs/lessons-learned.md', '']
    if repaired:
        lines += ['บาง expected values โดยเฉพาะ hashCode ที่ขึ้นกับ object identity ให้ผลซ้ำกันใน observation JVM แต่เปลี่ยนใน JUnit จึงใช้ follow-up fixed-validation-pruning-v1 ตัดเฉพาะ generated methods ที่ล้มเหลวบน fixed revision แล้วรัน fixed สองครั้ง/buggy/coverage ใหม่ ไม่ใช้ buggy outcome เลือก test และไม่สร้าง input เพิ่ม จำนวน final tests อาจต่ำกว่า budget 30 ที่นับ proposed inputs', '']
    for quality in data.get('generation_quality', []):
        if quality.get('runs'):
            lines.append(f"- {quality['generator']}: {quality['proposed_inputs']} proposed inputs, {quality.get('retained_tests', 0)} final tests, {quality.get('exception_oracles', 0)} assertions ของ exception class และ {quality.get('value_oracles', 0)} ของผลค่า/ชนิด/nullness จาก {quality['runs']} runs")
    lines += ['Exception oracle ยืนยันเพียงชนิด exception และ opaque object oracle ไม่ยืนยัน state ภายใน การใช้ null/constructor fixtures จึงอาจให้ assertions จำนวนมากแต่สำรวจพฤติกรรมไม่ลึก ต้องพิจารณา coverage กับการตรวจพบ fault ร่วมกัน', '']
    sources = data.get('report_sources', {})
    if sources.get('configuration'):
        config = {k: v for k, v in sources['configuration'].items() if k != 'source_sha256'}
        lines += ['## ภาคผนวก ก Configuration ที่ใช้จริง', '',
                  f"ต้นทาง {sources['configuration_path']} รวม source SHA-256 ในไฟล์เต็ม", '',
                  '```json', json.dumps(config, ensure_ascii=False, indent=2), '```', '']
        environment = sources.get('environment')
        if environment:
            lines += ['สภาพแวดล้อมที่บันทึกจริง (ไม่รวมชื่อเครื่อง/ข้อมูลล็อกอิน)', '',
                      '```json', json.dumps(environment, ensure_ascii=False, indent=2), '```', '']
    example = sources.get('example')
    if example:
        lines += ['## ภาคผนวก ข ตัวอย่าง test และผลรันจริง', '',
                  f"source: {example['source_path']}", f"record: {example['record_path']}", '',
                  '```java', example['java_excerpt'], '```', '', '```json',
                  json.dumps(example['record_excerpt'], ensure_ascii=False, indent=2), '```', '']
    if sources.get('prompt'):
        lines += ['## ภาคผนวก ค Prompt กลางของเครื่องมือ AI', '',
                  'ต้นทาง prompts/round2-unit-test.md แต่ละ project เพิ่ม source, build configuration และ target inventory ใน ai-context/prompt.md การมี prompt ยังไม่ใช่หลักฐานว่าใช้บริการ AI แล้ว', '',
                  '```text', sources['prompt'].strip(), '```', '']
    lines += ['## จำนวน test cases และหน่วยการนับ', '',
              'นับ declared test methods ที่คงเหลือในชุด primary ซึ่งประเมินสำเร็จเท่านั้น รวมหลายโปรเจกต์และหลาย run indices; กรณีซ้ำข้ามรอบนับซ้ำ ไม่ใช่จำนวน semantic scenarios ที่ไม่ซ้ำ และไม่คูณจำนวนครั้งที่รัน fixed/buggy; ไม่รวม pilot หรือ repair histories', '']
    for item in data['test_case_counts']:
        lines.append(f"- {item['generator']}: {item['retained_methods_completed_suites']:,} methods ใน {item['completed_runs']} completed runs")
    lines += ['', 'จำนวน methods ของชุดที่ยังไม่สมบูรณ์แยกไว้ใน test-case-counts.csv และไม่อ้างว่าเป็น tests ที่ใช้วัดผลสำเร็จแล้ว', '']
    return "\n".join(lines)


def ai_provider_evidence(root, rows):
    indexed = {(r['project'], r['generator'], int(r['seed'])): r for r in rows}
    attempts = []
    for path in sorted((root / 'ai-tests/provider-captures').glob('*/*/*/operator-metadata.json')):
        operator = json.loads(path.read_text(encoding='utf-8'))
        if operator.get('prompt_iteration', 1) != 1:
            continue
        tool = path.parent.parent.parent.name
        project, bug = path.parent.parent.name.rsplit('-', 1)
        seed = int(path.parent.name.split('-')[0][1:])
        run = indexed.get((project, tool, seed), {})
        attempts.append({'project': project, 'bug_id': bug, 'tool': tool, 'run_index': seed,
            'model': operator.get('model'), 'submitted_at': operator.get('submitted_at'),
            'observed_at': operator.get('generated_at'), 'ui_observed_seconds_upper_bound': operator.get('generation_seconds'),
            'status': run.get('status', 'pending_evaluation'), 'failed_stage': run.get('failed_stage'),
            'raw_test_methods': run.get('ai_raw_test_method_count'), 'final_test_methods': run.get('test_count'),
            'capture_path': relative(path.parent, root), 'capture_metadata_sha256': hashlib.sha256(path.read_bytes()).hexdigest()})
    summaries = []
    for tool in ('claude', 'intellisphere'):
        selected = [r for r in attempts if r['tool'] == tool]
        summaries.append({'tool':tool, 'captured_runs':len(selected),
            'captured_projects':len({r['project'] for r in selected}),
            'completed_runs':sum(r['status'] == 'complete' for r in selected),
            'incomplete_runs':sum(r['status'] not in ('complete','pending_evaluation','running') for r in selected),
            'models':sorted({r['model'] for r in selected if r['model']})})
    status_path = root / 'ai-tests/provider-status.json'
    status = json.loads(status_path.read_text(encoding='utf-8')) if status_path.is_file() else []
    retries = []
    for path in sorted((root/'results/validation/ai-provider-service-retry').glob('*/*/*/retry-lineage.json')):
        ledger = json.loads(path.read_text())
        operator = json.loads((path.parent/'original-capture/operator-metadata.json').read_text())
        retries.append({'project':path.parent.parent.name,'run_index':path.parent.name[1:],
            'tool':'intellisphere','reason':ledger['reason'],'model':operator.get('model'),
            'ui_observed_seconds_upper_bound':operator.get('generation_seconds'),
            'history_path':relative(path.parent,root),'response_sha256':ledger['response_sha256'],
            'record_sha256':ledger['record_sha256']})
    return {'attempts':attempts, 'summary':summaries, 'provider_status':status, 'service_retry_history':retries}


def four_method_comparison(rows):
    groups = defaultdict(dict)
    for row in rows:
        if completed(row):
            groups[(row['project'], row['bug_id'], row['seed'], row['budget'])][row['generator']] = row
    matched = [group for group in groups.values() if all(method in group for method in METHODS)]
    return {'matched_groups':len(matched),
        'distinct_bugs':len({(g['cmaes']['project'],g['cmaes']['bug_id']) for g in matched}),
        'methods':[{'generator':method, **summarize([g[method] for g in matched])} for method in METHODS]}


def aggregate(root, results, output, manifest=None):
    rows, issues = load_records(results, root)
    expected = load_manifest(manifest)
    present = {key(row) for row in rows}
    missing = [row for row in expected if key(row) not in present] if expected is not None else []
    grouped = defaultdict(list)
    for row in rows:
        grouped[(row.get("project"), row.get("generator"), str(row.get("budget")))].append(row)
    method_summary = [{"generator": method, **summarize([r for r in rows if r.get("generator") == method])} for method in METHODS]
    project_summary = [{"project": group[0], "generator": group[1], "budget": group[2], **summarize(items)} for group, items in sorted(grouped.items(), key=lambda x: str(x[0]))]
    matched = defaultdict(dict)
    for row in rows:
        if completed(row):
            group = (row.get("project"), row.get("bug_id"), str(row.get("seed")), str(row.get("budget")))
            matched[group].setdefault(row.get("generator"), []).append(row.get("run_id"))
    matched_rows = [{"project": k[0], "bug_id": k[1], "seed": k[2], "budget": k[3],
                     "methods_present": sorted(v), "all_four_present": all(m in v for m in METHODS),
                     "ambiguous_repeats": any(len(ids) > 1 for ids in v.values())} for k, v in sorted(matched.items())]
    data = {"metadata": {"schema_version": 1, "generated_at_utc": datetime.now(timezone.utc).isoformat(),
                         "results_path": relative(results, root), "manifest_path": relative(manifest, root) if manifest else None,
                         "expected_runs": len(expected) if expected is not None else None,
                         "completion_claim": "not_certified", "pilot_excluded_from_study_metrics": True},
            "test_case_counts": test_case_counts(rows), "overall": summarize(rows), "method_summary": method_summary, "project_method_summary": project_summary,
            "matched_groups": matched_rows, "runs": rows, "planned_runs": expected, "missing_runs": missing,
            "issues": issues, "pilot": load_pilot(root), 'paired_comparison': paired_comparison(rows),
            'report_sources': report_sources(root, results, rows), 'generation_quality': generation_quality(root, rows),
            'ai_provider_evidence':ai_provider_evidence(root, rows), 'four_method_comparison':four_method_comparison(rows)}
    output.mkdir(parents=True, exist_ok=True)
    for filename, content, fields in [("normalized-runs.csv", rows, FIELDS),
                                     ("incomplete-runs.csv", [r for r in rows if not completed(r)], FIELDS),
                                     ("test-case-counts.csv", data["test_case_counts"], None), ("method-summary.csv", method_summary, None), ("project-method-summary.csv", project_summary, None),
                                     ("matched-groups.csv", matched_rows, None), ("missing-runs.csv", missing, None),
                                     ("issues.csv", issues, ["record_path", "issue"]), ("pilot-summary.csv", data["pilot"], None),
                                     ('ai-provider-attempts.csv',data['ai_provider_evidence']['attempts'],None),
                                     ('ai-provider-service-retries.csv',data['ai_provider_evidence']['service_retry_history'],None)]:
        write_csv(output / filename, content, fields)
    (output / "summary.json").write_text(json.dumps(data, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    (output / "report.md").write_text(make_report(data), encoding="utf-8")
    return data


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--root", type=Path, default=Path(__file__).resolve().parents[2])
    parser.add_argument("--results", type=Path, default=Path("results/study"))
    parser.add_argument("--output", type=Path, default=Path("output/submission"))
    parser.add_argument("--manifest", type=Path)
    args = parser.parse_args()
    root = args.root.resolve()
    resolve = lambda path: path if path.is_absolute() else root / path
    data = aggregate(root, resolve(args.results), resolve(args.output), resolve(args.manifest) if args.manifest else None)
    print(json.dumps({"records": data["overall"]["observed_runs"], "issues": len(data["issues"]), "output": str(resolve(args.output))}))


if __name__ == "__main__":
    main()
