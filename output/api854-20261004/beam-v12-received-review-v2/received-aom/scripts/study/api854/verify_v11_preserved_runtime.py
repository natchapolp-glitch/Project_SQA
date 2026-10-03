"""Re-exercise the 64 retained setter/JDOM/Math/Buffer/Lang cases under v11."""
import argparse
from pathlib import Path
from . import verify_v10_recipe_runtime as retained
from .common import sha256
from .preparation import encoded
from .fixture_policy import POLICY_V11


if __name__=='__main__':
    p=argparse.ArgumentParser(description=__doc__);p.add_argument('--defects4j',type=Path,required=True)
    p.add_argument('--output',type=Path,required=True);a=p.parse_args()
    if a.output.exists(): p.error('Choose a new evidence destination')
    retained.V6=POLICY_V11
    result=retained.verify(a.defects4j)
    result.update(component_fixture_policy_id=POLICY_V11,policy_adapter_sha256=sha256(__file__))
    a.output.parent.mkdir(parents=True,exist_ok=True)
    a.output.write_bytes(encoded(result))
    print({k:result[k] for k in ('status','fixed_source_integration_cases','component_fixture_policy_id')})
