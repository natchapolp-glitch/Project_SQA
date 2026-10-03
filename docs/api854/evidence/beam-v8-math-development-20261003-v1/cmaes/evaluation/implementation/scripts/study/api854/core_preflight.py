"""Explicit execution binding for Aom's immutable preparation-only protocol."""
from .common import implementation_hashes, read_json, sha256

CORE_SHA256 = "675a480915c40ab19f7be57b56b046fb8c8924e7330e4cc8956b2ed3de6d31ad"
CORE_RUN = "api854-pilot-preflight-20261003-v1"


def bind(path, *, stage, condition, run_id):
    core = read_json(path)
    if (sha256(path) != CORE_SHA256 or core.get("state") != "frozen_core"
            or core.get("condition") != CORE_RUN or run_id != CORE_RUN
            or condition != "preflight" or stage != "prepare"
            or core.get("enabled_stages") != ["prepare"]):
        raise ValueError("Frozen core permits only its exact preflight preparation run")
    # This supplements the core with recorded local implementation policy. It
    # does not modify its bytes or pretend the implementation/AI settings froze.
    return {"schema_version": 1, "status": "core_preflight_only", "preparation_only": True,
            "core_protocol_sha256": CORE_SHA256, "defects4j_version": core["defects4j_version"],
            "timezone": core["execution_timezone"], "seed": core["generator_seed"],
            "budget": core["algorithm_input_budget"], "test_method_cap": core["test_method_cap"],
            "command_timeout_seconds": core["command_timeout_seconds"],
            "observation_timeout_seconds": core["observation_timeout_seconds"],
            "target_selection": "shared-declaration-signatures-v1", "compatibility_policy": "none",
            "context_selection": "modified-java-and-root-build-v1",
            "source_sha256": implementation_hashes(),
            "approval_state": "local_execution_binding_pending_three_owner_review"}
