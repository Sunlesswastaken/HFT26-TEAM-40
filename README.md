# Lynq

Hackathon project — Android (Kotlin) client + Python (FastAPI) backend.

## Layout

```
Lynq/
├── app/          # Android / Kotlin app module
├── backend/      # FastAPI service
└── .gitignore
```

## Getting started

### Android
1. Open the project root in Android Studio.
2. Copy `local.properties.example` to `local.properties` and set your SDK path.
3. Build with `./gradlew assembleDebug` (or run from the IDE).

### Backend
```bash
cd backend
python -m venv .venv
source .venv/bin/activate
pip install -r requirements.txt
uvicorn main:app --reload
```

#### Autodeploy to Render (Blueprint)
You can deploy the backend directly to Render with zero CLI setup:
1. Open [dashboard.render.com/blueprints](https://dashboard.render.com/blueprints).
2. Click **New Blueprint Instance** and connect this repository.
3. Render automatically loads [`render.yaml`](render.yaml), asks for your `FEATHERLESS_API_KEY`, and deploys with free SSL.
4. Set the resulting Render URL in the Lynq app Settings under **API Base URL** (`https://<service>.onrender.com/v1`).

## Secrets

Nothing sensitive gets committed here.

- `backend/.env` holds the Featherless API key. Start from `backend/.env.example`.
- Android signing keys (`*.jks`, `*.keystore`, `keystore.properties`) stay local.
- If a secret is ever committed by accident, rotate it — deleting the file is not enough.

## Branches

- `main` — working, demo-ready code.
- `feat/<name>` — per-feature work, merged into `main` via PR.

Keep commits small and descriptive so we can demo from `git log` if needed.
