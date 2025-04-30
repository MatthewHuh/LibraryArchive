package DAO;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {

    private Connection connection;

    //constructor
    DBConnector() {
        Dotenv dotenv = Dotenv
                .configure()
                .ignoreIfMissing()     // won’t crash if no .env found
                .load();

        String host     = dotenv.get("DB_HOST");
        String port     = dotenv.get("DB_PORT");
        String name     = dotenv.get("DB_NAME");
        String user     = dotenv.get("DB_USER");
        String password = dotenv.get("DB_PASSWORD");

        String url = String.format(
                "jdbc:mysql://%s:%s/%s?useSSL=false&serverTimezone=UTC",
                host, port, name
        );

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url,user,password);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void close() {
        if (connection != null) {
            try{
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
