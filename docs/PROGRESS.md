# Lynq — Progress

## Done

- Project skeleton: Gradle, Compose, terminal theme, version catalog.
- Domain models matching AGENTS.md (Message, Peer, Priority, DeliveryStatus).
- **UI direction: black/green field terminal** (see `docs/design/DESIGN.md`):
  - Mockups validated at 390×844, all screens + states, clean checks
  - Two-tier type: Silkscreen (identity) + JetBrains Mono (reading/data) + VT323 (CRT flavor)
  - All five screens ported to Compose; old minimal-theme screens deleted
  - CRT scanline overlay, ASCII mesh topology, corner brackets, SMA attrs
- Fonts bundled as app resources (OFL: Silkscreen, VT323, JetBrains Mono NF)
- `compileDebugKotlin` green, `assembleDebug` green

## In progress

- On-device visual pass (waiting for phone via adb)

## Blocked

- Mesh engine: port plan documented (MeshRouter/SeenSet/Wire CBOR/ForwardSync
  from Knit, GPL-3.0 applies) — not started. UI first per team call.