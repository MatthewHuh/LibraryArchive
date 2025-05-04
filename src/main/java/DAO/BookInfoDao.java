package DAO;

import POJO.Book;
import POJO.BookInfo;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;
import java.sql.Date;

public class BookInfoDao implements DAO<BookInfo> {

    HikariDataSource ds = DBConnectionPool.getDataSource();

    @Override
    public BookInfo get(int id)  {
        //initialize variables
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        BookInfo bookInfo = null;

        try{
            //get connection
            connection = ds.getConnection();

            //prepare statement
            String query = "SELECT * FROM book_info WHERE book_info_id = ?";
            ps = connection.prepareStatement(query);
            ps.setInt(1, id);

            //execute
            rs = ps.executeQuery();

            //return results
            if(rs.next()){
                int bookInfoId = rs.getInt("book_info_id");
                String author = rs.getString("author");
                String genre = rs.getString("genre");
                String title = rs.getString("title");
                Date releaseDate = rs.getDate("release_date");
                bookInfo = new BookInfo(bookInfoId, author, genre, title, releaseDate);
                return bookInfo;
            }
        }
        catch(SQLException e){
            e.printStackTrace(); //can have more robust logging
        }
        finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (connection != null) connection.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return bookInfo;
    }

    @Override
    public List<BookInfo> getAll() {
        //initialize variables
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List<BookInfo> bookInfoList = new ArrayList<>();


        try{
            //get connection
            connection = ds.getConnection();
            //prepare statement
            String query = "SELECT * FROM books";
            ps = connection.prepareStatement(query);
            //execute query
            rs = ps.executeQuery();

            while(rs.next()){
                int bookInfoId = rs.getInt("book_info_id");
                String author = rs.getString("author");
                String genre = rs.getString("genre");
                String title = rs.getString("title");
                Date releaseDate = rs.getDate("release_date");
                bookInfoList.add(new BookInfo(bookInfoId, author, genre, title, releaseDate));
            }
        }
        catch (SQLException e){
            e.printStackTrace(); //can have more robust logging
        }
        finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (connection != null) connection.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return bookInfoList;
    }
    //insert returns number of rows changed
    @Override
    public int insert(BookInfo bookInfo) {
        Connection connection = null;
        PreparedStatement ps = null;
        int rs = 0;

        try{
            //get connection
            connection = ds.getConnection();

            //prepare statement
            String query = "INSERT INTO book_info (author, genre, title, release_date) VALUES (?, ?, ?, ?)";
            ps = connection.prepareStatement(query);

            ps.setString(1, bookInfo.getAuthor());
            ps.setString(2, bookInfo.getGenre());
            ps.setString(3, bookInfo.getTitle());
            ps.setDate(4, bookInfo.getReleaseDate());

            //execute update
            rs = ps.executeUpdate();
            return rs;
        }
        catch (SQLException e){
            e.printStackTrace(); //can have more robust logging
        }
        finally {
            try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (connection != null) connection.close(); } catch (SQLException e) { e.printStackTrace(); }
        }

        return rs;
    }

    @Override
    public int update(BookInfo bookInfo) {
        //initialize variables
        Connection connection = null;
        PreparedStatement ps = null;
        int rs = 0;

        try{
            //get connection
            connection = ds.getConnection();

            //prepare statement
            String query = "UPDATE book_info SET author = ?, genre = ?, title = ?, release_date = ? WHERE book_info_id = ?";
            ps = connection.prepareStatement(query);
            ps.setString(1, bookInfo.getAuthor());
            ps.setString(2, bookInfo.getGenre());
            ps.setString(3, bookInfo.getTitle());
            ps.setDate(4, bookInfo.getReleaseDate());
            ps.setInt(5, bookInfo.getBookInfoID());

            //execute update
            rs = ps.executeUpdate();

            return rs;
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (connection != null) connection.close(); } catch (SQLException e) { e.printStackTrace(); }
        }

        return rs;
    }

}
