package DAO;

import POJO.Book;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;

public class BookDAO implements DAO<Book> {

    HikariDataSource ds = DBConnectionPool.getDataSource();

    @Override
    public Book get(int id)  {
        //initialize variables
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        Book book = null;

        try{
            //get connection
            connection = ds.getConnection();

            //prepare statement
            String query = "SELECT * FROM books WHERE book_id = ?";
            ps = connection.prepareStatement(query);
            ps.setInt(1, id);

            //execute
            rs = ps.executeQuery();

            //return results
            if(rs.next()){
                int book_id = rs.getInt("book_id");
                String book_info_id = rs.getString("isbn");
                int library_id = rs.getInt("library_id");
                boolean is_available = rs.getBoolean("is_available");
                book = new Book(book_id, book_info_id, library_id,  is_available);
                return book;
            }
        }
        catch(SQLException e){
            DAO.logSQLException(e, "BookDAO");
        }
        finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (connection != null) connection.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return book;
    }

    public boolean hasAvailableCopies(String isbn) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean available = false;

        try {
            connection = ds.getConnection();
            String query =
                    "SELECT COUNT(*) " +
                            "  FROM books " +
                            " WHERE isbn = ? " +
                            "   AND is_available = 1";
            ps = connection.prepareStatement(query);
            ps.setString(1, isbn);

            rs = ps.executeQuery();
            if (rs.next()) {
                available = rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            DAO.logSQLException(e, "BookDAO");
        } finally {
            try { if (rs != null)         rs.close();       } catch (SQLException e) { e.printStackTrace(); }
            try { if (ps != null)         ps.close();       } catch (SQLException e) { e.printStackTrace(); }
            try { if (connection != null) connection.close(); } catch (SQLException e) { e.printStackTrace(); }
        }

        return available;
    }

    @Override
    public List<Book> getAll() {
        //initialize variables
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List<Book> books = new ArrayList<>();


        try{
            //get connection
            connection = ds.getConnection();
            //prepare statement
            String query = "SELECT * FROM books";
            ps = connection.prepareStatement(query);
            //execute query
            rs = ps.executeQuery();

            while(rs.next()){
                int book_id = rs.getInt("book_id");
                String book_info_id = rs.getString("isbn");
                int library_id = rs.getInt("library_id");
                boolean is_available = rs.getBoolean("is_available");
                books.add(new Book(book_id, book_info_id, library_id, is_available));
            }
        }
        catch (SQLException e){
            DAO.logSQLException(e, "BookDAO");
        }
        finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (connection != null) connection.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return books;
    }
    //insert returns number of rows changed
    @Override
    public int insert(Book book) {
        Connection connection = null;
        PreparedStatement ps = null;
        int rs = 0;

        try{
            //get connection
            connection = ds.getConnection();

            //prepare statement
            String query = "INSERT INTO books (isbn, library_id) VALUES (?, ?)";
            ps = connection.prepareStatement(query);
            ps.setString(1, book.getBookInfoID());
            ps.setInt(2, book.getLibraryID());

            //execute update
            rs = ps.executeUpdate();
            return rs;
        }
        catch (SQLException e){
            DAO.logSQLException(e, "BookDAO");
        }
        finally {
            try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (connection != null) connection.close(); } catch (SQLException e) { e.printStackTrace(); }
        }

        return rs;
    }

    @Override
    public int update(Book book) {
        //initialize variables
        Connection connection = null;
        PreparedStatement ps = null;
        int rs = 0;

        try{
            //get connection
            connection = ds.getConnection();

            //prepare statement
            String query = "UPDATE books SET isbn = ?, library_id = ? WHERE book_id = ?";
            ps = connection.prepareStatement(query);
            ps.setString(1, book.getBookInfoID());
            ps.setInt(2, book.getLibraryID());
            ps.setInt(3, book.getBookID());

            //execute update
            rs = ps.executeUpdate();

            return rs;
        }
        catch (SQLException e){
            DAO.logSQLException(e, "BookDAO");
        }
        finally {
            try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (connection != null) connection.close(); } catch (SQLException e) { e.printStackTrace(); }
        }

        return rs;
    }

    public int delete(int book_id) {
        Connection connection = null;
        PreparedStatement ps = null;
        int rs = 0;

        try{
            //get connection
            connection = ds.getConnection();

            //prepare statement
            String query = "DELETE FROM books WHERE book_id = ?";
            ps = connection.prepareStatement(query);
            ps.setInt(1, book_id);

            //execute update
            rs = ps.executeUpdate();
            return rs;
        }
        catch (SQLException e) {
            DAO.logSQLException(e, "BookDAO");
        }
        finally {
            try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (connection != null) connection.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return rs;
    }

}
