# 👶 CodeCrafters –Bloom Baby shop Management System

> A JavaFX + MySQL desktop application that manages inventory, income/expenses and employee salaries for a children's clothing shop in Maharagama, Sri Lanka.

**Team CodeCrafters** |  Object Oriented Programming | Group Assignment  
Faculty of Technology, University of Sri Jayewardenepura  
Bachelor of Information and Communication Technology Honours Degree – First Year, Second Semester

![Java](https://img.shields.io/badge/Java-17+-orange)
![JavaFX](https://img.shields.io/badge/UI-JavaFX%2021-blueviolet)
![Database](https://img.shields.io/badge/Database-MySQL-blue)
![Build](https://img.shields.io/badge/Build-Maven-red)
![Status](https://img.shields.io/badge/Status-Development%20Finished-brightgreen)
![License](https://img.shields.io/badge/Use-Academic-lightgrey)

---

## 📑 Table of Contents

1. [Project Overview](#-project-overview)
2. [Problem Statement](#-problem-statement)
3. [Current Features](#-current-features)
4. [Scope](#-scope)
5. [OOP Concepts Applied](#-oop-concepts-applied)
6. [System Architecture](#-system-architecture)
7. [Project Structure](#-project-structure)
8. [Database Design](#-database-design)
9. [Tech Stack](#-tech-stack)
10. [Getting Started](#-getting-started)
11. [Using the Application](#-using-the-application)
12. [Security](#-security)
13. [Exception Handling](#-exception-handling)
14. [Git Workflow](#-git-workflow)
15. [Team Members](#-team-members)
16. [Project Progress](#-project-progress)
17. [Documentation](#-documentation)
18. [Future Enhancements](#-future-enhancements)
19. [Acknowledgements](#-acknowledgements)
20. [Submission](#-submission)

---

## 📌 Project Overview

This project was developed for a local clothing shop in **Maharagama, Sri Lanka**, owned by **Mr. Amila Buddika**. The shop sells clothing for babies and children, and serves both:

- **B2B (wholesale)** customers, who are other businesses buying in bulk
- **B2C (retail)** customers, who are individual buyers

The application replaces manual record keeping with a single desktop program backed by a MySQL database. After logging in, the shop owner can track stock, record income and expenses, pay employee salaries, and see a live dashboard of the shop's position.

---

## ❗ Problem Statement

Based on our client interview, the shop currently faces these problems:

| Problem | Impact |
|---|---|
| No consistent inventory records | Stock shortages and overstocking go unnoticed |
| Manual monthly salary tracking | Calculation errors, no history |
| Income and expenses not recorded in one place | Real costs remain hidden |
| No clear view of the shop's balance | The owner cannot see actual financial standing |
| Separate wholesale and retail prices handled by hand | Pricing mistakes |

**Our solution:** one integrated application that digitizes these operations and gives the owner an at-a-glance dashboard.

---

## ✨ Current Features

### 🔐 Login and Registration
- Register a new account (username, password, full name, optional email / phone / address)
- Log in with username and password
- Input validation (required fields, password length of at least 6, matching passwords, email format, duplicate usernames)
- Passwords are stored **salted and hashed** (PBKDF2)

### 📊 Dashboard
- Total number of items in stock
- Number of low-stock items (quantity below 10) with a list of what is running low
- Net balance (income minus expenses)
- Total salaries still pending

### 📦 Inventory / Stock Management
- Add new items with code, name, size, quantity and price
- Increase or reduce stock for a selected item
- Delete items
- Reducing stock below zero is blocked with an `InsufficientStockException`
- Duplicate item codes are rejected

### 💰 Transactions (Income / Expense)
- Record income and expense transactions with a description and amount (dated automatically)
- Full transaction history, newest first
- **Export CSV Report** of all transactions to a location you choose

### 👥 Salary Payments
- Add employees with name, role and monthly salary
- Mark a salary as **Paid** or **Pending**
- Paying a salary automatically records a matching **Expense** transaction; both steps happen in a single database transaction, so either both succeed or neither does

### 🧮 Pricing Logic (service layer)
- `RetailPricing`: unit price × quantity
- `WholesalePricing`: 10% discount on orders of 10 or more items
- `PricingCalculator` picks the strategy based on the customer type
- *This logic is implemented and demonstrates polymorphism, but it is not yet connected to a billing screen.*

---

## 📐 Scope

### ✅ Implemented
Login and registration, inventory management, low-stock alerts, income/expense transactions, employee salary payments, dashboard summary, CSV export of transactions, and persistent MySQL storage.

### 🛠️ Planned (see [Project Progress](#-project-progress))
Billing and invoices with B2B/B2C pricing in the UI, purchase cost tracking, profit calculation, and richer reports.

### ❌ Out of Scope
Online shopping, online payment gateways, mobile app, supplier portal, bank integration, automated tax filing, attendance and leave management, and multi-branch management.

---

## 🧠 OOP Concepts Applied

| Concept | Where it is used in this project |
|---|---|
| **Encapsulation** | Private fields with controlled accessors and validation in `Item`, `Employee`, `Txn` and `Person` (for example, no negative prices, quantities, salaries or amounts) |
| **Abstraction** | `abstract class Person` defines shared structure and an abstract `getRole()` |
| **Inheritance** | `Employee extends Person`, `Customer extends Person`, `InsufficientStockException extends BabyShopException` |
| **Polymorphism** | `PricingStrategy` interface with `RetailPricing` and `WholesalePricing`, chosen at runtime by `PricingCalculator`; overridden `getRole()` in subclasses |
| **Method Overloading** | `PricingCalculator.calculate(item, quantity)` and `calculate(item, quantity, customer)` |
| **Interfaces & Generics** | Generic `Repository<T>` interface implemented by `ItemDAO`, `EmployeeDAO` and `TransactionDAO` |
| **Exception Handling** | Custom checked exceptions (`BabyShopException`, `InsufficientStockException`), plus try-catch and try-with-resources throughout the DAO and controller classes |
| **Collections** | `ObservableList`, `ArrayList` and streams for tables, dashboard totals and low-stock lists |
| **File Handling** | CSV export through `CsvExporter` using `BufferedWriter` and try-with-resources |
| **Packages** | Code organized into `model`, `dao`, `service`, `db`, `exception` and `util` |

---

## 🏗️ System Architecture

The application follows a layered structure:

```
┌──────────────────────────────────────────────┐
│  UI Layer        FXML views + Controllers    │  ← login.fxml, main.fxml,
│                  (JavaFX)                    │    LoginController, MainController
├──────────────────────────────────────────────┤
│  Service Layer   Business rules              │  ← InventoryService,
│                                              │    PricingCalculator + strategies
├──────────────────────────────────────────────┤
│  DAO Layer       Data access (JDBC)          │  ← ItemDAO, EmployeeDAO,
│                                              │    TransactionDAO, UserDAO
├──────────────────────────────────────────────┤
│  Database        MySQL (DBConnection)        │
└──────────────────────────────────────────────┘
```

> Note: `MainController` currently calls the DAOs directly for most actions and uses `InventoryService` for stock calculations. Moving more business rules into the service layer is a planned improvement.

---

## 📂 Project Structure

```
CodeCrafters/
│
├── .github/workflows/blank.yml        # Placeholder CI workflow
├── docs/
│   └── diagrams/                      # Use case, class, ER, activity and sequence diagrams (PNG)
├── src/main/
│   ├── java/com/codecrafters/babyshop/
│   │   ├── App.java                   # Application entry point (JavaFX)
│   │   ├── LoginController.java       # Login / register screen logic
│   │   ├── MainController.java        # Dashboard, inventory, transactions, salaries
│   │   ├── dao/                       # Repository<T>, ItemDAO, EmployeeDAO, TransactionDAO, UserDAO
│   │   ├── db/                        # DBConnection (connection + table creation)
│   │   ├── exception/                 # BabyShopException, InsufficientStockException
│   │   ├── model/                     # Person, Employee, Customer, Item, Txn
│   │   ├── service/                   # InventoryService, PricingStrategy, RetailPricing,
│   │   │                              # WholesalePricing, PricingCalculator
│   │   └── util/                      # CsvExporter
│   └── resources/
│       ├── com/codecrafters/babyshop/ # login.fxml, main.fxml
│       └── db.properties              # Database connection settings
├── schema.sql                         # Reference SQL schema
├── pom.xml                            # Maven build file
├── RUN_GUIDE_WINDOWS.txt              # Detailed Windows setup guide
├── .gitignore
└── README.md
```

---

## 🗄️ Database Design

Database name: **`babyshop`** (MySQL). Tables are created automatically on first run by `DBConnection.init()`; `schema.sql` contains the same definitions for reference.

| Table | Columns |
|---|---|
| **item** | `code` (PK), `name`, `size`, `qty`, `price` |
| **transactions** | `id` (PK, auto), `date`, `description`, `type` (Income / Expense), `amount` |
| **employee** | `id` (PK, auto), `name`, `role`, `salary`, `paid` |
| **users** | `user_id` (PK, auto), `username` (unique), `password` (salted hash) |
| **user_details** | `detail_id` (PK, auto), `user_id` (FK → users, cascade), `full_name`, `email`, `phone`, `address` |

📎 The ER diagram is in [`docs/diagrams/Er-diagram.png`](docs/diagrams/Er-diagram.png).

---

## 🛠️ Tech Stack

| Area | Technology |
|---|---|
| Language | Java 17+ |
| UI | JavaFX 21.0.2 (FXML) with the [AtlantaFX](https://github.com/mkpaz/atlantafx) `PrimerLight` theme |
| Database | MySQL via `mysql-connector-j` 8.4.0 (JDBC) |
| Build tool | Maven (with `javafx-maven-plugin` 0.0.8) |
| IDE | IntelliJ IDEA |
| Version control | Git and GitHub |
| Diagrams | draw.io |

---

## 🚀 Getting Started

### Prerequisites
- JDK 17 or newer
- Maven (bundled with IntelliJ IDEA)
- MySQL Server running locally (or WAMP/XAMPP with the MySQL service started; Apache/PHP is not needed)
- Git
- Internet access on the first build so Maven can download dependencies

### Installation

```bash
# 1. Clone the repository
git clone https://github.com/<your-username>/CodeCrafters.git

# 2. Move into the project folder
cd CodeCrafters
```

### Configure the database

1. Make sure the MySQL service is running.
2. Optionally create a database named `babyshop` (the default JDBC URL also creates it automatically with `createDatabaseIfNotExist=true`).
3. Open `src/main/resources/db.properties` and check the values:

```properties
db.url=jdbc:mysql://localhost:3306/babyshop?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true
db.user=root
db.password=
```

Change `db.user`, `db.password` or the port to match your local MySQL setup.

> `db.properties` is listed in `.gitignore`, so each team member keeps their own local credentials. Do not commit real passwords.

### Run the application

**Option A – IntelliJ IDEA**
1. Open the project folder (the one containing `pom.xml`) as a Maven project and wait for dependencies to download.
2. Run `src/main/java/com/codecrafters/babyshop/App.java`.

**Option B – Maven**
```bash
mvn javafx:run
```
(or use *Maven tool window → Plugins → javafx → javafx:run* in IntelliJ).

For step-by-step Windows instructions and troubleshooting, see [`RUN_GUIDE_WINDOWS.txt`](RUN_GUIDE_WINDOWS.txt).

### Troubleshooting

| Problem | Fix |
|---|---|
| "Cannot connect to the database" | Confirm the MySQL service is running |
| "Access denied" | Correct `db.user` / `db.password` in `db.properties` |
| "Communications link failure" | Check the MySQL port (default 3306) in `db.url` |
| Maven dependency errors | Check your internet connection and reload the Maven project |
| JavaFX runtime errors | Use JDK 17+ and launch through `javafx:run` or IntelliJ's Maven integration |

---

## 🖥️ Using the Application

1. **Register** a new account on the first screen, then **log in**. There are no pre-seeded default accounts.
2. **Dashboard** – view stock totals, low-stock alerts, net balance and pending salaries.
3. **Inventory** – add items; select a row, enter a quantity, then use **+ Add Stock** or **- Reduce Stock**; delete items you no longer sell.
4. **Transactions (Income / Expense)** – record money in or out; use **Export CSV Report** to save the history as a `.csv` file.
5. **Salary Payments** – add employees and use **Mark Selected as Paid**, which also creates the matching expense entry.

All amounts are shown in Sri Lankan rupees (Rs.).

---

## 🔒 Security

- Passwords are never stored in plain text. They are hashed with **PBKDF2-HMAC-SHA256** (65,536 iterations, 16-byte random salt per user) and verified with a constant-time comparison.
- All database queries that take user input use **prepared statements**.
- Multi-step writes (registering a user, paying a salary) run inside database transactions with rollback on failure.
- Role-based access control (Owner / Staff) is **not yet implemented**; every logged-in user currently sees all tabs.

---

## ⚠️ Exception Handling

| Exception / mechanism | When it is used |
|---|---|
| `InsufficientStockException` (extends `BabyShopException`) | A stock reduction would take the quantity below zero |
| `IllegalArgumentException` | Negative or invalid values in `Item`, `Employee`, `Txn` and `Person`, or a quantity of zero or less |
| `NumberFormatException` | Non-numeric text entered for quantity, price, amount or salary |
| `SQLIntegrityConstraintViolationException` | Duplicate item code or username |
| `SQLException` | Database connection or query problems (shown in an error dialog) |
| `IOException` | CSV export failures |

The application shows clear alert messages to the user instead of crashing.

---

## 🌿 Git Workflow

### Branches

| Branch | Purpose |
|---|---|
| `main` | Stable, final version |
| `develop` | Integration of completed features |
| `feature/<name>` | Individual feature work (for example `feature/billing`) |

### Workflow

```bash
git checkout develop
git pull
git checkout -b feature/billing

# ...write code...

git add .
git commit -m "Add billing screen using PricingCalculator"
git push -u origin feature/billing
```

Then open a **Pull Request** into `develop`, request a teammate's review, and merge after approval.

### Rules
- Avoid committing directly to `main` or `develop`.
- Pull from `develop` regularly to avoid conflicts.
- Write clear, descriptive commit messages.
- Never commit `db.properties` with real credentials (it is git-ignored).

---

## 👥 Team Members

**Team CodeCrafters**

| Name | Student ID | GitHub | Responsibility |
|---|---|---|---|
| Member 1 | | @username | |
| Member 2 | | @username | |
| Member 3 | | @username | |
| Member 4 | | @username | |
| Member 5 | | @username | |

---

## ✅ Project Progress

- [x] Client interview and requirement gathering
- [x] GitHub repository setup
- [x] UML, ER, activity and sequence diagrams
- [x] Model classes (`Person`, `Employee`, `Customer`, `Item`, `Txn`)
- [x] Custom exception hierarchy
- [x] Database and DAO layer (MySQL)
- [x] Login and registration with hashed passwords
- [x] Inventory and low-stock dashboard
- [x] Income / expense transactions
- [x] Salary payment module
- [x] CSV export of transactions
- [x] Pricing strategies (retail / wholesale)
- [ ] Billing screen with multi-item sales and invoices (B2B / B2C)
- [ ] Purchase cost tracking and profit calculation
- [ ] Additional reports (sales, inventory, salary)
- [ ] Role-based access (Owner / Staff)
- [ ] Automated tests and a real CI build
- [ ] Presentation and viva preparation

---

## 📄 Documentation

All diagrams are in [`docs/diagrams/`](docs/diagrams/):

- 🎭 Use case diagram: `Use case diagram/use-case-diagram.png`
- 📐 Class diagram: `Class diagram/class-diagram.png`
- 🗂️ ER diagram: `Er-diagram.png`
- 🔄 Activity diagrams: `Activity diagram/` (Create Sale, Receive Stock, Monthly Salary)
- ⏱️ Sequence diagrams: `Sequence diagram/` (Create Sale, Receive Stock, Monthly Salary)
- 🪟 Windows setup guide: [`RUN_GUIDE_WINDOWS.txt`](RUN_GUIDE_WINDOWS.txt)

> The Software Requirements Specification (SRS) is not included in this repository yet. Add it to `docs/` and link it here.

---

## 🔮 Future Enhancements

- Full billing module with invoice numbers and printable invoices
- Wholesale / retail pricing wired into the sales screen
- Purchase cost tracking, COGS and profit reports
- Owner / Staff roles with restricted access
- Dashboard charts
- Automatic database backups
- Barcode scanning for products
- Attendance and leave management

---

## 🙏 Acknowledgements

- **Mr. Amila Buddika**, the client, for his time and valuable input during the requirement-gathering interview
- Lecturers and instructors of **IIC 1153 – Object Oriented Programming**, Faculty of Technology, University of Sri Jayewardenepura
- [AtlantaFX](https://github.com/mkpaz/atlantafx) for the JavaFX 

<p align="center">Made with ☕ and Java by <b>Team CodeCrafters</b></p>
