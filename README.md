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

## Secrets

Nothing sensitive gets committed here.

- `backend/.env` holds the Featherless API key. Start from `backend/.env.example`.
- Android signing keys (`*.jks`, `*.keystore`, `keystore.properties`) stay local.
- If a secret is ever committed by accident, rotate it — deleting the file is not enough.

## Branches

- `main` — working, demo-ready code.
- `feat/<name>` — per-feature work, merged into `main` via PR.

Keep commits small and descriptive so we can demo from `git log` if needed.
