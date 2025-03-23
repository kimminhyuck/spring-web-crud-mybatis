package com.myproject.security;

import com.myproject.dao.UserDAO; // MyBatis 기반 사용자 조회 DAO (인터페이스)
import com.myproject.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * CustomUserDetailsService
 * ────────────────────────────────
 * Spring Security의 UserDetailsService 인터페이스를 구현하여,
 * MyBatis를 통해 DB에서 사용자 정보를 조회하고, 이를 CustomUserDetails로 변환합니다.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserDAO userDAO; // MyBatis 매퍼 인터페이스를 통해 사용자 정보를 조회합니다.

    /**
     * 사용자 이름(username)을 기반으로 사용자 정보를 조회하여 UserDetails 객체로 반환합니다.
     *
     * @param username 로그인에 사용된 사용자 이름
     * @return CustomUserDetails 객체 (UserDetails 구현체)
     * @throws UsernameNotFoundException 사용자를 찾을 수 없는 경우 예외 발생
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // MyBatis를 이용하여 사용자 조회
        User user = userDAO.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        // 조회된 User 객체를 기반으로 CustomUserDetails 생성 및 반환
        return new CustomUserDetails(user);
    }
}
