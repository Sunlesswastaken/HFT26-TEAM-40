import React from "react";
import {
  AbsoluteFill,
  Audio,
  Img,
  Sequence,
  interpolate,
  spring,
  staticFile,
  useCurrentFrame,
  useVideoConfig,
  Easing,
} from "remotion";
import {demoConfig} from "./demoConfig";

const second = (value: number) => Math.round(value * 30);

const progress = (frame: number, start: number, end: number) =>
  interpolate(frame, [start, end], [0, 1], {
    extrapolateLeft: "clamp",
    extrapolateRight: "clamp",
    easing: Easing.bezier(0.16, 1, 0.3, 1),
  });

// --- Shell Component with Grid and Ambient Radial Glow ---
const Shell: React.FC<{
  children: React.ReactNode;
  accentColor?: string;
  glowPosition?: "left" | "right" | "center";
}> = ({children, accentColor = demoConfig.colors.primary, glowPosition = "right"}) => {
  const frame = useCurrentFrame();
  const pulse = Math.sin(frame / 35) * 0.15 + 0.85;

  let glowLeft = "75%";
  if (glowPosition === "left") glowLeft = "25%";
  if (glowPosition === "center") glowLeft = "50%";

  return (
    <AbsoluteFill
      style={{
        background: `radial-gradient(circle at ${glowLeft} 45%, ${accentColor}18 0%, ${demoConfig.colors.background} 75%)`,
        backgroundColor: demoConfig.colors.background,
        color: "#ffffff",
        fontFamily:
          '-apple-system, BlinkMacSystemFont, "SF Pro Display", "SF Pro Text", "Segoe UI", Roboto, Helvetica, Arial, sans-serif',
        overflow: "hidden",
      }}
    >
      {/* Precision Tech Grid */}
      <div
        style={{
          position: "absolute",
          inset: 0,
          backgroundImage:
            "linear-gradient(rgba(255, 255, 255, 0.04) 1px, transparent 1px), linear-gradient(90deg, rgba(255, 255, 255, 0.04) 1px, transparent 1px)",
          backgroundSize: "60px 60px",
          opacity: 0.6,
        }}
      />

      {/* Ambient Pulsing Orb */}
      <div
        style={{
          position: "absolute",
          left: glowLeft,
          top: "45%",
          width: 700,
          height: 700,
          transform: `translate(-50%, -50%) scale(${pulse})`,
          background: `radial-gradient(circle, ${accentColor}25 0%, transparent 70%)`,
          filter: "blur(60px)",
          pointerEvents: "none",
        }}
      />

      {/* Top Header Bar */}
      <div
        style={{
          position: "absolute",
          top: 48,
          left: 90,
          right: 90,
          display: "flex",
          justifyContent: "space-between",
          alignItems: "center",
          zIndex: 10,
        }}
      >
        <div style={{display: "flex", alignItems: "center", gap: 16}}>
          <div
            style={{
              width: 44,
              height: 44,
              borderRadius: 12,
              background: "linear-gradient(135deg, #0A84FF, #0056B3)",
              display: "flex",
              alignItems: "center",
              justifyContent: "center",
              boxShadow: "0 4px 16px rgba(10, 132, 255, 0.4)",
            }}
          >
            {/* Minimal App Icon Waveform */}
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none">
              <path
                d="M4 12C4 7.58 7.58 4 12 4M12 20C7.58 20 4 16.42 4 12M12 4C16.42 4 20 7.58 20 12M20 12C20 16.42 16.42 20 12 20"
                stroke="#fff"
                strokeWidth="2.5"
                strokeLinecap="round"
              />
              <circle cx="12" cy="12" r="3" fill="#fff" />
            </svg>
          </div>
          <div>
            <div style={{fontSize: 24, fontWeight: 800, letterSpacing: "-0.02em", color: "#fff"}}>
              Lynq
            </div>
            <div style={{fontSize: 13, color: demoConfig.colors.muted, fontWeight: 500}}>
              Offline Emergency Mesh Protocol
            </div>
          </div>
        </div>

        {/* Live Network Badge */}
        <div
          style={{
            display: "flex",
            alignItems: "center",
            gap: 10,
            padding: "8px 18px",
            borderRadius: 999,
            background: "rgba(255, 255, 255, 0.06)",
            border: "1px solid rgba(255, 255, 255, 0.12)",
            backdropFilter: "blur(20px)",
          }}
        >
          <div
            style={{
              width: 8,
              height: 8,
              borderRadius: 4,
              background: demoConfig.colors.mesh,
              boxShadow: `0 0 10px ${demoConfig.colors.mesh}`,
            }}
          />
          <span style={{fontSize: 14, fontWeight: 600, color: "#e2e8f0", letterSpacing: "0.02em"}}>
            P2P CLUSTER ACTIVE · 6 PEERS
          </span>
        </div>
      </div>

      {children}
    </AbsoluteFill>
  );
};

