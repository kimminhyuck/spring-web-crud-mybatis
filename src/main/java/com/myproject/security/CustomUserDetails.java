package com.myproject.security;

import com.myproject.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;
import java.util.List;

/**
 * CustomUserDetails
 * ────────────────────────────────
 * User 모델을 Spring Security의 UserDetails 인터페이스로 변환하여,
 * 인증 및 권한 부여에 필요한 정보를 제공합니다.
 */
public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    /**
     * 사용자 권한을 GrantedAuthority 객체로 반환합니다.
     * 예제에서는 단일 권한(ADMIN 또는 USER)을 ROLE_ 접두어와 함께 설정합니다.
     *
     * @return GrantedAuthority 목록
     */
    @Override
    public List<GrantedAuthority> getAuthorities() {
        // user.getRole()의 결과에 "ROLE_" 접두어를 붙여 GrantedAuthority 생성
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    // 아래 메서드들은 추가적인 계정 상태 관리가 필요한 경우 수정 가능

    @Override
    public boolean isAccountNonExpired() {
        return true; // 계정 만료 체크 필요 시 로직 추가
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 계정 잠금 체크 필요 시 로직 추가
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 자격 증명 만료 체크 필요 시 로직 추가
    }

    @Override
    public boolean isEnabled() {
        return true; // 계정 활성화 상태 체크 필요 시 로직 추가
    }

    /**
     * 원본 User 객체를 반환 (필요 시 서비스 계층에서 활용 가능)
     */
    public User getUser() {
        return this.user;
    }
}
