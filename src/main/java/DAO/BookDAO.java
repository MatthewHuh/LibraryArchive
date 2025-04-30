package DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class BookDAO implements DAO {

    @Override
    public Object get(int id) throws SQLException {
        DBConnector db = new DBConnector();
        Connection con = db.getConnection();
        Statement stmt = con.createStatement();
        
        ResultSet rs = stmt.executeQuery("SELECT * FROM book WHERE id = " + id);
        return null;
    }

    @Override
    public List getAll() throws SQLException {
        return List.of();
    }

    @Override
    public int save(Object o) throws SQLException {
        return 0;
    }

    @Override
    public int insert(Object o) throws SQLException {
        return 0;
    }

    @Override
    public int update(Object o) throws SQLException {
        return 0;
    }

    @Override
    public int markInactive(int id) throws SQLException {
        return 0;
    }
}
