package com.konkuk.chapterkeep.member.controller;

import com.konkuk.chapterkeep.common.response.dto.DataResponseDto;
import com.konkuk.chapterkeep.common.response.enums.Code;
import com.konkuk.chapterkeep.member.service.SignUpService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Iterator;

@RestController
@RequiredArgsConstructor
public class MemberController {

    @GetMapping("/id-test")  // 임시 로그인 테스트 api
    public DataResponseDto<String> idTest() {
        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        return new DataResponseDto<>(id, Code.OK,"사용자 아이디 불러오기 성공");
    }
    @GetMapping("/role-test")  // 임시 로그인 테스트 api
    public DataResponseDto<String> roleTest() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iter = authorities.iterator();
        GrantedAuthority auth = iter.next();
        String role = auth.getAuthority();

        return new DataResponseDto<>(role, Code.OK,"사용자 role 불러오기 성공");
    }


}
