"""Read-only peer comparison and scoped Beam return; no shared recipe mutation."""
from pathlib import Path
from datetime import datetime, timezone
import copy
import hashlib
import io
import json
import subprocess

BASE = Path(__file__).resolve().parent
ROOT = BASE.parents[2]
AP = BASE/'received-aom/output/api854-20261003/aom-beam-buffer-verdict-intake-v1'
CP = BASE/'received-champ/output/api854-20261003/champ-six-message-joint-review-v3'
BUFFER = ROOT/'output/api854-20261003/beam-buffer-joint-review-v1'
LANG = ROOT/'output/api854-20261003/beam-final-recipe-return-v1'
FIELDS = ['project','bug_id','class','constructor_types','method','parameter_types']

def require(condition, message):
    if not condition: raise ValueError(message)
def sha(path): return hashlib.sha256(path.read_bytes()).hexdigest()
def read(path): return json.loads(path.read_text(encoding='utf-8'))
def key(row): return tuple(row['target'][k] for k in FIELDS)
def binding(path, commit):
    return {'commit':commit,'path':path.relative_to(ROOT).as_posix(),'sha256':sha(path)}
def write(name, data):
    with (BASE/name).open('x',encoding='utf-8',newline='\n') as stream:
        json.dump(data,stream,ensure_ascii=False,indent=2);stream.write('\n')


def compare(buffer, lang, old_buffer, old_lang, template):
    actual = {key(r):r for r in buffer['candidates']}
    original = {key(r):r for r in old_buffer['candidates']}
    require(len(actual)==len(buffer['candidates'])==8 and set(actual)==set(original), 'Exact buffer identity inventory differs')
    require(set(actual)=={key(r) for r in template['candidates']}, 'Aom template identity inventory differs')
    for identity, row in actual.items():
        require(row['champ_verdict']=='accepted_for_prospective_bounded_shared_recipe_composition', 'Missing Champ buffer verdict')
        for name, value in original[identity].items():
            if name!='champ_verdict': require(row[name]==value, 'Buffer recipe/oracle/evidence changed: '+name)
        require(row['accepted_into_shared_inputs'] is False, 'Candidate silently integrated')
    csv = buffer['csv_stream_condition_change']
    original_csv = old_buffer['csv_stream_condition_change']
    require(csv['champ_verdict']=='accepted_as_explicit_prospective_condition_change', 'Missing explicit Csv verdict')
    condition = {k:v for k,v in csv['agreed_condition'].items() if k!='scope'}
    require(condition==original_csv['beam_proposed_condition']==template['csv_stream_condition_change']['proposed_condition'],
            'Csv stream/receiver/domain agreement differs')
    require(len(condition['existing_selected_methods'])==5, 'Csv existing methods lost')
    require(buffer['joint_acceptance_complete'] is True, 'Buffer joint receipt not complete')
    require(buffer['gate_a_approved'] is False and buffer['team_or_primary_approval'] is False,
            'Bounded agreement incorrectly authorizes Gate A')
    lang_rows = {key(r):r for r in lang['candidates']}
    old_rows = {key(r):r for r in old_lang['candidates']}
    require(len(lang_rows)==len(lang['candidates'])==2 and set(lang_rows)==set(old_rows), 'Lang identity inventory differs')
    for name in ['fixed_source_sha256','reference_observations_sha256','reference_suite_sha256','reference_coverage_sha256']:
        require(lang[name]==old_lang[name], 'Lang evidence pin differs: '+name)
    for identity, row in lang_rows.items():
        require(row['champ_joint_verdict']=='accepted_for_prospective_bounded_development_composition', 'Missing Champ Lang verdict')
        require(old_rows[identity]['beam_joint_verdict']=='accepted_for_bounded_prospective_development_composition', 'Missing prior Beam Lang verdict')
        require(row['accepted_into_shared_inputs'] is False, 'Lang silently integrated')
        require('reflection' in row['agreed_preconditions'], 'Private invocation scope missing')
        if row['target']['method']=='validateArray':
            require('int[] only' in row['agreed_preconditions'] and 'no non-array Object' in row['agreed_preconditions'], 'Lang array domain broadened')
            require('exact documented message' in row['agreed_exception_oracle'] and 'unchanged' in row['agreed_exception_oracle'] and
                    'void plus unchanged contents' in row['agreed_exception_oracle'], 'Lang state/message oracle weakened')
        else:
            require('null=true' in row['agreed_exception_oracle'] and 'empty=false' in row['agreed_exception_oracle'] and
                    'nonempty all-zero=true' in row['agreed_exception_oracle'], 'Lang Boolean oracle weakened')


