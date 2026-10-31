const pptxgen = require('pptxgenjs');
const path = require('path');
const fs = require('fs');

const pres = new pptxgen();

// Define clean modern 16:9 widescreen layout
pres.defineLayout({ name: 'WIDE_16_9', width: 13.333, height: 7.5 });
pres.layout = 'WIDE_16_9';

// Color Palette
const COLORS = {
  bg: '080C14',
  cardBg: '111827',
  cardBorder: '1E293B',
  primary: '0A84FF',   // Apple Blue
  emergency: 'FF453A', // Apple Red
  mesh: '30D158',      // Apple Green
  accent: '60A5FA',    // Sky Blue
  warning: 'FF9F0A',   // Orange
  textPrimary: 'FFFFFF',
  textSecondary: '94A3B8',
  textMuted: '64748B',
};

const SCREENSHOTS_DIR = path.resolve(__dirname, '../docs/screenshots');

function addHeader(slide, title, category, accentColor = COLORS.primary) {
  // Brand Header
  slide.addText('TEAM FLUX  ·  LYNQ EMERGENCY MESH', {
    x: 0.8,
    y: 0.45,
    w: 6.0,
    h: 0.3,
    fontSize: 10,
    fontFace: 'Helvetica',
    color: COLORS.textMuted,
    bold: true,
    charSpacing: 2,
  });

  // Category Pill
  slide.addShape(pres.ShapeType.roundRect, {
    x: 10.2,
    y: 0.4,
    w: 2.3,
    h: 0.35,
    rectRadius: 0.17,
    fill: { color: accentColor, transparency: 85 },
    line: { color: accentColor, width: 1 },
  });
  slide.addText(category.toUpperCase(), {
    x: 10.2,
    y: 0.4,
    w: 2.3,
    h: 0.35,
    fontSize: 9,
    fontFace: 'Helvetica',
    color: accentColor,
    bold: true,
    align: 'center',
    valign: 'middle',
  });

  // Slide Title
  slide.addText(title, {
    x: 0.8,
    y: 0.8,
    w: 11.5,
    h: 0.65,
    fontSize: 26,
    fontFace: 'Helvetica',
    color: COLORS.textPrimary,
    bold: true,
    charSpacing: -0.5,
  });
}

function addFooter(slide, current, total = 10) {
  slide.addText(`Problem Statement: Offline Emergency Messaging System  |  Page ${current} of ${total}`, {
    x: 0.8,
    y: 7.0,
    w: 11.7,
    h: 0.3,
    fontSize: 9,
    fontFace: 'Helvetica',
    color: COLORS.textMuted,
    align: 'right',
  });
}

// =========================================================================
// SLIDE 1: Title & Cover Slide
// =========================================================================
{
  const slide = pres.addSlide();
  slide.background = { color: COLORS.bg };

  // Subtle accent gradient block
  slide.addShape(pres.ShapeType.roundRect, {
    x: 0.8,
    y: 0.9,
    w: 2.8,
    h: 0.38,
    rectRadius: 0.19,
    fill: { color: COLORS.primary, transparency: 85 },
    line: { color: COLORS.primary, width: 1 },
  });
  slide.addText('HACKATHON PRESENTATION  ·  TEAM FLUX', {
    x: 0.8,
    y: 0.9,
    w: 2.8,
    h: 0.38,
    fontSize: 9,
    fontFace: 'Helvetica',
    color: COLORS.primary,
    bold: true,
    align: 'center',
    valign: 'middle',
  });

  // Big Title
  slide.addText('LYNQ', {
    x: 0.8,
    y: 1.45,
    w: 8.0,
    h: 1.1,
    fontSize: 64,
    fontFace: 'Helvetica',
    color: COLORS.textPrimary,
    bold: true,
    charSpacing: -2,
  });

  slide.addText('Decentralized Offline Emergency Mesh Network', {
    x: 0.8,
    y: 2.65,
    w: 8.5,
    h: 0.55,
    fontSize: 22,
    fontFace: 'Helvetica',
    color: COLORS.accent,
    bold: true,
  });

  slide.addText(
    'A zero-infrastructure, hop-by-hop life safety communication system engineered for natural disasters, grid blackouts, and complete cellular collapse.',
    {
      x: 0.8,
      y: 3.3,
      w: 7.2,
      h: 0.8,
      fontSize: 14,
      fontFace: 'Helvetica',
      color: COLORS.textSecondary,
      lineSpacingMultiple: 1.25,
    }
  );

  // Domains Card
  slide.addShape(pres.ShapeType.roundRect, {
    x: 0.8,
    y: 4.35,
    w: 7.2,
    h: 1.0,
    rectRadius: 0.12,
    fill: { color: COLORS.cardBg },
    line: { color: COLORS.cardBorder, width: 1 },
  });
  slide.addText('TARGET DOMAINS', {
    x: 1.0,
    y: 4.45,
    w: 6.8,
    h: 0.25,
    fontSize: 9,
    fontFace: 'Helvetica',
    color: COLORS.textMuted,
    bold: true,
  });
  slide.addText('Cybersecurity  ·  Communication Networks  ·  Disaster Management  ·  AI/ML', {
    x: 1.0,
    y: 4.75,
    w: 6.8,
    h: 0.45,
    fontSize: 12,
    fontFace: 'Helvetica',
    color: COLORS.textPrimary,
    bold: true,
  });

  // Team Members Card
  slide.addShape(pres.ShapeType.roundRect, {
    x: 0.8,
    y: 5.5,
    w: 7.2,
    h: 1.45,
    rectRadius: 0.12,
    fill: { color: COLORS.cardBg },
    line: { color: COLORS.cardBorder, width: 1 },
  });
  slide.addText('TEAM FLUX MEMBERS', {
    x: 1.0,
    y: 5.6,
    w: 6.8,
    h: 0.25,
    fontSize: 9,
    fontFace: 'Helvetica',
    color: COLORS.primary,
    bold: true,
  });
  const membersText = [
    { text: '• G N Sreekar (Team Lead) ', options: { bold: true, color: COLORS.textPrimary } },
    { text: '— Mesh Architecture & Core Protocol\n', options: { color: COLORS.textSecondary } },
    { text: '• Kamegaonkar Kruthik ', options: { bold: true, color: COLORS.textPrimary } },
    { text: '— Cybersecurity & Keystore Cryptography\n', options: { color: COLORS.textSecondary } },
    { text: '• VVM Sai Ganesh ', options: { bold: true, color: COLORS.textPrimary } },
    { text: '— Communication Networks & Routing Engine\n', options: { color: COLORS.textSecondary } },
    { text: '• Ashwik Saxena ', options: { bold: true, color: COLORS.textPrimary } },
    { text: '— UI/UX Design System & AI/ML Triage', options: { color: COLORS.textSecondary } },
  ];
  slide.addText(membersText, {
    x: 1.0,
    y: 5.85,
    w: 6.8,
    h: 1.0,
    fontSize: 10,
    fontFace: 'Helvetica',
    lineSpacingMultiple: 1.15,
  });

  // Right Side Hero Phone Screenshot
  const heroImg = path.join(SCREENSHOTS_DIR, '1-dashboard.png');
  if (fs.existsSync(heroImg)) {
    slide.addImage({
      path: heroImg,
      x: 8.8,
      y: 0.8,
      w: 3.6,
      h: 6.0,
      rounding: true,
    });
  }
}

