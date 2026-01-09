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

### Forgot Password

- **URL**: `/api/auth/forgot-password`
- **Method**: `POST`
- **Body**: `ForgotPasswordRequest`
  ```json
  {
    "email": "user@example.com"
  }
  ```
- **Response**: `MessageResponse`
- **Note**: Sends password reset email if user exists.

### Reset Password

- **URL**: `/api/auth/reset-password`
- **Method**: `POST`
- **Body**: `ResetPasswordRequest`
  ```json
  {
    "token": "uuid-token-from-email",
    "newPassword": "newPassword123"
  }
  ```
- **Response**: `MessageResponse`

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

### Update Tournament (Admin)

- **URL**: `/api/tournaments/update`
- **Method**: `POST`
- **Body**: `Tournament` object (must include ID)

### Delete Tournament (Admin)

- **URL**: `/api/tournaments/delete`
- **Method**: `POST`
- **Body**: `IdRequest`

### Register User to Tournament

- **URL**: `/api/tournaments/{id}/register`
- **Method**: `POST`
- **Path Variable**: `id` (Tournament ID)
- **Body**: `IdRequest`
  ```json
  {
    "id": 5 // User ID
  }
  ```
- **Response**: Success message
- **Note**: Sends registration confirmation email.

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

### Update Match (Admin/Referee)

- **URL**: `/api/matches/update`
- **Method**: `POST`
- **Body**: `UpdateMatchRequest`
  ```json
  {
    "id": 1,
    "player1Id": 2,
    "player2Id": 3,
    "refereeId": 4,
    "bestOfFrames": 9,
    "startTime": "2023-10-10T14:00:00"
  }
  ```

### Filter Matches by Status

- **URL**: `/api/matches/filter`
- **Method**: `POST`
- **Body**: `MatchStatusRequest`
  ```json
  {
    "status": "LIVE" // LIVE, SCHEDULED, COMPLETED
  }
  ```
- **Response**: `List<Match>`

### Update Match Status (Admin/Referee)

- **URL**: `/api/matches/status`
- **Method**: `POST`
- **Body**: `MatchStatusRequest`
  ```json
  {
    "matchId": 1,
    "status": "LIVE"
  }
  ```
- **Response**: Updated `Match`

### Record Match Event (Admin/Referee)

- **URL**: `/api/matches/event`
- **Method**: `POST`
- **Body**: `EventRequest`
  ```json
  {
    "frameId": 10,
    "playerId": 2,
    "type": "POT", // POT, FOUL, MISS, SAFETY, END_FRAME
    "points": 1,
    "details": "Red"
  }
  ```
- **Response**: `MatchEvent`
- **Note**: Triggers WebSocket broadcast to `/topic/match/{matchId}`

### Undo Last Event (Admin/Referee)

- **URL**: `/api/matches/undo`
- **Method**: `POST`
- **Body**: `IdRequest` (Frame ID)
  ```json
  {
    "id": 10
  }
  ```
- **Response**: Success message

### Delete Match (Admin)

- **URL**: `/api/matches/delete`
- **Method**: `POST`
- **Body**: `IdRequest`

---

## 5. Overlay Controller (`/api/overlays`)

### Get Overlay Config

- **URL**: `/api/overlays/get`
- **Body**: `IdRequest` (`id` is Tournament ID)
- **Response**: `OverlayConfig`

### Update Overlay Config (Admin)

- **URL**: `/api/overlays/update`
- **Body**: `OverlayConfigRequest`
  ```json
  {
    "tournamentId": 1,
    "fontFamily": "Roboto",
    "primaryColor": "#FF0000",
    "secondaryColor": "#FFFFFF",
    "layoutPreset": "MODERN"
  }
  ```

---

## 6. Player Search

### Search Players

- **URL**: `/api/players/search`
- **Body**: `SearchRequest`
  ```json
  {
    "query": "ronnie"
  }
  ```
- **Response**: `List<Player>`

---

## 7. WebSocket API (Live Scores)

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

---

## 8. Newsletter Controller (`/api/newsletter`)

### Subscribe to Newsletter

- **URL**: `/api/newsletter/subscribe`
- **Method**: `POST`
- **Body**: `NewsletterRequest`
  ```json
  {
    "email": "user@example.com"
  }
  ```
- **Response**: `MessageResponse`

### Unsubscribe from Newsletter

- **URL**: `/api/newsletter/unsubscribe`
- **Method**: `POST`
- **Body**: `NewsletterRequest`
  ```json
  {
    "email": "user@example.com"
  }
  ```
- **Response**: `MessageResponse`

---

## 9. User Controller (`/api/users`)

### Upload Profile Photo

- **URL**: `/api/users/{id}/photo`
- **Method**: `POST`
- **Path Variable**: `id` (User ID)
- **Body**: Multipart form data with `file` field
- **Constraints**: Max 2MB, JPEG/PNG
- **Response**: `MessageResponse`

### Get Profile Photo

- **URL**: `/api/users/{id}/photo`
- **Method**: `GET`
- **Path Variable**: `id` (User ID)
- **Response**: Image binary (JPEG)

### Get All Users

- **URL**: `/api/users/all`
- **Method**: `POST`
- **Response**: `List<User>`

### Get User by ID

- **URL**: `/api/users/get`
- **Method**: `POST`
- **Body**: `IdRequest`
- **Response**: `User`

