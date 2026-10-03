from pathlib import Path
import tempfile
import unittest
from scripts.study.api854.owner_source_selection_v3 import select_files


class ExportedSourceRootTests(unittest.TestCase):
    def setUp(self):
        self.temp=tempfile.TemporaryDirectory()
        self.addCleanup(self.temp.cleanup)
        self.root=Path(self.temp.name)

    def file(self,name):
        path=self.root/name
        path.parent.mkdir(parents=True,exist_ok=True)
        path.write_text('/* offline source selection unit fixture */\n')

    def test_exported_production_root_excludes_gwt_duplicate(self):
        core='src/main/java/org/joda/time/DateTimeZone.java'
        self.file(core)
        self.file('JodaTimeContrib/gwt/src/main/gwt-emul/org/joda/time/emul/org/joda/time/DateTimeZone.java')
        self.assertEqual(select_files(self.root,['org.joda.time.DateTimeZone'],'src/main/java'),[core])

    def test_source_root_cannot_escape_worktree(self):
        with self.assertRaisesRegex(ValueError,'inside the worktree'):
            select_files(self.root,['p.Target'],'..')

    def test_missing_core_source_does_not_fall_back_to_contrib(self):
        (self.root/'src/main/java').mkdir(parents=True)
        self.file('Contrib/src/p/Target.java')
        with self.assertRaisesRegex(ValueError,'No fixed production source'):
            select_files(self.root,['p.Target'],'src/main/java')
