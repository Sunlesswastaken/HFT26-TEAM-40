# Lynq — Progress

## Done

- Project skeleton: Gradle, Compose, theme (light + dark), version catalog.
- Domain models matching AGENTS.md (Message, Peer, Priority, DeliveryStatus).
- Reusable components: cards, pills, route traces, avatars, mesh graph, SOS button, app shell.
- Demo data for UI development (`demo/DemoData.kt`).
- UI direction fixed: **minimal & clean** — tokens documented in `docs/design/DESIGN.md`.
- HTML mockups of all 5 screens validated at 390×844 (light, dark, tracking, open-thread states):
  `docs/design/mockups/`, screenshots in `shots/`.
- All 5 screens ported to Compose, wired to navigation + MainActivity; `compileDebugKotlin` green.

## In progress

- On-device visual pass (screenshots come from tooling checks only this session — no image-capable reviewer available).
- Wire screens to the engine once the router lands (screens already take data via parameters; `DemoData` swap point is documented).

## Blocked

- None.