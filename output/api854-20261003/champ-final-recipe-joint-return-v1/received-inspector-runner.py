import json, runpy, subprocess, sys
script, repository, mode, beam_commit, destination = sys.argv[1:6]
original_output, original_run = subprocess.check_output, subprocess.run
def at_repository(command, *args, **kwargs):
    if isinstance(command, list) and command[0] == 'git':
        command = ['git', '-C', repository, *command[1:]]
    return original_output(command, *args, **kwargs)
subprocess.check_output = at_repository
if mode == 'aom-buffer':
    sys.argv = [script, '--snapshot', '.', '--output', destination]
    runpy.run_path(script, run_name='__main__')
else:
    def pinned_run(command, *args, **kwargs):
        if isinstance(command, list) and command[0] == 'git':
            kwargs['cwd'] = repository
            if 'input' in kwargs:
                kwargs['input'] = kwargs['input'].replace(b'HEAD:', (beam_commit + ':').encode())
        return original_run(command, *args, **kwargs)
    subprocess.run = pinned_run
    module = runpy.run_path(script)
    result = module['audit']()
    with open(destination, 'x', encoding='utf-8', newline='\n') as stream:
        json.dump(result, stream, indent=2); stream.write('\n')
    print(json.dumps({k: result[k] for k in ['status', 'received_git_blobs_verified', 'packet_checksum_entries_verified', 'negative_controls_rejected']}))
