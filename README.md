# Snooker-Manager

A comprehensive backend for managing Snooker Tournaments, Players, Matches, and Live Scores.

## Features

- **User Authentication**: JWT-based secure login and registration with Role-Based Access Control (Admin, Referee, User).
- **Tournament Management**: Create, update, and manage tournaments including advanced bracket structures.
- **Match Management**: Schedule matches, assign players/referees, and track timeline events (pots, fouls, undo logic).
- **Live Scoring**: Real-time score updates via WebSocket (STOMP).
- **Player Management**: Search functionality and statistics tracking.
- **Broadcast Ready**: Custom OBS Overlay settings per tournament.
- **Public API**: Authenticated-free access for frontend home page (Live Scores, Schedules).

## Tech Stack

- **Java 21**, **Spring Boot 3.5.9**
- **Spring Security** (JWT)
- **Spring Data JPA** (MySQL)
- **WebSocket** (STOMP)

## Getting Started

### Prerequisites

- Java 21+
- MySQL Server

### Configuration

Update `src/main/resources/application.properties` with your database credentials:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/snooker?useSSL=false
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
```

### Build & Run

```bash
mvn clean install
mvn spring-boot:run
```

The app will start at `http://localhost:8080`.

## API Documentation

See [API_DOCUMENTATION.md](API_DOCUMENTATION.md) for detailed endpoint usage.
