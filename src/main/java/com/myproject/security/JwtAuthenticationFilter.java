package com.myproject.security;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * JwtAuthenticationFilter
 * ────────────────────────────────
 * - HTTP 요청 시 JWT 쿠키를 검증하고, 유효하면 사용자 인증 정보를 SecurityContext에 설정.
 * - Access Token이 만료되었을 경우, Refresh Token을 사용하여 자동으로 새 Access Token 발급.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil; // JWT 검증 및 생성 유틸

    @Autowired
    private CustomUserDetailsService userDetailsService; // 사용자 정보 로드 서비스

    /**
     * 🔹 HTTP 요청의 쿠키에서 특정 이름의 JWT 토큰을 가져오는 메서드
     *
     * @param request HttpServletRequest 객체
     * @param tokenName 가져올 쿠키의 이름 (jwt, refreshToken 등)
     * @return JWT 토큰 문자열, 존재하지 않을 경우 null 반환
     */
    private String getTokenFromCookie(HttpServletRequest request, String tokenName) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (tokenName.equals(cookie.getName())) { 
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                    HttpServletResponse response, 
                                    FilterChain filterChain)
            throws ServletException, IOException {
        try {
            // 1️⃣ Access Token 확인
            String accessToken = getTokenFromCookie(request, "jwt");

            if (StringUtils.hasText(accessToken) && jwtUtil.validateToken(accessToken)) {
                // ✅ Access Token이 유효한 경우
                authenticateUser(accessToken, request);
            } else {
                // 2️⃣ Access Token이 만료된 경우, Refresh Token 확인
                String refreshToken = getTokenFromCookie(request, "refreshToken");

                if (StringUtils.hasText(refreshToken) && jwtUtil.validateToken(refreshToken)) {
                    // ✅ Refresh Token이 유효하면 새로운 Access Token 발급
                    String username = jwtUtil.getUsernameFromToken(refreshToken);
                    String newAccessToken = jwtUtil.generateToken(username);

                    // 쿠키에 새 Access Token 저장
                    Cookie newAccessCookie = new Cookie("jwt", newAccessToken);
                    newAccessCookie.setHttpOnly(true);
                    newAccessCookie.setSecure(false); // 배포 시 true로 변경
                    newAccessCookie.setPath("/");
                    newAccessCookie.setMaxAge(3600);

                    response.addCookie(newAccessCookie);

                    // 사용자 인증 정보 설정
                    authenticateUser(newAccessToken, request);
                } else {
                    // ❌ Refresh Token도 만료됨 → 로그인 필요
                    logger.warn("Access Token 및 Refresh Token 모두 만료됨. 로그인 필요.");
                }
            }
        } catch (Exception ex) {
            logger.error("JWT 인증 과정에서 오류 발생", ex);
        }

        // 다음 필터로 요청 전달
        filterChain.doFilter(request, response);
    }

    /**
     * 🔹 JWT 토큰을 기반으로 사용자 인증을 수행하는 메서드
     *
     * @param token JWT 토큰
     * @param request HttpServletRequest 객체
     */
    private void authenticateUser(String token, HttpServletRequest request) {
        String username = jwtUtil.getUsernameFromToken(token);
        CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(username);

        UsernamePasswordAuthenticationToken authentication = 
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        logger.info("JWT 인증 성공: " + username);
    }
}
