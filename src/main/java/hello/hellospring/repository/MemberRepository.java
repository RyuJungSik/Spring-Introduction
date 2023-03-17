package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface MemberRepository {
Member save(Member member);

Optional<Member> findById(Long id);

Optional<Member> findByName(String name);

List<Member> findAll();
}
