package DAO;


import POJO.BorrowDisplayObject;
import POJO.BorrowRecord;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;


public class BorrowRecordDAO implements DAO<BorrowRecord> {

    HikariDataSource ds = DBConnectionPool.getDataSource();

    @Override
    public BorrowRecord get(int id)  {
        //initialize variables
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        BorrowRecord borrowRecord = null;

        try{
            //get connection
            connection = ds.getConnection();

            //prepare statement
            String query = "SELECT * FROM borrow_record WHERE borrow_record_id=?";
            ps = connection.prepareStatement(query);
            ps.setInt(1, id);

            //execute
            rs = ps.executeQuery();

            //return results
            if(rs.next()){
                int borrow_record_id = rs.getInt("borrow_record_id");
                int member_id = rs.getInt("member_id");
                int book_id = rs.getInt("book_id");
                Date return_date = rs.getDate("return_date");
                boolean  is_returned = rs.getBoolean("is_returned");

                borrowRecord = new BorrowRecord(borrow_record_id, member_id, book_id, return_date, is_returned);
                return borrowRecord;
            }
        }
        catch(SQLException e){
            DAO.logSQLException(e, "Borrow Record DAO");
        }
        finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (connection != null) connection.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return borrowRecord;
    }

    public List<BorrowRecord> getMemberBorrow(int id) {
        //initialize variables
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List<BorrowRecord> borrowRecords = new ArrayList<>();


        try{
            //get connection
            connection = ds.getConnection();
            //prepare statement
            String query = "SELECT * FROM borrow_record WHERE member_id=?";
            ps = connection.prepareStatement(query);
            ps.setInt(1, id);
            //execute query
            rs = ps.executeQuery();

            while(rs.next()){
                int borrow_record_id = rs.getInt("borrow_record_id");
                int member_id = rs.getInt("member_id");
                int book_id = rs.getInt("book_id");
                Date return_date = rs.getDate("return_date");
                boolean  is_returned = rs.getBoolean("is_returned");
                borrowRecords.add(new BorrowRecord(borrow_record_id, member_id, book_id, return_date, is_returned));

            }
        }
        catch (SQLException e){
            DAO.logSQLException(e, "Borrow Record DAO");
        }
        finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (connection != null) connection.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return borrowRecords;
    }

    @Override
    public List<BorrowRecord> getAll() {
        //initialize variables
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List<BorrowRecord> borrowRecords = new ArrayList<>();


        try{
            //get connection
            connection = ds.getConnection();
            //prepare statement
            String query = "SELECT * FROM borrow_record";
            ps = connection.prepareStatement(query);
            //execute query
            rs = ps.executeQuery();

            while(rs.next()){
                int borrow_record_id = rs.getInt("borrow_record_id");
                int member_id = rs.getInt("member_id");
                int book_id = rs.getInt("book_id");
                Date return_date = rs.getDate("return_date");
                boolean  is_returned = rs.getBoolean("is_returned");
                borrowRecords.add(new BorrowRecord(borrow_record_id, member_id, book_id, return_date, is_returned));

            }
        }
        catch (SQLException e){
            DAO.logSQLException(e, "Borrow Record DAO");
        }
        finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (connection != null) connection.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return borrowRecords;
    }

    //insert returns number of rows changed
    @Override
    public int insert(BorrowRecord borrowRecord) {
        Connection connection = null;
        PreparedStatement ps = null;
        int rs = 0;

        try{
            //get connection
            connection = ds.getConnection();
            //prepare statement
            String query = "INSERT INTO borrow_record (member_id, book_id, return_date) VALUES (?, ?, ?)";
            ps = connection.prepareStatement(query);
            ps.setInt(1, borrowRecord.getMemberID());
            ps.setInt(2, borrowRecord.getBookID());
            ps.setDate(3, borrowRecord.getReturnDate());

            //execute update
            rs = ps.executeUpdate();
            return rs;
        }
        catch (SQLException e){
            DAO.logSQLException(e, "Borrow Record DAO");
        }
        finally {
            try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (connection != null) connection.close(); } catch (SQLException e) { e.printStackTrace(); }
        }

        return rs;
    }

    @Override
    public int update(BorrowRecord borrowRecord) {
        //initialize variables
        Connection connection = null;
        PreparedStatement ps = null;
        int rs = 0;

        try{
            //get connection
            connection = ds.getConnection();

            //prepare statement
            String query = "UPDATE borrow_record SET member_id = ?, book_id = ?, return_date = ?, is_returned = ? WHERE borrow_record_id = ?";
            ps = connection.prepareStatement(query);
            ps.setInt(1, borrowRecord.getMemberID());
            ps.setInt(2, borrowRecord.getBookID());
            ps.setDate(3, borrowRecord.getReturnDate());
            ps.setBoolean(4, borrowRecord.isReturned());
            ps.setInt(5, borrowRecord.getBorrowRecordID());

            //execute update
            rs = ps.executeUpdate();

            return rs;
        }
        catch (SQLException e){
            DAO.logSQLException(e, "Borrow Record DAO");
        }
        finally {
            try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (connection != null) connection.close(); } catch (SQLException e) { e.printStackTrace(); }
        }

        return rs;
    }

    //Used for displaying all information related to a book
    public List<BorrowDisplayObject> getBorrowDisplayObject(String isbn) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List<BorrowDisplayObject> borrowDisplayObjectList = new ArrayList<>();

        try{
            //get connection
            connection = ds.getConnection();
            //prepare statement
            String query = "SELECT b.book_id, b.is_available, bi.title, l.name as library_name, l.address as library_address FROM book_info bi INNER JOIN books b ON bi.isbn = b.isbn INNER JOIN libraries l ON b.library_id = l.library_id WHERE bi.isbn = ? ";
            ps = connection.prepareStatement(query);
            ps.setString(1, isbn);
            //execute query
            rs = ps.executeQuery();

            while(rs.next()){

                int book_id = rs.getInt("book_id");
                String title = rs.getString("title");
                String library_name = rs.getString("library_name");
                String library_address = rs.getString("library_address");
                boolean is_available = rs.getBoolean("is_available");
                //return date is just two weeks from now
                LocalDate futureDate = LocalDate.now().plusWeeks(2);;
                Date returnDate = Date.valueOf(futureDate);
                borrowDisplayObjectList.add(new BorrowDisplayObject(book_id, title, isbn, is_available, returnDate, library_name, library_address));
            }

        }
        catch (SQLException e){
            DAO.logSQLException(e, "Borrow Record DAO");
        }
        finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (connection != null) connection.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return borrowDisplayObjectList;
    }

    public boolean hasActiveBorrowRecord(int id) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try{
            //get connection
            connection = ds.getConnection();

            //prepare statement
            String query = "Select * FROM borrow_record WHERE member_id = ? AND is_returned = 0";
            ps = connection.prepareStatement(query);
            ps.setInt(1, id);

            //execute query
            rs = ps.executeQuery();

            if(rs.next()){
                return true;
            }
            return false;

        } catch (SQLException e) {
            DAO.logSQLException(e, "Borrow Record DAO");
        }
        finally {
            //close
            try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (connection != null) connection.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return false;
    }



}
