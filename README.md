## Project Overview

The **Library Management System** is a desktop application built with JavaFX, JDBC, and MySQL to members to borrow return book online. It follows a three-tier architecture:

1. **Presentation Layer**  
   - JavaFX & FXML for a responsive, form-driven UI  
   - Inline validation and user feedback via CSS-styled error labels  
2. **Logic Layer**  
   - DAO pattern with plain JDBC and HikariCP connection pooling  
   - Business rules (e.g., borrowing limits, late-fee calculations, BCrypt password hashing) encapsulated in service and DAO classes  
3. **Data Layer**  
   - MySQL schema normalized to BCNF, with tables for `book_info`, `books`, `members`, `libraries`, and `borrow_record`  
   - Referential integrity enforced by foreign keys  

Key features include:

- **Secure Authentication**: Sign up and log in with BCrypt-hashed passwords  
- **Book Catalog**: Browse “New Arrivals” and “Recommended” sections, search by title, author, ISBN, genre, or year  
- **CRUD Operations**: Add, edit, and delete members, books, and borrow records via intuitive JavaFX forms  
- **Borrow/Return Workflow**: Real-time availability toggling, due-date reminders, late-fee computation, and return processing  
- **Admin Dashboard**: Declare new book titles and allocate copies to library branches  










Set Up Instructions
## 🚀 Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

- **Open JDK 24** https://jdk.java.net/24/
  - Download the Open JDK Build for your os then extract files and navigate to bin directory and add that path to your path enviroment variable
- **JavaFX SDK** https://jdk.java.net/javafx24/
  - Download the Java FX SDK Build for your os then extract the files navigate to the lib directory and copy and save that path
- **Maven** (3.6+)  https://maven.apache.org/download.cgi
- **MySQL 8+* (or compatible) https://dev.mysql.com/downloads/
  - Download MySQL shell and server then make sure bin directory for both are added to the path eniroment variable
- IntelliJ IDEA https://www.jetbrains.com/idea/download/?section=windows

### 1. Clone the repository

```bash
git clone git@github.com:MatthewHuh/LibraryArchive.git
cd LibraryArchive
```

### 2. Configure .env file

create a file named .env in the root directory of the project.

copy and fill in using your information
```bash
DB_HOST=localhost
DB_PORT=3306
DB_NAME=LibraryArchive
DB_USER=root
DB_PASSWORD=your_mysql_password
```

### 3. Initialize the Database

Run the Schema.sql script (found in src/MySQL/)

open a terminal. Type
```bash
mysql -u root -p
```
then enter your password when propmted

then copy the text in the Schema.sql file and paste it in the terminal and press enter

The database and tables should be created

use the following sql commands to verify the database and tables were created
```bash
use libraryarchive;
show tables;
```

### 4. Seed dummy data

Run the Initialize.java class in src/main/java Using IntelliJ

### 5. Build & Run
Open the cloned repository as a project in IntelliJ

right click on the root directory of the project and select "open module settings"
![image](https://github.com/user-attachments/assets/be0b0f53-8e74-4411-b33a-8fa91541b8ed)

then select libraries from the Project Settings list
![image](https://github.com/user-attachments/assets/cad60f77-50b9-45a7-bb91-cded41a14ee1)


click on the plus in the top right corner and select java
![image](https://github.com/user-attachments/assets/2ce2e3bd-3a08-4749-b3a6-40246036886f)


navigate to the lib folder of your JavaFX SDK using the path that you saved

press select folders the apply then click ok
![image](https://github.com/user-attachments/assets/97d812b8-e2ea-425e-a712-7762d98abdc8)


then open Main.java in intelliJ and next to the debug button press the arrow and select edit configurations
![image](https://github.com/user-attachments/assets/ba272778-f6d4-44a9-8440-eb359511c52a)


the click on the plus icon and select application
![image](https://github.com/user-attachments/assets/bbd3edcc-fbd7-4604-9c06-71ba462aa589)

in the name field type "Main" in the "Build and run" click on the "modify options" and select add vm options
![image](https://github.com/user-attachments/assets/c18315f5-b705-411b-9c24-3e74b80f90b5)

in the vm options paste the following replacing the path with your path to your JavaFX SDK lib folder

```bash
--module-path <path-to-FX> --add-modules javafx.controls,javafx.fxml --enable-native-access=javafx.graphics
```
![image](https://github.com/user-attachments/assets/b79b0e48-ac84-4b6a-b262-f1d95315c7c3)

in the main class section type Main
![image](https://github.com/user-attachments/assets/081d4ae6-f504-4011-b910-01f8241cbea2)

then click apply and ok

then run the application using the run configuration and clicing the green run arrow.
![image](https://github.com/user-attachments/assets/f5b049ca-e387-4e58-a2f5-ad6e1d3b9555)



