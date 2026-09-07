# TaskFlow — Role-Based Task Management System

TaskFlow is a secure, role-based task management REST API built with Spring Boot. Admins create and assign tasks to team members; employees track and update their own work. Built to demonstrate production-style backend architecture — authentication, authorization, containerization, and CI/CD — not just CRUD operations.

## Features

- **JWT Authentication** — secure login with token-based sessions
- **Role-Based Access Control** — Admins manage all tasks; Employees only see their own
- **Task Management** — create, assign, track status (`TODO` → `IN_PROGRESS` → `DONE`)
- **Comments** — add contextual notes on tasks
- **Dashboard Analytics** — task counts by status
- **API Documentation** — auto-generated via Swagger/OpenAPI
- **Containerized** — Docker-ready for consistent deployment
- **CI Pipeline** — automated build and test via GitHub Actions

## Tech Stack

**Backend:** Spring Boot 3, Spring Security, Spring Data JPA
**Database:** MySQL
**Auth:** JWT (jjwt library), BCrypt password hashing
**Docs:** Springdoc OpenAPI (Swagger UI)
**DevOps:** Docker, GitHub Actions

## Architecture
Client → Controller → Service → Repository → MySQL
↓
JWT Filter (Spring Security)


Layered architecture separates concerns: controllers handle HTTP, services contain business logic, repositories handle data access. DTOs prevent entity classes (with password fields) from being directly exposed via API responses.

## Getting Started

### Prerequisites
- Java 21
- Maven
- MySQL running locally
- Docker (optional, for containerized run)

### Local Setup

1. Clone the repo:
```bash
git clone https://github.com/srithar2004/TaskFlow.git
cd TaskFlow
```

2. Create the database:
```sql
CREATE DATABASE taskflow_db;
```

3. Set environment variables (or create `application-local.properties`):
```properties
DB_URL=jdbc:mysql://localhost:3306/taskflow_db
DB_USERNAME=root
DB_PASSWORD=your_password
JWT_SECRET=your_secret_key_at_least_32_chars_long
```

4. Run the app:
```bash
mvn spring-boot:run
```

5. Open Swagger UI:
  http://localhost:8081/swagger-ui/index.html


### Run with Docker

```bash
docker build -t taskflow-app .
docker run -p 8081:8081 \
  -e DB_URL=jdbc:mysql://host.docker.internal:3306/taskflow_db \
  -e DB_USERNAME=root \
  -e DB_PASSWORD=your_password \
  -e JWT_SECRET=your_secret_key \
  taskflow-app
```

## API Endpoints

| Method | Endpoint | Description | Access |
|--------|----------|--------------|--------|
| POST | `/api/auth/register` | Register new user | Public |
| POST | `/api/auth/login` | Login, get JWT token | Public |
| POST | `/api/tasks` | Create a task | Admin only |
| GET | `/api/tasks/my-tasks` | View own assigned tasks | Authenticated |
| GET | `/api/tasks/all` | View all tasks | Admin only |
| PATCH | `/api/tasks/{id}/status` | Update task status | Owner or Admin |
| GET | `/api/tasks/dashboard` | Task count summary | Authenticated |
| POST | `/api/tasks/{id}/comments` | Add comment to task | Authenticated |
| GET | `/api/tasks/{id}/comments` | View task comments | Authenticated |

## Security Notes

- Passwords hashed with BCrypt, never stored in plain text
- JWT tokens expire after 24 hours
- Role checks enforced at the service layer, not just UI
- Sensitive config (DB password, JWT secret) loaded via environment variables, never hardcoded

## Future Improvements

- Kubernetes deployment
- Redis caching for dashboard queries
- Email notifications on task assignment
- Refresh token support

## Author

Built by Srithar — https://www.linkedin.com/in/srithar-p-b75a92255/
