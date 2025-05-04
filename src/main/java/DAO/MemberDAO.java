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

        Book member = null;


    }

    @Override
    public List<Member> getAll() {
        return List.of();
    }

    @Override
    public int insert(Member member) {
        return 0;
    }

    @Override
    public int update(Member member) {
        return 0;
    }
}
