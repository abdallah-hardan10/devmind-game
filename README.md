# 🧠 DevMind Game API

A math speed game API built with **Spring Boot** and **MySQL** for DevMind's Backend Task.  
Players solve arithmetic equations based on difficulty and are timed and scored.

---

## Features

- Start a new game with a name and difficulty level
- Automatically generate math questions per difficulty
- Evaluate answers and record time taken
- Keep history of answers and show best performance
- End game and return final results

---

## Tech Stack

- Java 17
- Spring Boot 3.5.3
- Spring Data JPA
- MySQL
- Lombok
- JUnit 5 + MockMvc

---

## Requirements

- JDK 17+
- Maven 3+
- MySQL running and configured in `application.properties`

---

## Running the App

```bash
# 1. Clone the project
git clone https://github.com/your-username/devmind-game.git

# 2. Open in IntelliJ or any IDE
# 3. Make sure your MySQL DB is running and connected
# 4. Run the application
mvn spring-boot:run
