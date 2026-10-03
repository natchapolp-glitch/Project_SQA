"""Reproduce the Cli differential failure and independently compare option content.

Retains original suites/results; never repairs assertions or changes shared oracle.
"""
import argparse
import base64
import json
import os
from pathlib import Path
import re
import tempfile

from .common import ROOT,read_json,write_json,sha256
from .review_joint_recipe_intake import require,extract_archive
from .review_v10_readiness import BatchedObjects
from .evaluate_csv_development import PROJECTS,sources,command,production_dependencies
from .start_csv_development import AOM

BASE=ROOT/'output/api854-20261004/champ-cli-native-measurement-v1'


def content(observation):
    parsed=re.fullmatch(r'void\|state=cli:array\[(.*)\]:array\[java\.lang\.String:([A-Za-z0-9+/=]+);\]',observation)
    require(parsed is not None,'Unexpected diagnostic observation grammar')
    option_text=parsed[1]
    rows=re.findall(r'option:([^:;]+):array\[java\.lang\.String:([A-Za-z0-9+/=]+);\];',option_text)
    require(len(rows)==2 and ''.join('option:'+k+':array[java.lang.String:'+v+';];' for k,v in rows)==option_text,
            'Diagnostic contains unsupported/extra state')
    return {'ordered_options':[(k,base64.b64decode(v,validate=True).decode()) for k,v in rows],
            'option_content':sorted((k,base64.b64decode(v,validate=True).decode()) for k,v in rows),
            'positional_arguments':[base64.b64decode(parsed[2],validate=True).decode()]}


