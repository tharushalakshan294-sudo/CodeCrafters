# CodeCrafters
# 👕 Clothing Shop Business Management System

> A Java, OOP-based business management application for a children's clothing shop in Maharagama, Sri Lanka.

**Team CodeCrafters** | Object Oriented Programming | Group Assignment
Faculty of Technology, University of Sri Jayewardenepura
Bachelor of Information and Communication Technology Honours Degree – First Year, Second Semester

![Java](https://img.shields.io/badge/Java-17+-orange)
![Database](https://img.shields.io/badge/Database-SQLite-blue)
![Status](https://img.shields.io/badge/Status-In%20Development-yellow)
![License](https://img.shields.io/badge/Use-Academic-lightgrey)

---

## 📑 Table of Contents

1. [Project Overview](#-project-overview)
2. [Problem Statement](#-problem-statement)
3. [Objectives](#-objectives)
4. [Features](#-features)
5. [Scope](#-scope)
6. [OOP Concepts Applied](#-oop-concepts-applied)
7. [System Architecture](#-system-architecture)
8. [Project Structure](#-project-structure)
9. [Database Design](#-database-design)
10. [Tech Stack](#-tech-stack)
11. [Getting Started](#-getting-started)
12. [User Roles](#-user-roles)
13. [File Handling](#-file-handling)
14. [Exception Handling](#-exception-handling)
15. [Git Workflow](#-git-workflow)
16. [Team Members](#-team-members)
17. [Project Progress](#-project-progress)
18. [Documentation](#-documentation)
19. [Future Enhancements](#-future-enhancements)
20. [Acknowledgements](#-acknowledgements)

---

## 📌 Project Overview

This project was developed for a local clothing shop in **Maharagama, Sri Lanka**, owned by **Mr. Amila Buddika**. The shop sells clothing for **boys and girls up to 12 years of age** and serves both:

- **B2B (wholesale)** customers, who are other businesses buying in bulk
- **B2C (retail)** customers, who are individual buyers

The system replaces the shop's manual processes with a centralized, database-backed Java application that manages billing, inventory, employee salaries, expenses, and business profitability.

---

## ❗ Problem Statement

Based on our client interview, the shop currently faces these problems:

| Problem | Impact |
|---|---|
| Manual billing | Slow, error-prone, and hard to trace |
| No consistent inventory records | Stock shortages and overstocking go unnoticed |
| Separate wholesale and retail prices handled by hand | Pricing mistakes |
| Manual monthly salary calculation | Calculation errors, no history |
| Transportation and other expenses not recorded | Real costs remain hidden |
| No clear profit calculation | The owner cannot see actual profitability |

**Our solution:** one integrated application that automates these operations and gives the owner accurate financial insight.

---

## 🎯 Objectives

1. Replace manual billing with a computerized billing system.
2. Support both B2B wholesale and B2C retail sales with separate prices.
3. Maintain accurate, up-to-date inventory records.
4. Automate monthly employee salary calculation.
5. Record transportation costs (stock collection and customer delivery) and other business expenses.
6. Track product purchasing costs.
7. Calculate revenue, gross profit, and estimated net profit.
8. Generate useful reports for the shop owner.
9. Store business data securely and persistently.

---

## ✨ Features

### 📦 Product and Inventory Management
- Register, update, search, and deactivate products
- Support for categories (Boys / Girls), sizes, and colours
- Separate wholesale and retail selling prices per product
- Stock receiving with quantity, date, and purchase cost
- Automatic stock reduction after each sale
- Low-stock notifications at a defined minimum level
- Stock history of additions and reductions

### 🧾 Billing and Sales
- Create sales with multiple products in one transaction
- Choose transaction type (B2B or B2C); the correct price is applied automatically
- Automatic subtotal and total calculation
- Unique invoice number and timestamp per sale
- Sales blocked if requested quantity exceeds available stock
- Retrieve previous invoices and sales records
- Separate B2B and B2C sales summaries

### 👥 Employee and Salary Management
- Register, update, and deactivate employees
- Fixed monthly salary with allowances and deductions
- Automatic gross and net salary calculation
- Monthly salary payment records and summaries

### 🚚 Expense Management
- Transportation expenses (stock collection and customer delivery)
- Other business expenses (rent, electricity, water, communication, etc.)
- Totals by period and by category
- Full expense history

### 💰 Profit and Financial Management
- Total sales revenue for any period
- Cost of goods sold (COGS) from recorded purchase costs
- Gross profit = Revenue − COGS
- Estimated net profit = Gross profit − Operating expenses
- Separate B2B and B2C financial performance
- Rules designed to avoid double-counting costs

### 📊 Reports
- Daily and monthly sales reports
- Inventory and low-stock reports
- Salary, transportation, and expense reports
- Revenue and profit summaries
- Date-range selection

### 🔐 Authentication and Access Control
- Login and logout with credentials
- Passwords stored as hashes
- Salary and financial data restricted to the Owner

---

## 📐 Scope

### ✅ In Scope (Initial Version)
Billing and invoices, B2B/B2C pricing, product and inventory management, employee and salary management, expense recording, purchase cost management, profit calculation, daily and monthly reports, and persistent storage.

### ❌ Out of Scope
Online shopping, online payment gateways, mobile app, supplier portal, bank integration, automated tax filing, attendance and leave management, and multi-branch management.

> 📝 Some advanced modules are planned for future phases (see [Future Enhancements](#-future-enhancements)).

---

## 🧠 OOP Concepts Applied

| Concept | Where it is used in this project |
|---|---|
| **Encapsulation** | Private fields with getters/setters and validation in all model classes (for example, no negative prices or quantities) |
| **Abstraction** | `abstract class Person` and `abstract class Expense` define shared structure and contracts |
| **Inheritance** | `Employee extends Person`, `TransportExpense` / `BusinessExpense extends Expense`, `Owner` / `Staff extends User` |
| **Polymorphism** | `PricingStrategy` interface with `WholesalePricing` and `RetailPricing`; overridden methods such as `calculateAmount()` |
| **Exception Handling** | Custom exceptions plus try-catch-finally across the service and UI layers |
| **Collections & Generics** | `ArrayList`, `HashMap`, and a generic `Repository<T>` interface |
| **File Handling** | Invoice files, CSV report export, error logs, configuration, database backup |
| **Interfaces & Packages** | `Repository<T>`, `Payable`, `Reportable`, `PricingStrategy`; code organized into packages |

---

## 🏗️ System Architecture

The system follows a **layered architecture**:

```
┌──────────────────────────────┐
│     UI Layer (ui)            │  ← user interaction
├──────────────────────────────┤
│     Service Layer (service)  │  ← business rules
├──────────────────────────────┤
│     Repository Layer         │  ← data access
├──────────────────────────────┤
│     Database / Files         │  ← persistent storage
└──────────────────────────────┘
```

Each layer talks only to the layer directly below it, which keeps the code organized, reusable, and easy to maintain.

---

## 📂 Project Structure

```
clothing-shop-management-system/
│
├── docs/                         # SRS, UML diagrams, ER diagram
├── src/
│   └── com/clothingshop/
│       ├── model/                # Product, Sale, SaleItem, Employee, User, Expense...
│       ├── interfaces/           # Repository<T>, Payable, Reportable
│       ├── pricing/              # PricingStrategy, WholesalePricing, RetailPricing
│       ├── exception/            # Custom exception classes
│       ├── repository/           # Database and file access classes
│       ├── service/              # BillingService, InventoryService, SalaryService, ProfitService
│       ├── util/                 # FileUtil, Validator, Logger
│       ├── ui/                   # User interface screens
│       └── Main.java             # Application entry point
│
├── data/                         # Database file, backups (git-ignored)
├── output/                       # Generated invoices and reports (git-ignored)
├── logs/                         # Error logs (git-ignored)
├── .gitignore
└── README.md
```

---

## 🗄️ Database Design

Main entities (from the SRS):

| Entity | Key information |
|---|---|
| **Product** | Product ID, name, category, size, colour, purchase cost, wholesale price, retail price |
| **Inventory** | Stock ID, product ID, available quantity, minimum stock level |
| **Customer** | Customer ID, name, contact details, customer type |
| **Sale** | Sale ID, invoice number, date, customer type, total amount |
| **Sale Item** | Sale item ID, sale ID, product ID, quantity, unit selling price |
| **Employee** | Employee ID, name, contact details, job role, monthly salary |
| **Salary** | Salary ID, employee ID, pay period, basic salary, allowances, deductions, net salary |
| **Transportation Expense** | Expense ID, date, purpose, description, amount |
| **Business Expense** | Expense ID, date, category, description, amount |
| **Product Purchase** | Purchase ID, product ID, purchase date, quantity, unit cost, total cost |
| **User** | User ID, username, password hash, role |

📎 The full ER diagram is available in the [`docs/`](docs/) folder.

---

## 🛠️ Tech Stack

| Area | Technology |
|---|---|
| Language | Java (JDK 17 or higher) |
| Database | SQLite via JDBC |
| User interface | Java Swing *(update if you choose JavaFX or console)* |
| Build / IDE | IntelliJ IDEA / Eclipse / NetBeans |
| Version control | Git and GitHub |
| Diagrams | draw.io |

---

## 🚀 Getting Started

### Prerequisites
- JDK 17 or higher installed
- Git installed
- An IDE (IntelliJ IDEA, Eclipse, or NetBeans)
- SQLite JDBC driver (`sqlite-jdbc`)

### Installation

```bash
# 1. Clone the repository
git clone https://github.com/<your-username>/<repository-name>.git

# 2. Move into the project folder
cd <repository-name>

# 3. Switch to the development branch
git checkout develop
```

### Running the application

1. Open the project in your IDE.
2. Add the `sqlite-jdbc` library to the project classpath.
3. Run `Main.java` located in `com.clothingshop`.
4. On the first run, the database and default tables are created automatically.

### Default login (for testing only)

| Role | Username | Password |
|---|---|---|
| Owner | `owner` | `owner123` |
| Staff | `staff` | `staff123` |

> ⚠️ Change these credentials in a real deployment. Replace this table with your own seeded accounts.

---

## 👤 User Roles

| Feature | Owner | Staff |
|---|:---:|:---:|
| Billing and invoices | ✅ | ✅ |
| Product search and stock view | ✅ | ✅ |
| Add / update products | ✅ | Limited |
| Register stock and purchases | ✅ | ❌ |
| Employee and salary management | ✅ | ❌ |
| Expense recording | ✅ | ❌ |
| Profit and financial reports | ✅ | ❌ |
| Backup and restore | ✅ | ❌ |

> The final permissions will be confirmed with the client.

---

## 📁 File Handling

| Purpose | File type | Location |
|---|---|---|
| Invoices | `.txt` | `output/invoices/` |
| Report export | `.csv` | `output/reports/` |
| Error and activity log | `.log` | `logs/` |
| Application configuration | `.properties` | project root |
| Database backup | `.db` copy | `data/backups/` |

---

## ⚠️ Exception Handling

Custom exceptions keep error handling clear and consistent:

| Exception | When it is thrown |
|---|---|
| `InsufficientStockException` | Requested quantity exceeds available stock |
| `InvalidInputException` | Invalid, empty, or negative input values |
| `DuplicateRecordException` | A duplicate product ID, username, or similar |
| `RecordNotFoundException` | A requested record does not exist |
| `AuthenticationException` | Wrong username or password |
| `UnauthorizedAccessException` | A user tries to access a restricted function |

The application shows clear messages to the user and writes errors to the log file instead of crashing.

---

## 🌿 Git Workflow

### Branches

| Branch | Purpose |
|---|---|
| `main` | Stable, final version (protected) |
| `develop` | Integration of completed features |
| `feature/<name>` | Individual feature work (for example `feature/billing`) |

### Workflow

```bash
git checkout develop
git pull
git checkout -b feature/billing

# ...write code...

git add .
git commit -m "Add BillingService with wholesale/retail price selection"
git push -u origin feature/billing
```

Then open a **Pull Request** into `develop`, request a teammate's review, and merge after approval.

### Rules
- Never commit directly to `main` or `develop`.
- Pull from `develop` regularly to avoid conflicts.
- Write clear, descriptive commit messages.
- Link commits and pull requests to issues (for example `Closes #5`).

### Commit message examples

```
Add Product model with validation
Implement InsufficientStockException
Fix total calculation in BillingService
Add CSV export for monthly sales report
```

---

## 👥 Team Members

**Team CodeCrafters**

| Name | Student ID | GitHub | Responsibility |
|---|---|---|---|
| Member 1 | | @username | Product, Inventory and Purchase |
| Member 2 | | @username | Billing and B2B/B2C Pricing |
| Member 3 | | @username | Employee, Salary and Login |
| Member 4 | | @username | Expenses, Profit, Reports and Exceptions |
| Member 5 | | @username | Database layer, Git management and Documentation |

---

## ✅ Project Progress

- [x] Client interview and requirement gathering
- [x] Software Requirements Specification (SRS)
- [x] GitHub repository setup
- [ ] UML class diagram and ER diagram
- [ ] Custom exceptions and base classes
- [ ] Model classes
- [ ] Database and repository layer
- [ ] Product and inventory module
- [ ] Billing and pricing module
- [ ] Employee and salary module
- [ ] Expense and profit module
- [ ] Reports and file handling
- [ ] Login and role-based access
- [ ] Testing and bug fixing
- [ ] Presentation and viva preparation

---

## 📄 Documentation

- 📘 [Software Requirements Specification (SRS)](docs/SRS_Document.pdf)
- 📐 UML Class Diagram: `docs/` *(to be added)*
- 🗂️ ER Diagram: `docs/` *(to be added)*

---

## 🔮 Future Enhancements

- Dashboard with charts
- Automatic database backup scheduling
- Barcode scanning for products
- Attendance and leave management
- Mobile application
- Online ordering and payment integration
- Multi-branch support

---

## 🙏 Acknowledgements

- **Mr. Amila Buddika**, the client, for his time and valuable input during the requirement-gathering interview
- Lecturers and instructors of **IIC 1153 – Object Oriented Programming**, Faculty of Technology, University of Sri Jayewardenepura

---

## 📬 Submission

- **Course:** IIC 1153 – Object Oriented Programming
- **Deadline:** 19 November 2026
- **Code:** `GA_Codes_<GroupNo>.zip`
- **Presentation:** `GA_<GroupNo>.pptx`

---

<p align="center">Made with ☕ and Java by <b>Team CodeCrafters</b></p>

