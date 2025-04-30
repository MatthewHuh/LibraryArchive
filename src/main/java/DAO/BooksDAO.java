package DAO;

import java.sql.SQLException;
import java.util.List;

public class BooksDAO implements DAO {

    @Override
    public Object get(int id) throws SQLException {
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