def audit():
    provenance = read(BASE/'provenance.json')
    specs = list(dict.fromkeys(p['source_commit']+':'+p['source_path'] for p in provenance))
    stream = io.BytesIO(subprocess.check_output(['git','cat-file','--batch'],cwd=ROOT,input=''.join(s+'\n' for s in specs).encode()))
    blobs = {}
    for spec in specs:
        header = stream.readline().split()
        require(len(header)==3 and header[1]==b'blob', 'Missing source Git blob')
        blobs[spec] = stream.read(int(header[2])); require(stream.read(1)==b'\n','Malformed Git batch')
    require(stream.read()==b'', 'Git batch tail')
    for item in provenance:
        require(hashlib.sha256(blobs[item['source_commit']+':'+item['source_path']]).hexdigest()==item['sha256'], 'Peer Git hash differs')
        require(sha(ROOT/item['received_path'])==item['sha256'], 'Received bytes differ')
    count = 0
    for base in [AP,CP]:
        for path, value in read(base/'checksums.json').items():
            require(sha(base/path)==value,'Peer packet checksum differs: '+path);count+=1
    old_buffer, old_lang = read(BUFFER/'beam-buffer-verdict.json'), read(LANG/'beam-lang-verdict.json')
    buffer, lang, template = read(CP/'champ-buffer-verdict.json'),read(CP/'champ-lang-verdict.json'),read(AP/'champ-buffer-return.template.json')
    require(sha(BUFFER/'beam-buffer-verdict.json')==template['beam_verdict_sha256']==buffer['received_beam_verdict']['sha256'], 'Buffer binding differs')
    require(sha(BUFFER/'reference/preexecution-seal.json')==template['preexecution_seal_sha256'], 'Reference seal binding differs')
    require(sha(BUFFER/'reference/receipt.json')==template['reference_receipt_sha256'], 'Reference receipt binding differs')
    require(sha(AP/'audit/receipt.json')==template['aom_receipt_sha256'], 'Aom audit binding differs')
    require((AP/'received/beam-buffer-verdict.json').read_bytes()==(BUFFER/'beam-buffer-verdict.json').read_bytes(), 'Aom received verdict changed')
    aom_audit = read(AP/'audit/receipt.json')
    require(aom_audit['independently_checked_cases']==42 and aom_audit['fixed_observations']==84 and
            aom_audit['historical_cmaes_string_append_entry_hits']==0 and aom_audit['historical_fscs_string_append_entry_hits']==2,
            'Historical Buffer observations/coverage changed')
    require(sha(BASE/'received-champ/scripts/study/api854/review_joint_recipe_intake.py')==read(CP/'receipt.json')['reviewer_source_sha256'], 'Champ producer pin differs')
    compare(buffer,lang,old_buffer,old_lang,template)
    for row in old_buffer['candidates']:
        for item in row['evidence']: require(sha(ROOT/item['path'])==item['sha256'],'Historical Beam evidence differs')
    for project in ['JacksonCore','Csv']:
        require(read(BUFFER/'reference'/project/'evaluation/record.json')['fault_detected'] is False,
                'Historical reference fault label changed')
    for row in lang['candidates']:
        for item in row['evidence']: require(sha(ROOT/item['path'])==item['sha256'],'Lang reference evidence differs')
    for path,value in old_lang['current_runtime_source_sha256'].items():
        require(sha(ROOT/path)==value, 'Current Beam runtime changed')
    negative = []
    controls = [
        ('wrong_buffer_overload',lambda b,l:b['candidates'][0]['target'].update(parameter_types='int')),
        ('changed_buffer_preconditions',lambda b,l:b['candidates'][0].update(preconditions='any input')),
        ('changed_csv_stream',lambda b,l:b['csv_stream_condition_change']['agreed_condition']['streams'].update(negative_vector_0='other')),
        ('missing_champ_verdict',lambda b,l:b['candidates'][0].update(champ_verdict=None)),
        ('weakened_lang_state_oracle',lambda b,l:next(c for c in l['candidates'] if c['target']['method']=='validateArray').update(agreed_exception_oracle='void')),
        ('broadened_lang_array_domain',lambda b,l:next(c for c in l['candidates'] if c['target']['method']=='validateArray').update(agreed_preconditions='reflection for any Object')),
        ('improper_gate_approval',lambda b,l:b.update(gate_a_approved=True)),
    ]
    for label, mutate in controls:
        b,l=copy.deepcopy(buffer),copy.deepcopy(lang);mutate(b,l)
        try: compare(b,l,old_buffer,old_lang,template)
        except ValueError: negative.append(label)
        else: raise ValueError('Invalid agreement accepted: '+label)
    commits = {label:next(p['source_commit'] for p in provenance if '/received-'+label+'/' in '/'+p['received_path']) for label in ['aom','champ']}
    return {'status':'pass','source_commits':commits,'received_git_blobs_verified':len(provenance),
            'peer_packet_checksum_entries_verified':count,'buffer_signatures_agreed':8,'csv_existing_methods_affected':5,
            'lang_signatures_agreed':2,'historical_runtime_pins_unchanged':41,'negative_controls_rejected':negative,
            'historical_cmaes_string_append_entry_hits':0,'historical_fscs_string_append_entry_hits':2,
            'no_recipe_or_domain_delta':True,'new_defects4j_runs':0,'live_kku_requests':0,'live_queue_mutations':0,
            'primary_results_added':0,'gate_a_approved':False,'shared_preparation_modified':False,
            'producer_sha256':sha(Path(__file__))}


