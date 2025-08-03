# Task Management System - Deployment README

## ✨ Project Overview

This is a Task Management System built with Spring Boot (Java) on the backend and a lightweight HTML/JavaScript UI served through the Spring Boot app. Authentication is managed using AWS Cognito, and tasks and projects are stored in an H2 database (local or persistent file).

[▶️ Watch Demo Video](https://raw.githubusercontent.com/maichaouat/task-manager/main/src/main/resources/assets/demo.mp4)

---------

## 🏢 Core Features

* User authentication with AWS Cognito
* CRUD operations for Projects and Tasks
* RESTful API endpoints secured via OAuth2
* Pagination in GET requests
* Client-side UI for interacting with the system
* Unit tests for core functionality 

---------
## 🧩 Modular Architecture Designed for Scalability

The system is built as a modular monolith with four distinct modules:

* auth — handles user authentication via AWS Cognito
* project — manages project-related logic and data
* task — manages task-related features
* common — holds shared components (DTOs, exceptions, utilities)

This structure was intentionally chosen to:

* Separate concerns and simplify development and maintenance
* Allow each module to evolve independently
* Enable potential future migration to microservices if needed

Even though the system is currently deployed as a monolith (for simplicity and educational goals), this modular separation allows for **future scalability** as usage grows — especially if the system is expected to handle 10,000+ users per day.

The client-side is implemented as a **lightweight HTML/JavaScript UI** that interacts with secure REST endpoints.Each user’s actions are tracked by storing their **user ID** in the backend database — which enables personalization and accountability.

Currently, the system uses an H2 local database due to project constraints, but the architecture supports future migration to a production-ready database like PostgreSQL (e.g., AWS RDS), along with Flyway for schema versioning and controlled migrations.

-----------

## 🚀 URLs

### 🌐 Local Development URLs

Home UI Page: http://localhost:8080

API Base URL: http://localhost:8080/api/projects

Cognito Login URL: https://eu-north-1abjcamvis.auth.eu-north-1.amazoncognito.com/login?client_id=3jj2ag9hpk2apiihddacenjuol&response_type=code&scope=email+openid+phone+profile&redirect_uri=http%3A%2F%2Flocalhost%3A8080%2Flogin%2Foauth2%2Fcode%2Fcognito

-------------

## 🔧 Setup Instructions (for Dev)
1. Clone the repository
2. Configure `application.yml` with:
    - AWS Cognito client ID, secret, and redirect URI
    - H2 or real DB connection
3. Run the Spring Boot application
4. Navigate to `http://localhost:8080`
5. Login via Cognito
6. Click on "Go to Projects Page"

------------------
# 📈 Scalability Strategy for 10K Daily Users

This project was intentionally designed with a modular monolith architecture to support future growth. While deployment today may be as a single Spring Boot application, the separation into auth, task, project, and common modules allows for:

* **Independent scaling:** Each module can later be extracted into a microservice if traffic requires separation of concerns.
* **Easy maintainability:** Developers can isolate changes by domain without impacting the rest of the system.
* **Service boundary clarity:** Simplifies migration to distributed services when required.

If the system needs to handle 10,000 daily users, I recommend the following deployment plan:

## 🧱 Backend Deployment

**Containerized Deployment:** Deploy the Spring Boot app in a Docker container using AWS ECS with Fargate or Kubernetes (EKS) for scalability and orchestration.

**Horizontal Scaling:** Use multiple app replicas behind a load balancer to distribute traffic efficiently.

**PostgreSQL Production DB:** Use AWS RDS with PostgreSQL, configured for auto-scaling and multi-AZ for high availability.

**Flyway DB Migrations:** Manage schema changes safely using Flyway to track and apply migrations consistently.

**Logging & Monitoring:** Use AWS CloudWatch, Prometheus, and Grafana to monitor system health, detect bottlenecks, and trigger alerts.


## 🌐 Frontend Hosting

**S3 + CloudFront**: Move the static home.html and other assets to AWS S3, served via CloudFront (CDN) for fast, global delivery.

**Future Frontend:** Consider migrating to React or Angular for better user interactivity and maintainability.

## 🔐 Authentication

**Token Refresh:** Support frontend refresh tokens and handle token expiration.


## 📊 Monitoring & Performance

**Spring Boot Actuator:** Expose health checks, metrics, and readiness probes.

**Caching Layer:** Use Redis or Memcached for repeated queries and frequent reads.


-----

## 🚫 Known Limitations
- H2 DB is not suited for production
- Static HTML is basic and not responsive
- No admin panel or multi-role permissions yet

---

## ✉ Suggestions for Improvement
- Replace the local H2 database with a reliable, scalable external database
- Add Docker support for containerization
- Introduce Angular/React frontend for richer UX
- Implement role-based access control (admin vs user)
- Add integration tests

-------
