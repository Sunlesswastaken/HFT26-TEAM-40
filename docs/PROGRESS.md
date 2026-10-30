# Lynq — Progress

## Done

- Project skeleton: Gradle, Compose, version catalog, edge-to-edge support.
- Domain models matching AGENTS.md (Message, Peer, Priority, DeliveryStatus).
- **UI overhaul: Apple Minimalist Design System & Obsidian Knowledge Graph (Oil-UI Skill)**:
  - **Apple Dark System Palette**: Pure OLED black (`#000000`), Apple Secondary System Background (`#1C1C1E`), Tertiary (`#2C2C2E`), Quaternary (`#3A3A3C`), and hairline dividers (`0.5dp, rgba(255,255,255,0.10)`). Replaced all harsh glowing borders with subtle, refined Apple grouped surfaces.
  - **Apple SF Pro Typography Hierarchy**: Large Title (32-34sp bold), Title 2 (22sp bold), Headline (17sp semibold), Body (16-17sp regular), Subhead (15sp), Footnote (13sp), and Caption (11sp). Monospace font (JetBrains Mono) reserved strictly for cryptographic hashes, fingerprints, and coordinates. Monospace shouting eliminated from all screens.
  - **Authentic Obsidian Knowledge Graph (`MeshGraph.kt`)**:
    - Deep space cosmic canvas with subtle dust particles.
    - Organic floating nodes with harmonic drift physics.
    - Delicate translucent gravitational filaments connecting nodes.
    - Multi-touch interactive Pan & Pinch-to-Zoom gestures (`0.6x` to `2.8x`).
    - Interactive tap to inspect nodes.
    - Live SOS neural pathway: Pulsing crimson beam (`#FF453A`) with animated energy packets traveling from `You → Aarav → Kabir → Priya`.
  - **Apple Minimalist Screens (Oil-UI "One Protagonist" Rule)**:
    - **Network Dashboard**: Dynamic status capsule ("4 online"), live Obsidian graph protagonist, Apple Emergency SOS action trigger, Health-style metric row, and Inset Grouped list of recent transmissions.
    - **Messages**: Apple iMessage aesthetic. Inset Grouped conversation list with contact avatars and blue unread pips; fluid conversation thread with iOS blue (`#0A84FF`) and dark gray (`#2C2C2E`) message bubbles, delivery receipts, hop breadcrumbs, and capsule composer.
    - **Emergency SOS**: Apple Satellite Emergency SOS layout. Category selector cards (Medical, Fire, Trapped, Threat), GPS coordinate card with accuracy chip, prominent hold/send emergency trigger, and live multi-hop propagation timeline (`Origin → Aarav → Kabir → Priya`) with hop meter.
    - **Mesh Explorer**: Expansive interactive Obsidian knowledge graph, zoom reset controls, node metrics, and Inset Grouped reachable peers list.
    - **Settings**: iOS Inset Grouped settings with hardware Keystore identity, radio parameters, security details, and protocol info.
  - **Apple Tab Bar (`CmdBar.kt`)**: Translucent frosted glass bar with subtle hairline divider, vector glyphs, active blue tint (`#0A84FF`), and emergency red SOS indicator.
  - **Apple Fluid Transitions**: `slideInHorizontally` + `fadeIn` with Apple spring easing `FastOutSlowInEasing`.
  - HTML mockups updated in `docs/design/mockups/` (`tokens.css`, `dashboard.html`, `emergency.html`, `mesh.html`, `messages.html`, `settings.html`) at **390×844**.
  - `./gradlew compileDebugKotlin` and `./gradlew assembleDebug` compiling cleanly with 0 errors.

## Next steps

- Mesh engine: Nearby Connections `Strategy.P2P_CLUSTER`, router algorithm (seen-set dedupe, TTL hop decrement, multi-hop broadcast forwarding, ACK loopback).