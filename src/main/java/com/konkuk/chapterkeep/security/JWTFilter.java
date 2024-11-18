package com.konkuk.chapterkeep.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.konkuk.chapterkeep.common.response.dto.ErrorResponseDto;
import com.konkuk.chapterkeep.common.response.enums.Code;
import com.konkuk.chapterkeep.domain.Member;
import com.konkuk.chapterkeep.domain.enums.Role;
import com.konkuk.chapterkeep.member.dto.CustomMemberDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, IOException {

        try {
            // request 헤더에서 jwt 토큰 추출
            String authorization = request.getHeader("Authorization");
//            System.out.println("[DEBUG] Authorization 헤더 값: " + authorization);


            // Authorization 헤더 검증
            if (authorization == null || !authorization.startsWith("Bearer ")) {
//                System.out.println("[DEBUG] Authorization 헤더가 null이거나 Bearer로 시작하지 않습니다.");
                System.out.println("token null");
                filterChain.doFilter(request, response); // 다음 필터로 요청, 응답 전달
                return; // 헤더 검증 실패 시 메서드 종료 (필수)
            }

            // Bearer 제거 후 순수 토큰 추출
            String token = authorization.split(" ")[1]; // 요청 헤더에서 추출한 jwt 토큰을 공백을 기준으로 분리하여 생성된 배열에서 두 번째 인자 추출 <= ["Bearer", "토큰값"]
//            System.out.println("[DEBUG] JWT 토큰 값: " + token);

            // 토큰 만료 여부 검증
            if (jwtUtil.isExpired(token)) {
                throw new IllegalArgumentException("토큰이 만료되었습니다.");
            }

//            System.out.println("[DEBUG] 토큰 만료 여부 검증 이후");

            // 토큰에서 username 과 role 추출
            String username = jwtUtil.getUsername(token);
            String role = jwtUtil.getRole(token);

//            System.out.println("[DEBUG] 토큰 username 과 role 추출 이후");

            // 추출한 정보로 member 생성
            Member member = Member.builder()
                    .name(username)
                    .password("")
                    .nickname("")
                    .introduction("")
                    .profileUrl("")
                    .role(Role.valueOf(role))
                    .visibility(true)
                    .build();

//            System.out.println("[DEBUG] 추출한 정보로 member 생성 이후");

            // UserDetails 에 member 담기
            CustomMemberDetails customMemberDetails = new CustomMemberDetails(member);

//            System.out.println("[DEBUG] UserDetails 에 member 담기 이후");

            // 시큐리티 인증 토큰 객체 생성
            Authentication authToken = new UsernamePasswordAuthenticationToken(customMemberDetails, null, customMemberDetails.getAuthorities());

//            System.out.println("[DEBUG] 시큐리티 인증 토큰 객체 생성 이후");

            // 임시 세션에 사용자 등록
            SecurityContextHolder.getContext().setAuthentication(authToken);

//            System.out.println("[DEBUG] 임시 세션에 사용자 등록 이후");


            // 다음 필터로 요청, 응답 전달
            filterChain.doFilter(request, response);
        } catch (Exception e) {

            Code errorCode;
            String customMessage;

            if (e.getMessage().contains("만료")) {
                errorCode = Code.INVALID_TOKEN;
                customMessage = "토큰이 만료되었습니다.";
            } else if (e.getMessage().contains("서명")) {
                errorCode = Code.INVALID_TOKEN;
                customMessage = "서명이 유효하지 않습니다.";
            } else {
                errorCode = Code.INVALID_TOKEN;
                customMessage = "";
            }

            setErrorResponse(response, errorCode, customMessage);
        }
    }

    // 에러 응답 설정 메서드
    private void setErrorResponse(HttpServletResponse response, Code code, String customMessage) throws IOException {
        ErrorResponseDto errorResponse = new ErrorResponseDto(code, code.getMessage(customMessage));
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
    }}