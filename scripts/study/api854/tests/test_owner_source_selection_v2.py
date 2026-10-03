from pathlib import Path
import tempfile
import unittest
from scripts.study.api854.owner_source_selection_v2 import select_files


class DollarSourceSelectionTests(unittest.TestCase):
    def setUp(self):
        self.temp=tempfile.TemporaryDirectory()
        self.addCleanup(self.temp.cleanup)
        self.root=Path(self.temp.name)

    def file(self, name):
        target=self.root/name
        target.parent.mkdir(parents=True,exist_ok=True)
        target.write_text('package p; /* offline selection unit fixture */\n')

    def test_literal_dollar_top_level_and_nested_class(self):
        self.file('src/main/java/p/$Gson$Types.java')
        self.file('src/test/java/p/$Gson$Types.java')
        self.assertEqual(select_files(self.root,['p.$Gson$Types','p.$Gson$Types$Inner']),
                         ['src/main/java/p/$Gson$Types.java'])

    def test_enclosing_class_fallback_preserved(self):
        self.file('src/p/Outer.java')
        self.assertEqual(select_files(self.root,['p.Outer$Inner$Deep']),['src/p/Outer.java'])

    def test_ambiguous_literal_source_not_silently_replaced(self):
        self.file('a/p/Top$Name.java')
        self.file('b/p/Top$Name.java')
        self.file('a/p/Top.java')
        with self.assertRaisesRegex(ValueError,'Ambiguous'):
            select_files(self.root,['p.Top$Name'])