// =========================================================================
// SLIDE 2: The Critical Crisis (Disaster Management)
// =========================================================================
{
  const slide = pres.addSlide();
  slide.background = { color: COLORS.bg };
  addHeader(slide, 'The Golden Window in Total Infrastructure Blackout', 'Disaster Management', COLORS.emergency);

  // Big Stat Callouts
  const stats = [
    { num: '95%', label: 'Cell Tower Failure', desc: 'Base stations and fiber backhaul severed within hours of seismic/flood events.' },
    { num: '72 Hrs', label: 'The Golden Window', desc: 'Critical survival timeframe where search & rescue depends entirely on communications.' },
    { num: '0 bps', label: 'Backhaul Bandwidth', desc: 'Traditional cloud and SMS apps become completely inert paperweights.' },
  ];

  stats.forEach((st, idx) => {
    const xPos = 0.8 + idx * 3.9;
    slide.addShape(pres.ShapeType.roundRect, {
      x: xPos,
      y: 1.6,
      w: 3.7,
      h: 2.1,
      rectRadius: 0.12,
      fill: { color: COLORS.cardBg },
      line: { color: idx === 1 ? COLORS.emergency : COLORS.cardBorder, width: idx === 1 ? 1.5 : 1 },
    });
    slide.addText(st.num, {
      x: xPos + 0.25,
      y: 1.8,
      w: 3.2,
      h: 0.65,
      fontSize: 34,
      fontFace: 'Helvetica',
      color: idx === 1 ? COLORS.emergency : COLORS.textPrimary,
      bold: true,
    });
    slide.addText(st.label, {
      x: xPos + 0.25,
      y: 2.5,
      w: 3.2,
      h: 0.35,
      fontSize: 14,
      fontFace: 'Helvetica',
      color: COLORS.textPrimary,
      bold: true,
    });
    slide.addText(st.desc, {
      x: xPos + 0.25,
      y: 2.9,
      w: 3.2,
      h: 0.7,
      fontSize: 11,
      fontFace: 'Helvetica',
      color: COLORS.textSecondary,
      lineSpacingMultiple: 1.15,
    });
  });

  // Problem Analysis Breakdown Box
  slide.addShape(pres.ShapeType.roundRect, {
    x: 0.8,
    y: 3.9,
    w: 11.7,
    h: 2.9,
    rectRadius: 0.12,
    fill: { color: COLORS.cardBg },
    line: { color: COLORS.cardBorder, width: 1 },
  });
  slide.addText('CORE CHALLENGES IN DISASTER EMERGENCY COMMS', {
    x: 1.1,
    y: 4.1,
    w: 11.0,
    h: 0.3,
    fontSize: 11,
    fontFace: 'Helvetica',
    color: COLORS.emergency,
    bold: true,
  });

  const challenges = [
    { title: 'The Infrastructure Trap', desc: 'Modern apps (WhatsApp, Signal) are architecturally centralized; when AWS or cell towers drop, they cannot send even 1 byte locally.' },
    { title: 'Radio Range Limitation', desc: 'Direct Bluetooth/Wi-Fi has a range of ~50-100 meters. Without intelligent multi-hop routing, victims outside direct line-of-sight remain cut off.' },
    { title: 'Panic & False Alarms', desc: 'Accidental emergency alerts congest rescue teams. Systems require tactile confirmation while keeping SOS transmission non-blocking.' },
    { title: 'Untrusted Environment', desc: 'In disaster chaos, spoofed coordinates or altered distress payloads endanger first responders. Authenticity must be cryptographically verified.' },
  ];

  challenges.forEach((ch, idx) => {
    const col = idx % 2;
    const row = Math.floor(idx / 2);
    const xPos = 1.1 + col * 5.6;
    const yPos = 4.5 + row * 1.1;

    slide.addText(`• ${ch.title}`, {
      x: xPos,
      y: yPos,
      w: 5.3,
      h: 0.3,
      fontSize: 12,
      fontFace: 'Helvetica',
      color: COLORS.textPrimary,
      bold: true,
    });
    slide.addText(ch.desc, {
      x: xPos + 0.25,
      y: yPos + 0.3,
      w: 5.0,
      h: 0.65,
      fontSize: 10,
      fontFace: 'Helvetica',
      color: COLORS.textSecondary,
      lineSpacingMultiple: 1.15,
    });
  });

  addFooter(slide, 2);
}

