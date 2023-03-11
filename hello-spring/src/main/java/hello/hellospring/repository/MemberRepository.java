package hello.hellospring.repository;

import hello.hellospring.domain.Member;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface MemberRepository {

    Member save(Member member) throws SQLException;
    Optional<Member> findById(Long id) throws SQLException;
    Optional<Member> findByName(String name) throws SQLException;
    List<Member> findAll() throws SQLException;

    void removeName(Member member);

    void changeName(Member member,String name);
}
