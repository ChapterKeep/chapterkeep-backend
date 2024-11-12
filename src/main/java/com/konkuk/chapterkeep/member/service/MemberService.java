package com.konkuk.chapterkeep.member.service;

import com.konkuk.chapterkeep.common.response.enums.Code;
import com.konkuk.chapterkeep.common.response.exception.GeneralException;
import com.konkuk.chapterkeep.domain.Member;
import com.konkuk.chapterkeep.domain.enums.Role;
import com.konkuk.chapterkeep.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    // 현재 인증된 사용자 Member 객체 반환
    private Member getCurrentMember() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByName(name);

        if (member == null) {
            throw new GeneralException(Code.MEMBER_NOT_FOUND, "현재 인증된 사용자가 없습니다.");
        }

        return member;
    }

    // 현재 인증된 사용자 ID를 반환
    public Long getCurrentMemberId() {
        return getCurrentMember().getMemberId();
    }

    // 현재 인증된 사용자 Role을 반환
    public Role getCurrentMemberRole() {
        return getCurrentMember().getRole();
    }

}