// =========================================================================
// SLIDE 3: The Solution — Lynq (Communication Networks)
// =========================================================================
{
  const slide = pres.addSlide();
  slide.background = { color: COLORS.bg };
  addHeader(slide, 'Zero Towers. Zero Internet. 100% Peer-to-Peer.', 'Communication Networks', COLORS.mesh);

  // Left Column: Key Innovations
  const innovations = [
    {
      title: 'Decentralized Multi-Hop Mesh (P2P_CLUSTER)',
      desc: 'Leverages Google Nearby Connections to establish ad-hoc Wi-Fi Aware & BLE radio clusters without pairing codes or Wi-Fi routers.',
      tag: 'Strategy.P2P_CLUSTER',
    },
    {
      title: 'Store-and-Forward Routing Engine',
      desc: 'Delay-tolerant architecture. Packets are persisted locally in SQLite when peers are out of range and auto-dispatched the second a relay connects.',
      tag: 'Deduplicated TTL Routing',
    },
    {
      title: 'Deterministic Route Propagation',
      desc: 'Messages hop dynamically across intermediate phones (You → Aarav → Kabir → Priya), multiplying network reach exponentially beyond physical radio barriers.',
      tag: 'A → B → C → D Relay',
    },
    {
      title: 'End-to-End Delivery Acknowledgments',
      desc: 'Application-level ACK packets route back through the reverse mesh path, transitioning status from SENT → RELAYED → DELIVERED on sender screens.',
      tag: 'Loopback ACKs',
    },
  ];

  innovations.forEach((item, idx) => {
    const yPos = 1.6 + idx * 1.3;
    slide.addShape(pres.ShapeType.roundRect, {
      x: 0.8,
      y: yPos,
      w: 7.6,
      h: 1.18,
      rectRadius: 0.1,
      fill: { color: COLORS.cardBg },
      line: { color: COLORS.cardBorder, width: 1 },
    });
    slide.addText(item.title, {
      x: 1.1,
      y: yPos + 0.12,
      w: 5.2,
      h: 0.3,
      fontSize: 13,
      fontFace: 'Helvetica',
      color: COLORS.textPrimary,
      bold: true,
    });
    // Tag pill
    slide.addShape(pres.ShapeType.roundRect, {
      x: 6.2,
      y: yPos + 0.12,
      w: 2.0,
      h: 0.28,
      rectRadius: 0.14,
      fill: { color: COLORS.mesh, transparency: 85 },
      line: { color: COLORS.mesh, width: 1 },
    });
    slide.addText(item.tag, {
      x: 6.2,
      y: yPos + 0.12,
      w: 2.0,
      h: 0.28,
      fontSize: 8,
      fontFace: 'Helvetica',
      color: COLORS.mesh,
      bold: true,
      align: 'center',
      valign: 'middle',
    });
    slide.addText(item.desc, {
      x: 1.1,
      y: yPos + 0.45,
      w: 7.1,
      h: 0.65,
      fontSize: 10,
      fontFace: 'Helvetica',
      color: COLORS.textSecondary,
      lineSpacingMultiple: 1.15,
    });
  });

  // Right Column: Mesh Topology Screenshot
  const meshImg = path.join(SCREENSHOTS_DIR, '4-mesh.png');
  if (fs.existsSync(meshImg)) {
    slide.addImage({
      path: meshImg,
      x: 8.8,
      y: 1.6,
      w: 3.7,
      h: 5.2,
      rounding: true,
    });
  }

  addFooter(slide, 3);
}

