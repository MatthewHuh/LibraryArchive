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
/src/main/java/POJO