// --- High-End Mobile Phone Frame Component ---
const PhoneFrame: React.FC<{
  image: string;
  label?: string;
  scale?: number;
  offsetX?: number;
  offsetY?: number;
  highlightColor?: string;
}> = ({
  image,
  label = "Live Physical Device",
  scale = 1,
  offsetX = 0,
  offsetY = 0,
  highlightColor = demoConfig.colors.primary,
}) => {
  const frame = useCurrentFrame();
  const drift = Math.sin(frame / 50) * 6;

  return (
    <div
      style={{
        position: "relative",
        transform: `translate(${offsetX}px, ${offsetY + drift}px) scale(${scale})`,
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
      }}
    >
      {/* Phone Chassis */}
      <div
        style={{
          position: "relative",
          width: 410,
          height: 880,
          borderRadius: 48,
          background: "#05070a",
          border: "3px solid rgba(255, 255, 255, 0.18)",
          boxShadow: `0 35px 90px rgba(0, 0, 0, 0.8), 0 0 50px ${highlightColor}22`,
          overflow: "hidden",
        }}
      >
        {/* Front Speaker & Camera Island */}
        <div
          style={{
            position: "absolute",
            top: 10,
            left: "50%",
            transform: "translateX(-50%)",
            width: 90,
            height: 18,
            borderRadius: 9,
            background: "#000",
            zIndex: 20,
            display: "flex",
            alignItems: "center",
            justifyContent: "center",
          }}
        >
          <div style={{width: 8, height: 8, borderRadius: 4, background: "#111", marginRight: 14}} />
          <div style={{width: 32, height: 4, borderRadius: 2, background: "#1c1c1e"}} />
        </div>

        {/* Screen Content */}
        <Img
          src={staticFile(image)}
          style={{
            width: "100%",
            height: "100%",
            objectFit: "cover",
            borderRadius: 45,
          }}
        />
      </div>

      {/* Floating Badge Below Phone */}
      {label && (
        <div
          style={{
            marginTop: 14,
            padding: "6px 18px",
            borderRadius: 999,
            background: "rgba(18, 24, 38, 0.85)",
            backdropFilter: "blur(12px)",
            border: "1px solid rgba(255, 255, 255, 0.15)",
            color: "#ffffff",
            fontSize: 13,
            fontWeight: 600,
            letterSpacing: "0.03em",
            whiteSpace: "nowrap",
            boxShadow: "0 8px 24px rgba(0,0,0,0.5)",
          }}
        >
          {label}
        </div>
      )}
    </div>
  );
};