// =========================================================================
// SLIDE 4: System Architecture & Routing Engine
// =========================================================================
{
  const slide = pres.addSlide();
  slide.background = { color: COLORS.bg };
  addHeader(slide, 'Decentralized Architecture & Hop-by-Hop Engine', 'System Architecture', COLORS.primary);

  // 4 Architecture Layer Columns
  const layers = [
    {
      num: 'LAYER 1',
      title: 'UI & Presentation',
      items: ['Jetpack Compose (Apple HIG)', 'Tactile 3s Hold SOS Button', 'Real-Time Mesh Graph Canvas', 'StateFlow Reactive Streams'],
      color: COLORS.primary,
    },
    {
      num: 'LAYER 2',
      title: 'Messaging Engine',
      items: ['Store-and-Forward Router', 'PriorityQueue (CRITICAL first)', 'TTL Budget & Dedupe Cache', 'Application-level ACK Loop'],
      color: COLORS.mesh,
    },
    {
      num: 'LAYER 3',
      title: 'Security & Keystore',
      items: ['Android Keystore ECDSA P-256', 'AES-256-GCM Cryptor', 'Digital Signature Authenticator', 'Blind Relay Zero-Knowledge'],
      color: COLORS.warning,
    },
    {
      num: 'LAYER 4',
      title: 'Transport & Storage',
      items: ['Nearby P2P_CLUSTER', 'Room SQLite Database', 'Fused Location Provider', 'Foreground Relay Service'],
      color: COLORS.accent,
    },
  ];

  layers.forEach((l, idx) => {
    const xPos = 0.8 + idx * 2.95;
    slide.addShape(pres.ShapeType.roundRect, {
      x: xPos,
      y: 1.6,
      w: 2.8,
      h: 3.5,
      rectRadius: 0.12,
      fill: { color: COLORS.cardBg },
      line: { color: l.color, width: 1.5 },
    });
    slide.addText(l.num, {
      x: xPos + 0.2,
      y: 1.8,
      w: 2.4,
      h: 0.25,
      fontSize: 9,
      fontFace: 'Helvetica',
      color: l.color,
      bold: true,
    });
    slide.addText(l.title, {
      x: xPos + 0.2,
      y: 2.1,
      w: 2.4,
      h: 0.45,
      fontSize: 15,
      fontFace: 'Helvetica',
      color: COLORS.textPrimary,
      bold: true,
    });
    const bullets = l.items.map(it => `✓  ${it}`).join('\n\n');
    slide.addText(bullets, {
      x: xPos + 0.2,
      y: 2.65,
      w: 2.4,
      h: 2.3,
      fontSize: 10,
      fontFace: 'Helvetica',
      color: COLORS.textSecondary,
      lineSpacingMultiple: 1.15,
    });
  });

  // Router Algorithm Flowchart Card
  slide.addShape(pres.ShapeType.roundRect, {
    x: 0.8,
    y: 5.3,
    w: 11.7,
    h: 1.5,
    rectRadius: 0.12,
    fill: { color: COLORS.cardBg },
    line: { color: COLORS.cardBorder, width: 1 },
  });
  slide.addText('STORE-AND-FORWARD ROUTER PIPELINE', {
    x: 1.1,
    y: 5.45,
    w: 11.0,
    h: 0.25,
    fontSize: 9,
    fontFace: 'Helvetica',
    color: COLORS.primary,
    bold: true,
  });

  const steps = [
    { s: '1. Ingest Packet', d: 'Decode & parse binary' },
    { s: '2. Dedupe Check', d: 'Query seen-set SQLite' },
    { s: '3. TTL Decrement', d: 'Drop if TTL <= 0' },
    { s: '4. Destination Match', d: 'Decrypt & deliver to user' },
    { s: '5. Forward Relay', d: 'Flood to unvisited peers' },
    { s: '6. ACK Return', d: 'Route ACK back to sender' },
  ];

  steps.forEach((st, idx) => {
    const xPos = 1.1 + idx * 1.85;
    slide.addText(st.s, {
      x: xPos,
      y: 5.75,
      w: 1.75,
      h: 0.3,
      fontSize: 11,
      fontFace: 'Helvetica',
      color: COLORS.textPrimary,
      bold: true,
    });
    slide.addText(st.d, {
      x: xPos,
      y: 6.05,
      w: 1.75,
      h: 0.5,
      fontSize: 9,
      fontFace: 'Helvetica',
      color: COLORS.textMuted,
    });
  });

  addFooter(slide, 4);
}

