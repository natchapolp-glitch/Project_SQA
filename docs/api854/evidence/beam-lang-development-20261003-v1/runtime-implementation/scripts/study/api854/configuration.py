"""Export a protocol PROPOSAL for Aom to review; never starts experiments."""
import argparse
from pathlib import Path

from .common import implementation_hashes, sha256, write_json


def proposal():
    return {"schema_version": 1, "status": "proposal_requires_team_review", "defects4j_version": "3.0.1",
            "timezone": "America/Los_Angeles", "seed": 101, "budget": 30, "test_method_cap": 30,
            "command_timeout_seconds": 900, "observation_timeout_seconds": 10,
            "target_selection": "shared-declaration-signatures-v1", "compatibility_policy": "none",
            "suite_packaging": "beam-java-suite-v1",
            "context_selection": "modified-java-and-root-build-v1",
            "model_policy": {"decision_date": "2026-10-03", "source": "user_relay_from_champ",
                             "status": "user_supplied_ids_pending_api_preflight", "allow_model_fallback": False,
                             "kku-claude": {"requested_name": "Claude Sonnet 5.0", "kku_model_id": "claude-sonnet-5"},
                             "kku-gemini": {"requested_name": "Gemini 3.5 Flash Lite", "kku_model_id": "gemini-3.5-flash-lite"}},
            "source_sha256": implementation_hashes(),
            "limitations": ["Probe discovery is provisional per bug until runtime and oracle review.",
                            "Queue integration and Gate A/B acceptance are outstanding.",
                            "User-supplied AI model IDs require authenticated KKU preflight; temperature, max output and prompt policy require Champ/Aom freeze."]}


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--output", required=True, type=Path)
    args = parser.parse_args()
    args.output.parent.mkdir(parents=True, exist_ok=True)
    write_json(args.output, proposal())
    print(f"PROPOSAL only; protocol SHA-256={sha256(args.output)}")


if __name__ == "__main__":
    main()
