# Frontend Integration Guide

This document assists the Frontend Team in integrating with the Snooker Backend. It details data flows, required endpoints per page, and real-time communication protocols.

## 1. System Architecture

The application follows a standard Client-Server architecture with Real-time capabilities.

```mermaid
graph TD
    Client["Frontend (React/Next.js)"]
    API["REST API (Spring Boot)"]
    WS["WebSocket Broker (STOMP)"]
    DB[("MySQL Database")]

    Client -- "HTTPS (JSON)" --> API
    Client -- "WSS (STOMP)" --> WS
    API -- "JPA" --> DB
    API -- "Events" --> WS
    WS -- "Push Updates" --> Client
```

---

## 2. Authentication & Security

### Auth Flow

The backend uses **JWT (JSON Web Token)** for security.

1.  **Login**: POST `/api/auth/signin` with `username` & `password`.
2.  **Storage**: Backend returns a `token`. Store this (e.g., `localStorage` or `sessionStorage`).
3.  **Requests**: Attach header `Authorization: Bearer <token>` to **ALL** protected requests.
    - _Public endpoints (Home page) do not require this header._
4.  **Roles**: standard users are `ROLE_USER`. Referees are `ROLE_REFEREE`. Admins are `ROLE_ADMIN`. Use `roles` from the login response to hide/show UI elements.

```mermaid
sequenceDiagram
    participant User
    participant Frontend
    participant Backend

    User->>Frontend: Enters Credentials
    Frontend->>Backend: POST /api/auth/signin
    Backend-->>Frontend: 200 OK { token, roles: ["ROLE_ADMIN"] }
    Frontend->>Frontend: Save Token to Storage

    User->>Frontend: Access Admin Dashboard
    Frontend->>Backend: POST /api/tournaments/create (Header: Bearer <token>)
    Backend-->>Frontend: 200 OK (Match Created)
```

---

## 3. Page Integration Guide

### A. Public Pages (No Login Required)

#### 1. Home / Landing Page

- **Goal**: Show current "Live" matches and "Upcoming" schedule.
- **Data Required**: List of Matches filtered by status.
- **Endpoints**:
  - `POST /api/matches/filter` payload: `{ "status": "LIVE" }`
  - `POST /api/matches/filter` payload: `{ "status": "SCHEDULED" }`

#### 2. Tournament Viewer

- **Goal**: Show brackets or list of tournaments.
- **Endpoints**:
  - `POST /api/tournaments/all` (Get list)
  - `POST /api/matches/tournament` payload: `{ "id": 1 }` (Get matches for a tournament)

---

### B. Admin Dashboard (Role: ADMIN)

#### 1. Tournament Management

- **Actions**: Create, Edit, Delete Tournaments.
- **Endpoints**: `/api/tournaments/create`, `/api/tournaments/update`.

#### 2. Match Scheduling

- **Actions**: Create a match, assign Players and Referee.
- **Endpoints**: `/api/matches/create`
- **Prerequisite**: Call `/api/players/all` and `/api/tournaments/all` to populate dropdowns.

---

### C. Referee Console (Role: REFEREE / ADMIN)

- **Goal**: Control the match state and score.
- **Data Flow**: Actions here trigger immediate WebSocket updates for viewers.

| Action          | Endpoint              | Payload                                                              | Note                  |
| :-------------- | :-------------------- | :------------------------------------------------------------------- | :-------------------- |
| **Start Match** | `/api/matches/status` | `{ "matchId": 1, "status": "LIVE" }`                                 | Changes state to Live |
| **Pot Red**     | `/api/matches/event`  | `{ "frameId": 10, "type": "POT", "points": 1, "details": "Red" }`    | Updates score         |
| **Foul**        | `/api/matches/event`  | `{ "frameId": 10, "type": "FOUL", "points": 4, "details": " foul" }` | Points to opponent    |
| **Undo**        | `/api/matches/undo`   | `{ "id": 10 }` (Frame ID)                                            | Reverts last action   |

---

### D. Live Scoreboard (For Public Viewers)

- **Goal**: Display real-time scores without refreshing.
- **Technology**: STOMP over WebSocket.

#### Connection Logic

1.  **Connect**: `http://localhost:8080/ws`
2.  **Subscribe**: `/topic/match/{matchId}`
3.  **Receive**: JSON object `ScoreUpdate`.

#### Payload Example (Incoming WebSocket Message)

```json
{
  "matchId": 1,
  "frameId": 10,
  "p1Score": 14,
  "p2Score": 0,
  "currentBreak": 14,
  "message": "Player 1 potted Red (1 point)"
}
```

```mermaid
sequenceDiagram
    participant Referee
    participant Backend
    participant Viewer(WS)

    Note over Viewer(WS): Subscribed to /topic/match/1

    Referee->>Backend: POST /api/matches/event (POT Red)
    Backend->>Backend: Update Database Score
    Backend->>Viewer(WS): PUSH message { p1Score: 1, message: "Red" }

    Viewer(WS)->>Viewer(WS): Update UI Scoreboard
```
