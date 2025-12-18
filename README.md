# 🎬 Final Term Project - Java (Spring Boot)

## 📖 Overview
**FinalTermProjectJava** is a Spring Boot–based movie management application that demonstrates clean architecture, modular design, and modern Java development practices.  
It integrates entity modeling, service-layer abstraction, caching mechanisms, and repository patterns to handle movie data efficiently.

---

## 🧩 Project Structure
FinalTermProjectJava/
│
├── src/
│ ├── main/
│ │ ├── java/com/example/FinalTermProjectJava/
│ │ │ ├── Entity/ # Movie and ArchivedMovie entities
│ │ │ ├── cache/ # Caching utilities
│ │ │ ├── config/ # Spring configuration
│ │ │ ├── controller/ # REST controllers for movies
│ │ │ ├── repository/ # JPA repositories for data access
│ │ │ ├── service/ # Business logic layer
│ │ │ ├── utils/ # Response handling utilities
│ │ │ └── FinalTermProjectJavaApplication.java # Main application entry point
│ │ └── resources/
│ │ ├── templates/movies.html # Thymeleaf UI template
│ │ └── application.properties # App configuration
│ └── test/java/... # Unit tests
│
├── pom.xml # Maven dependencies
├── mvnw, mvnw.cmd # Maven wrapper scripts
└── .gitignore, .gitattributes


---

## ⚙️ Features
- 📁 **Entity Management:** CRUD operations for movies and archived movies  
- 🔄 **Service Layer:** Decoupled business logic via `MovieService`  
- 💾 **Repository Layer:** JPA repositories for data access  
- 🧠 **Caching System:** Custom cache inspector for optimized performance  
- 🌐 **Thymeleaf UI:** Simple and clean web interface for movie display  
- ⚙️ **Configurable:** Environment-specific settings via `application.properties`  

---

## 🚀 Getting Started

### 1️⃣ Prerequisites
Ensure you have installed:
- **Java 17+**
- **Maven 3.9+**
- **Spring Boot 3.x**
- IDE such as **IntelliJ IDEA**, **Eclipse**, or **VS Code**

### 2️⃣ Clone the Repository
```bash
git clone https://github.com/jainishp75/FinalTermProjectJava.git
cd FinalTermProjectJava

