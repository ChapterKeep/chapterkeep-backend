package com.konkuk.chapterkeep.member.repository;

import com.konkuk.chapterkeep.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByName(String name);
    boolean existsByNickname(String nickname);
    Optional<Member> findByName(String name);
    void deleteByMemberId(Long id);

    @Query("SELECT m FROM Member m WHERE m.nickname LIKE %:nickname% AND m.visibility = true")
    List<Member> findByNicknameContainingAndVisibilityTrue(@Param("nickname") String nickname);


}
