import unittest

from atcg import CMAES, CMAESConfig, FSCSART, FSCSARTConfig


class GeneratorTests(unittest.TestCase):
    def test_fscs_art_is_seed_deterministic_and_bounded(self):
        config = FSCSARTConfig((0.0, -1.0), (2.0, 1.0), candidate_set_size=5, seed=7)
        first = FSCSART(config).generate(8)
        self.assertEqual(first, FSCSART(config).generate(8))
        self.assertTrue(all(0.0 <= point[0] <= 2.0 and -1.0 <= point[1] <= 1.0 for point in first))

    def test_cmaes_converges_on_bounded_sphere(self):
        search = CMAES(CMAESConfig((-5.0, -5.0), (5.0, 5.0), seed=7, population_size=8))
        point, score = search.minimize(lambda values: (values[0] - 1.0) ** 2 + (values[1] + 2.0) ** 2, 60)
        self.assertLess(score, 0.05)
        self.assertLess(abs(point[0] - 1.0), 0.3)
        self.assertLess(abs(point[1] + 2.0), 0.3)

    def test_cmaes_rejects_invalid_population_size(self):
        with self.assertRaises(ValueError):
            CMAESConfig((0.0,), (1.0,), population_size=1)