// --- Kinetic Left Typography Column ---
const LeftColumn: React.FC<{
  eyebrow: string;
  headline: string;
  subline: string;
  eyebrowColor?: string;
  children?: React.ReactNode;
}> = ({eyebrow, headline, subline, eyebrowColor = demoConfig.colors.primary, children}) => {
  const frame = useCurrentFrame();
  const t = progress(frame, 4, 28);

  return (
    <div
      style={{
        position: "absolute",
        left: 100,
        top: 170,
        width: 820,
        transform: `translateY(${(1 - t) * 30}px)`,
        opacity: t,
        display: "flex",
        flexDirection: "column",
        gap: 24,
      }}
    >
      {/* Eyebrow Pill */}
      <div style={{display: "flex"}}>
        <div
          style={{
            display: "inline-flex",
            alignItems: "center",
            gap: 8,
            padding: "8px 18px",
            borderRadius: 999,
            background: `${eyebrowColor}18`,
            border: `1px solid ${eyebrowColor}44`,
            color: eyebrowColor,
            fontSize: 14,
            fontWeight: 700,
            letterSpacing: "0.08em",
            textTransform: "uppercase",
          }}
        >
          <span style={{fontSize: 16}}>●</span> {eyebrow}
        </div>
      </div>

      {/* Main Headline */}
      <div
        style={{
          fontSize: 60,
          fontWeight: 850,
          lineHeight: 1.1,
          letterSpacing: "-0.03em",
          color: "#ffffff",
          whiteSpace: "pre-line",
          textShadow: "0 4px 20px rgba(0,0,0,0.5)",
        }}
      >
        {headline}
      </div>

      {/* Subline Explanation */}
      <div
        style={{
          fontSize: 22,
          lineHeight: 1.5,
          color: "#94a3b8",
          maxWidth: 720,
          fontWeight: 400,
        }}
      >
        {subline}
      </div>

      {/* Extra Badges / Metric Cards */}
      {children && <div style={{marginTop: 8}}>{children}</div>}
    </div>
  );
};

// ==========================================
// SCENE 1: Hook & Offline Architecture (0 - 8s)
// ==========================================
const Scene1Hook: React.FC = () => {
  const cfg = demoConfig.scenes.hook;
  return (
    <Shell accentColor={demoConfig.colors.primary}>
      <LeftColumn
        eyebrow={cfg.eyebrow}
        headline={cfg.headline}
        subline={cfg.subline}
        eyebrowColor={demoConfig.colors.primary}
      >
        <div style={{display: "flex", gap: 14, flexWrap: "wrap", marginTop: 12}}>
          {[
            {label: "Protocol", val: "Nearby P2P_CLUSTER"},
            {label: "Range", val: "Multi-Hop Flooding"},
            {label: "Infrastructure", val: "Zero Towers Needed"},
          ].map((item, idx) => (
            <div
              key={idx}
              style={{
                padding: "12px 20px",
                borderRadius: 16,
                background: "rgba(255, 255, 255, 0.05)",
                border: "1px solid rgba(255, 255, 255, 0.1)",
              }}
            >
              <div style={{fontSize: 12, color: "#64748b", fontWeight: 600}}>{item.label}</div>
              <div style={{fontSize: 17, color: "#f8fafc", fontWeight: 700, marginTop: 2}}>
                {item.val}
              </div>
            </div>
          ))}
        </div>
      </LeftColumn>

      <div style={{position: "absolute", right: 140, top: 120}}>
        <PhoneFrame
          image={cfg.screenshot}
          label="Lynq Network Dashboard · 5 Nodes Active"
          scale={0.94}
          highlightColor={demoConfig.colors.primary}
        />
      </div>
    </Shell>
  );
};