### Update User

- **URL**: `/api/users/update`
- **Method**: `POST`
- **Body**: `UserUpdateRequest`
  ```json
  {
    "id": 123,
    "username": "newname",
    "email": "new@email.com",
    "password": "newPassword123", // Optional
    "roles": ["admin", "referee"] // Optional
  }
  ```
- **Response**: Updated `User`
- **Note**: Password is automatically encrypted if provided.

### Delete User

- **URL**: `/api/users/delete`
- **Method**: `POST`
- **Body**: `IdRequest`
- **Response**: Success message

---

## 10. YouTube Controller (`/api/youtube`)

### Get Live Streams

Fetches all currently live streams from the UPBSA YouTube channel.

- **URL**: `/api/youtube/live-streams`
- **Method**: `GET`
- **Authentication**: Not required (public endpoint)
- **Response**: `List<YouTubeLiveStreamResponse>`
  ```json
  [
    {
      "id": "dQw4w9WgXcQ",
      "youtubeStreamId": "dQw4w9WgXcQ",
      "title": "Aditya Mehta vs Pankaj Advani - UPBSA Championship 2025",
      "description": "Live coverage of the championship match",
      "thumbnail": "https://i.ytimg.com/vi/dQw4w9WgXcQ/hqdefault.jpg",
      "viewerCount": 1247,
      "startTime": "2025-12-30T14:30:00Z",
      "isLive": true,
      "channelTitle": "UPBSA",
      "player1": {
        "name": "Aditya Mehta"
      },
      "player2": {
        "name": "Pankaj Advani"
      },
      "parsedTitle": true
    }
  ]
  ```

**Notes:**

- Results are cached for 60 seconds to minimize API quota usage
- Player names are automatically parsed from video titles
- If parsing fails, `parsedTitle` will be `false` and `originalTitle` will contain the raw title
- Supported title formats: "Player1 vs Player2", "Player1 VS Player2", "Player1 v/s Player2", "Player1 - Player2"
- **Fallback behavior**: If no live streams are active, returns the most recent completed streams from the channel
- `isLive` field indicates whether the stream is currently live (`true`) or a recent completed broadcast (`false`)

---

## 11. Overlay Configuration Controller (`/api/overlays`)

### Get Overlay Configuration

Retrieves the global overlay configuration for OBS Browser Source.

- **URL**: `/api/overlays/get`
- **Method**: `GET`
- **Authentication**: Not required (public endpoint)
- **Response**: `OverlayConfigResponse`
  ```json
  {
    "fontFamily": "Rajdhani",
    "primaryColor": "#00ff41",
    "secondaryColor": "#ffffff",
    "backgroundColor": "#0a0f0a",
    "accentColor": "#00cc33",
    "layoutPreset": "MODERN",
    "showPlayerPhotos": true,
    "showFlags": true,
    "animationSpeed": "normal"
  }
  ```

**Notes:**
- Public endpoint accessible without authentication
- Used by OBS Browser Source to fetch overlay styling
- Returns default configuration if none exists

### Update Overlay Configuration

Updates the global overlay configuration (Admin only).

- **URL**: `/api/overlays/update`
- **Method**: `POST`
- **Authentication**: Required (Admin role)
- **Body**: `OverlayConfigRequest`
  ```json
  {
    "fontFamily": "Roboto",
    "primaryColor": "#ff0000",
    "secondaryColor": "#ffffff",
    "backgroundColor": "#1a1a1a",
    "accentColor": "#cc0000",
    "layoutPreset": "CLASSIC",
    "showPlayerPhotos": true,
    "showFlags": true,
    "animationSpeed": "normal"
  }
  ```
- **Response**: Success message with updated configuration
  ```json
  {
    "message": "Overlay configuration updated successfully",
    "config": {
      "fontFamily": "Roboto",
      "primaryColor": "#ff0000",
      "secondaryColor": "#ffffff",
      "backgroundColor": "#1a1a1a",
      "accentColor": "#cc0000",
      "layoutPreset": "CLASSIC",
      "showPlayerPhotos": true,
      "showFlags": true,
      "animationSpeed": "normal"
    }
  }
  ```

**Field Validation:**

| Field | Type | Validation | Default |
|-------|------|-----------|---------|
| `fontFamily` | String | Must be one of: Rajdhani, Inter, Roboto, Open Sans, Montserrat | "Rajdhani" |
| `primaryColor` | String | Valid hex code (#RRGGBB) | "#00ff41" |
| `secondaryColor` | String | Valid hex code (#RRGGBB) | "#ffffff" |
| `backgroundColor` | String | Valid hex code (#RRGGBB) | "#0a0f0a" |
| `accentColor` | String | Valid hex code (#RRGGBB) | "#00cc33" |
| `layoutPreset` | String | Must be one of: MODERN, CLASSIC, COMPACT | "MODERN" |
| `showPlayerPhotos` | Boolean | true or false | true |
| `showFlags` | Boolean | true or false | true |
| `animationSpeed` | String | Must be one of: slow, normal, fast | "normal" |

**Notes:**
- All fields are optional (supports partial updates)
- Only one global configuration exists (id = 1)
- Admin role required for updates
- Invalid values return 400 Bad Request with error message

**OBS Browser Source URL:**
```
http://localhost:5173/overlay\?match\=123
```
