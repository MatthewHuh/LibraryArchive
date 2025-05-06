package DAO;
import java.sql.SQLException;
import java.util.List;

public interface DAO <T>{
    T get(int id);

    List<T>  getAll();

    int insert(T t) throws SQLException;

    int update(T t);
}