// ==========================================
// SCENE 2: Multi-Hop Mesh Topology (8 - 16s)
// ==========================================
const Scene2Mesh: React.FC = () => {
  const cfg = demoConfig.scenes.mesh;
  const frame = useCurrentFrame();

  return (
    <Shell accentColor={demoConfig.colors.mesh}>
      <LeftColumn
        eyebrow={cfg.eyebrow}
        headline={cfg.headline}
        subline={cfg.subline}
        eyebrowColor={demoConfig.colors.mesh}
      >
        {/* Dynamic Route Pathway Indicator */}
        <div
          style={{
            padding: "20px 24px",
            borderRadius: 20,
            background: "rgba(18, 24, 38, 0.8)",
            border: "1px solid rgba(48, 209, 88, 0.3)",
            maxWidth: 680,
            marginTop: 10,
          }}
        >
          <div
            style={{
              fontSize: 13,
              color: demoConfig.colors.mesh,
              fontWeight: 700,
              textTransform: "uppercase",
              letterSpacing: "0.06em",
              marginBottom: 12,
            }}
          >
            Real-Time Relay Path (Hop Limit: 6)
          </div>
          <div style={{display: "flex", alignItems: "center", gap: 10, flexWrap: "wrap"}}>
            {cfg.route.map((node, i) => {
              const isLast = i === cfg.route.length - 1;
              const isActive = Math.floor(frame / 18) % cfg.route.length === i;
              return (
                <React.Fragment key={node}>
                  <div
                    style={{
                      padding: "8px 16px",
                      borderRadius: 12,
                      background: isActive
                        ? "rgba(48, 209, 88, 0.25)"
                        : "rgba(255, 255, 255, 0.08)",
                      border: isActive
                        ? `2px solid ${demoConfig.colors.mesh}`
                        : "1px solid rgba(255, 255, 255, 0.15)",
                      color: isActive ? "#ffffff" : "#cbd5e1",
                      fontSize: 16,
                      fontWeight: 700,
                      display: "flex",
                      alignItems: "center",
                      gap: 8,
                      transform: isActive ? "scale(1.06)" : "scale(1)",
                      transition: "transform 0.2s ease",
                    }}
                  >
                    <span
                      style={{
                        width: 8,
                        height: 8,
                        borderRadius: 4,
                        background: i === 0 ? "#0A84FF" : isLast ? "#FF453A" : "#30D158",
                      }}
                    />
                    {node}
                  </div>
                  {!isLast && (
                    <span style={{color: demoConfig.colors.mesh, fontSize: 18, fontWeight: 800}}>
                      ➔
                    </span>
                  )}
                </React.Fragment>
              );
            })}
          </div>
        </div>
      </LeftColumn>

      <div style={{position: "absolute", right: 140, top: 120}}>
        <PhoneFrame
          image={cfg.screenshot}
          label="Live Constellation & SOS Red Path"
          scale={0.94}
          highlightColor={demoConfig.colors.mesh}
        />
      </div>
    </Shell>
  );
};

// ==========================================
// SCENE 3: Critical Emergency SOS (16 - 25s)
// ==========================================
const Scene3Sos: React.FC = () => {
  const cfg = demoConfig.scenes.sos;
  return (
    <Shell accentColor={demoConfig.colors.emergency}>
      <LeftColumn
        eyebrow={cfg.eyebrow}
        headline={cfg.headline}
        subline={cfg.subline}
        eyebrowColor={demoConfig.colors.emergency}
      >
        <div
          style={{
            padding: "20px 24px",
            borderRadius: 20,
            background: "rgba(255, 69, 58, 0.08)",
            border: "1px solid rgba(255, 69, 58, 0.35)",
            maxWidth: 680,
            display: "flex",
            flexDirection: "column",
            gap: 12,
            marginTop: 10,
          }}
        >
          <div style={{display: "flex", justifyContent: "space-between", alignItems: "center"}}>
            <span style={{color: "#FF453A", fontWeight: 800, fontSize: 15, letterSpacing: "0.04em"}}>
              🚨 UNCONDITIONAL PRIORITY: CRITICAL
            </span>
            <span
              style={{
                fontSize: 12,
                color: "#ff8b85",
                background: "rgba(255, 69, 58, 0.2)",
                padding: "4px 10px",
                borderRadius: 8,
                fontWeight: 700,
              }}
            >
              GPS BROADCAST ACTIVE
            </span>
          </div>
          <div style={{fontSize: 16, color: "#fecdd3", lineHeight: 1.4}}>
            <strong>Payload:</strong> Medical Emergency · Inhaler Needed
            <br />
            <strong>Location:</strong> 19.0760° N, 72.8777° E (±4m Accuracy)
            <br />
            <strong>Delivery:</strong> Relaying across all reachable nodes simultaneously
          </div>
        </div>
      </LeftColumn>

      <div style={{position: "absolute", right: 140, top: 120}}>
        <PhoneFrame
          image={cfg.screenshot}
          label="Live SOS Broadcasting & Multi-Hop Relay"
          scale={0.94}
          highlightColor={demoConfig.colors.emergency}
        />
      </div>
    </Shell>
  );
};

