"""Explicit runner routing across allocated owners; never changes job ownership."""
from urllib.parse import urlsplit
from .common import read_json, sha256
from .champ_queue import ChampQueueClient
from .queue_client import QueueClient
from .kku_client import http_transport


def assignment(plan, worker_id, role, owner, stage, approaches):
    actors = [a for a in plan.get("runners", []) if a.get("worker_id") == worker_id]
    if len(actors) != 1:
        raise ValueError("Runner must match one explicit host assignment")
    actor = actors[0]
    if (actor.get("role") != role or owner not in actor.get("owners", [])
            or stage not in actor.get("stages", []) or not approaches
            or any(a not in actor.get("approaches", []) for a in approaches)):
        raise ValueError("Runner role/owner/stage/approach is outside its assignment")
    if stage == "generate" and any(a not in actor.get("generation_approaches", actor["approaches"]) for a in approaches):
        raise ValueError("This runner is not assigned this generator")
    return actor


class TeamQueueClient(ChampQueueClient):
    team_routing = True

    @classmethod
    def from_plan(cls, access_path, plan_path, worker_id, *, owner, stage, approaches, protocol_path, condition):
        access, plan, protocol = read_json(access_path), read_json(plan_path), read_json(protocol_path)
        role, url = access.get("role"), urlsplit(access["base_url"])
        if (role not in {"aom", "beam", "champ"} or access.get("schema_version") != "1.0"
                or not isinstance(access.get("worker_token"), str) or not access["worker_token"]
                or url.scheme != "https" or not url.hostname or url.username or url.password
                or url.path not in {"", "/"} or url.query or url.fragment or url.port not in {None, 443}):
            raise ValueError("Require a private team-role access file and explicit HTTPS origin")
        actor = assignment(plan, worker_id, role, owner, stage, approaches)
        if condition == "primary":
            if (plan.get("status") != "frozen" or protocol.get("runner_plan_sha256") != sha256(plan_path)
                    or protocol.get("state") != "frozen" or protocol.get("approval_state") != "frozen"):
                raise ValueError("Primary routing requires the reviewed plan pinned in frozen protocol")
        elif condition not in {"development", "preflight", "mock-integration"}:
            raise ValueError("Unknown runner condition")
        if condition == "preflight" and (stage != "prepare" or protocol.get("state") != "frozen_core"):
            raise ValueError("Preflight routing is preparation only")
        client = cls.__new__(cls)
        QueueClient.__init__(client, access["base_url"], access["worker_token"])
        client.transport, client.mock_mode = http_transport, False
        client.role, client.worker_id, client.actor = role, worker_id, actor
        client.owner, client.stage, client.approaches = owner, stage, tuple(approaches)
        client.plan_sha256 = sha256(plan_path)
        return client

    def check(self):
        result = super().check()
        result["role"], result["runner_plan_sha256"] = self.role, self.plan_sha256
        return result

    def claim(self, worker_id, stage, approaches=None, owner=None, lease_seconds=900):
        if (worker_id != self.worker_id or stage != self.stage or owner != self.owner
                or not approaches or any(a not in self.approaches for a in approaches)):
            raise ValueError("Claim differs from explicit runner assignment")
        return QueueClient.claim(self, worker_id, stage, approaches, owner, lease_seconds)