def produce():
    result = audit()
    commits = result['source_commits']
    buffer, lang = read(CP/'champ-buffer-verdict.json'),read(CP/'champ-lang-verdict.json')
    old_lang = read(LANG/'beam-lang-verdict.json')
    beam_commit = subprocess.check_output(['git','rev-parse','HEAD'],cwd=ROOT,text=True).strip()
    common = {'reviewer':'beam','reviewer_base_commit':beam_commit,'checked_at_utc':datetime.now(timezone.utc).isoformat(),
              'source_commits':commits,'gate_a_approved':False,'team_or_primary_approval':False,
              'new_shared_preparation':False,'shared_runtime_modified':False,'final_prompt_reserve':None,
              'reviewer_runtime_source_sha256':old_lang['current_runtime_source_sha256'],
              'primary_results_added':0,'live_kku_requests':0,'live_queue_mutations':0}
    returned = read(AP/'champ-buffer-return.template.json')
    rows = {key(r):r for r in buffer['candidates']}
    for row in returned['candidates']:
        peer = rows[key(row)]
        row.update(champ_verdict=peer['champ_verdict'],accepted_preconditions=peer['preconditions'],
                   accepted_oracle=peer['meaningful_oracle'],evidence=[binding(CP/'champ-buffer-verdict.json',commits['champ']),
                    *peer['evidence']],accepted_into_shared_inputs=False)
    returned['csv_stream_condition_change'].update(champ_verdict=buffer['csv_stream_condition_change']['champ_verdict'],
        agreed_condition=buffer['csv_stream_condition_change']['agreed_condition'],
        evidence=[binding(CP/'champ-buffer-verdict.json',commits['champ'])])
    returned.update(common,example_only=False,review_status='beam_confirms_exact_buffer_csv_component_agreement',
                    joint_acceptance=True,scope='Exactly Buffer 8/Csv bounded prospective development; final condition not composed',
                    received_champ_verdict=binding(CP/'champ-buffer-verdict.json',commits['champ']),
                    received_aom_review=binding(AP/'audit/receipt.json',commits['aom']))
    write('beam-buffer-csv-joint-confirmation.json',returned)
    lang_return = copy.deepcopy(lang)
    old_rows={key(row):row for row in old_lang['candidates']}
    for row in lang_return['candidates']:
        original=old_rows[key(row)]
        row.update(beam_joint_verdict=original['beam_joint_verdict'],
                   agreed_preconditions='Private static helper invoked by reflection. '+original['beam_proposed_preconditions'],
                   agreed_exception_oracle=original['beam_proposed_oracle'],accepted_into_shared_inputs=False,
                   received_champ_preconditions=row['agreed_preconditions'],received_champ_oracle=row['agreed_exception_oracle'],
                   evidence=[*row['evidence'],binding(LANG/'beam-lang-verdict.json',beam_commit),
                             binding(CP/'champ-lang-verdict.json',commits['champ'])])
    lang_return.update(common,example_only=False,review_status='beam_and_champ_bounded_lang_component_agreement_confirmed',
                       joint_acceptance_complete=True,new_shared_preparation_created=False,
                       reviewer_commit=beam_commit,received_champ_reviewer_commit=lang['reviewer_commit'],
                       received_champ_reviewer_runtime_source_sha256=lang['reviewer_runtime_source_sha256'],
                       beam_current_runtime_source_sha256=old_lang['current_runtime_source_sha256'],
                       received_champ_verdict=binding(CP/'champ-lang-verdict.json',commits['champ']),
                       received_beam_verdict=binding(LANG/'beam-lang-verdict.json',beam_commit),
                       preparation_authorization_scope='Prospective Lang two private helpers only; same bounded domain/oracle across four approaches; no primary/live approval')
    write('beam-lang-joint-confirmation.json',lang_return)
    result.update(checked_at_utc=common['checked_at_utc'],reviewer_base_commit=beam_commit,
                  buffer_confirmation_sha256=sha(BASE/'beam-buffer-csv-joint-confirmation.json'),
                  lang_confirmation_sha256=sha(BASE/'beam-lang-joint-confirmation.json'),
                  proposed_union={'selected':390,'exclusions':301,'denominator':691,'implemented':False,
                                  'basis':'shared v9 380 + Buffer 8 + Lang 2; preserve existing setter/JDOM/Math; excludes Chronology'})
    write('receipt.json',result)
    print(json.dumps({k:result[k] for k in ['status','received_git_blobs_verified','peer_packet_checksum_entries_verified',
                                          'buffer_signatures_agreed','lang_signatures_agreed','negative_controls_rejected']}))


if __name__=='__main__': produce()