// ==========================================
// SCENE 4: Encrypted Messaging & ACKs (25 - 34s)
// ==========================================
const Scene4Chat: React.FC = () => {
  const cfg = demoConfig.scenes.chat;
  return (
    <Shell accentColor="#0A84FF">
      <LeftColumn
        eyebrow={cfg.eyebrow}
        headline={cfg.headline}
        subline={cfg.subline}
        eyebrowColor="#0A84FF"
      >
        <div
          style={{
            display: "flex",
            flexDirection: "column",
            gap: 12,
            marginTop: 12,
            maxWidth: 680,
          }}
        >
          <div
            style={{
              padding: "16px 20px",
              borderRadius: 16,
              background: "rgba(255, 255, 255, 0.05)",
              border: "1px solid rgba(255, 255, 255, 0.12)",
            }}
          >
            <div style={{fontSize: 12, color: "#94a3b8", fontWeight: 600}}>
              CRYPTO SPECIFICATION
            </div>
            <div style={{fontSize: 16, color: "#fff", fontWeight: 700, marginTop: 4}}>
              AES-256-GCM + Android Keystore ECDSA P-256
            </div>
            <div style={{fontSize: 14, color: "#64748b", marginTop: 4}}>
              Zero plaintext leaks: Relays forward purely blinded ciphertexts.
            </div>
          </div>

          <div
            style={{
              padding: "14px 20px",
              borderRadius: 16,
              background: "rgba(10, 132, 255, 0.12)",
              border: "1px solid rgba(10, 132, 255, 0.3)",
              display: "flex",
              alignItems: "center",
              gap: 12,
            }}
          >
            <div
              style={{
                width: 10,
                height: 10,
                borderRadius: 5,
                background: "#0A84FF",
                boxShadow: "0 0 10px #0A84FF",
              }}
            />
            <div style={{fontSize: 15, fontWeight: 700, color: "#93c5fd"}}>
              Hop-by-Hop Application Level ACKs: Sent ➔ Relayed ➔ Delivered
            </div>
          </div>
        </div>
      </LeftColumn>

      <div style={{position: "absolute", right: 140, top: 120}}>
        <PhoneFrame
          image={cfg.screenshot}
          label="Encrypted iMessage Thread · 2 Hops Relay"
          scale={0.94}
          highlightColor="#0A84FF"
        />
      </div>
    </Shell>
  );
};

// ==========================================
// SCENE 5: Apple HIG & Light/Dark Mode (34 - 42s)
// ==========================================
const Scene5Design: React.FC = () => {
  const cfg = demoConfig.scenes.design;
  return (
    <Shell accentColor="#F59E0B">
      <LeftColumn
        eyebrow={cfg.eyebrow}
        headline={cfg.headline}
        subline={cfg.subline}
        eyebrowColor="#F59E0B"
      >
        <div style={{display: "flex", gap: 14, marginTop: 12}}>
          <div
            style={{
              padding: "16px 20px",
              borderRadius: 16,
              background: "rgba(255, 255, 255, 0.05)",
              border: "1px solid rgba(255, 255, 255, 0.1)",
              flex: 1,
            }}
          >
            <div style={{fontSize: 13, color: "#f59e0b", fontWeight: 700}}>🌙 Dark Mode</div>
            <div style={{fontSize: 14, color: "#94a3b8", marginTop: 4}}>
              OLED black saves crucial device battery during crises.
            </div>
          </div>
          <div
            style={{
              padding: "16px 20px",
              borderRadius: 16,
              background: "rgba(255, 255, 255, 0.05)",
              border: "1px solid rgba(255, 255, 255, 0.1)",
              flex: 1,
            }}
          >
            <div style={{fontSize: 13, color: "#38bdf8", fontWeight: 700}}>☀️ Light Mode</div>
            <div style={{fontSize: 14, color: "#94a3b8", marginTop: 4}}>
              High-contrast glare legibility in bright daylight rescue fields.
            </div>
          </div>
        </div>
      </LeftColumn>

      {/* Dual Phone Showcase: Dark and Light Side by Side */}
      <div style={{position: "absolute", right: 300, top: 120}}>
        <PhoneFrame
          image={cfg.screenshotDark}
          label="Dark Appearance"
          scale={0.88}
          highlightColor="#334155"
        />
      </div>
      <div style={{position: "absolute", right: 60, top: 150}}>
        <PhoneFrame
          image={cfg.screenshotLight}
          label="Light Appearance"
          scale={0.88}
          highlightColor="#0A84FF"
        />
      </div>
    </Shell>
  );
};

