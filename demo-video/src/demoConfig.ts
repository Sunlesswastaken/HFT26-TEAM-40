export const demoConfig = {
  productName: "Lynq",
  url: "lynq.mesh",
  tagline: "Offline Emergency Mesh Network",
  colors: {
    ink: "#080b11",
    cardDark: "#121721",
    borderDark: "rgba(255, 255, 255, 0.10)",
    muted: "#8e99a8",
    primary: "#0A84FF", // Apple system blue
    emergency: "#FF453A", // Apple system red
    mesh: "#30D158", // Apple system green
    warning: "#FF9F0A", // Apple system orange
    background: "#080c14",
    lightBackground: "#F2F4F7",
  },
  scenes: {
    hook: {
      eyebrow: "OFFLINE RESILIENCE",
      headline: "Zero Cellular.\nZero Internet.\n100% Connected.",
      subline: "When disasters strike and cell towers collapse, Lynq turns everyday Android devices into a decentralized, hop-by-hop mesh network.",
      badge: "Google Nearby P2P_CLUSTER",
      screenshot: "screenshots/1-dashboard.png",
    },
    mesh: {
      eyebrow: "DECENTRALIZED TOPOLOGY",
      headline: "Multi-Hop Packet Forwarding",
      subline: "Packets dynamically route across intermediate phones (You → Aarav → Kabir → Priya) reaching destinations far outside physical radio range.",
      badge: "Dynamic TTL & Route Deduplication",
      screenshot: "screenshots/4-mesh.png",
      route: ["You", "Aarav", "Kabir", "Priya"],
    },
    sos: {
      eyebrow: "CRITICAL LIFE SAFETY",
      headline: "3-Second Hold SOS Broadcast",
      subline: "Emergency alerts preempt all network traffic, broadcasting triage categories, GPS coordinates, and real-time multi-node relay tracking.",
      badge: "Priority: CRITICAL · Auto Relaying",
      screenshot: "screenshots/state-sos-tracking.png",
      category: "Medical Emergency",
    },
    chat: {
      eyebrow: "END-TO-END SECURITY",
      headline: "Encrypted Multi-Hop Chat",
      subline: "AES-256-GCM ciphertexts with hardware Keystore ECDSA signatures. Intermediate relays forward blindly without decryption.",
      badge: "SENT ➔ RELAYED ➔ DELIVERED",
      screenshot: "screenshots/state-thread.png",
    },
    design: {
      eyebrow: "APPLE HIG DESIGN CRAFT",
      headline: "Engineered for Extreme Stress",
      subline: "Tactile controls, 48dp touch targets, and full dynamic Light & Dark mode support built for emergency responders.",
      badge: "Apple Design System · WCAG AAA",
      screenshotDark: "screenshots/5-settings.png",
      screenshotLight: "screenshots/light-dashboard.png",
    },
    cta: {
      eyebrow: "THE FUTURE OF DISASTER COMMS",
      headline: "Built for when nothing else works.",
      subline: "Open-source, resilient, decentralized communication for everyone.",
      website: "github.com/Sunlesswastaken/Lynq",
    },
  },
};
