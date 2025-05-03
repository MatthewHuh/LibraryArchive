package DAO;

import POJO.Book;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;
import java.util.List;
import java.sql.SQLException;

public class BookDAO implements DAO {
    HikariDataSource ds = DBConnectionPool.getDataSource();


    @Override
    public Object get(int id)  {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try{
            connection = ds.getConnection();
            String query = "SELECT * FROM books WHERE id = ?";
            ps = connection.prepareStatement(query);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if(rs.next()){
                Book book = new Book(rs.getInt("book_id"), rs.getInt("book_info_id"), rs.getInt("library_id"));
                return book;
            }
        }
        catch(SQLException e){
            e.printStackTrace(); //can have more robust logging
        }
        finally {
            try { rs.close(); } catch (Exception e) { /* Ignored */ }
            try { ps.close(); } catch (Exception e) { /* Ignored */ }
            try { connection.close(); } catch (Exception e) { /* Ignored */ }
        }
        return null;
    }

    @Override
    public List getAll() {
        return List.of();
    }

    @Override
    public int insert(Object o) {
        return 0;
    }

    @Override
    public int update(Object o) {
        return 0;
    }

    public int markInactive(int id) {
        return 0;
    }
}
