# Backend Implementation Plan

## Objective
Implement backend services and APIs that support the frontend work package described in `FRONTEND_AI_TASKS.md`. The backend should be designed so the frontend developer can implement UI independently and integrate later.

## Scope
The backend will provide:
- Alerts and notification support
- Sensor history endpoints
- Health status endpoint
- Authentication infrastructure (implemented but not enforced)
- Improved reliability and status tracking for MQTT and database

## Backend Responsibilities
### 1. Alerts and Notifications
- Add `Alert` model and MongoDB repository.
- Add an alert persistence layer and controller.
- Generate alert records when sensor readings exceed thresholds.
- Provide endpoints for active alerts and resolving alerts.

API:
- `GET /api/alerts`
- `POST /api/alerts/{id}/resolve`

### 2. Sensor History
- Add controller endpoints to return sensor reading history.
- Support latest readings and date-range queries.
- Provide a simple query API for the frontend charts.

API:
- `GET /api/readings/latest?count=50`
- `GET /api/readings?start=ISO_DATE&end=ISO_DATE&limit=100`

### 3. Health Endpoint
- Add a health endpoint reporting:
  - application status
  - MongoDB availability
  - MQTT connection status

API:
- `GET /api/health`

### 4. Authentication Infrastructure
- Add `User` model and repository.
- Add auth endpoints for login and registration.
- Implement token generation and basic password hashing.
- Keep auth disabled on general endpoints for now.

API:
- `POST /api/auth/login`
- `POST /api/auth/register`

### 5. Reliability and Backend Improvements
- Add stable MQTT connection state reporting.
- Improve controller responses and validation.
- Keep CORS open for frontend development.

## Implementation Steps
1. Create backend plan and update the repo with new models and controllers.
2. Add `Alert` entity and alert repository.
3. Add `User` entity and auth repository.
4. Add `AlertController`, `ReadingsController`, `HealthController`, and `AuthController`.
5. Enhance `CompostService` to generate alert entries when thresholds trigger.
6. Update `MqttService` to expose connection status.
7. Run backend compile and verify.

## Notes for Frontend Integration
Use these backend endpoint contracts:
- `GET /api/alerts`
- `POST /api/alerts/{id}/resolve`
- `GET /api/readings/latest?count=50`
- `GET /api/readings?start=2026-04-01T00:00:00&end=2026-04-30T23:59:59&limit=100`
- `GET /api/health`
- `POST /api/auth/login`
- `POST /api/auth/register`

Auth exists but should not be required yet.

## Current Status
- Existing batch and sensor endpoints remain unchanged.
- Backend is being extended in a compatible way, without breaking current dashboard or history APIs.
