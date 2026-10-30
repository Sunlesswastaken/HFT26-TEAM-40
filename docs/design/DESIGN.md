# Lynq UI design — Minimal & Clean

Status: mockups validated (light, dark, and key states), Compose port in progress.

## North star

An emergency tool with a live network view: calm, readable under stress,
one clear focal point per screen. Not a chat app.

## Tokens (source of truth: `app/.../ui/theme/`)

| Token | Light | Dark | Use |
|---|---|---|---|
| bg | #F7F7F4 | #0A0B0A | page background |
| surface | #FFFFFF | #121413 | cards, rows, nav |
| sunken | #EFEFE9 | #171A18 | avatars, chips, neutral pills |
| ink | #141614 | #ECEEE9 | text, filled avatars, "YOU" node |
| muted | #6B7069 | #98A09B | secondary text, routes, timestamps |
| hairline | #E2E2DA | #262A27 | 1px card borders, graph lines |
| signal | #14634F | #6FD3B2 | delivered status, online dot |
| sos | #B42318 | #FF6A5D | SOS only — never decorative |
| warn | #8A5A00 | #E0A94D | HIGH priority marks |

Type: system sans, one family. Display 40/light (-1.2 tracking) for hero
numbers; headline 26/semibold for screen titles; body 14–16; labelSmall
11/medium/+1.4 tracking uppercase for eyebrows. Monospace for device IDs,
routes, hops, timestamps.

Shapes: cards 18px radius, flat, 1px hairline border, no shadow. Pills and
avatars circular.

## Screen inventory

1. **Dashboard** — brand row + "4 reachable" live pill; hero stats
   (reachable / relaying / queued) as big light numbers; mesh preview card;
   activity rows with route traces; quick SOS button.
2. **Messages** — thread list (avatar, name, priority mark, time, preview,
   delivery pill, unread badge) + open thread with bubbles, per-message
   route trace and delivery state.
3. **Emergency** — category chips, optional note, location toggle,
   large hold-to-send SOS button; after sending, live hop-by-hop tracking;
   received alerts history.
4. **Mesh Explorer** — radial topology with highlighted live route (red),
   "SOS passing" flow tags, legend, relay activity, device list with
   hop counts and last-seen.
5. **Settings** — identity card (name, key fingerprint mono, device),
   security, battery & network, permissions, honest scope note.

App shell: top brand row + 5-item bottom nav (Mesh / Messages / SOS / Explore
/ Settings), SOS item in signal red — never decoratively.

## Graph layout rule

`YOU` centre; peers on the ring. A highlighted multi-hop route must connect
consecutive ring neighbours (chords), never pass through the centre node.
Relaying peers render filled (ink); reachable peers are sunken circles.

## Deliverables

- Mockups: `docs/design/mockups/*.html` (tokens.css shared; `?theme=dark`,
  `?state=open|tracking` URL states)
- Screenshots: `shots/*.png` (light set, `sheet.png` overview, `state-*.png`)

## Delivery notes

- Eyebrow labels (11px uppercase) are a deliberate part of the token set,
  matching the app's `labelSmall` — used to group sections, not as decoration.
- Opens in mobile viewport 390×844. All automated tool checks pass
  (contrast, overflow, console errors). One contrast fix applied:
  neutral "sent/pending" pill text darkened from #6B7069 to #54594F (4.39→4.84:1).
- Visual self-review was impossible in this session (no image input, reviewer
  also blind). Geometry of graphs was verified by coordinate calculation
  instead; flag for a human eye pass before final polish.