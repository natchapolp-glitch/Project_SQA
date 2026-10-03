"""The receipt-backed policy delta must stay one signature, with no enum promotion."""
import unittest

from scripts.study.api854.common import ROOT, read_json
from scripts.study.api854.fixture_policy import POLICY_V5, POLICY_V6, select
from scripts.study.api854.preparation import clean_targets


def identities(targets):
    return {tuple(row[key] for key in ('class','constructor_types','method','parameter_types'))
        for row in clean_targets(targets)}


class RecipeV8PolicyTests(unittest.TestCase):
    def test_inventory_delta_is_exactly_one_setter_and_prior_policy_keeps_314(self):
        prior, current, all_targets = set(), set(), set()
        base = ROOT/'output/api854-20261003/prepare-v3'
        for row in read_json(base/'index.json')['records']:
            targets = read_json(base/f"{row['project']}-{row['bug_id']}"/'targets.json')['targets']
            # Include bug identity because the same signature can occur in two bugs.
            key = (row['project'],row['bug_id'])
            prior |= {key+identity for identity in identities(select(targets,POLICY_V5)[0])}
            current |= {key+identity for identity in identities(select(targets,POLICY_V6)[0])}
            all_targets |= {key+identity for identity in identities(targets)}
        self.assertEqual(len(all_targets),691)
        self.assertEqual(len(prior),377)
        self.assertEqual(len(current),378)
        self.assertEqual(prior-current,set())
        self.assertEqual(current-prior,{('Codec',1,'org.apache.commons.codec.language.Metaphone',
            '', 'setMaxCodeLen','int')})

    def test_empty_enum_signatures_are_still_excluded(self):
        targets = read_json(ROOT/'output/api854-20261003/prepare-v3/JacksonXml-1/targets.json')['targets']
        empty = [t for t in targets if 'FromXmlParser$Feature' in t['parameter_types']]
        self.assertEqual(len(empty),4)
        selected, excluded = select(empty,POLICY_V6)
        self.assertEqual(selected,[])
        self.assertEqual(len(excluded),4)

    def test_setter_receipt_does_not_approve_other_parameter_or_receiver_shapes(self):
        target = {'class':'org.apache.commons.codec.language.Metaphone','constructor_types':'',
            'method':'setMaxCodeLen','parameter_types':'int'}
        wrong = [{**target,'parameter_types':'long'},{**target,'constructor_types':'int'}]
        self.assertEqual(select([target,*wrong],POLICY_V6)[0],[target])


if __name__=='__main__':
    unittest.main()
