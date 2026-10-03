import json
from pathlib import Path
import tempfile
import unittest
from unittest.mock import patch

from scripts.study.api854 import prepare_owner_sources as target


class OwnerPreparationTests(unittest.TestCase):
    def setUp(self):
        self.temp=tempfile.TemporaryDirectory()
        self.addCleanup(self.temp.cleanup)
        self.root=Path(self.temp.name)
        self.install=self.root/'install'
        self.d4j=self.install/'framework/bin/defects4j'
        modified=self.install/'framework/projects/Chart/modified_classes/3.src'
        modified.parent.mkdir(parents=True)
        modified.write_text('p.Target\n')
        self.row={'project':'Chart','bug_id':3,'owner':'aom'}
        self.output=self.root/'evidence'
        self.output.mkdir()
        self.trees=self.root/'trees'

    def command(self,args,output,timeout,cwd=None):
        output.mkdir(parents=True)
        (output/'command.log').write_text('offline unit fixture\n')
        if 'checkout' in args:
            tree=Path(args[-1])
            (tree/'src/p').mkdir(parents=True)
            (tree/'.defects4j.config').write_text('pid=Chart\nvid=3f\n')
            (tree/'src/p/Target.java').write_text('package p; public class Target {}\n')
            (tree/'build.xml').write_text('<project/>\n')
            (tree/'failing_tests').write_text('PRIVATE_NOT_CONTEXT\n')
        if args[0]=='git' and 'rev-parse' in args:return 'a'*40
        return ''

    def run_prepare(self, callback=None):
        with patch.object(target,'command',side_effect=callback or self.command):
            return target.prepare_one(self.row,d4j=self.d4j,worktrees=self.trees,output=self.output,
                timeout=5,compile_timeout=5)

    def test_build_failure_keeps_verified_source_without_claiming_primary(self):
        def fail(args,output,timeout,cwd=None):
            result=self.command(args,output,timeout,cwd)
            if 'compile' in args:
                (output/'command.json').write_text(json.dumps({'timed_out':False,'exit_code':1}))
                raise RuntimeError('unit fixture compile failed')
            return result
        row=self.run_prepare(fail)
        self.assertTrue(row['source_prepared'])
        self.assertFalse(row['primary_usable'])
        self.assertEqual(row['compile_status'],'failed')
        self.assertNotIn('PRIVATE_NOT_CONTEXT',(self.output/'Chart-3/context/context.md').read_text())
        target.verify_artifacts(self.output/'Chart-3')

    def test_wrong_fixed_tag_refuses_source_and_compile(self):
        called=[]
        def wrong(args,output,timeout,cwd=None):
            called.append(args)
            result=self.command(args,output,timeout,cwd)
            return 'b'*40 if str(args[-1]).endswith('^{commit}') else result
        row=self.run_prepare(wrong)
        self.assertFalse(row['source_prepared'])
        self.assertFalse(any('compile' in args for args in called))
        self.assertFalse((self.output/'Chart-3/context').exists())

    def test_resume_audit_rejects_changed_saved_source(self):
        self.run_prepare()
        source=self.output/'Chart-3/context/fixed-source/src/p/Target.java'
        source.write_text('changed')
        with self.assertRaisesRegex(ValueError,'evidence changed'):
            target.verify_artifacts(self.output/'Chart-3')

    def test_summary_includes_missing_owner_bugs_and_zero_primary(self):
        self.run_prepare()
        result=target.summarize(self.output,[self.row,{**self.row,'bug_id':6}])
        self.assertEqual(result['statuses'],{'source_prepared':1,'not_attempted':1})
        self.assertEqual(result['primary_completed'],0)
        self.assertEqual(result['source_prepared'],1)


if __name__=='__main__':unittest.main()
