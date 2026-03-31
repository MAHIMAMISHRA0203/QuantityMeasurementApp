📏 Quantity Measurement App - Backend

This is the backend service for the Quantity Measurement App, built using Spring Boot.

It provides REST APIs for performing various quantity operations along with secure authentication and authorization using JWT and Google OAuth2.


🚀 Features

🔢 Quantity Operations

➕ Add two quantities

➖ Subtract two quantities

➗ Divide quantities

⚖️ Compare quantities


🔄 Convert quantities between units

📐 Supported Measurement Types

📏 Length

⚖️ Weight

🌡️ Temperature

🧪 Volume

🔐 Authentication & Security

👤 User Registration

🔑 User Login (JWT-based)

🪪 Token Generation & Validation

🚫 Logout (Token Blacklisting)

🌐 Google OAuth2 Login

🔒 Secured APIs using Spring Security

🛠️ Tech Stack

Java

Spring Boot

Spring Web

Spring Security

Spring Data JPA

Hibernate

MySql Database

JWT (JSON Web Token)

OAuth2 (Google Authentication)

Maven

📂 Project Structure
src/main/java/com/example/quantity_measurement_app


│
├── config         # Configuration classes

├── controller     # REST controllers

├── dto            # Request/Response DTOs

├── entity         # JPA entities

├── exception      # Custom exceptions & handlers

├── model          # Domain models

├── repository     # Database repositories

├── security       # JWT, OAuth2, Spring Security config

├── service        # Business logic layer

├── unit           # Unit enums & measurement logic

└── Application.java   # Main application class

⚙️ Setup & Run

1️⃣ Clone the Repository

git clone https://github.com/MAHIMAMISHRA0203/quantity-measurement-app.git

cd quantity-measurement-app
2️⃣ Build the Project
mvn clean install
3️⃣ Run the Application

mvn spring-boot:run
🌐 API Base URL http://localhost:8080