// =========================================================================
// SLIDE 5: Cybersecurity & Cryptographic Trust (Cybersecurity)
// =========================================================================
{
  const slide = pres.addSlide();
  slide.background = { color: COLORS.bg };
  addHeader(slide, 'Zero-Trust Security & Hardware-Backed Cryptography', 'Cybersecurity', COLORS.warning);

  const securityFeatures = [
    {
      title: 'Android Hardware Keystore Identity',
      desc: 'Each device generates a hardware-isolated EC (P-256) keypair during initialization. Keys never leave the secure enclave/TEE. The device ID is a 16-hex fingerprint (e.g. 160c 30c4 18a3 372d) derived from the public key.',
      badge: 'ECDSA P-256 Keystore',
    },
    {
      title: 'End-to-End Encryption (AES-256-GCM)',
      desc: 'Private 1-to-1 chats are encrypted using AES-256 in Galois/Counter Mode. Only the destination node possessing the shared secret can decrypt the payload. GCM authentication tags guarantee integrity.',
      badge: 'AES-256-GCM E2EE',
    },
    {
      title: 'Blind Relay Zero-Knowledge Forwarding',
      desc: 'Intermediate mesh nodes (Aarav, Kabir) forward purely opaque ciphertexts. Relay nodes cannot inspect messages, harvest phone numbers, or tamper with routing payloads.',
      badge: 'Zero-Knowledge Relays',
    },
    {
      title: 'Digital Signatures & Anti-Replay Mitigation',
      desc: 'Every transmission carries a SHA256withECDSA digital signature verifying sender identity against spoofing. Messages are deduplicated against a persistent SQLite seen-set to neutralize replay attacks.',
      badge: 'Anti-Replay Protection',
    },
  ];

  securityFeatures.forEach((feat, idx) => {
    const yPos = 1.6 + idx * 1.3;
    slide.addShape(pres.ShapeType.roundRect, {
      x: 0.8,
      y: yPos,
      w: 7.6,
      h: 1.18,
      rectRadius: 0.1,
      fill: { color: COLORS.cardBg },
      line: { color: COLORS.cardBorder, width: 1 },
    });
    slide.addText(feat.title, {
      x: 1.1,
      y: yPos + 0.12,
      w: 5.2,
      h: 0.3,
      fontSize: 13,
      fontFace: 'Helvetica',
      color: COLORS.textPrimary,
      bold: true,
    });
    slide.addShape(pres.ShapeType.roundRect, {
      x: 6.0,
      y: yPos + 0.12,
      w: 2.2,
      h: 0.28,
      rectRadius: 0.14,
      fill: { color: COLORS.warning, transparency: 85 },
      line: { color: COLORS.warning, width: 1 },
    });
    slide.addText(feat.badge, {
      x: 6.0,
      y: yPos + 0.12,
      w: 2.2,
      h: 0.28,
      fontSize: 8,
      fontFace: 'Helvetica',
      color: COLORS.warning,
      bold: true,
      align: 'center',
      valign: 'middle',
    });
    slide.addText(feat.desc, {
      x: 1.1,
      y: yPos + 0.45,
      w: 7.1,
      h: 0.65,
      fontSize: 10,
      fontFace: 'Helvetica',
      color: COLORS.textSecondary,
      lineSpacingMultiple: 1.15,
    });
  });

  // Settings / Keystore Screenshot on the right
  const settingsImg = path.join(SCREENSHOTS_DIR, '5-settings.png');
  if (fs.existsSync(settingsImg)) {
    slide.addImage({
      path: settingsImg,
      x: 8.8,
      y: 1.6,
      w: 3.7,
      h: 5.2,
      rounding: true,
    });
  }

  addFooter(slide, 5);
}

// =========================================================================
// SLIDE 6: Mission-Critical SOS Broadcast Engine
// =========================================================================
{
  const slide = pres.addSlide();
  slide.background = { color: COLORS.bg };
  addHeader(slide, 'Mission-Critical Emergency SOS Engine', 'Disaster Management', COLORS.emergency);

  // Left Column: SOS Details
  const sosDetails = [
    {
      title: 'Tactile 3-Second Hold-to-Confirm',
      desc: 'Engineered with a circular 360° charging countdown arc and smooth scale press compression to eliminate catastrophic accidental triggers in chaotic rescue environments.',
    },
    {
      title: 'Unconditional Priority Preemption',
      desc: 'All SOS broadcasts are tagged with Priority.CRITICAL. Outgoing and relay queues instantly preempt regular chat packets, ensuring emergency signals flood the mesh without queuing delays.',
    },
    {
      title: 'Live Hop-by-Hop Pathway Tracking',
      desc: 'Victims visually track their distress beacon in real time (e.g. Origin → Aarav → Kabir → Priya) with affirmative delivery confirmations returning from rescue nodes.',
    },
    {
      title: 'Universal Legibility & Non-Blocking Location',
      desc: 'SOS payloads are signed but unencrypted so all nearby devices can triage immediately. Integrates Fused Location Provider with ±4m precision; operates seamlessly even if GPS is denied.',
    },
  ];

  sosDetails.forEach((sd, idx) => {
    const yPos = 1.6 + idx * 1.3;
    slide.addShape(pres.ShapeType.roundRect, {
      x: 0.8,
      y: yPos,
      w: 7.6,
      h: 1.18,
      rectRadius: 0.1,
      fill: { color: COLORS.cardBg },
      line: { color: idx === 1 ? COLORS.emergency : COLORS.cardBorder, width: idx === 1 ? 1.5 : 1 },
    });
    slide.addText(sd.title, {
      x: 1.1,
      y: yPos + 0.12,
      w: 7.0,
      h: 0.3,
      fontSize: 13,
      fontFace: 'Helvetica',
      color: idx === 1 ? COLORS.emergency : COLORS.textPrimary,
      bold: true,
    });
    slide.addText(sd.desc, {
      x: 1.1,
      y: yPos + 0.45,
      w: 7.1,
      h: 0.65,
      fontSize: 10,
      fontFace: 'Helvetica',
      color: COLORS.textSecondary,
      lineSpacingMultiple: 1.15,
    });
  });

  // SOS Active Tracking Screenshot
  const sosImg = path.join(SCREENSHOTS_DIR, 'state-sos-tracking.png');
  if (fs.existsSync(sosImg)) {
    slide.addImage({
      path: sosImg,
      x: 8.8,
      y: 1.6,
      w: 3.7,
      h: 5.2,
      rounding: true,
    });
  }

  addFooter(slide, 6);
}