def run(output,defects4j):
    output=Path(output).resolve();defects4j=Path(defects4j).resolve()
    require(output.is_relative_to(ROOT/'output') and not output.exists(),'New contained output required')
    for relative,h in read_json(BASE/'checksums.json').items():require(sha256(BASE/relative)==h,'Original native packet changed')
    original=read_json(BASE/'fscs-art/receipt.json')
    require(original['fault_detected'] is True and original['stages']['buggy']['counts']['failed']==1,'Original differential result differs')
    java=(BASE/'fscs-art/sources/GeneratedStudyTest.java').read_text(encoding='utf-8')
    method=re.search(r'public void generated16\(\) \{(.*?)\n  \}',java,re.DOTALL)
    require(method is not None,'Exact triggering method missing')
    expected=re.search(r'assertEquals\("([^"]+)"',method[1])[1]
    vector=json.loads('['+re.search(r'new double\[\]\{([^}]+)\}',method[1])[1]+']')
    diagnostic='''public class CliOrderDiagnostic {
 public static void main(String[] args) {
  System.out.println("CLI_DIAGNOSTIC:"+SqaProbe.observeWithPolicy(
    "org.apache.commons.cli.CommandLine", "", "addOption", "org.apache.commons.cli.Option",
    new double[]{%s}, "aom-beam-champ-graphics-fixtures-v12-development"));
 }
}
'''%','.join(map(str,vector))
    output.mkdir(parents=True);(output/'producer.py').write_bytes(Path(__file__).read_bytes())
    (output/'CliOrderDiagnostic.java').write_text(diagnostic,encoding='utf-8',newline='\n')
    native_seal=read_json(BASE/'preexecution-seal.json');deps=production_dependencies(defects4j,'Cli')
    write_json(output/'preexecution-plan.json',{'condition':original['condition'],'purpose':'Post-result semantic audit of unordered options, not scientific assertion repair',
        'original_receipt_sha256':sha256(BASE/'fscs-art/receipt.json'),'original_native_manifest_sha256':sha256(BASE/'checksums.json'),
        'triggering_method':'GeneratedStudyTest.generated16','target':'CommandLine.addOption(Option)','input_vector':vector,
        'original_fixed_assertion':expected,'aom_commit':AOM,'diagnostic_sha256':sha256(output/'CliOrderDiagnostic.java'),
        'production_dependencies_sha256':{p.relative_to(defects4j).as_posix():sha256(p) for p in deps},
        'requests_cap':0,'primary_results_allowed':False,'do_not_count_mismatch_as_confirmed_fault':True})
    observations={};aom=BatchedObjects(AOM)
    with tempfile.TemporaryDirectory(prefix='.champ-cli-order-audit-',dir=ROOT/'output') as folder:
        temp=Path(folder).resolve();require(temp.is_relative_to(ROOT/'output'),'Unsafe cleanup path')
        helper=temp/'helper';helper.mkdir();extract_archive(AOM,['algorithms/java/SqaProbe.java'],helper)
        require(sha256(helper/'algorithms/java/SqaProbe.java')==native_seal['runtime_source_sha256']['algorithms/java/SqaProbe.java'],'Helper pin differs')
        helperclasses=temp/'helperclasses';helperclasses.mkdir()
        _,r=command(['javac','--release','8','-d',helperclasses,helper/'algorithms/java/SqaProbe.java',output/'CliOrderDiagnostic.java'],temp,output,'compile-diagnostic')
        require(r['exit_code']==0,'Diagnostic compile failed')
        repo,source_root,_,fixed,buggy,_=PROJECTS['Cli']
        for version,revision in (('fixed',fixed),('buggy',buggy)):
            base=temp/version;base.mkdir();h=sources(defects4j/('project_repos/'+repo),revision,base,source_root)
            require(h==native_seal['production_source_sha256'][version]['archive_sha256'],'Production archive differs')
            for relative,expected_hash in native_seal['production_source_sha256'][version]['sources'].items():
                require(sha256(base/relative)==expected_hash,'Production source differs')
            classes=base/'classes';classes.mkdir()
            _,r=command(['javac','--release','7','-g','-cp',os.pathsep.join(map(str,deps)),'-d',classes,*sorted((base/source_root).rglob('*.java'))],temp,output,'compile-'+version)
            require(r['exit_code']==0,'Exact production compile failed')
            for repeat in (1,2):
                raw,r=command(['java','-Duser.timezone=UTC','-cp',os.pathsep.join(map(str,[classes,helperclasses,*deps])),'CliOrderDiagnostic'],temp,output,version+'-'+str(repeat))
                require(r['exit_code']==0,'Diagnostic execution failed')
                lines=[l.decode().split(':',1)[1] for l in raw.splitlines() if l.startswith(b'CLI_DIAGNOSTIC:')]
                require(len(lines)==1,'Missing actual diagnostic')
                observations[version+'-'+str(repeat)]={'raw':lines[0],'decoded':content(lines[0])}
    require(observations['fixed-1']==observations['fixed-2'] and observations['buggy-1']==observations['buggy-2'],'Diagnostic not repeatable')
    require(observations['fixed-1']['raw']==expected and observations['buggy-1']['raw']!=expected,'Exact failure not reproduced')
    fixed=observations['fixed-1']['decoded'];buggy=observations['buggy-1']['decoded']
    require(fixed['option_content']==buggy['option_content']==[('extra','left'),('x','alpha')],'Option contents differ')
    require(fixed['positional_arguments']==buggy['positional_arguments']==['positional'],'Arguments differ')
    require(fixed['ordered_options']!=buggy['ordered_options'],'Order mismatch missing')
    receipt={'status':'oracle_order_false_positive_quarantined','condition':original['condition'],
        'raw_native_fault_flag_retained':True,'raw_buggy_failed_tests':1,'confirmed_semantic_fault':False,
        'observations':observations,'finding':'Only option iteration order differs; mappings/values/arguments match. Fixed uses HashSet and buggy HashMap; target source promises no option order.',
        'decision':'Do not count this failure as a detected bug. Quarantine Cli fault comparison until a prospective oracle condition handles unordered options.',
        'unchanged_original_suites':True,'primary_results_added':0,'kku_requests':0,'queue_mutations':0,
        'limitations':['This disproves semantic-fault interpretation of this assertion only, not existence of Cli-1 bug',
                       'Independent comparison covers the sealed two-option fixture, not arbitrary option states']}
    write_json(output/'receipt.json',receipt)
    write_json(output/'checksums.json',{p.relative_to(output).as_posix():sha256(p) for p in sorted(output.rglob('*')) if p.is_file()})
    return receipt


if __name__=='__main__':
    parser=argparse.ArgumentParser(description=__doc__);parser.add_argument('--output',type=Path,required=True)
    parser.add_argument('--defects4j',type=Path,required=True);args=parser.parse_args()
    try:r=run(args.output,args.defects4j)
    except Exception as error:
        out=args.output.resolve()
        if out.is_relative_to(ROOT/'output') and out.exists() and not (out/'checksums.json').exists():
            write_json(out/'failed-attempt.json',{'status':'diagnostic_attempt_failed','error_type':type(error).__name__,'error':str(error),'primary_results_added':0})
            write_json(out/'checksums.json',{p.relative_to(out).as_posix():sha256(p) for p in sorted(out.rglob('*')) if p.is_file()})
        raise
    print(json.dumps({'status':r['status'],'confirmed_semantic_fault':r['confirmed_semantic_fault']}))
