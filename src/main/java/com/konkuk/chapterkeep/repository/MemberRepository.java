package com.konkuk.chapterkeep.repository;

import com.konkuk.chapterkeep.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByName(String name);
    boolean existsByNickname(String nickname);


}
