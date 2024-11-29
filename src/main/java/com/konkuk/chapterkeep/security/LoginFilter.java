package com.konkuk.chapterkeep.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.konkuk.chapterkeep.common.response.dto.DataResponseDto;
import com.konkuk.chapterkeep.common.response.dto.ErrorResponseDto;
import com.konkuk.chapterkeep.common.response.exception.GeneralException;
import com.konkuk.chapterkeep.member.dto.LoginReqDto;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StreamUtils;
import com.konkuk.chapterkeep.common.response.enums.Code;
import jakarta.servlet.FilterChain;



import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    // 검증을 담당하는 메서드
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {


        try {
            // ObjectMapper를 사용해 JSON 데이터를 Java 객체로 변환 후 dto에 저장
            ObjectMapper objectMapper = new ObjectMapper();
            ServletInputStream inputStream = request.getInputStream();
            String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

            LoginReqDto loginReqDto = objectMapper.readValue(messageBody, LoginReqDto.class);

            // id와 password를 추출하여 UsernamePasswordAuthenticationToken 생성
            String username = loginReqDto.getId();
            String password = loginReqDto.getPassword();
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);

            // 인증 매니저에게 전달하여 인증 시도
            return authenticationManager.authenticate(authToken);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    // 로그인 성공 시 자동 호출되어 실행되는 메서드
    @Override
    public void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException {
        // Authentication 객체로부터 username 추출
        String username = authentication.getName();
        String rawPassword = obtainPassword(request);

        // CustomUserDetails 객체로부터 role 추출
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();


        // 추출한 username 과 role을 기반으로 JWT 토큰 생성
        String token = jwtUtil.createJwt(username, role, 60 * 60 * 10 * 1000L); // 인자 : username, role, 유효시간(36,000초인 10시간으로 지정)

        // jwt 토큰을 응답 헤더에 담기
        response.addHeader("Authorization","Bearer " + token); // ex) 'Authorization: Bearer 인증토큰'

        // 바디에 DataResponseDto 형식으로 응답 설정
        DataResponseDto<Map<String, String>> successResponse = new DataResponseDto<>(
                Map.of("message", "로그인 성공"),
                Code.OK
        );

        // 응답을 JSON 형식으로 변환하여 출력
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(successResponse));

    }


    // 로그인 실패 시 자동 호출되어 실행되는 메서드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized

        // 실패 원인에 따라 적절한 Code 설정
        Code errorCode;
        String customMessage;

        if (failed.getMessage().contains("회원을 찾을 수 없습니다")) {
            errorCode = Code.MEMBER_NOT_FOUND;
            customMessage = "아이디가 잘못되었습니다.";
        } else if (failed.getMessage().contains("비밀번호가 잘못되었습니다")) {
            errorCode = Code.UNAUTHORIZED;
            customMessage = "비밀번호가 잘못되었습니다.";
        } else {
            errorCode = Code.UNAUTHORIZED;
            customMessage = "인증에 실패하였습니다.";
        }

        // ErrorResponseDto 생성
        ErrorResponseDto errorResponse = new ErrorResponseDto(errorCode, errorCode.getMessage(customMessage));

        // JSON 형식으로 응답 출력
        response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
    }



}