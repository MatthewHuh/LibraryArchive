package DAO;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public interface DAO <T>{


    T get(int id);

    List<T>  getAll();

    int insert(T t);

    int update(T t);

    static void logSQLException(SQLException e, String className) {
        Logger logger = Logger.getLogger(className);
        logger.log(Level.SEVERE, "SQLException in " + className + ": " + e.getMessage(), e);
    }
}
