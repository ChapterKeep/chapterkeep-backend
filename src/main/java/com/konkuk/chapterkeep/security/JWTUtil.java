package com.konkuk.chapterkeep.security;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTUtil {

    // SecretKey 객체 타입으로서의 키 필요
    private SecretKey secretKey;

    // 생성자
    public JWTUtil(@Value("${spring.jwt.secret}")String secret) {
        // String 타입으로 선언했던 키 값을 암호화해서 SecretKey 타입으로 만들어 초기화
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    // 토큰에서 username 검증,추출하는 메서드
    public String getUsername(String token) {
        // jwt 파싱해서 secretKey로 서명검증 및 payload에서 username 추출
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("username", String.class);
    }

    // 토큰에서 role 검증,추출하는 메서드
    public String getRole(String token) {
        // jwt 파싱해서 secretKey로 서명검증 및 payload에서 role 추출
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
    }

    // 토큰에서 만료여부 검증,추출하는 메서드
    public Boolean isExpired(String token) {
        // jwt 파싱해서 secretKey로 서명검증 및 payload에서 현재시간 기준 만료여부 추출
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }

    // 토큰 생성 메서드
    // username, role, 토큰유효시간 을 입력받아 JWT 토큰을 생성해 String 형태로 반환
    public String createJwt(String username, String role, Long expiredMs) {
        return Jwts.builder()
                .claim("username", username)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis())) // 토큰 발행 시각
                .expiration(new Date(System.currentTimeMillis() + expiredMs)) // 토큰 만료 시각 (발생시각 + 유효시간)
                .signWith(secretKey)
                .compact();
    }
}