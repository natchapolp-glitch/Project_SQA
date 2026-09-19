import unittest

import generate_lang1_junit_suite


class JUnitSuiteGenerationTests(unittest.TestCase):
    def test_exception_difference_generates_type_and_message_assertions(self):
        rows = [{
            "input": "bad",
            "different": True,
            "fixed": {
                "status": "exception",
                "type": "java.lang.NumberFormatException",
                "message": "bad input",
            },
        }]

        source, test_count = generate_lang1_junit_suite.render_suite(rows, "GeneratedTest")

        self.assertEqual(test_count, 1)
        self.assertIn("Throwable observed = null;", source)
        self.assertIn('"java.lang.NumberFormatException"', source)
        self.assertIn('"bad input"', source)

    def test_null_value_and_duplicate_input_generate_one_safe_test(self):
        row = {
            "input": "",
            "different": True,
            "fixed": {"status": "value", "type": "null", "value": ""},
        }

        source, test_count = generate_lang1_junit_suite.render_suite([row, row], "GeneratedTest")

        self.assertEqual(test_count, 1)
        self.assertIn('actual == null ? "null"', source)
        self.assertIn('"exception:" + observed.getClass().getName()', source)
        self.assertEqual(source.count("public void generatedCase"), 1)