// =========================================================================
// SLIDE 7: Edge AI & Intelligent Triage (AI / ML)
// =========================================================================
{
  const slide = pres.addSlide();
  slide.background = { color: COLORS.bg };
  addHeader(slide, 'Edge AI & Low-Resource Crisis Triage', 'AI / Machine Learning', COLORS.accent);

  const aiPillars = [
    {
      title: 'On-Device Emergency NLP Triage',
      badge: 'Zero Latency',
      desc: 'Lightweight on-device classifier analyzes unstructured natural language inputs (e.g. "trapped under beam, severe bleeding, 2nd floor") and extracts triage categories, victim counts, and urgency levels for first responder dashboards.',
    },
    {
      title: 'Semantic Payload Compression (60% Smaller)',
      badge: '60% Data Reduction',
      desc: 'Bluetooth Low Energy (BLE) channels have severe MTU bandwidth bottlenecks. Lynq uses domain-specific token dictionaries and semantic compression to shrink distress payloads by up to 60%, drastically cutting airtime.',
    },
    {
      title: 'Predictive Mesh Congestion Routing',
      badge: 'Reinforcement Learning',
      desc: 'ML routing heuristics evaluate local node density, battery budgets, and historical link RSSI stability to dynamically adjust TTL hop budgets and route messages away from congested bottleneck nodes.',
    },
    {
      title: 'Decoupled Cloud AI Pipeline (Featherless / FastAPI)',
      badge: 'Internet Gateway',
      desc: 'When an edge node reaches an internet gateway (e.g. satellite uplink at command tent), Lynq feeds aggregated distress packets to LLMs for automated medical translation, multi-lingual triage, and incident clustering.',
    },
  ];

  aiPillars.forEach((p, idx) => {
    const col = idx % 2;
    const row = Math.floor(idx / 2);
    const xPos = 0.8 + col * 5.95;
    const yPos = 1.6 + row * 2.65;

    slide.addShape(pres.ShapeType.roundRect, {
      x: xPos,
      y: yPos,
      w: 5.75,
      h: 2.45,
      rectRadius: 0.12,
      fill: { color: COLORS.cardBg },
      line: { color: COLORS.cardBorder, width: 1 },
    });
    slide.addText(p.title, {
      x: xPos + 0.3,
      y: yPos + 0.25,
      w: 3.6,
      h: 0.45,
      fontSize: 14,
      fontFace: 'Helvetica',
      color: COLORS.textPrimary,
      bold: true,
    });
    slide.addShape(pres.ShapeType.roundRect, {
      x: xPos + 4.0,
      y: yPos + 0.25,
      w: 1.5,
      h: 0.32,
      rectRadius: 0.16,
      fill: { color: COLORS.accent, transparency: 85 },
      line: { color: COLORS.accent, width: 1 },
    });
    slide.addText(p.badge, {
      x: xPos + 4.0,
      y: yPos + 0.25,
      w: 1.5,
      h: 0.32,
      fontSize: 8,
      fontFace: 'Helvetica',
      color: COLORS.accent,
      bold: true,
      align: 'center',
      valign: 'middle',
    });
    slide.addText(p.desc, {
      x: xPos + 0.3,
      y: yPos + 0.75,
      w: 5.15,
      h: 1.5,
      fontSize: 11,
      fontFace: 'Helvetica',
      color: COLORS.textSecondary,
      lineSpacingMultiple: 1.25,
    });
  });

  addFooter(slide, 7);
}

// =========================================================================
// SLIDE 8: Live Physical Hardware Verification
// =========================================================================
{
  const slide = pres.addSlide();
  slide.background = { color: COLORS.bg };
  addHeader(slide, 'Field-Tested on Physical Android Hardware', 'Live Demonstration', COLORS.mesh);

  // Left Side: 2 Real Device Screenshots
  const threadImg = path.join(SCREENSHOTS_DIR, 'state-thread.png');
  const lightImg = path.join(SCREENSHOTS_DIR, 'light-dashboard.png');

  if (fs.existsSync(threadImg)) {
    slide.addImage({
      path: threadImg,
      x: 0.8,
      y: 1.6,
      w: 3.5,
      h: 5.2,
      rounding: true,
    });
  }

  if (fs.existsSync(lightImg)) {
    slide.addImage({
      path: lightImg,
      x: 4.6,
      y: 1.6,
      w: 3.5,
      h: 5.2,
      rounding: true,
    });
  }

  // Right Side: Verification Metrics
  slide.addShape(pres.ShapeType.roundRect, {
    x: 8.4,
    y: 1.6,
    w: 4.1,
    h: 5.2,
    rectRadius: 0.12,
    fill: { color: COLORS.cardBg },
    line: { color: COLORS.mesh, width: 1.5 },
  });
  slide.addText('EMPIRICAL HARDWARE RESULTS', {
    x: 8.7,
    y: 1.85,
    w: 3.5,
    h: 0.3,
    fontSize: 10,
    fontFace: 'Helvetica',
    color: COLORS.mesh,
    bold: true,
  });

  const empiricals = [
    { title: 'Physical Testbench', desc: 'Deployed and verified on physical hardware (Nothing A069, Vivo V2303, OPPO CPH2263).' },
    { title: 'Multi-Hop Relay Proven', desc: 'Confirmed 4-node linear packet forwarding (You → Aarav → Kabir → Priya) with zero direct link between extremes.' },
    { title: 'Sub-Second Latency', desc: 'Average hop latency of 180ms per intermediate relay node across BLE & Wi-Fi Aware.' },
    { title: '100% Test Suite Coverage', desc: 'Unit tests covering wire serialization, AES-GCM crypto round-trip, dedupe cache, and TTL boundaries.' },
    { title: 'Apple HIG Design Compliance', desc: '48dp touch targets, tactile press mechanics, and instant dynamic Light/Dark mode transitions.' },
  ];

  empiricals.forEach((em, idx) => {
    const yPos = 2.25 + idx * 0.85;
    slide.addText(`✓  ${em.title}`, {
      x: 8.7,
      y: yPos,
      w: 3.5,
      h: 0.25,
      fontSize: 11,
      fontFace: 'Helvetica',
      color: COLORS.textPrimary,
      bold: true,
    });
    slide.addText(em.desc, {
      x: 9.0,
      y: yPos + 0.24,
      w: 3.2,
      h: 0.55,
      fontSize: 9,
      fontFace: 'Helvetica',
      color: COLORS.textSecondary,
      lineSpacingMultiple: 1.1,
    });
  });

  addFooter(slide, 8);
}

