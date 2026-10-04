"""Source-only selection: try a literal Java filename before inner-class prefixes."""
from pathlib import Path
from .context_export import BUILD_FILES


def select_files(worktree, classes):
    selected=[]
    for name in classes:
        # A dollar is legal in a top-level Java identifier ($Gson$Types.java).
        # Only fall back to enclosing names when the literal file is absent.
        candidate=name
        while True:
            suffix=candidate.replace('.','/')+'.java'
            matches=[p for p in worktree.rglob(Path(suffix).name)
                if p.as_posix().endswith('/'+suffix)
                and not any(part.lower() in {'target','build','tests','test','generated'}
                            for part in p.relative_to(worktree).parts[:-1])]
            if len(matches)>1:raise ValueError(f'Ambiguous fixed production source for {name}')
            if matches:
                selected.append(matches[0].relative_to(worktree).as_posix())
                break
            if '$' not in candidate:raise ValueError(f'No fixed production source for {name}')
            candidate=candidate.rsplit('$',1)[0]
    selected.extend(name for name in sorted(BUILD_FILES) if (worktree/name).is_file())
    return sorted(set(selected))
