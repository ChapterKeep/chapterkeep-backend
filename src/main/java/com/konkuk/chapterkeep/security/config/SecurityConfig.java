package com.konkuk.chapterkeep.security.config;

import com.konkuk.chapterkeep.security.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return this.authenticationConfiguration.getAuthenticationManager();
    }


    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // cors 설정
        http
                .cors((corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {

                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                        CorsConfiguration configuration = new CorsConfiguration();

                        // Origin을 http://localhost:3000 만 허용
                        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));

                        // 모든 메서드(GET, POST, PUT, DELETE 등) 허용
                        configuration.setAllowedMethods(Collections.singletonList("*"));

                        // 자격 증명(쿠키, Authorization 헤더 등)을 포함한 요청을 허용
                        configuration.setAllowCredentials(true);

                        // 모든 HTTP 헤더 허용
                        configuration.setAllowedHeaders(Collections.singletonList("*"));

                        // 3600초(1시간) 동안 캐시
                        configuration.setMaxAge(3600L);

                        // 클라이언트에게 노출할 헤더 설정 - Authorization 헤더를 노출하여 클라이언트가 접근할 수 있게 설정
                        configuration.setExposedHeaders(Collections.singletonList("Authorization"));

                        return configuration;
                    }
                })));

        // csrf disable
        http
                .csrf((auth) -> auth.disable());

        // Form 로그인 방식 disable
        http
                .formLogin((auth) -> auth.disable());

        // http basic 인증 방식 disable
        http
                .httpBasic((auth) -> auth.disable());

        // 경로별 인가 작업
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/","/login", "/signup","/check-id","/check-nickname").permitAll() // 모든 사용자 허용
                        .requestMatchers("/admin").hasRole("ADMIN") // admin 권한 사용자만 허용
                        .anyRequest().authenticated()); // 로그인한 사용자만 허용

        // JWTFilter 등록
        http
                .addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class);

        // LoginFilter 등록
        http
                .addFilterAt(new LoginFilter( authenticationManager(authenticationConfiguration) , jwtUtil), UsernamePasswordAuthenticationFilter.class);


        // 세션 설정
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));


        return http.build();
    }


}