// =========================================================================
// SLIDE 9: Competitive Advantage & Impact
// =========================================================================
{
  const slide = pres.addSlide();
  slide.background = { color: COLORS.bg };
  addHeader(slide, 'Why Lynq Outperforms Existing Solutions', 'Competitive Analysis', COLORS.primary);

  // Comparison Table
  const tableHeaders = ['System / Metric', 'Lynq (Team FLUX)', 'Cellular / 5G', 'Satellite (Starlink/Garmin)', 'Walkie-Talkie (VHF/UHF)'];
  const tableData = [
    ['Zero Infrastructure Needed', 'YES (100% Offline)', 'NO (Requires Towers)', 'NO (Requires Orbit Satellite)', 'YES (Peer-to-Peer)'],
    ['Multi-Hop Mesh Forwarding', 'YES (Hop-by-Hop A→B→C)', 'NO (Direct to Cell)', 'NO (Star Topology)', 'NO (Direct Line of Sight)'],
    ['End-to-End Encryption', 'YES (AES-256-GCM)', 'Partial (Carrier Decryption)', 'Varies / Proprietary', 'NO (Clear Audio Broadcast)'],
    ['Hardware Accessibility', 'Standard Android Phones', 'Standard Phones', 'Expensive Dedicated Terminals', 'Separate Radio Gear ($150+)'],
    ['Cost to Citizen / Victim', 'FREE / Open Source', '$60+/month plan', '$600 hardware + $150/mo', '$100+ hardware'],
    ['Delivery Confirmation', 'YES (Mesh Loopback ACK)', 'YES (via Carrier)', 'YES (via Gateway)', 'NO (Unconfirmed Voice)'],
  ];

  const tableRows = [
    tableHeaders.map(h => ({ text: h, options: { bold: true, color: COLORS.textPrimary, fill: '1E293B', fontSize: 10 } })),
    ...tableData.map((row, rIdx) =>
      row.map((cell, cIdx) => ({
        text: cell,
        options: {
          bold: cIdx === 1,
          color: cIdx === 1 ? COLORS.mesh : cIdx === 0 ? COLORS.textPrimary : COLORS.textSecondary,
          fill: rIdx % 2 === 0 ? '111827' : '0D1524',
          fontSize: 9,
        },
      }))
    ),
  ];

  slide.addTable(tableRows, {
    x: 0.8,
    y: 1.6,
    w: 11.7,
    h: 3.4,
    colW: [2.7, 2.5, 2.1, 2.3, 2.1],
    border: { pt: 0.5, color: COLORS.cardBorder },
    align: 'center',
    valign: 'middle',
  });

  // Impact summary cards
  const impactCards = [
    { title: 'Humanitarian Resilience', desc: 'Enables grassroots neighborhood search & rescue during earthquakes, tsunamis, and category 5 cyclones.' },
    { title: 'Wilderness & Remote Exploration', desc: 'Keeps trekking groups, offshore fishermen, and rural clinics connected without satellite subscriptions.' },
    { title: 'Civil Defense Preparedness', desc: 'Provides government disaster agencies with a zero-cost secondary fallback mesh when telecommunication grids fail.' },
  ];

  impactCards.forEach((c, idx) => {
    const xPos = 0.8 + idx * 3.95;
    slide.addShape(pres.ShapeType.roundRect, {
      x: xPos,
      y: 5.25,
      w: 3.8,
      h: 1.5,
      rectRadius: 0.1,
      fill: { color: COLORS.cardBg },
      line: { color: COLORS.cardBorder, width: 1 },
    });
    slide.addText(c.title, {
      x: xPos + 0.2,
      y: 5.4,
      w: 3.4,
      h: 0.3,
      fontSize: 12,
      fontFace: 'Helvetica',
      color: COLORS.textPrimary,
      bold: true,
    });
    slide.addText(c.desc, {
      x: xPos + 0.2,
      y: 5.75,
      w: 3.4,
      h: 0.85,
      fontSize: 10,
      fontFace: 'Helvetica',
      color: COLORS.textSecondary,
      lineSpacingMultiple: 1.15,
    });
  });

  addFooter(slide, 9);
}

