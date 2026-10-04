"""User-selected exact model IDs shared by discovery and primary generation."""
from __future__ import annotations

import json
from pathlib import Path

from .kku_client import KKUError, Model, pin_model

MODEL_SELECTION = Path(__file__).resolve().parents[3] / "experiments/configs/api854-20261003/model-selection.json"


def load_selection(path: Path = MODEL_SELECTION) -> dict:
    config = json.loads(path.read_text(encoding="utf-8"))
    models = config.get("models")
    if config.get("schema_version") != 1 or config.get("silent_fallback_allowed") is not False or \
            not isinstance(models, dict) or set(models) != {"kku-claude", "kku-gemini"}:
        raise ValueError("Invalid primary model selection manifest")
    for approach, family in (("kku-claude", "sonnet"), ("kku-gemini", "flash-lite")):
        entry = models[approach]
        if not isinstance(entry, dict) or entry.get("family") != family or not isinstance(entry.get("id"), str) \
                or not isinstance(entry.get("name"), str) or entry["id"] != entry["name"]:
            raise ValueError("Selection must contain exact user-selected string model IDs")
        pin_model([Model(entry["id"], entry["name"])], family, entry["name"])
    return models


def resolve_selected(models: list[Model], approach: str, selection: dict | None = None) -> Model:
    selected = (selection or load_selection())[approach]
    model = pin_model(models, selected["family"], selected["name"])
    if model.id != selected["id"]:
        raise KKUError("model_mapping", "Model ID changed from the user-selected mapping; no silent substitution")
    return model
