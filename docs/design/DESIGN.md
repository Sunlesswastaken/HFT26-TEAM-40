# Lynq UI design — Black & Green Field Terminal

Status: mockups validated (all screens + states), Compose port pending.

## North star

A field instrument, not a chat app: near-black phosphor CRT, one green,
pixel type for identity, ASCII motifs for the mesh. Emergency-grade
readability at arm's length.

## Tokens (`docs/design/mockups/tokens.css`)

| Token | Value | Use |
|---|---|---|
| bg | #060A07 | page (near-black, green-tinted, never #000) |
| panel | #0A120C | panels, rows, nav |
| panel-hi | #0E1A10 | raised panels, active inputs |
| line / line-hi | #1C3320 / #2E5236 | hairline / lit borders |
| ph | #7DF2A0 | primary text (phosphor green) |
| ph-dim | #5FC47F | secondary text (~8.8:1) |
| ph-faint | #4E9C69 | meta, timestamps (~5.7:1) |
| amber | #E8C468 | warnings + HIGH priority only |
| sos | #FF7A68 | SOS / critical only (~5.5:1) |

Type — **two-tier system**:
- **Silkscreen** (pixel, OFL) = identity tier: screen titles, section heads,
  buttons, tags, readout numbers, route traces, and **names** (people are
  headings, so they stay pixel)
- **Sans** (system) = reading tier: message bodies, note input, previews,
  descriptions, explainer copy — anything longer than a label
- **VT323** (terminal, OFL) = flavor only: boot log, ASCII topology glyph art,
  timestamps/hop counts/IDs (data, not prose)

Fonts bundled in `docs/design/mockups/fonts/`.

Shapes: ONE radius — 2px, near-sharp. Corner brackets `┌ ┐` motif on key
panels.

## Signature elements

- **CRT scanlines** + subtle vignette (fixed overlay, pointer-events none)
- **Boot log** on the dashboard — terminal is alive, blinking cursor `_`
- **ASCII mesh topology** (`= | > -` links, YOU at centre, relay nodes lit)
- **Route traces** everywhere: `YOU > AARAV > KABIR > PRIYA`
- **ASCII-only glyphs** — VT323 has no box-drawing/symbols (verified);
  all icons are ASCII text: `[*] [M] [!] [=] [$]`, status tags `DLV RLY SENT`
- **Hop meter** `[####......] 3/6` for live SOS tracking
- **Red SOS lever** — full-width, the only solid red element on any screen

## Screens

1. **DASH** — boot log, readouts (REACHABLE/RELAYING/QUEUED), ASCII topology,
   activity feed with routes, SOS lever
2. **MSGS** — thread list (unread badges, hop counts, status tags) + open
   thread with per-message route traces
3. **SOS** — categories, note, location toggle → post-send live hop tracker
   with hop meter; received alerts history
4. **MESH** — full ASCII topology with live SOS route, relay activity, device
   list (id · hops · seen)
5. **SYS** — identity (node fingerprint in dashed box), security, mesh
   counters, permissions, scope note

## Knit feature mapping (new screens worth adding)

From the Knit repo exploration (GPL-3.0, /tmp/opencode/knit):
- **Your mesh** screen — messages your phone relayed for others (fits SYS)
- **Drafts** — composer keeps unsent text (fits MSGS)
- **Delivery ticks ✓/✓✓ per message** (already have DLV/RLY/SENT)

## Port plan (engine)

Port from Knit (all pure Kotlin, tested): `MeshRouter` (dedup/TTL/jitter/
overhear-suppression), `SeenSet`, CBOR `Wire` envelope (stripped to Lynq's
CHAT/SOS/ACK/PRESENCE), `ForwardStore`/`ForwardSync` custody, `NodeId`.
Skip: raw NAN/BLE transports (Lynq uses Nearby Connections), spools, LoRa.
License note: porting GPL-3.0 code makes Lynq GPL-3.0.

## Deliverables

- Mockups: `docs/design/mockups/*.html` + `tokens.css` + `fonts/`
- Screenshots: `shots/` (light set — dark IS the design; states:
  `state-thread-open.png`, `state-sos-tracking.png`)

## Delivery notes

- ASCII mesh line-height 1.15 is deliberate: square character cell spacing.
- Scanline overlay is a design feature, not a lint issue to remove.
- Fonts verified: VT323 does NOT cover Unicode box-drawing or symbols —
  the design is pure ASCII by necessity, which strengthened the aesthetic.
- Visual self-review impossible this session (no image input); all
  automated checks pass. Human eye pass recommended.