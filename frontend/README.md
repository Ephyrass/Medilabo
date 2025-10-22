# Frontend - Vue 3 + Vite + Tailwind (Medilabo)

This is a minimal Vue 3 frontend scaffold for the Medilabo project.

## Features
- Vue 3 with Vite
- Tailwind CSS
- Axios for API calls
- Dev server proxies `/api` to `http://localhost:8080` (Gateway)

## Quick start

Install dependencies:

```bash
cd frontend
npm install
```

Run dev server:

```bash
npm run dev
```

Build for production:

```bash
npm run build
```

Preview build locally:

```bash
npm run preview
```

## Docker

Build and run:

```bash
cd frontend
npm run build
docker build -t medilabo/frontend .
docker run -p 80:80 medilabo/frontend
```

The dev server proxies API calls to the Gateway on `http://localhost:8080`.

