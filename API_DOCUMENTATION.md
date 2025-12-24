# Snooker Backend API Documentation

## Overview

This API provides endpoints for managing the Snooker Tournament system. All REST API endpoints use `POST` requests.

### Base URL

`http://localhost:8080/api`

### Authentication

Most endpoints (except Auth and read-only generic ones if configured) require a valid JWT Token.

- **Header**: `Authorization: Bearer <token>`

---

## 1. Authentication Controller (`/api/auth`)

### Sign In

Authenticate a user and retrieve a JWT token.

- **URL**: `/api/auth/signin`
- **Method**: `POST`
- **Body**: `LoginRequest`
  ```json
  {
    "username": "admin",
    "password": "password123"
  }
  ```
- **Response**: `JwtResponse`
  ```json
  {
    "token": "eyJhbG...",
    "type": "Bearer",
    "id": 1,
    "username": "admin",
    "email": "admin@test.com",
    "roles": ["ROLE_ADMIN"]
  }
  ```

### Sign Up

Register a new user.

- **URL**: `/api/auth/signup`
- **Method**: `POST`
- **Body**: `SignupRequest`
  ```json
  {
    "username": "new_user",
    "email": "user@test.com",
    "password": "password123",
    "role": ["user"]
    // Roles: "user", "admin", "referee"
  }
  ```
- **Response**: `MessageResponse`
  ```json
  {
    "message": "User registered successfully!"
  }
  ```

---

## 2. Player Controller (`/api/players`)

### Get All Players

- **URL**: `/api/players/all`
- **Method**: `POST`
- **Body**: (Empty or generic)
- **Response**: `List<Player>`

### Get Player by ID

- **URL**: `/api/players/get`
- **Method**: `POST`
- **Body**: `IdRequest`
  ```json
  {
    "id": 1
  }
  ```
- **Response**: `Player`

### Create Player (Admin)

- **URL**: `/api/players/create`
- **Method**: `POST`
- **Body**: `Player`
  ```json
  {
    "name": "Ronnie O'Sullivan",
    "nationality": "British"
  }
  ```
- **Response**: `Player`

### Update Player (Admin)

- **URL**: `/api/players/update`
- **Method**: `POST`
- **Body**: `Player` object (must include ID)

### Delete Player (Admin)

- **URL**: `/api/players/delete`
- **Method**: `POST`
- **Body**: `IdRequest`
  ```json
  {
    "id": 1
  }
  ```

---

## 3. Tournament Controller (`/api/tournaments`)

### Get All Tournaments

- **URL**: `/api/tournaments/all`
- **Method**: `POST`
- **Response**: `List<Tournament>`

### Get Tournament by ID

- **URL**: `/api/tournaments/get`
- **Method**: `POST`
- **Body**: `IdRequest` (`id`)
- **Response**: `Tournament`

### Create Tournament (Admin)

- **URL**: `/api/tournaments/create`
- **Method**: `POST`
- **Body**: `Tournament`
  ```json
  {
    "name": "World Championship 2025",
    "startDate": "2025-04-15T10:00:00",
    "endDate": "2025-05-01T22:00:00",
    "status": "UPCOMING"
  }
  ```

### Update/Delete

- Similar structure to Player Controller using `/update` and `/delete` endpoints.

---

## 4. Match Controller (`/api/matches`)

### Get All Matches

- **URL**: `/api/matches/all`
- **Method**: `POST`
- **Response**: `List<Match>`

### Get Matches by Tournament

- **URL**: `/api/matches/tournament`
- **Method**: `POST`
- **Body**: `IdRequest` (`id` is Tournament ID)
- **Response**: `List<Match>`

### Get Match by ID

- **URL**: `/api/matches/get`
- **Method**: `POST`
- **Body**: `IdRequest` (`id`)

### Create Match (Admin)

- **URL**: `/api/matches/create`
- **Method**: `POST`
- **Body**: `Match` object

### Update Match Status (Admin/Referee)

- **URL**: `/api/matches/status`
- **Method**: `POST`
- **Body**: `MatchStatusRequest`
  ```json
  {
    "matchId": 1,
    "status": "LIVE"
    // Statuses: SCHEDULED, LIVE, COMPLETED, CANCELLED
  }
  ```

---

## 5. WebSocket API (Live Scores)

### Configuration

- **Endpoint**: `/ws` (SockJS/STOMP)
- **Allowed Origins**: `*`

### Subscription

- **Topic**: `/topic/match/{matchId}`
- **Description**: Listen here for real-time score updates for a specific match.

### Publishing (Client -> Server)

- **Destination**: `/app/score/{matchId}`
- **Payload**: `ScoreUpdate`
  ```json
  {
    "matchId": 1,
    "frameNumber": 3,
    "scorePlayer1": 45,
    "scorePlayer2": 10,
    "currentBreak": 12,
    "currentBreakPlayerId": 1
  }
  ```
