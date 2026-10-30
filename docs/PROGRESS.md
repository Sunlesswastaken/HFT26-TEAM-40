# Lynq — Progress

## Done

- **Project Skeleton & Architecture**:
  - Gradle 9.6 + Kotlin 2.2 + Compose + version catalog setup.
  - Strict decoupling of engine, networking, data, and UI layers.
  - Clean edge-to-edge support with custom dark theme.

- **Domain Models & Wire Protocol**:
  - Domain models matching AGENTS.md (`MessageType`, `Priority`, `DeliveryStatus`, `Peer`, `LynqMessage`).
  - Wire protocol model (`WireMessage`) serialized via `kotlinx.serialization` with TTL hop limits, route histories, and cryptographic signatures.

- **Nearby Connections Mesh (`Strategy.P2P_CLUSTER`)**:
  - `NearbyManager`: Background advertising, automatic endpoint discovery, mutual connection handshakes, and raw byte transfers.
  - Dynamic peer identity mapping with PRESENCE exchanges.

- **Store-and-Forward Routing Engine (`MessageRouter`)**:
  - Deduplication with persistent SQLite `seen_messages` table.
  - TTL hop decrementing and expiry enforcement.
  - Multi-hop broadcast flooding for emergency SOS.
  - Application-level ACK loopback (`A → B → C → D` and ACK `D → C → B → A`).
  - Priority queue (`CRITICAL` > `HIGH` > `NORMAL`).
  - Built-in multi-hop simulation harness for live demonstration on real devices and emulators.

- **Cryptographic Security & Identity (`CryptoManager`)**:
  - Android Keystore backed EC (P-256) keypair generation and verification.
  - Human-readable device identity fingerprint (`xxxx xxxx xxxx xxxx`).
  - SHA256withECDSA digital signatures for origin authenticity.
  - AES-256-GCM end-to-end encryption for 1-to-1 CHAT messages.
  - Plaintext SOS payloads with digital signatures for universal emergency readability.

- **Local Persistence Layer (`LynqDatabase`)**:
  - SQLite database persisting messages, reachable peers, deduplication seen-set, and priority queue.
  - Reactive `StateFlow` streams automatically updating UI.

- **Safe Location Provider (`LocationProvider`)**:
  - Non-blocking Google Play Services Fused Location Provider.
  - Never blocks SOS alerts if GPS is unavailable or permissions are denied.

- **Background Relay Service (`LynqMeshService`)**:
  - Foreground service with notification maintaining mesh links when the screen locks.

- **UI Overhaul & ViewModel Integration**:
  - Fully bound to `LynqViewModel` across all 5 screens: Network Dashboard, Messages, Emergency SOS, Mesh Explorer, and Settings.
  - Interactive Obsidian knowledge graph with harmonic physics and multi-touch zoom (`MeshGraph.kt`).
  - Real-time SOS broadcast flow with live hop timeline and ACK confirmation.
  - Runtime permissions requester for Bluetooth, Nearby Wi-Fi, Location, and Notifications in `MainActivity`.

- **Verification & Testing**:
  - Unit test suite (`MeshProtocolTest.kt`): wire serialization round-trip, crypto keygen & signing, E2EE AES-GCM encryption/decryption, priority queue ordering, and TTL route decrements.
  - `./gradlew testDebugUnitTest` passing 100%.
  - `./gradlew assembleDebug` builds `app-debug.apk` cleanly with 0 errors.

## Next steps

- Multi-device field verification on physical hardware across 4 devices.
- Video demo recording of multi-hop SOS packet propagation (`A → B → C → D`).