"""Download and bind shared-v3 preparation identically for CPU and API workers."""
import json
from .preparation import explicit_context, shared_context, digest, validate


def load(client, job, protocol):
    histories = job.get("payload", {}).get("stage_history", [])
    prepared = next((h for h in reversed(histories) if h.get("stage") == "prepare" and h.get("outcome") == "prepared"), None)
    if not prepared:
        raise ValueError("Successful preparation is required")
    def download(name):
        found = [a for a in prepared.get("artifacts", []) if a.get("name") == name]
        if len(found) != 1 or type(found[0].get("size")) is not int or not 0 < found[0]["size"] <= 20 * 1024 * 1024:
            raise ValueError("Shared preparation artifact missing, ambiguous or oversized")
        data = client.download(found[0])
        if len(data) != found[0]["size"] or digest(data) != found[0]["sha256"]:
            raise ValueError("Shared preparation artifact hash/size differs")
        return data
    manifest = json.loads(download("context-manifest.json"))
    metadata = json.loads(download("prepare-metadata.json"))
    if (manifest.get("project") != job["project"] or manifest.get("bug_id") != job["bug_id"]
            or manifest.get("revision") != f"{job['bug_id']}f" or manifest.get("contains_execution_logs") is not False
            or not shared_context(metadata.get("prepare_contract"))
            or metadata.get('prepare_contract') != protocol.get('generation', {}).get('prepare_contract')
            or any(prepared.get("metadata", {}).get(k) != v for k, v in metadata.items())):
        raise ValueError("Shared preparation identity/stage metadata differs")
    targets, prompt, policy = download("targets.json"), download("prompt.md"), download("prepare-policy.json")
    recipe = None
    if explicit_context(metadata.get('prepare_contract')):
        raw_recipe = download('fixture-recipes.json')
        recipe = json.loads(raw_recipe)
        from .fixture_policy import validate_recipe
        validate_recipe(recipe, protocol['source_sha256'], policy=protocol['fixture_policy_id'])
        if digest(raw_recipe) != metadata.get('fixture_recipes_sha256'):
            raise ValueError('Explicit recipe artifact bytes differ')
    validate(manifest, metadata, prompt, targets, policy, require_eligible=True, fixture_recipe=recipe)
    if metadata["policy_sha256"] != protocol.get("prepare_policy_sha256"):
        raise ValueError("Preparation policy differs from protocol")
    return {"metadata": metadata, "manifest": manifest, "targets": json.loads(targets)["targets"],
            "fixture_classes": json.loads(targets)["fixture_classes"], "fixture_recipe": recipe,
            "prepare_attempt_id": prepared["attempt_id"]}
