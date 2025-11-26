Backend API for a Learning Management System (LMS) built using Java, Spring Boot, Hibernate, PostgreSQL, Spring Security and Apache Kafka. The app provides the core functionalities needed for an online learning platform, including user management, course creation, secure authentication/authorization with asynchronous email notifications.

🚀 Tech Stack

- Spring Boot
- Spring Data JPA (Hibernate)
- Spring Security (JWT-based authentication)
- PostgreSQL
- Apache Kafka
- Docker
- Mockito, JUnit

🛠️ Features 

🔐 Authentication & Authorization

Secure login using JWT (JSON Web Tokens)
Role-based access control (Admin, Instructor, Student)
Password encryption using BCrypt

📚 Course Management

Create, update, and delete courses (admin)
Search and filter courses

🎓 Enrollment System

Student enrollment in available courses
Asynchronous email notifications on course registration with Kafka

📧 Event-Driven Email Notifications

Kafka-based messaging for decoupled email service
Outbox pattern implementation for reliable event delivery

📊 Reporting & Dashboard (Prototype level)

Basic statistics: number of users, courses, enrollments

🗄️ Database Integration

PostgreSQL for persistent storage
ORM with Hibernate / Spring Data JPA

🌐 RESTful API Design

Clean and consistent REST API endpoints
JSON-based request/response structures