// ==========================================
// SCENE 6: Outro & Call to Action (42 - 50s)
// ==========================================
const Scene6Outro: React.FC = () => {
  const cfg = demoConfig.scenes.cta;
  const frame = useCurrentFrame();
  const scale = progress(frame, 0, 30);

  return (
    <Shell accentColor="#0A84FF" glowPosition="center">
      <div
        style={{
          position: "absolute",
          inset: 0,
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
          justifyContent: "center",
          textAlign: "center",
          padding: "0 100px",
          transform: `scale(${0.95 + scale * 0.05})`,
        }}
      >
        <div
          style={{
            display: "inline-flex",
            alignItems: "center",
            gap: 10,
            padding: "10px 24px",
            borderRadius: 999,
            background: "rgba(10, 132, 255, 0.15)",
            border: "1px solid rgba(10, 132, 255, 0.35)",
            color: "#60a5fa",
            fontSize: 15,
            fontWeight: 800,
            letterSpacing: "0.08em",
            textTransform: "uppercase",
            marginBottom: 28,
          }}
        >
          {cfg.eyebrow}
        </div>

        <div
          style={{
            fontSize: 76,
            fontWeight: 900,
            letterSpacing: "-0.04em",
            lineHeight: 1.05,
            maxWidth: 1100,
            color: "#ffffff",
          }}
        >
          {cfg.headline}
        </div>

        <div
          style={{
            fontSize: 26,
            color: "#94a3b8",
            marginTop: 24,
            maxWidth: 800,
            lineHeight: 1.4,
          }}
        >
          {cfg.subline}
        </div>

        {/* Feature Badges Grid */}
        <div style={{display: "flex", gap: 16, marginTop: 40, flexWrap: "wrap", justifyContent: "center"}}>
          {[
            "100% Offline P2P Mesh",
            "Multi-Hop Packet Routing",
            "AES-256-GCM Keystore Cryptography",
            "3-Second Hold SOS Broadcast",
            "Apple HIG Design System",
          ].map((tag) => (
            <div
              key={tag}
              style={{
                padding: "12px 24px",
                borderRadius: 999,
                background: "rgba(255, 255, 255, 0.06)",
                border: "1px solid rgba(255, 255, 255, 0.15)",
                color: "#e2e8f0",
                fontSize: 16,
                fontWeight: 700,
              }}
            >
              ✓ {tag}
            </div>
          ))}
        </div>

        {/* Action Button */}
        <div
          style={{
            marginTop: 48,
            padding: "20px 48px",
            borderRadius: 999,
            background: "linear-gradient(135deg, #0A84FF, #0056B3)",
            boxShadow: "0 10px 40px rgba(10, 132, 255, 0.4)",
            color: "#ffffff",
            fontSize: 22,
            fontWeight: 800,
            letterSpacing: "0.02em",
          }}
        >
          Explore Lynq on GitHub ➔
        </div>
      </div>
    </Shell>
  );
};

// ==========================================
// MAIN COMPOSITION
// ==========================================
export const ProductDemo: React.FC = () => {
  return (
    <>
      <Audio src={staticFile("ambient.mp3")} volume={0.25} />

      {/* Scene 1: Hook (0s - 8s) */}
      <Sequence from={0} durationInFrames={second(8)}>
        <Scene1Hook />
      </Sequence>

      {/* Scene 2: Mesh Topology (8s - 16s) */}
      <Sequence from={second(8)} durationInFrames={second(8)}>
        <Scene2Mesh />
      </Sequence>

      {/* Scene 3: SOS Broadcast (16s - 25s) */}
      <Sequence from={second(16)} durationInFrames={second(9)}>
        <Scene3Sos />
      </Sequence>

      {/* Scene 4: Encrypted Chat (25s - 34s) */}
      <Sequence from={second(25)} durationInFrames={second(9)}>
        <Scene4Chat />
      </Sequence>

      {/* Scene 5: Apple HIG Design (34s - 42s) */}
      <Sequence from={second(34)} durationInFrames={second(8)}>
        <Scene5Design />
      </Sequence>

      {/* Scene 6: Outro (42s - 50s) */}
      <Sequence from={second(42)} durationInFrames={second(8)}>
        <Scene6Outro />
      </Sequence>
    </>
  );
};
