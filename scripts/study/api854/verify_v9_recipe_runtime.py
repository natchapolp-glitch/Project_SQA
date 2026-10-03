"""Exercise the composed shared helper against exact fixed production source, offline.

This checks recipe integration, not a Defects4J evaluation or primary result.
Dependencies are read from an existing Defects4J installation; no downloads.
"""
import argparse
import base64
import csv
import io
import json
import os
from pathlib import Path
import subprocess
import tarfile
import tempfile

from .common import ROOT, read_json, sha256, write_json
from .preparation import digest

V5 = 'beam-explicit-fixtures-v5-proposal'
V6 = 'beam-explicit-fixtures-v7-development'


def run(command, **kwargs):
    result = subprocess.run(command, capture_output=True, timeout=90, **kwargs)
    if result.returncode:
        raise ValueError(result.stderr.decode('utf-8', errors='replace'))
    return result.stdout


def extract_sources(repository, revision, destination, prefix='src/java'):
    raw = run(['git', '--git-dir='+str(repository), 'archive', revision, prefix])
    with tarfile.open(fileobj=io.BytesIO(raw)) as archive:
        for member in archive.getmembers():
            if member.isfile() and member.name.endswith('.java'):
                path = (destination/member.name).resolve()
                if not path.is_relative_to(destination.resolve()):
                    raise ValueError('Archive source escapes destination')
                path.parent.mkdir(parents=True, exist_ok=True)
                path.write_bytes(archive.extractfile(member).read())


def observe(classes, jars, target, vector, policy):
    raw = run(['java', '-cp', os.pathsep.join(map(str, [classes, *jars])), 'SqaProbe',
        'observe', target['class'], target['constructor_types'], target['method'],
        target['parameter_types'], ','.join(map(str,vector)), policy]).decode('utf-8')
    if 'SQA_FIXTURE_FAILURE:' in raw:
        return {'fixture_error':base64.b64decode(raw.split('SQA_FIXTURE_FAILURE:')[1].strip()).decode('utf-8')}
    return {'outcome':base64.b64decode(raw.split('SQA_RESULT:')[1].strip()).decode('utf-8'),
        **json.loads(raw.split('SQA_TRACE:')[1].splitlines()[0])}


