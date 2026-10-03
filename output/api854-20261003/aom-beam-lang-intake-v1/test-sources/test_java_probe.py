import base64
import json
import os
from pathlib import Path
import shutil
import subprocess
import tempfile
import unittest


@unittest.skipUnless(shutil.which('javac') and shutil.which('java'), 'JDK required for real probe integration')
class JavaProbeIntegrationTests(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.temp = tempfile.TemporaryDirectory()
        cls.folder = Path(cls.temp.name)
        fixture = cls.folder / 'ProbeFixture.java'
        fixture.write_text('''public class ProbeFixture {
          private ProbeFixture() {}
          public int add(int input) { return input + 1; }
          public static Object opaque() { return new Object(); }
          public static int fail(int value) { throw new IllegalArgumentException("expected"); }
          public static char[] large() { char[] a = new char[4096]; java.util.Arrays.fill(a, 'a'); return a; }
          public static char[] largeOther() { char[] a = new char[4096]; java.util.Arrays.fill(a, 'b'); return a; }
          public static abstract class Base { public abstract int value(int input); }
          public static class Concrete extends Base {
            public Concrete() {}
            public int value(int input) { return input; }
          }
          public static class Broken {
            public Broken() { throw new NullPointerException("fixture setup"); }
            public int value() { return 7; }
          }
          public static int dom(org.w3c.dom.Node node) { return node.getChildNodes().getLength(); }
          public static int unsupported(java.io.InputStream input) { return input == null ? 0 : 1; }
        }''')
        root = Path(__file__).resolve().parents[3]
        subprocess.run(['javac', '--release', '8', '-d', str(cls.folder),
                        str(root / 'algorithms/java/SqaProbe.java'), str(fixture)],
                       check=True, capture_output=True, text=True)

    @classmethod
    def tearDownClass(cls):
        cls.temp.cleanup()

    def run_probe(self, *args):
        return subprocess.run(['java', '-cp', str(self.folder), 'SqaProbe', *args],
                              capture_output=True, text=True, timeout=10)

    def outcome(self, *args):
        result = self.run_probe('observe', *args)
        self.assertEqual(result.returncode, 0, result.stderr)
        return base64.b64decode(result.stdout.strip().split('SQA_RESULT:')[1]).decode()

    def test_private_constructor_is_usable_fixture(self):
        value = self.outcome('ProbeFixture', '', 'add', 'int', '0,0,0')
        self.assertEqual(value, 'value:java.lang.Integer:MQ==')

    def test_opaque_return_has_type_oracle_without_identity_hash(self):
        self.assertEqual(self.outcome('ProbeFixture', '', 'opaque', '', '0,0,0'),
                         'value:object-type:java.lang.Object')

    def test_target_exception_and_harness_failure_are_distinct(self):
        self.assertEqual(self.outcome('ProbeFixture', '', 'fail', 'int', '0,0,0'),
                         'exception:java.lang.IllegalArgumentException')
        error = self.run_probe('observe', 'ProbeFixture', '', 'missing', '', '0,0,0')
        self.assertNotEqual(error.returncode, 0)
        self.assertIn('SQA_HARNESS', error.stderr)

    def test_abstract_target_uses_concrete_fixture(self):
        fixture_list = self.folder / 'fixtures.txt'
        fixture_list.write_text('ProbeFixture$Concrete\n')
        result = self.run_probe('discover', '--fixtures', str(fixture_list), 'ProbeFixture$Base')
        self.assertEqual(result.returncode, 0, result.stderr)
        data = json.loads(result.stdout)
        self.assertEqual(data['targets'][0]['class'], 'ProbeFixture$Concrete')
        self.assertEqual(data['targets'][0]['method'], 'value')

    def test_long_exact_outputs_use_stable_distinguishable_bounded_digest(self):
        first = self.outcome('ProbeFixture', '', 'large', '', '0,0,0')
        self.assertRegex(first, r'^sha256:[0-9a-f]{64}:bytes:\d+$')
        self.assertEqual(first, self.outcome('ProbeFixture', '', 'large', '', '0,0,0'))
        self.assertNotEqual(first, self.outcome('ProbeFixture', '', 'largeOther', '', '0,0,0'))
        self.assertLess(len(first), 200)

    def test_explicit_fixture_rejects_constructor_exception_before_target(self):
        result = self.run_probe('observe', 'ProbeFixture$Broken', '', 'value', '', '0,0,0',
                                'beam-explicit-fixtures-v3-proposal')
        self.assertEqual(result.returncode, 0, result.stderr)
        self.assertIn('SQA_FIXTURE_FAILURE:', result.stdout)
        self.assertNotIn('SQA_RESULT:', result.stdout)

    def test_explicit_fixture_never_turns_unsupported_reference_into_null(self):
        result = self.run_probe('observe', 'ProbeFixture', '', 'unsupported', 'java.io.InputStream', '0,0,0',
                                'beam-explicit-fixtures-v3-proposal')
        self.assertIn('SQA_FIXTURE_FAILURE:', result.stdout)
        self.assertNotIn('SQA_RESULT:', result.stdout)

    def test_explicit_dom_recipe_reaches_target_with_structural_oracle(self):
        result = self.run_probe('observe', 'ProbeFixture', '', 'dom', 'org.w3c.dom.Node', '0,0,0',
                                'beam-explicit-fixtures-v3-proposal')
        self.assertEqual(result.returncode, 0, result.stderr)
        self.assertIn('"target_invoked":true', result.stdout)
        outcome = base64.b64decode(result.stdout.split('SQA_RESULT:')[1].strip()).decode()
        self.assertIn('value:java.lang.Integer:Mw==|state=node:', outcome)
        self.assertIn('nested', outcome)

    def test_explicit_opaque_oracle_failure_is_not_a_target_exception(self):
        result = self.run_probe('observe', 'ProbeFixture', '', 'opaque', '', '0,0,0',
                                'beam-explicit-fixtures-v3-proposal')
        self.assertIn('SQA_FIXTURE_FAILURE:', result.stdout)
        self.assertNotIn('SQA_RESULT:', result.stdout)


if __name__ == '__main__':
    unittest.main()
