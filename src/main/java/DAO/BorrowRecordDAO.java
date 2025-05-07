package DAO;


import POJO.BorrowDisplayObject;
import POJO.BorrowRecord;
import POJO.Library;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;
import java.sql.SQLException;

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
                int late_fee = rs.getInt("late_fee");
                Date return_date = rs.getDate("return_date");

                borrowRecord = new BorrowRecord(borrow_record_id, member_id, book_id, late_fee, return_date);
                return borrowRecord;
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
        return borrowRecord;
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
                int late_fee = rs.getInt("late_fee");
                Date return_date = rs.getDate("return_date");
                borrowRecords.add(new BorrowRecord(borrow_record_id, member_id, book_id, late_fee, return_date ));

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
            String query = "INSERT INTO borrow_record (member_id, book_id, late_fee, return_date) VALUES (?, ?, ?, ?)";
            ps = connection.prepareStatement(query);
            ps.setInt(1, borrowRecord.getMemberID());
            ps.setInt(2, borrowRecord.getBookID());
            ps.setInt(3, borrowRecord.getLateFee());
            ps.setDate(4, borrowRecord.getReturnDate());

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
    public int update(BorrowRecord borrowRecord) {
        //initialize variables
        Connection connection = null;
        PreparedStatement ps = null;
        int rs = 0;

        try{
            //get connection
            connection = ds.getConnection();

            //prepare statement
            String query = "UPDATE borrow_record SET member_id = ?, book_id = ?, late_fee = ?, return_date = ? WHERE library_id = ?";
            ps = connection.prepareStatement(query);
            ps.setInt(1, borrowRecord.getMemberID());
            ps.setInt(2, borrowRecord.getBookID());
            ps.setInt(3, borrowRecord.getLateFee());
            ps.setDate(4, borrowRecord.getReturnDate());

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

    public List<BorrowDisplayObject> getBorrowDisplayObject(String isbn) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List<BorrowDisplayObject> borrowDisplayObjectList = new ArrayList<>();

        try{
            //get connection
            connection = ds.getConnection();
            //prepare statement
            String query = "SELECT book_id, title, return_date, name as library_name, address as library_address FROM book_info INNER JOIN books on book_info.isbn = books.isbn INNER JOIN books on library.library_id = books.library_id WHERE book_info.isbn = ?";
            ps = connection.prepareStatement(query);
            ps.setString(1, isbn);
            //execute query
            rs = ps.executeQuery();

            while(rs.next()){

                int book_id = rs.getInt("book_id");
                String title = rs.getString("title");
                Date return_date = rs.getDate("return_date");
                String library_name = rs.getString("library_name");
                String library_address = rs.getString("library_address");

                borrowDisplayObjectList.add(new BorrowDisplayObject(book_id, title, isbn, true, return_date, library_name, library_address));
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
        return borrowDisplayObjectList;
    }



}
