import runpy, subprocess, sys
script, repository = sys.argv[1:3]
original = subprocess.check_output
def at_repository(command, *args, **kwargs):
    if isinstance(command, list) and command[0] == "git":
        command = ["git", "-C", repository, *command[1:]]
    return original(command, *args, **kwargs)
subprocess.check_output = at_repository
sys.argv = [script, *sys.argv[3:]]
runpy.run_path(script, run_name="__main__")
