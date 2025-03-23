package com.myproject.security;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc; // Spring MVC 설정

/**
 * 🔹 Spring Security 설정 (Spring Security 5.x)
 * - JWT 기반 인증을 고려하여 stateless 세션 관리 적용
 * - 특정 엔드포인트(로그인, 회원가입)는 모두에게 공개
 * - JwtAuthenticationFilter를 UsernamePasswordAuthenticationFilter 앞에 추가하여 요청에 포함된 JWT를 검증
 */
@Configuration
@EnableWebSecurity
@EnableWebMvc // Spring MVC 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsService userDetailsService;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
                          @Qualifier("customUserDetailsService") UserDetailsService userDetailsService) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userDetailsService = userDetailsService;
    }

    /**
     * 🔹 Security 설정 (HttpSecurity 설정)
     * - CSRF 비활성화 (Stateless 환경에서는 필요 없음)
     * - 세션을 사용하지 않음 (STATELESS)
     * - 특정 엔드포인트는 허용하고, 나머지는 인증 필요
     * - JWT 인증 필터 추가
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable() // CSRF 보호 비활성화 (REST API에서는 필요 없음)
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 사용 안 함
            .and()
            .authorizeRequests() // Spring Security 5.x 방식
            .mvcMatchers("/api/auth/login", "/api/auth/signup").permitAll() // mvcMatchers() 사용
            .anyRequest().authenticated() // 나머지 요청 인증 필요
            .and()
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // JWT 필터 추가
    }

    /**
     * 🔹 AuthenticationProvider 설정 (UserDetailsService 적용)
     * - UserDetailsService를 사용하여 인증 처리
     * - 비밀번호 암호화를 위해 BCryptPasswordEncoder 사용
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * 🔹 AuthenticationManager 설정
     * - AuthenticationProvider를 이용한 인증 관리
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * 🔹 비밀번호 암호화 (BCrypt 적용)
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