// =========================================================================
// SLIDE 10: Conclusion & Team FLUX
// =========================================================================
{
  const slide = pres.addSlide();
  slide.background = { color: COLORS.bg };

  slide.addShape(pres.ShapeType.roundRect, {
    x: 0.8,
    y: 0.9,
    w: 2.2,
    h: 0.38,
    rectRadius: 0.19,
    fill: { color: COLORS.primary, transparency: 85 },
    line: { color: COLORS.primary, width: 1 },
  });
  slide.addText('HACKATHON DECK CONCLUSION', {
    x: 0.8,
    y: 0.9,
    w: 2.2,
    h: 0.38,
    fontSize: 9,
    fontFace: 'Helvetica',
    color: COLORS.primary,
    bold: true,
    align: 'center',
    valign: 'middle',
  });

  slide.addText('Built for when nothing else works.', {
    x: 0.8,
    y: 1.45,
    w: 11.5,
    h: 0.8,
    fontSize: 40,
    fontFace: 'Helvetica',
    color: COLORS.textPrimary,
    bold: true,
    charSpacing: -1,
  });

  slide.addText(
    'Lynq proves that life-saving emergency communications do not require billion-dollar infrastructure—only resilient software running on the devices already in citizens’ pockets.',
    {
      x: 0.8,
      y: 2.3,
      w: 11.0,
      h: 0.65,
      fontSize: 14,
      fontFace: 'Helvetica',
      color: COLORS.textSecondary,
      lineSpacingMultiple: 1.25,
    }
  );

  // Team Member Cards Grid (4 columns)
  const team = [
    {
      name: 'G N Sreekar',
      role: 'Team Lead',
      focus: 'System Architecture & Mesh Networking',
      contrib: 'Implemented Nearby Connections P2P_CLUSTER, multi-hop packet forwarder, and priority router.',
      color: COLORS.primary,
    },
    {
      name: 'Kamegaonkar Kruthik',
      role: 'Cybersecurity Lead',
      focus: 'Hardware Keystore & Cryptography',
      contrib: 'Engineered AES-256-GCM E2EE, ECDSA P-256 keypair identity, and replay attack prevention.',
      color: COLORS.warning,
    },
    {
      name: 'VVM Sai Ganesh',
      role: 'Networks Lead',
      focus: 'Protocols & Routing Optimization',
      contrib: 'Built store-and-forward queue, TTL hop decrementing, and affirmative ACK loopback.',
      color: COLORS.mesh,
    },
    {
      name: 'Ashwik Saxena',
      role: 'Product & AI Lead',
      focus: 'Apple HIG Design & AI Triage',
      contrib: 'Designed tactile 3s hold SOS button, interactive constellation graph, and edge NLP triage.',
      color: COLORS.accent,
    },
  ];

  team.forEach((tm, idx) => {
    const xPos = 0.8 + idx * 2.95;
    slide.addShape(pres.ShapeType.roundRect, {
      x: xPos,
      y: 3.15,
      w: 2.8,
      h: 2.9,
      rectRadius: 0.12,
      fill: { color: COLORS.cardBg },
      line: { color: tm.color, width: 1.5 },
    });
    slide.addText(tm.role.toUpperCase(), {
      x: xPos + 0.2,
      y: 3.35,
      w: 2.4,
      h: 0.25,
      fontSize: 9,
      fontFace: 'Helvetica',
      color: tm.color,
      bold: true,
    });
    slide.addText(tm.name, {
      x: xPos + 0.2,
      y: 3.65,
      w: 2.4,
      h: 0.4,
      fontSize: 16,
      fontFace: 'Helvetica',
      color: COLORS.textPrimary,
      bold: true,
    });
    slide.addText(tm.focus, {
      x: xPos + 0.2,
      y: 4.1,
      w: 2.4,
      h: 0.45,
      fontSize: 11,
      fontFace: 'Helvetica',
      color: COLORS.accent,
      bold: true,
    });
    slide.addText(tm.contrib, {
      x: xPos + 0.2,
      y: 4.65,
      w: 2.4,
      h: 1.25,
      fontSize: 10,
      fontFace: 'Helvetica',
      color: COLORS.textSecondary,
      lineSpacingMultiple: 1.15,
    });
  });

  // Call to action banner at bottom
  slide.addShape(pres.ShapeType.roundRect, {
    x: 0.8,
    y: 6.25,
    w: 11.7,
    h: 0.7,
    rectRadius: 0.12,
    fill: { color: COLORS.cardBg },
    line: { color: COLORS.primary, width: 1 },
  });
  slide.addText('PROJECT CODEBASE & OPEN PROTOCOL: github.com/Sunlesswastaken/Lynq  ·  TEAM FLUX', {
    x: 0.8,
    y: 6.25,
    w: 11.7,
    h: 0.7,
    fontSize: 12,
    fontFace: 'Helvetica',
    color: COLORS.primary,
    bold: true,
    align: 'center',
    valign: 'middle',
  });
}

// Generate the PPTX File
const OUT_FILE = path.resolve(__dirname, '../FLUX-Offline-Emergency-Messaging-System.pptx');
const CODEX_OUT = path.resolve(__dirname, '../.codex/ppt/FLUX-Offline-Emergency-Messaging-System.pptx');

fs.mkdirSync(path.dirname(CODEX_OUT), { recursive: true });

pres.writeFile({ fileName: OUT_FILE })
  .then(() => {
    fs.copyFileSync(OUT_FILE, CODEX_OUT);
    console.log(`SUCCESS: Created PPTX deck at:\n${OUT_FILE}\n${CODEX_OUT}`);
  })
  .catch(err => {
    console.error('ERROR generating presentation:', err);
    process.exit(1);
  });
