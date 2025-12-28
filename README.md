📚 Library Management System (Java + JDBC + MySQL)

A console-based Library Management System built using Core Java, JDBC, and MySQL, following a clean layered architecture (DAO–Service–Model).
The project supports Admin & User roles, persistent database storage, and proper separation of concerns.

🚀 Features
👨‍💼 Admin

Add new books (with quantity)

Delete books

View all books

Issue books to users

Return books

View all issued books

Role-based access control

👤 User

View available books

Issue a book

Return a book

⚙️ System

MySQL database integration using JDBC

DAO pattern for database operations

Service layer for business logic

Role-based authentication

Fine calculation for late returns

Clean and modular package structure

🛠️ Tech Stack

Java 17

JDBC

MySQL

IntelliJ IDEA

MySQL Connector/J

<h2>🗂️ Project Structure</h2>
<pre>
LibraryApp/
│
├── src/
│   └── library/
│       ├── dao/
│       │   ├── BookDAO.java
│       │   ├── UserDAO.java
│       │   └── IssuedBookDAO.java
│       │
│       ├── model/
│       │   ├── Book.java
│       │   ├── User.java
│       │   ├── IssuedBook.java
│       │   └── Role.java
│       │
│       ├── service/
│       │   ├── LibraryService.java
│       │   └── AuthService.java
│       │
│       ├── util/
│       │   ├── DBConnection.java
│       │   └── FileStorage.java
│       │
│       └── Main.java
│
├── README.md
├── .gitignore
├── LibraryApp.iml
└── mysql-connector-j-9.5.0.jar
</pre>

🧠 Architecture Overview
Main
↓
Service Layer (Business Logic)
↓
DAO Layer (Database Operations)
↓
MySQL Database


Model → Plain Java objects (Book, User, IssuedBook)

DAO → All SQL & JDBC logic

Service → Validation, business rules, role handling

Util → DB connection & file utilities

🗄️ Database Schema
📘 books
CREATE TABLE books (
id INT AUTO_INCREMENT PRIMARY KEY,
name VARCHAR(100) UNIQUE NOT NULL,
quantity INT NOT NULL
);

👤 users
CREATE TABLE users (
id INT AUTO_INCREMENT PRIMARY KEY,
name VARCHAR(100) UNIQUE NOT NULL,
role ENUM('ADMIN','USER') NOT NULL
);

📕 issued_books
CREATE TABLE issued_books (
id INT AUTO_INCREMENT PRIMARY KEY,
book_id INT,
user_id INT,
issue_date DATE,
due_date DATE,
FOREIGN KEY (book_id) REFERENCES books(id),
FOREIGN KEY (user_id) REFERENCES users(id)
);

▶️ How to Run

Clone the repository

git clone https://github.com/RohitMane2005/library-management-system.git


Open in IntelliJ IDEA

Add MySQL Connector/J to project libraries

Update database credentials in:

src/library/util/DBConnection.java


Run:

Main.java

🔐 Sample Login
1. Admin
2. User
   Choose role:


Admin credentials handled internally

Users are auto-created on first issue

🎯 What This Project Demonstrates

Strong Core Java fundamentals

Real-world JDBC usage

DAO & Service layer design

Database normalization

Role-based access

Clean code practices

📌 Future Improvements

Spring Boot REST API

Web UI (HTML/CSS/JS)

Hibernate/JPA

Dockerized MySQL

Unit testing (JUnit)

👨‍💻 Author

Rohit Mane
Java Developer | Backend Enthusiast

⭐ If you like this project

Give it a ⭐ on GitHub and feel free to fork!
