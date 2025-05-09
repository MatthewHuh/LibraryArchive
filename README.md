Set Up Instructions
## 🚀 Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

- **Java 17+** JDK  
- **Maven** (3.6+)  
- **MySQL 8+** (or compatible)  
- **SceneBuilder** (optional, for editing FXML)  

### 1. Clone the repository

bash:
git clone https://github.com/MatthewHuh/LibraryArchive.git
cd LibraryArchive


Project Code Submission
1. Code Organization

Presentation Layer:

/src/main/java/controller Contains Controllers
/src/main/resources/view Contains FXML files

Database Layer:

/src/main/MySQL/Schema.sql Contains the SQL script that can be used to recreate the database schema
/src/main/test/Initialize Java script used to initalize the database with sample data

Business Layer:

/src/main/java/DAO contains Database Access Objects
/src/main/java/DAO/DBConnectionPool Used to connect the application to the database.
/src/main/java/POJO contains plain old java objects used by the DAOs

2. JDBC code

All JDBC code can be found within the DAO directory
/src/main/java/DAO 

3. SQL Script For Database Creation

/src/main/MySQL/Schema.sql 

4. Database Initialization Script

/src/main/test/Initialize Java script used to initalize the database with sample data

5. Error Handling and Comments:

Error's are logged to console. 
/src/main/java/DAO/DAO contains the utility function for handling database errors.
/src/main/java/DAO/DBConnectionPool Checks if a connection was succesfully established with MySQL.

6. Permissions And Path



