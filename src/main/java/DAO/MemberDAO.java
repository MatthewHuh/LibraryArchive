package DAO;

import POJO.Book;
import POJO.Member;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;


public class MemberDAO implements DAO<Member> {

    HikariDataSource ds = DBConnectionPool.getDataSource();

    @Override
    public Member get(int id) {
        //initialize variables
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        Member member = null;

        try{
            //get connection
            connection = ds.getConnection();

            //prepare statement
            String query = "SELECT * FROM members WHERE id = ?";
            ps = connection.prepareStatement(query);
            ps.setInt(1, id);

            //execute
            rs = ps.executeQuery();

            //return results
            if(rs.next()){
                String first_name = rs.getString("first_name");
                String last_name = rs.getString("last_name");
                String phone_number = rs.getString("phone_number");
                String email = rs.getString("email");
                String address = rs.getString("address");
                String password = ""; // don't return password
                member = new Member(id, first_name, last_name, phone_number, email, address, password);
                return member;
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (connection != null) connection.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return member;
    }

    @Override
    public List<Member> getAll() {
        //initialize variables
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List<Member> members = new ArrayList<>();

        try{
            //get connection
            connection = ds.getConnection();
            //prepare statement
            String query = "SELECT * FROM members";
            ps = connection.prepareStatement(query);
            //execute query
            rs = ps.executeQuery();

            while(rs.next()){
                int member_id = rs.getInt("member_id");
                String first_name = rs.getString("first_name");
                String last_name = rs.getString("last_name");
                String phone_number = rs.getString("phone_number");
                String email = rs.getString("email");
                String address = rs.getString("address");
                String password = ""; // don't return password

                members.add(new Member(member_id, first_name, last_name, phone_number, email, address, password));
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (connection != null) connection.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return members;
    }

    @Override
    public int insert(Member member) {
        Connection connection = null;
        PreparedStatement ps = null;
        int rs = 0;

        try{
            //get connection
            connection = ds.getConnection();

            //prepare statement
            String query = "insert into members (first_name, last_name, phone_number, email, address, hashed_password) values(?, ?, ?, ?, ?, ?)";
            ps = connection.prepareStatement(query);
            ps.setString(1, member.getFirstName());
            ps.setString(2, member.getLastName());
            ps.setString(3, member.getPhoneNumber());
            ps.setString(4, member.getEmail());
            ps.setString(5, member.getAddress());
            ps.setString(6, member.getHashedPassword());

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

    @Override
    public int update(Member member) {
        //initialize variables
        Connection connection = null;
        PreparedStatement ps = null;
        int rs = 0;

        try{
            //get connection
            connection = ds.getConnection();

            //prepare statement
            String query = "UPDATE books SET first_name = ?, last_name = ?, phone_number = ?, email = ?, address = ?, hashed_password WHERE id = ?";
            ps = connection.prepareStatement(query);
            ps.setString(1, member.getFirstName());
            ps.setString(2, member.getLastName());
            ps.setString(3, member.getPhoneNumber());
            ps.setString(4, member.getEmail());
            ps.setString(5, member.getAddress());
            ps.setString(6, member.getHashedPassword());
            ps.setInt(7, member.getMemberId());

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