def verify(defects4j):
    defects4j = Path(defects4j).resolve()
    targets = {
        'setter':{'class':'org.apache.commons.codec.language.Metaphone', 'constructor_types':'',
            'method':'setMaxCodeLen', 'parameter_types':'int'},
        'jdom':{'class':'org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer',
            'constructor_types':'java.lang.Object,java.util.Locale', 'method':'attributeIterator',
            'parameter_types':'org.apache.commons.jxpath.ri.QName'}}
    jars = sorted((defects4j/'framework/projects/JxPath/lib').rglob('*.jar'))
    if not jars or not (defects4j/'project_repos/commons-codec.git').exists():
        raise ValueError('An initialized local Defects4J installation is required')
    with tempfile.TemporaryDirectory() as temporary:
        temp = Path(temporary)
        sources = []
        dependency_revisions = {}
        for project, repo, source in (
            ('Codec','commons-codec.git','src/java/org/apache/commons/codec/language/Metaphone.java'),
            ('JxPath','commons-jxpath.git','src/java/org/apache/commons/jxpath/ri/model/jdom/JDOMNodePointer.java'),
            ('Math','commons-math.git','src/main/java/org/apache/commons/math3/fraction/BigFraction.java')):
            folder = temp/project
            # Defects4J's synthetic checkout tag is local to the original host;
            # its underlying fixed production commit is available in the mirror.
            with (defects4j/f'framework/projects/{project}/commit-db').open(encoding='utf-8') as stream:
                revision = next(row[2] for row in csv.reader(stream) if row[0] == '1')
            dependency_revisions[project] = revision
            extract_sources(defects4j/'project_repos'/repo, revision, folder,
                'src/main/java' if project=='Math' else 'src/java')
            retained = ROOT/f'output/api854-20261003/prepare-v3/{project}-1/fixed-source'/source
            if (folder/source).read_bytes().replace(b'\r\n',b'\n') != retained.read_bytes().replace(b'\r\n',b'\n'):
                raise ValueError('Fixed production source content differs from retained preparation: '+project)
            # Native git archive can apply CRLF conversion. Compile the exact
            # retained bytes after checking source equivalence to the fixed mirror.
            (folder/source).write_bytes(retained.read_bytes())
            if sha256(folder/source) != sha256(retained):
                raise ValueError('Fixed production source differs from retained preparation')
            sources.append(folder/source)
        math_files = {}
        for cls in ('BigFraction','Fraction','BigFractionField','FractionField'):
            relative = f'src/main/java/org/apache/commons/math3/fraction/{cls}.java'
            source = temp/'Math'/relative
            original = (ROOT/'docs/api854/evidence/beam-champ-math-field-20261003-attempt2/supplemental-fixed-source'/(cls+'.java')
                if cls.endswith('Field') else ROOT/'output/api854-20261003/prepare-v3/Math-1/fixed-source'/relative)
            if source.read_bytes().replace(b'\r\n',b'\n') != original.read_bytes().replace(b'\r\n',b'\n'):
                raise ValueError('Math fixed source/factory differs: '+cls)
            source.write_bytes(original.read_bytes())
            math_files[original.relative_to(ROOT).as_posix()] = sha256(original)
            if source not in sources:
                sources.append(source)
        classes = temp/'classes'
        classes.mkdir()
        helper = ROOT/'algorithms/java/SqaProbe.java'
        run(['javac','--release','8','-cp',os.pathsep.join(map(str,jars)),
            '-sourcepath',os.pathsep.join([str(temp/p/'src/java') for p in ('Codec','JxPath')]+[str(temp/'Math/src/main/java')]),
            '-d',str(classes),str(helper),*map(str,sources)])
        observations = []
        for raw, limit in ((-12,0),(-4,1),(4,4),(12,8)):
            first = observe(classes,jars,targets['setter'],[raw,0,0],V6)
            second = observe(classes,jars,targets['setter'],[raw,0,0],V6)
            if first != second or first.get('target_invoked') is not True:
                raise ValueError('Setter observations must repeat and invoke the target')
            prefix = f'void|state=metaphone:maxCodeLen={limit}:encoded='
            ending = f':maxCodeLenAfterEncoding={limit}'
            if not first['outcome'].startswith(prefix) or not first['outcome'].endswith(ending):
                raise ValueError('Setter getter/state oracle differs')
            encoded = first['outcome'][len(prefix):-len(ending)]
            if len(encoded) > limit:
                raise ValueError('Encoding violates prospective bound')
            observations.append({'target':targets['setter'],'vector':[raw,0,0], 'limit':limit,
                'fixed_first':first,'fixed_second':second})
        for raw in (-0.5,0.5):
            first = observe(classes,jars,targets['jdom'],[raw]*9,V6)
            second = observe(classes,jars,targets['jdom'],[raw]*9,V6)
            expected = base64.b64encode(('left' if raw < 0 else 'right').encode()).decode()
            if (first != second or first.get('target_invoked') is not True
                    or 'jdom-attribute:name=java.lang.String:aWQ=' not in first.get('outcome','')
                    or ':namespace=java.lang.String:' not in first['outcome']
                    or ':value=java.lang.String:'+expected not in first['outcome']):
                raise ValueError('JDOM attribute projection differs')
            observations.append({'target':targets['jdom'],'vector':[raw]*9,
                'fixed_first':first,'fixed_second':second})
        if observations[-1]['fixed_first'] == observations[-2]['fixed_first']:
            raise ValueError('Attribute oracle cannot distinguish fixture values')
        for cls in ('BigFraction','Fraction'):
            target = {'class':'org.apache.commons.math3.fraction.'+cls,'constructor_types':'double',
                'method':'getField','parameter_types':''}
            for value, rational in ((0.5,'7/4'),(-0.5,'-3/4')):
                first = observe(classes,jars,target,[value]*3,V6)
                second = observe(classes,jars,target,[value]*3,V6)
                expected = ('value:fraction-field:runtime=type:org.apache.commons.math3.fraction.'+cls
                    +':zero=fraction:0/1:one=fraction:1/1|state=fraction:'+rational)
                if first != second or first.get('target_invoked') is not True or first.get('outcome') != expected:
                    raise ValueError('Math field factory/state projection differs')
                observations.append({'target':target,'vector':[value]*3,'fixed_first':first,'fixed_second':second})
            legacy_field = observe(classes,jars,target,[0.5]*3,'beam-explicit-fixtures-v6-development')
            if legacy_field.get('fixture_error') != 'No structural oracle: org.apache.commons.math3.fraction.'+cls+'Field':
                raise ValueError('Previous v6 Math field behavior changed')
        legacy_setter = observe(classes,jars,targets['setter'],[4,0,0],V5)
        legacy_jdom = observe(classes,jars,targets['jdom'],[0.5]*9,V5)
        if (legacy_setter.get('outcome') != 'void|state=stateless-scalars'
                or legacy_jdom.get('fixture_error') != 'No structural oracle: org.jdom.Attribute'):
            raise ValueError('Previous fixture policy behavior changed')
        # A no-op setter in a temporary source copy must be visible to the oracle.
        setter_source = sources[0]
        text = setter_source.read_text(encoding='utf-8')
        if 'this.maxCodeLen = maxCodeLen;' not in text:
            raise ValueError('Fixed setter source changed')
        setter_source.write_text(text.replace('this.maxCodeLen = maxCodeLen;',
            'this.maxCodeLen = 4;'), encoding='utf-8')
        run(['javac','--release','8','-cp',str(classes),'-d',str(classes),str(setter_source)])
        mutated = observe(classes,jars,targets['setter'],[-12,0,0],V6)
        if mutated == observations[0]['fixed_first']:
            raise ValueError('Setter mutation was invisible to the state oracle')
        return {'status':'pass','scope':'Offline fixed-source shared recipe integration; not Defects4J evaluation',
            'runtime_helper_sha256':sha256(helper), 'verifier_sha256':sha256(__file__),
            'dependency_fixed_revisions':dependency_revisions,
            'math_source_and_factory_sha256':math_files,
            'production_source_sha256':{p:sha256(ROOT/p) for p in (
                'output/api854-20261003/prepare-v3/Codec-1/fixed-source/src/java/org/apache/commons/codec/language/Metaphone.java',
                'output/api854-20261003/prepare-v3/JxPath-1/fixed-source/src/java/org/apache/commons/jxpath/ri/model/jdom/JDOMNodePointer.java')},
            'dependency_sha256':{p.relative_to(defects4j).as_posix():sha256(p) for p in jars},
            'policy':V6,'observations':observations,'legacy_policy_behavior_preserved':True,
            'temporary_setter_mutation_detected':True,'mutated_observation':mutated,
            'primary':False,'team_semantic_approval':False,'gate_a_passed':False,
            'live_requests':0,'queue_mutations':0}


if __name__=='__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--defects4j',type=Path,required=True)
    parser.add_argument('--output',type=Path,required=True)
    args = parser.parse_args()
    result = verify(args.defects4j)
    write_json(args.output,result)
    print({'status':result['status'],'repeated_fixed_cases':len(result['observations']),
        'legacy_policy_behavior_preserved':True,'temporary_setter_mutation_detected':True})
