
import json,pathlib,sys
snapshot,targets,classpath,output,approach,project,bug=sys.argv[1:]
sys.path.insert(0,str(pathlib.Path(snapshot)/'scripts/study'))
from generate import generate_suite
rows=json.loads(pathlib.Path(targets).read_text(encoding='utf-8'))['targets']
r=generate_suite(project,int(bug),approach,30,101,rows,classpath,pathlib.Path(output),10,
                fixture_policy='aom-beam-champ-graphics-fixtures-v12-development')
print(json.dumps(r));sys.exit(0 if r['test_count'] else 1)
