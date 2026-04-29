# Frontend Developer & Antigravity AI Instructions

## Overview
This document contains the frontend work scope for the Compost monitoring system. It is written for the frontend developer and for Antigravity AI to execute the work.

The frontend will be built with React + Vite and must work independently of the backend while the backend developer implements the matching API.

## Goals
- Implement alerts and notifications UI
- Implement sensor history and charts
- Implement authentication infrastructure, but keep it disabled for now
- Improve mobile reliability and Android-directed UX
- Keep integration simple by working against the backend API contract below

## Frontend Work Package

### 1. Alerts and Notifications

#### What to build
- Create an `alerts` panel on the dashboard showing active alerts.
- Fetch alerts from the backend using `GET /api/alerts`.
- Support alert resolving via `POST /api/alerts/{id}/resolve`.
- Display alert type, message, timestamp, and severity.
- Add notification toasts when critical alerts appear.
- Optionally add a mobile vibration or sound cue on high-severity alerts.

#### UI requirements
- Add a small alerts list to the dashboard.
- Show a badge or count for active alerts.
- Provide a visible `Resolve` button for each alert.
- Use the existing `NotificationSystem` provider for toast messages.

#### Acceptance criteria
- Alerts appear without page refresh.
- Resolving an alert updates the UI.
- Critical alerts create a toast notification.

### 2. Sensor History and Analytics

#### What to build
- Add a `History` view or panel for sensor trend data.
- Use charts to display sensor readings over time.
- Support at least temperature, gas, and weight.
- Fetch data from backend endpoints:
  - `GET /api/readings/latest?count=50`
  - `GET /api/readings?start=ISO_DATE&end=ISO_DATE&limit=100`
- Add a date range picker for custom history queries.
- Add an export-to-CSV button for sensor history.

#### UI requirements
- Use a chart library such as Chart.js, Recharts, or similar.
- Show lines or area charts for each metric.
- Provide a loading state while data is fetched.
- Provide a helpful empty state when no readings exist.

#### Acceptance criteria
- User can view recent readings as charts.
- User can filter by date range.
- Charts update when the selected range changes.

### 3. Authentication Infrastructure (Disabled for now)

#### What to implement
- Build a login screen and authentication service.
- Implement frontend JWT support:
  - store token in `localStorage` or `sessionStorage`
  - attach `Authorization: Bearer <token>` header to outgoing API requests
- Create a config flag `authEnabled = false`.
- Do not enforce login gating yet; keep public access to the dashboard, history, and request pages.
- Keep the auth code present so it can be enabled later with minimal changes.

#### UI requirements
- Add a `/login` route and login form.
- Add a simple user state context or hook.
- Show login form error messages.
- Keep current nav and flow unchanged when auth is disabled.

#### Integration behavior
- When `authEnabled` is false:
  - do not redirect to `/login`
  - allow all pages to load normally
  - still initialize auth state and token storage if present

#### Acceptance criteria
- Login form exists and validates credentials locally.
- API request wrapper can attach a JWT header.
- Pages remain accessible while auth is disabled.

### 4. Android-directed UX and Reliability

#### What to improve
- Make UI responsive for Android / mobile screen sizes.
- Add a mobile-friendly dashboard layout.
- Add a visible online/offline indicator.
- Add pull-to-refresh support on the dashboard if possible.
- Add a PWA manifest skeleton and service worker registration for caching.
- Improve error handling for backend failures.

#### UI requirements
- Use responsive CSS or layout breakpoints.
- Make buttons large enough for touch.
- Surface connection errors in a toast or banner.
- Provide a simple app install / offline-ready experience.

#### Acceptance criteria
- Dashboard works on narrow screens.
- The app shows a status if backend is unreachable.
- The UI remains usable during transient network issues.

## Backend Integration Contract for Frontend
The frontend developer can build against these backend endpoints. Mock them if needed until the backend is ready.

### Authentication endpoints
- `POST /api/auth/login`
  - Request: `{ email, password }`
  - Response: `{ token: string, user: { email, role } }`
- `POST /api/auth/register`
  - Request: `{ email, password, role }`
  - Response: `{ message: string }`

> Auth should be implemented but not enforced yet.

### Alerts endpoints
- `GET /api/alerts`
  - Response: `[{ id, type, message, timestamp, resolved }]`
- `POST /api/alerts/{id}/resolve`
  - Response: `{ message: string }`

### Sensor readings endpoints
- `GET /api/readings/latest?count=50`
  - Response: `[{ id, temperature, gasLevel, weight, timestamp }]`
- `GET /api/readings?start=2026-04-01T00:00:00&end=2026-04-30T23:59:59&limit=100`
  - Response: same shape as above

### System health endpoint
- `GET /api/health`
  - Response: `{ status: 'UP' | 'DOWN', db: string, mqtt: string }`

### Existing backend endpoints to keep
- `GET /api/compost/status`
- `POST /api/compost/add-waste?weight=...`
- `GET /api/batches/latest`
- `GET /api/batches/history`
- `POST /api/batches/request`

## Instructions for Antigravity AI
1. Use this document as the source of truth for frontend scope.
2. Implement React components, hooks, and services for:
   - alerts panel
   - sensor history with charts
   - auth infrastructure with `authEnabled = false`
   - responsive mobile layout
3. Keep backend dependencies abstracted behind the API contract.
4. Mock API responses when endpoints are not yet available.
5. Do not enable route protection or require login until the backend authorizes it.
6. Preserve the existing dashboard, history, and request pages while enhancing them.

## Notes for the Frontend Developer
- Backend will support new alert, reading, and health endpoints.
- Build alert and history views so they can integrate with the backend in one pass.
- Authentication should be implemented as a disabled capability, not an active barrier.
- Antigravity AI should produce components and services that are easy to wire to real APIs later.

---

This file is intended to let the frontend developer and Antigravity AI work in parallel with the backend developer and integrate cleanly at the end.