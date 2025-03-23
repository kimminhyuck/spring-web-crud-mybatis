package com.myproject.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * JwtUtil 클래스
 * ────────────────────────────────
 * JWT 토큰을 생성하고, 검증하며, 토큰으로부터 사용자 정보를 추출하는 역할을 합니다.
 * - generateToken(): JWT Access Token 생성
 * - generateRefreshToken(): JWT Refresh Token 생성
 * - getUsernameFromToken(): 토큰에서 사용자 이름 추출
 * - validateToken(): 토큰의 유효성 검증
 * - createJwtCookie(): JWT를 쿠키로 설정
 */
@Component
public class JwtUtil {

    // JWT 비밀키 (길이가 충분해야 함)
    private final String jwtSecret = "your-very-long-secret-key-for-jwt-generation-which-should-be-kept-safe";

    // Access Token 만료 시간 (1시간 = 3600000ms)
    private final long accessTokenExpiration = 3600000;

    // Refresh Token 만료 시간 (7일 = 604800000ms)
    private final long refreshTokenExpiration = 604800000;

    // SecretKey 객체 생성
    private final SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

    /**
     * 🔹 JWT Access Token 생성 (1시간 유효)
     * @param username 사용자 이름
     * @return 생성된 JWT 토큰
     */
    public String generateToken(String username) {
        return Jwts.builder()
                   .setSubject(username)
                   .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
                   .signWith(key, SignatureAlgorithm.HS512)  // 비밀 키로 서명
                   .compact();
    }

    /**
     * 🔹 JWT Refresh Token 생성 (7일 유효)
     * @param username 사용자 이름
     * @return 생성된 Refresh Token
     */
    public String generateRefreshToken(String username) {
        return createToken(username, refreshTokenExpiration);
    }

    /**
     * 🔹 JWT 토큰 생성 공통 메서드
     */
    private String createToken(String username, long expirationTime) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256)  // 비밀 키로 서명
                .compact();
    }

    /**
     * 🔹 JWT 토큰에서 사용자 이름 추출
     * @param token JWT 토큰
     * @return 사용자 이름
     */
    public String getUsernameFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        } catch (ExpiredJwtException e) {
            System.out.println("JWT 토큰이 만료되었습니다.");
            return null;
        } catch (JwtException e) {
            System.out.println("유효하지 않은 JWT: " + e.getMessage());
            return null;
        }
    }

    /**
     * 🔹 JWT 토큰 검증 메서드
     * @param token 검증할 토큰
     * @return 유효한 토큰이면 true, 아니면 false
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("토큰이 만료됨: " + e.getMessage());
        } catch (JwtException e) {
            System.out.println("토큰이 유효하지 않음: " + e.getMessage());
        }
        return false;
    }

    /**
     * 🔹 JWT Access Token을 쿠키로 생성 (1시간 유지)
     * @param token JWT 토큰
     * @return HTTP 쿠키 설정
     */
    public ResponseCookie createJwtCookie(String token) {
        return createCookie("jwt", token, accessTokenExpiration);
    }

    /**
     * 🔹 JWT Refresh Token을 쿠키로 생성 (7일 유지)
     * @param token JWT Refresh Token
     * @return HTTP 쿠키 설정
     */
    public ResponseCookie createRefreshCookie(String token) {
        return createCookie("refreshToken", token, refreshTokenExpiration);
    }

    /**
     * 🔹 공통 쿠키 생성 메서드
     * @param name 쿠키 이름
     * @param token JWT 토큰 값
     * @param maxAge 쿠키 유효 시간 (초 단위)
     * @return 생성된 HTTP 쿠키
     */
    private ResponseCookie createCookie(String name, String token, long maxAge) {
        return ResponseCookie.from(name, token)
            .httpOnly(true)
            .secure(true)  // HTTPS 환경에서 true로 설정
            .sameSite("None") // CORS 환경에서는 None 필요
            .path("/")
            .maxAge(maxAge / 1000) // 초 단위로 변환
            .build();
    }

    /**
     * 🔹 JWT 쿠키 삭제 메서드 (로그아웃 시 사용)
     * @param name 삭제할 쿠키 이름
     * @return 빈 값의 쿠키 반환
     */
    public ResponseCookie deleteCookie(String name) {
        return ResponseCookie.from(name, "")
            .httpOnly(true)
            .secure(true)
            .sameSite("None")
            .path("/")
            .maxAge(0) // 즉시 만료
            .build();
    }
}
