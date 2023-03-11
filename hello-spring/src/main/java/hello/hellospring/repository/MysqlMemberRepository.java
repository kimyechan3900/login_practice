package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class MysqlMemberRepository implements MemberRepository{

    private static Connection conn;
    @Autowired
    MysqlMemberRepository()  {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn= DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/yechan","root","1460"
            );
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Member save(Member member) {
        ResultSet rs;
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(
                    "INSERT INTO table_demo2(seq,name) values(?, ?)"
            );
            rs=ps.executeQuery("SELECT * FROM table_demo2");
            ps.setString(1,Integer.toString(rs.getRow()));
            ps.setString(2,member.getName());
            ps.executeUpdate();
            ps.close();
            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return member;
    }

    @Override
    public Optional<Member> findById(Long id)  {
        ResultSet rs;
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("SELECT * FROM table_demo2 WHERE seq=?");
            ps.setString(1,Integer.toString(Math.toIntExact(id)));
            rs=ps.executeQuery();
            rs.next();

            if(rs.getRow()!=0) {
                Member member=new Member();
                member.setId(rs.getLong("seq"));
                member.setName(rs.getString("name"));
                return Optional.ofNullable(member);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Optional<Member> findByName(String name)  {
        ResultSet rs;
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("SELECT * FROM table_demo2 WHERE name= ?");
            ps.setString(1,name);
            rs=ps.executeQuery();
            rs.next();
            if(rs.getRow()!=0) {
                Member member=new Member();
                member.setId(rs.getLong("seq"));
                member.setName(rs.getString("name"));
                return Optional.ofNullable(member);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;

    }

    @Override
    public List<Member> findAll()  {
        ResultSet rs;
        PreparedStatement ps = null;
        ArrayList<Member> memberlist=new ArrayList<>();
        try {
            ps = conn.prepareStatement("SELECT * FROM table_demo2");
            rs=ps.executeQuery();

            while(rs.next()){
                Member member=new Member();
                member.setId(rs.getLong("seq"));
                member.setName(rs.getString("name"));
                memberlist.add(member);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return memberlist;
    }

    @Override
    public void removeName(Member member) {
        PreparedStatement ps=null;
        try {
            ps=conn.prepareStatement("DELETE FROM table_demo2 WHERE name = ?");
            ps.setString(1,member.getName());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }



    }

    @Override
    public void changeName(Member member, String name) {
        ResultSet rs;
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("SELECT * FROM table_demo2 WHERE name= ?");
            ps.setString(1,member.getName());
            rs=ps.executeQuery();
            rs.next();
            if(rs.getRow()!=0) {
                ps=conn.prepareStatement("UPDATE table_demo2 SET name= ? WHERE seq = ?");
                ps.setString(1,name);
                ps.setString(2,Long.toString(rs.getLong("seq")));
                System.out.println(name+" "+rs.getLong("seq"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
