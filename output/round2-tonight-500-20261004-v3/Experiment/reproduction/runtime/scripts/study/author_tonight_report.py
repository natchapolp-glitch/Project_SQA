#!/usr/bin/env python3
"""Author a report and demo instructions from one immutable submission snapshot."""
import argparse
import json
from pathlib import Path
from xml.sax.saxutils import escape

from reportlab.lib import colors
from reportlab.lib.enums import TA_LEFT
from reportlab.lib.styles import getSampleStyleSheet, ParagraphStyle
from reportlab.lib.units import inch
from reportlab.pdfbase import pdfmetrics
from reportlab.pdfbase.ttfonts import TTFont
from reportlab.platypus import SimpleDocTemplate, Paragraph, Spacer, Table, TableStyle, PageBreak


def display(value, suffix=""):
    if value is None:
        return "N/A"
    return (f"{value:.1f}" if isinstance(value, float) else str(value)) + suffix


def author(root):
    snapshot = json.loads((root / "Experiment/snapshot.json").read_text(encoding="utf-8"))
    summary = snapshot["summary"]
    methods = summary["per_method"]
    pdfmetrics.registerFont(TTFont("ReportFont", "C:/Windows/Fonts/tahoma.ttf"))
    pdfmetrics.registerFont(TTFont("ReportBold", "C:/Windows/Fonts/tahomabd.ttf"))
    styles = getSampleStyleSheet()
    for name in ("Normal", "BodyText", "Heading1", "Heading2", "Title"):
        styles[name].fontName = "ReportFont"
        styles[name].textColor = colors.HexColor("#17344A")
    styles["BodyText"].fontSize = 10
    styles["BodyText"].leading = 15
    styles["BodyText"].spaceAfter = 9
    styles["Heading1"].fontName = "ReportBold"
    styles["Heading1"].fontSize = 22
    styles["Heading1"].leading = 29
    styles["Heading2"].fontName = "ReportBold"
    styles["Heading2"].fontSize = 13
    styles["Heading2"].spaceBefore = 12
    styles["Title"].fontName = "ReportBold"
    styles["Title"].fontSize = 29
    styles["Title"].leading = 37
    styles.add(ParagraphStyle("SmallCell", fontName="ReportFont", fontSize=8, leading=11, alignment=TA_LEFT))
    story, source = [], []
    def p(text):
        story.append(Paragraph(escape(text), styles["BodyText"]))
        source.append(text + "\n")
    def heading(title, first=False):
        if not first:
            story.append(PageBreak())
            source.append("\n---\n")
        story.append(Paragraph(escape(title), styles["Heading1"]))
        story.append(Spacer(1, 0.15 * inch))
        source.append("# " + title + "\n")
    def table(values, widths):
        cells = [[Paragraph(escape(str(c)), styles["SmallCell"]) for c in row] for row in values]
        t = Table(cells, colWidths=widths, repeatRows=1, hAlign="LEFT")
        t.setStyle(TableStyle([("BACKGROUND", (0, 0), (-1, 0), colors.HexColor("#E9F2F5")),
                              ("GRID", (0, 0), (-1, -1), 0.35, colors.HexColor("#D5DFE6")),
                              ("VALIGN", (0, 0), (-1, -1), "TOP"),
                              ("LEFTPADDING", (0, 0), (-1, -1), 7), ("RIGHTPADDING", (0, 0), (-1, -1), 7),
                              ("TOPPADDING", (0, 0), (-1, -1), 8), ("BOTTOMPADDING", (0, 0), (-1, -1), 8)]))
        story.append(t)
        story.append(Spacer(1, 12))
        source.extend(" | ".join(map(str, r)) + "\n" for r in values)
    story.append(Paragraph("AI-Assisted Testing vs.<br/>Automatic Test Generation", styles["Title"]))
    story.append(Spacer(1, 25))
    p("CP353201 Software Quality Assurance | Round 2 | 4 October 2026")
    p("CMA-ES / FSCS-ART / KKU Sonnet 5 / KKU Gemini 3.5 Flash Lite")
    p(f"Measured deadline snapshot: {summary['recorded_bugs']} bugs across {summary['recorded_projects']} projects. "
      f"{summary['attempted_jobs']} recorded bug/method jobs out of {summary['planned_jobs']:,} planned. This is a partial benchmark; the chosen target is {summary['planned_bugs']} bugs, not a completion claim.")
    story.append(Spacer(1, 18))
    for member in snapshot["members"]:
        p(member["name"] + " | " + member["id"])
    p("Repository: https://github.com/natchapolp-glitch/Project_SQA | branch Team")
    p("Results snapshot (UTC): " + summary["snapshot_at_utc"])
    source.insert(0, "# AI-Assisted Testing vs. Automatic Test Generation\n")

    heading("1. Objective and declared scope")
    p("The study implements two algorithms and uses two university-hosted AI models to generate unit tests for real Java bugs in Defects4J. We compare fixed validation, buggy-revision fault detection, target-class code coverage, generation/evaluation time and reported token usage.")
    table([["Item", "Experimental design"], ["Dataset", "Defects4J 3.0.1; 854 active bugs in 17 projects"],
           ["Selected target", f"{summary['planned_bugs']} bugs / {summary['planned_jobs']} jobs; project-sorted round robin, without outcome filtering"],
           ["Executed scope", f"{summary['recorded_bugs']} bugs / {summary['recorded_projects']} projects recorded at this snapshot"],
           ["Algorithms", "CMA-ES and FSCS-ART; seed 101, budget 30 vectors"],
           ["AI models", "claude-sonnet-5 and gemini-3.5-flash-lite via KKU only"],
           ["Validation", "One unchanged suite: fixed-1, fixed-2, buggy, fixed coverage"],
           ["Environment", "Java 11.0.32.1; WSL Ubuntu; America/Los_Angeles test timezone"],
           ["Host", "Ryzen 3 3100, 4 cores/8 threads, 16 GB RAM; one CPU slot"]], [105, 385])
    p("A bug/method pair is the counting unit. Multiple JUnit methods, P01-P04 calls, fixed validation repeats and rejected extensions are not additional bugs. Missing measurements are left unavailable rather than replaced by zero.")
    p("The course's round-two specification asks for all Java projects, measurements, comparisons, reproducible source/test/prompt/configuration evidence, a complete report, presentation and demo. The snapshot discloses which work remains; folder structure or a planned inventory does not prove it was executed.")

    heading("2. Algorithms and test oracles")
    p("CMA-ES proposes vectors from a multivariate normal distribution and adapts the mean, covariance and step size from objective scores. The implemented objective minimizes observed fixed-behavior frequency as an output-diversity proxy. Runtime coverage is measured afterwards; it is not the optimization fitness.")
    p("FSCS-ART draws a candidate set and selects the point whose minimum normalized distance to previously selected points is greatest. The candidate-set size is 10. Both methods use the same target inventory, vector bounds, seed and bounded Java adapter/receiver policy.")
    p("The shared adapter discovers declarations in modified classes available in both revisions. Existing v12 fixtures construct supported receivers and arguments. Unsupported signatures or generation/build failures are retained as outcomes; the study does not claim arbitrary support for every Java declaration.")
    p("Algorithm tests use stable fixed-revision reference observations as their expected values. Each retained observation is checked twice and must invoke its intended target. This supports regression comparison but differs from contract-derived AI assertions. Opaque objects, unsupported constructors and limited vector domains restrict fault detection.")
    table([["Method", "Budget", "Oracle / input policy"], ["CMA-ES", "30 vectors", "Stable fixed observations; behavior-frequency objective"],
           ["FSCS-ART", "30 vectors", "Stable fixed observations; normalized input distance"],
           ["AI", "Up to 12 + 4 methods", "Buggy context, API contracts, one bounded fixed-feedback repair"]], [85, 85, 320])

    heading("3. AI generation protocol")
    p("P01 analyzes context. P02 creates complete Java 7-compatible JUnit 4 source for the Java 11 runtime. Optional P03 repairs compile or fixed-validation issues once. After measuring fixed coverage, P04 may add new test classes; invalid additions are rejected while preserving the valid base suite. No buggy failure feedback is used in repair or extension prompts.")
    p("Both models receive identical master wording, context selection and repair/extension policies within a condition. Temperature 0, max_tokens 4,096 and stream false are requested. Sonnet uses /messages with thinking disabled; Gemini uses /chat/completions. Accepted requests do not prove effective settings, and output truncation remains a measured outcome.")
    table([["Condition", "Cases / context", "Important distinction"],
           ["Initial pilot", "Csv-1, Lang-1, Math-1; source up to 12,000 characters + signatures up to 6,000", "P01 also included buggy source; Sonnet Lang output incomplete"],
           ["Compact nightly", "Remaining cases; P01 metadata/signatures only; P02 same source budget", "Prefer 4-6 concise methods, hard maximum 12; predetermined key alias per case"]], [95, 235, 160])
    p("The protocols are labelled separately and preserved with source hashes. Aggregate descriptive values do not establish a controlled comparison between the two prompt conditions. Ten supplied key aliases are available, but key count alone does not demonstrate independent accounts or quota buckets.")
    p("Raw assistant text, actual sent prompts, model/provider IDs, usage/quota observations, generated Java and all validation stages are retained. Credentials are stored only in ignored local private files and are excluded from the delivery.")

    heading("4. Validation and measurement")
    p("For executable generated suites, the evaluator runs fixed twice, then buggy, then Cobertura coverage on modified classes. A detected fault requires a valid suite and a genuine failing buggy-revision test. Linkage errors, harness failures, timeouts and fixed-invalid assertions are not counted as detected faults.")
    p("Algorithm method entries and target invocations are checked with stage counters. AI entries are verified through Defects4J Formatter.startTest ledgers across all four stages. Missing AI skipped/target-check counters remain unavailable. Passing fixed twice improves confidence in repeatability but does not prove absence of flakiness.")
    table([["Metric", "Definition / denominator"], ["Evaluation rate", "DONE / recorded attempted jobs for that method"],
           ["Fault detection", "Fault-detecting cases / cases with valid measured buggy results"],
           ["Line coverage", "Covered lines / total lines in modified target classes; average only measured ratios"],
           ["Condition coverage", "Cobertura branch/condition counters; average only measured nonzero denominators"],
           ["Time", "Generation API/algorithm time and selected evaluation time reported separately"],
           ["Tokens", "Reported input/output components; unknown usage is not estimated as zero"]], [115, 375])
    p("Selected-suite evaluation time excludes failed earlier base/repair/extension evaluations and shared checkout/setup. Per-job elapsed time, stage timings and logs remain in the raw records. No total full-dataset completion time is extrapolated from this small snapshot.")

    heading("5. Recorded outcomes")
    table([["Method", "Attempted", "DONE", "Fault result", "Fault cases"],
           *[[m["name"], m["attempted_cases"], m["successfully_evaluated"], m["fault_detection_denominator"], m["fault_detecting_cases"]] for m in methods.values()]],
          [140, 70, 65, 105, 110])
    p(f"All four method outcomes are recorded for {summary['bugs_four_methods_recorded']} bugs. All four evaluations completed successfully for {summary['bugs_four_methods_evaluated']} bugs. These are different counts; fixed-invalid or output-incomplete results are still experiment outcomes.")
    for method, values in methods.items():
        p(values["name"] + ": " + ", ".join(f"{k}={v}" for k, v in values["states"].items()))
    p(f"PENDING describes selected work absent from the sealed snapshot, which may include a running job. QUOTA_PAUSED describes a blocked stage, not a generated-suite failure. This report does not claim {summary['planned_bugs']} attempted cases for either AI. The original 854-bug inventory is retained separately; bugs outside the selected scope are not PENDING jobs in this table.")
    heading('Recorded coverage by Java project')
    from collections import Counter
    project_cases = Counter(r['case'].rsplit('-', 1)[0] for r in snapshot['results'])
    table([['Project', 'Recorded method outcomes'], *[[p, n] for p, n in sorted(project_cases.items())]], [245, 245])

    heading("6. Coverage and token observations")
    table([["Method", "Line cases", "Mean line %", "Condition cases", "Mean condition %"],
           *[[m["name"], m["line_coverage_cases"], display(m["mean_fixed_line_coverage_percent"]),
              m["condition_coverage_cases"], display(m["mean_fixed_condition_coverage_percent"])] for m in methods.values()]],
          [140, 80, 80, 95, 95])
    p("Averages exclude missing measurements and zero denominators. Different target classes, supported domains, suite sizes and failed-case selection mean these descriptive averages do not prove one method is superior.")
    table([["AI", "Input recorded", "Output recorded", "API calls"],
           *[[methods[m]["name"], display(methods[m]["recorded_prompt_tokens"]), display(methods[m]["recorded_completion_tokens"]), methods[m]["request_attempts"]]
             for m in ("kku-claude", "kku-gemini")]], [160, 110, 110, 110])
    p("Sonnet responses may omit total_tokens. When input/output components are present, they are reported explicitly without claiming a provider-reported total. Quota records reflect response time, not guaranteed current availability. No precise daily reset time or pooled quota is asserted.")
    table([['Method', 'Mean generation s', 'Mean selected evaluation s', 'Measured timing cases'],
           *[[m['name'], display(m['mean_generation_seconds']), display(m['mean_evaluation_seconds']),
              str(m['generation_seconds_cases']) + ' / ' + str(m['evaluation_seconds_cases'])] for m in methods.values()]], [140, 110, 130, 110])
    p('Means use recorded numeric timings only, including unsuccessful jobs when a timing was captured. The last column gives generation/evaluation timing counts. These are not total pipeline completion times.')

    heading("7. Problems, limitations and learning")
    p("Initial Sonnet Lang-1 generation stopped at the 4,096-token output limit; the incomplete Java is preserved without repeated generation to obtain a better result. Sonnet Math-1 stopped before P02 because remaining quota was below the conservative byte-based operator guard; this is not a provider report of zero quota.")
    p("Cli-1 exposed a missing Hamcrest class on the test classpath (NoClassDefFoundError: org/hamcrest/SelfDescribing). This is recorded as an infrastructure failure rather than a product fault or a model-quality result. Other build/generation/repair failures are retained with their original stages and logs.")
    p("The pilot and nightly cohorts use different prompt compaction. Algorithms use fixed-reference observations while AI uses contract reasoning with bounded fixed feedback. Unequal suite sizes, only one generation per bug/method, bounded adapters and partial dataset execution limit causal or statistical conclusions.")
    p("Useful learning: source compilation is not proof that tests execute; fixed validity is not fault detection; high target coverage does not guarantee the dataset bug is exposed; quota pauses are not failed tests; and planned case counts must remain separate from measured outcomes.")
    p("Further work is to complete missing cases and projects, recover infrastructure under a declared new condition without regenerating model outcomes, collect more independent runs/configurations when time permits, and compare matched domains with transparent resource budgets.")

    heading("8. Reproduction, demo and evidence")
    p("Start from the root README and Presentation/demo-guide.md. demo.py shows actual snapshot outcomes and can replay a selected DONE archive on the original Java 11/Defects4J host. Replay writes a fresh output directory, keeps old evidence unchanged, uses the existing CPU lock and sends no AI requests.")
    table([["Folder", "Evidence"], ["Algorithm1_CMAES / Algorithm2_FSCSART", "Code, Configuration, Result_Round1 and Test; Round2 explicitly not performed"],
           ["AI1_KKU_Claude / AI2_KKU_Gemini", "Condition-labelled master prompts, actual prompts/raw answers, generated/final source"],
           ["Experiment", "Full planned inventory, snapshot manifest, protocols/runtime, measured evaluations and contexts"],
           ["Report", "This report, source, data tables and four method summaries"],
           ["Presentation", "Editable deck, demo guide, replay script and rehearsal receipt"]], [200, 290])
    p("Reference: course project specification supplied by the user, clauses 1.5-1.10 and 2.2. Dataset: https://github.com/rjust/defects4j. Layout/workflow reference only: MammamiaPizza/ProjectSQAGroup11 branch jiratchaya_673380510-1; none of the friend's results are imported as our own.")
    p("CMA-ES background: Hansen, The CMA Evolution Strategy: A Tutorial, https://arxiv.org/abs/1604.00772. FSCS-ART background: Chen, Leung and Mak, Adaptive Random Testing (2004). Actual implementation choices are disclosed in the included generator and configuration rather than inferred from algorithm names.")
    p("The complete per-case table is Report/data/final_comparison.csv. Each attempted job links to a retained outcome. Checksums and package verification provide byte-level traceability; they do not substitute for the experiment or independent scientific review.")
    def footer(canvas, doc):
        canvas.setFont("ReportFont", 8)
        canvas.setFillColor(colors.HexColor("#526371"))
        canvas.drawString(48, 26, "CP353201 | Measured partial benchmark | Round 2")
        canvas.drawRightString(547, 26, str(doc.page))
    path = root / "Report/SQA_Round2_Report.pdf"
    SimpleDocTemplate(str(path), pagesize=(595.28, 841.89), leftMargin=48, rightMargin=48,
                      topMargin=46, bottomMargin=48, title="SQA Round 2 measured benchmark", author="Project SQA team").build(story, onFirstPage=footer, onLaterPages=footer)
    (root / "Report/report-source.md").write_text("\n".join(source), encoding="utf-8")
    (root / "README.md").write_text(
        "# CP353201 Round 2: measured partial benchmark\n\n"
        f"Snapshot {summary['snapshot_at_utc']}: {summary['recorded_bugs']} bugs / {summary['recorded_projects']} projects; "
        f"{summary['attempted_jobs']} recorded jobs out of {summary['planned_jobs']} planned for {summary['planned_bugs']} selected bugs.\n\n"
        "**The selected target is not a completion claim.** Both successful and unsuccessful outcomes are included. "
        "Unknown coverage/fault metrics are unavailable, not zero. Pilot and compact protocols are disclosed separately.\n\n"
        "Methods: CMA-ES, FSCS-ART, KKU Sonnet 5, KKU Gemini 3.5 Flash Lite.\n\n"
        "## Members\n\n" + "\n".join(f"- {m['name']} — {m['id']}" for m in snapshot["members"]) + "\n\n"
        "## Deliverables\n\n"
        "- [Report PDF](Report/SQA_Round2_Report.pdf) and [source](Report/report-source.md)\n"
        "- [Comparison table](Report/data/final_comparison.csv) and [summary](Report/data/summary.json)\n"
        "- [Presentation](Presentation/SQA_Round2.pptx) and [demo guide](Presentation/demo-guide.md)\n"
        "- [Snapshot/evidence index](Experiment/snapshot.json)\n\n"
        "## Reproduction\n\nUse Java 11, Defects4J 3.0.1 and Ubuntu/WSL. "
        "`python3 -B Presentation/demo.py` prints stored measurements. "
        "See the demo guide for fresh archive replay with a new output directory. No AI request is needed for replay. "
        "The exact generation runtime is included under Experiment/reproduction/runtime; original path bindings "
        "are retained in records for provenance. Fresh generation needs new worktrees/output, declared protocol "
        "and your own authorized KKU key in ignored private storage; credentials are not supplied. "
        "Generated probe class files are retained where required by sealed receipts; source is included.\n\n"
        "Result_Round2 has not been performed. Fixed validation twice does not constitute a second generation round. "
        "Full historical evidence remains in branch Team of the original repository; it was not pooled into these results.\n",
        encoding="utf-8")
    print(path)


if __name__ == "__main__":
    cli = argparse.ArgumentParser(description=__doc__)
    cli.add_argument("--snapshot", type=Path, required=True)
    author(cli.parse_args().snapshot.resolve())
