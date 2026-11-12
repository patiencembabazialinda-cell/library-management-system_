# Library Management System - The Nook of Knowledge

A simple desktop application built with **Java Swing** for managing a small library: adding books, registering readers, and borrowing and returning books.

Developed for **CSC 1102: Structured and Object-Oriented Programming** (Practical Exam Project) by **Group 13**.

## Features
- Dashboard showing total, available and borrowed books
- Book table with ID, title, author and status
- Search books by ID, title or author
- Add books (unique ID assigned automatically)
- Register readers
- Borrow and return books
- Input validation with clear messages

## Classes
| Class | Purpose |
|---|---|
| `Book` | Represents a book (ID, title, author, availability) |
| `Reader` | Represents a library member |
| `Library` | Stores `Book[]` and `Reader[]` arrays and handles the logic |
| `LibraryGUI` | Swing interface and the `main` method |

## How to Run
Requires the Java Development Kit (JDK) 17 or newer.

```
javac *.java
java LibraryGUI
```

## Group Members
- Alinda Patience Mbabazi - 26/U/03481/PSA
- Ezati Mary Immaculate - 26/U/03504/PS
- Tebandeke Allan - 26/U/3656/PS
- Aratusinguza Jacob - 26/U/03490/PS
- Wasike Emmanuel Erisa - 26/U/0318
