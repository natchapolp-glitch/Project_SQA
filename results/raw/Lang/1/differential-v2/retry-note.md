# Validation retry note

FSCS-ART and CMA-ES regression validation were first started concurrently. One
Defects4J `export cp.test` invocation failed while the other was operating on the
same project metadata; its outer stderr log is retained as negative evidence.
The CMA-ES validation was then rerun sequentially. Its final `validation.env`
records `buggy_exit=1` and `fixed_exit=0`, which is the result reported in the
pilot summary.
