package DAO;


import POJO.Library;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;

public class LibraryDao implements DAO<Library> {

    HikariDataSource ds = DBConnectionPool.getDataSource();

    @Override
    public Library get(int id)  {
        //initialize variables
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        Library library = null;

        try{
            //get connection
            connection = ds.getConnection();

            //prepare statement
            String query = "SELECT * FROM Libraries WHERE library_id = ?";
            ps = connection.prepareStatement(query);
            ps.setInt(1, id);

            //execute
            rs = ps.executeQuery();

            //return results
            if(rs.next()){
                int library_id = rs.getInt("library_id");
                String Address = rs.getString("Address");
                String Name = rs.getString("libraryName");
                library = new Library(library_id, Address, Name);
                return library;
            }
        }
        catch(SQLException e){
            DAO.logSQLException(e, "LibraryDAO");
        }
        finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (connection != null) connection.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return library;
    }

    @Override
    public List<Library> getAll() {
        //initialize variables
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List<Library> libraries = new ArrayList<>();


        try{
            //get connection
            connection = ds.getConnection();
            //prepare statement
            String query = "SELECT * FROM libraries";
            ps = connection.prepareStatement(query);
            //execute query
            rs = ps.executeQuery();

            while(rs.next()){
                int library_id = rs.getInt("library_id");
                String Address = rs.getString("Address");
                String Name = rs.getString("name");
                libraries.add(new Library(library_id, Address, Name));
            }
        }
        catch (SQLException e){
            DAO.logSQLException(e, "LibraryDAO");
        }
        finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (connection != null) connection.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return libraries;
    }

    //insert returns number of rows changed
    @Override
    public int insert(Library library) {
        Connection connection = null;
        PreparedStatement ps = null;
        int rs = 0;

        try{
            //get connection
            connection = ds.getConnection();
            //prepare statement
            String query = "INSERT INTO Libraries (address, name) VALUES (?, ?)";
            ps = connection.prepareStatement(query);
            ps.setString(1, library.getAddress());
            ps.setString(2, library.getName());

            //execute update
            rs = ps.executeUpdate();
            return rs;
        }
        catch (SQLException e){
            DAO.logSQLException(e, "LibraryDAO");
        }
        finally {
            try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (connection != null) connection.close(); } catch (SQLException e) { e.printStackTrace(); }
        }

        return rs;
    }

    @Override
    public int update(Library library) {
        //initialize variables
        Connection connection = null;
        PreparedStatement ps = null;
        int rs = 0;

        try{
            //get connection
            connection = ds.getConnection();

            //prepare statement
            String query = "UPDATE libraries SET address = ?, name = ? WHERE library_id = ?";
            ps = connection.prepareStatement(query);
            ps.setString(1, library.getAddress());
            ps.setString(2, library.getName());
            ps.setInt(3, library.getLibraryID());

            //execute update
            rs = ps.executeUpdate();

            return rs;
        }
        catch (SQLException e){
            DAO.logSQLException(e, "LibraryDAO");
        }
        finally {
            try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (connection != null) connection.close(); } catch (SQLException e) { e.printStackTrace(); }
        }

        return rs;
    }

}
