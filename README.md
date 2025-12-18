# FinalTermProjectJava

## Overview

FinalTermProjectJava is a Java-based backend application developed using **Spring Boot**. The project demonstrates the design and implementation of RESTful APIs, database integration using JPA, and a clean layered architecture commonly used in real-world backend systems.

This project was designed and developed by me to strengthen my backend development skills and to showcase practical experience with Java and Spring Boot.

---

## Key Features

* RESTful APIs for managing movie-related data
* CRUD operations using Spring Data JPA
* Separation of active and archived records for better data organization
* Maven-based project structure
* Configuration and basic caching concepts
* Clean package organization following backend best practices

---

## Tech Stack

* **Java**
* **Spring Boot**
* **Spring Data JPA**
* **Maven**
* **Relational Database (JPA-compatible)**

---

## Project Structure

```
FinalTermProjectJava
 ├── controller   # REST API controllers
 ├── entity       # JPA entities
 ├── repository   # Data access layer
 ├── config       # Application configuration
 ├── cache        # Cache-related components
 ├── pom.xml      # Maven dependencies
 └── Application  # Main Spring Boot application
```

---

## Getting Started

### Prerequisites

* Java 8 or later
* Maven
* Any JPA-supported relational database

### Setup & Run

```bash
git clone https://github.com/jainishp75/Jainish-Java-Project.git
cd Jainish-Java-Project
mvn clean install
mvn spring-boot:run
```

The application will start on the default Spring Boot port:

```
http://localhost:8080
```

---

## API Design

* APIs follow REST principles
* Controllers handle request/response mapping
* Repositories abstract database access using Spring Data JPA

This structure makes the application easy to extend with additional business logic and services.

---

## What This Project Demonstrates

* Hands-on experience with Java backend development
* Practical usage of Spring Boot and JPA
* Understanding of backend architecture and data modeling
* Ability to design and build a complete backend application end-to-end

---

## Author

**Jainish Patel**
GitHub: [https://github.com/jainishp75](https://github.com/jainishp75)
LinkedIn: [https://www.linkedin.com/in/jainish-patel-196ba2113](https://www.linkedin.com/in/jainish-patel-196ba2113)

---

## Notes
This project focuses on core backend functionality. Additional production-level enhancements such as service-layer abstraction, advanced exception handling, and security can be added as future improvements.


