package com.konkuk.chapterkeep.security;

import com.konkuk.chapterkeep.member.dto.CustomMemberDetails;
import com.konkuk.chapterkeep.member.service.CustomMemberDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
@AllArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final CustomMemberDetailsService customMemberDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String rawPassword = authentication.getCredentials().toString();

        // 아이디 확인
        CustomMemberDetails userDetails;
        try {
            userDetails = (CustomMemberDetails) customMemberDetailsService.loadUserByUsername(username);
            if (userDetails == null) {
                throw new AuthenticationException("해당 아이디를 가진 회원을 찾을 수 없습니다.") {};
            }
        } catch (UsernameNotFoundException ex) {
            throw new AuthenticationException("해당 아이디를 가진 회원을 찾을 수 없습니다.") {};
        }


        // 비밀번호 확인
        if (!passwordEncoder.matches(rawPassword, userDetails.getPassword())) {
            throw new AuthenticationException("비밀번호가 잘못되었습니다") {};
        }

        // 인증 성공 시 Authentication 객체 반환
        return new UsernamePasswordAuthenticationToken(userDetails, rawPassword, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
