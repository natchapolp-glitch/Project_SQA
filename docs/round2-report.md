# Report and presentation

The current report files are generated from raw records and the manifest:

- Markdown: `output/submission/report.md`
- PDF: `output/submission/SQA_Round2_Report.pdf`
- Editable presentation: `presentation/SQA_Round2.pptx`
- Demo walkthrough: `presentation/demo-guide.md`

Regenerate the evidence and PDF after changing records:

```bash
python scripts/reporting/aggregate.py --results results/study/round2-v4-20260929 \
  --manifest results/study/round2-v4-20260929/study-manifest.csv
python scripts/reporting/audit_evidence.py
python scripts/reporting/build_figures.py
python scripts/reporting/build_report.py
```

Slides use the same summary JSON. `scripts/reporting/build_slides.mjs` requires
the bundled `@oai/artifact-tool`; see `docs/reporting.md`. Render and inspect
each updated slide and report page before delivery.

The generated report retains the team names and IDs from the round-one report.
It labels the present study incomplete until runs and provider evidence are
complete. Do not fill missing measurements with zero or present the selected
one-bug-per-project sample as coverage of every Defects4J bug.
