"""Derive isolated benchmark Java bytes from fixed Git and verified D4J patch.

Only a fresh contained scratch tree is changed. Actual installed production and
Defects4J framework bytes remain untouched; parent commits are metadata only.
"""
import csv
import os
from pathlib import Path
import subprocess

from .common import ROOT,sha256,write_json
from .review_joint_recipe_intake import require,digest


def derive(defects4j,project,bug,repository,fixed,buggy,destination,source_root,evidence,patch_tool=None):
    from .evaluate_csv_development import sources
    destination=Path(destination).resolve();evidence=Path(evidence).resolve()
    require(destination.is_relative_to(ROOT/'output') and evidence.is_relative_to(ROOT/'output'),'Contained scratch/evidence required')
    require(destination.is_dir() and not any(destination.iterdir()) and not evidence.exists(),'Fresh empty scratch/evidence required')
    evidence.mkdir(parents=True)
    framework=str(Path(defects4j).resolve());prefix=f'framework/projects/{project}/'
    patch=Path(framework)/(prefix+f'patches/{bug}.src.patch');metadata=Path(framework)/(prefix+'active-bugs.csv')
    commit=subprocess.check_output(['git','-C',framework,'rev-parse','HEAD']).decode().strip()
    raw=patch.read_bytes()
    require(raw==subprocess.check_output(['git','-C',framework,'show',commit+':'+prefix+f'patches/{bug}.src.patch']),
            'Installed official benchmark patch differs from framework Git')
    with metadata.open(encoding='utf-8') as stream:row=next(r for r in csv.DictReader(stream) if r['bug.id']==str(bug))
    require(row['revision.id.fixed']==fixed and row['revision.id.buggy']==buggy,'Active benchmark metadata differs')
    archive=sources(Path(framework)/('project_repos/'+repository),fixed,destination,source_root)
    before={p.relative_to(destination).as_posix():sha256(p) for p in sorted(destination.rglob('*.java'))}
    headers=[line.split() for line in raw.decode().splitlines() if line.startswith('diff --git ')]
    require(headers and all(len(h)==4 and h[2].startswith('a/') and h[3].startswith('b/')
            and h[2][2:] in before and h[3][2:]==h[2][2:] for h in headers),'Patch escapes exact fixed Java inventory')
    (evidence/'official.src.patch').write_bytes(raw);(evidence/'active-bugs.csv').write_bytes(metadata.read_bytes())
    env=dict(os.environ);env['GIT_CEILING_DIRECTORIES']=str(destination.parent)
    commands=[]
    for check in (True,False):
        if patch_tool:
            argv=[str(patch_tool),'--batch','--fuzz=0','-p1','-i',str(evidence/'official.src.patch')]
            if check:argv.append('--dry-run')
        else:
            argv=['git','-C',str(destination),'apply','--whitespace=nowarn']
            if check:argv.append('--check')
            argv.append(str(evidence/'official.src.patch'))
        result=subprocess.run(argv,cwd=destination,capture_output=True,env=env,timeout=60)
        name='check' if check else 'apply'
        (evidence/(name+'.stdout.log')).write_bytes(result.stdout);(evidence/(name+'.stderr.log')).write_bytes(result.stderr)
        commands.append({'argv':argv,'exit_code':result.returncode})
        require(result.returncode==0,'Exact official benchmark patch failed in scratch')
    after={p.relative_to(destination).as_posix():sha256(p) for p in sorted(destination.rglob('*.java'))}
    require(set(before)==set(after) and before!=after,'Patch did not change only the fixed source inventory')
    changed={p for p in before if before[p]!=after[p]}
    require(changed<={h[2][2:] for h in headers},'Patch changed unlisted production source')
    record={'project':project,'bug_id':bug,'source_mode':'fixed_git_plus_official_isolated_bug_patch',
        'framework_commit':commit,'fixed_revision':fixed,'buggy_parent_revision_metadata_only':buggy,
        'fixed_archive_sha256':archive,'official_patch_sha256':digest(raw),'official_patch_git_blob_verified':True,
        'active_bug_record':row,'active_bug_metadata_sha256':sha256(evidence/'active-bugs.csv'),
        'fixed_source_sha256':before,'expected_isolated_buggy_source_sha256':after,'changed_sources':sorted(changed),
        'commands':commands,'actual_production_source_replacement':False,'producer_sha256':sha256(Path(__file__))}
    if patch_tool:record['patch_tool_sha256']=sha256(Path(patch_tool))
    write_json(evidence/'derivation.json',record)
    return record
