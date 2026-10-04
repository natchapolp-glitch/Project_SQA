# CP353201 Round 2: measured partial benchmark

Snapshot 2026-10-04T14:44:13.524138+00:00: 130 bugs / 17 projects; 396 recorded jobs out of 2000 planned for 500 selected bugs.

**The selected target is not a completion claim.** Both successful and unsuccessful outcomes are included. Unknown coverage/fault metrics are unavailable, not zero. Pilot and compact protocols are disclosed separately.

Methods: CMA-ES, FSCS-ART, KKU Sonnet 5, KKU Gemini 3.5 Flash Lite.

## Members

- นายธนินธร อันทรบุตร — 673380043-6
- นายศุภกร กรมรินทร์ — 673380061-4
- นายณัชพล เพ็งพล — 673380267-4
- นายณัฐกรณ์ อินธิสาร — 673380268-2

## Deliverables

- [Report PDF](Report/SQA_Round2_Report.pdf) and [source](Report/report-source.md)
- [Comparison table](Report/data/final_comparison.csv) and [summary](Report/data/summary.json)
- [Presentation](Presentation/SQA_Round2.pptx) and [demo guide](Presentation/demo-guide.md)
- [Snapshot/evidence index](Experiment/snapshot.json)

## Reproduction

Use Java 11, Defects4J 3.0.1 and Ubuntu/WSL. `python3 -B Presentation/demo.py` prints stored measurements. See the demo guide for fresh archive replay with a new output directory. No AI request is needed for replay. The exact generation runtime is included under Experiment/reproduction/runtime; original path bindings are retained in records for provenance. Fresh generation needs new worktrees/output, declared protocol and your own authorized KKU key in ignored private storage; credentials are not supplied. Generated probe class files are retained where required by sealed receipts; source is included.

Result_Round2 has not been performed. Fixed validation twice does not constitute a second generation round. Full historical evidence remains in branch Team of the original repository; it was not pooled into these results.
