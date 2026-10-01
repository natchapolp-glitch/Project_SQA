# Claude Haiku checkpoint — 2 October 2026

KKU IntelSphere, explicitly selected `claude-haiku-latest`, account
`supakron.k@kkumail.com`. Only the original prepared prompts were sent.
No compilation, fixed/buggy, or coverage feedback was sent to the provider.

## Verified progress

- Overall: 163/204 planned identities. CMA-ES 51/51, FSCS-ART 51/51,
  KKU Gemini 51/51, KKU Claude 10/51. These are team protocol counts,
  not an instructor minimum.
- JacksonCore/101: 76 methods in the closed source block, capped at 30,
  then three fixed failures removed. Final 27 methods passed fixed twice,
  ran on buggy revision and produced coverage: lines 73/403, branches 58/242.
  Fault detection false. Truncated final TextBuffer block was not extracted.
- v55 removes one hash-identified filename header before the Java package
  declaration. Original response and unsuccessful v54 extraction preserved.
  Failed fixed attempt and removed method identities are preserved.
- Time/101, Compress/101, JacksonDatabind/101, JxPath/101 and Mockito/101:
  Haiku refused or requested additional context; no Java suite generated.
  Native exports, request metadata and provider screenshots retained.
- JacksonXml/101: 55 generated methods, capped at 30. v56 fixed compilation
  failed because `org.mockito` is absent from the project test classpath.
  No passing result or coverage inferred. v54 dispatch failure retained;
  v56 applies historical XML repairs only to their exact historical hash.
- Closure/101: previous server-busy result retained with unresolved actual
  backend label; a fresh original Haiku prompt is prepared in KKU but has
  not been submitted at this checkpoint.

## Evidence and limitations

`audit_kku_only_v4.py` combines v2's exact-hash Lang/v40 recovery check with
v3's v52/v53 checks. All seven completed fresh Claude records pass; zero
artifact audit issues. Earlier v3 omitted the existing v40 audit branch and
raised a KeyError; experimental records were not changed to fix the auditor.
Reused baseline records remain governed by their matching baseline audit.

Claude quota last observed: 85.1% used in KKU. Reset time not established.
Truthful academic context clarification is prepared in
`HAIKU_CONTEXT_CLARIFICATION_PROPOSAL.md` but remains unsubmitted pending
the owner's answer to the original-prompt constraint clarification.

41 Claude identities remain incomplete. Historical provenance gaps remain.
Report PDF is checkpoint 140; slides are checkpoint 136. Final report,
slides, ZIP, Git publication and Classroom submission remain unfinished.
All new work is local and unpushed. Last published commit:
`846fb43c4a3daa8f48febef0da7b559186086acb`.
