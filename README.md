# Inventory Management System

A Java-based desktop application for managing product inventory. This program provides functionality to add, modify, delete, and view parts and products. It also supports generating inventory reports, helping businesses maintain accurate stock levels.

---

## Features

> **Default Login:** Username: `test`, Password: `test`

* User authentication system (login screen and role-based access)
* Add, modify, and delete **Parts**
* Add, modify, and delete **Products**
* Associate parts with products
* Validate user input for numeric and text fields
* Generate inventory **summary reports**
* Export reports to **CSV** format
* JavaFX GUI for a user-friendly interface
* SQLite database for persistent storage (optional)
* Includes **unit tests** for core components

---

## Technologies Used

* **Java 11+**
* **JavaFX 11+** for GUI
* **SQLite** (via JDBC) for storage
* **SLF4J** for logging (via `slf4j-api` and `slf4j-simple`)
* **JUnit 5** for testing

---

## Setup Instructions

### 1. Prerequisites

* Install Java JDK 11–17
* Download JavaFX SDK: [https://gluonhq.com/products/javafx/](https://gluonhq.com/products/javafx/)

### 2. Project Structure

```
InventoryManagement/
├── src/
│   ├── Main/InventoryProgram.java
│   ├── Models/Part.java, Product.java
│   ├── Controllers/*.java
│   ├── SQLiteDatabase/DatabaseConnection.java
│   └── Tests/ (JUnit test classes)
├── lib/ (JavaFX, SQLite, SLF4J JARs)
└── README.md
```

### 3. Run Configuration (IntelliJ)

* Add JavaFX SDK to module libraries
* Add SQLite and SLF4J JARs to classpath
* VM options:

```bash
--module-path "path/to/javafx-sdk-21/lib" --add-modules javafx.controls,javafx.fxml,javafx.base
```

### 4. Running the App

* Open `InventoryProgram.java`
* Click **Run**

---

## Screenshots

*Add screenshots of the login screen, main dashboard, product form, and reports window here.*

---

## License

MIT License. Feel free to use, extend, or distribute.

---

## TODO

* Add undo/redo functionality
* Support PDF export for reports
* Improve role-based access control

---

## Author

Code Copilot — AI Programming Assistant
