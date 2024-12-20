package com.konkuk.chapterkeep.member.repository;

import com.konkuk.chapterkeep.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByName(String name);
    boolean existsByNickname(String nickname);
    Optional<Member> findByName(String name);
    void deleteByMemberId(Long id);

}
