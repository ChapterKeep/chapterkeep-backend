package com.konkuk.chapterkeep.member.service;

import com.konkuk.chapterkeep.domain.Member;
import com.konkuk.chapterkeep.member.dto.CustomMemberDetails;
import com.konkuk.chapterkeep.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CustomMemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Member member = memberRepository.findByName(username);

        if(member != null){
            return new CustomMemberDetails(member);
        }

        return null;
    }
}