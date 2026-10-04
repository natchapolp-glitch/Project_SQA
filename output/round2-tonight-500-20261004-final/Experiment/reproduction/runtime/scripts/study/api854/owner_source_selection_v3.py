"""Limit source selection to the production directory exported by Defects4J."""
from pathlib import Path
from .context_export import BUILD_FILES
from .owner_source_selection_v2 import select_files as select_dollar_files


def select_files(worktree, classes, source_root):
    worktree=Path(worktree).resolve()
    root=(worktree/source_root).resolve()
    if not root.is_relative_to(worktree) or not root.is_dir():
        raise ValueError('Exported production source root must be an existing directory inside the worktree')
    prefix=root.relative_to(worktree)
    selected=[(prefix/name).as_posix() for name in select_dollar_files(root,classes) if name.endswith('.java')]
    selected.extend(name for name in sorted(BUILD_FILES) if (worktree/name).is_file())
    return sorted(set(selected))
