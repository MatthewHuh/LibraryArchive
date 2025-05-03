package DAO;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;
import java.util.List;

public class BookDAO implements DAO {
    HikariDataSource ds = DBConnectionPool.getDataSource();


    @Override
    public Object get(int id)  {
        Connection connection = ds.getConnection();

        String query = "SELECT * FROM books WHERE id = ?";
        PreparedStatement prpStmt = connection.prepareStatement(query);
        prpStmt.setInt(1, id);
        ResultSet rs = prpStmt.executeQuery();
        return null;
    }

    @Override
    public List getAll() {
        return List.of();
    }

    @Override
    public int update(Object o) {
        return 0;
    }

    public int markInactive(int id) {
        return 0;
    }
}
