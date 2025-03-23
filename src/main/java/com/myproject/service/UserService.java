package com.myproject.service;

import com.myproject.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * UserService 인터페이스
 * ────────────────────────────────
 * 회원가입, 사용자 조회, 수정, 삭제 및 중복 체크 등 사용자 관련 비즈니스 로직을 정의합니다.
 */
public interface UserService extends UserDetailsService { // Spring Security의 UserDetailsService 상속

    /**
     * 신규 사용자 등록 (비밀번호 암호화 포함)
     * @param user 등록할 사용자 객체
     * @return 등록된 행의 수 (정상 등록 시 1 반환)
     */
    int registerUser(User user);

    /**
     * 사용자 이름(username)을 기반으로 사용자 정보를 조회합니다.
     * @param username 로그인 시 사용되는 사용자 이름
     * @return 조회된 User 객체, 없을 경우 null 반환
     */
    User findByUsername(String username);

    /**
     * 회원정보 수정 (비밀번호 변경 포함)
     * @param user 수정할 사용자 객체
     * @return 수정된 행의 수 (정상 수정 시 1 반환)
     */
    int updateUser(User user);

    /**
     * 회원정보 삭제
     * @param id 삭제할 사용자의 고유 ID
     * @return 삭제된 행의 수 (정상 삭제 시 1 반환)
     */
    int deleteUser(Long id);

    /**
     * 아이디 중복 여부를 체크합니다.
     * @param username 확인할 사용자 아이디
     * @return 중복되지 않았다면 true, 이미 존재하면 false 반환
     */
    boolean isUsernameAvailable(String username);

    /**
     * 이메일 중복 여부를 체크합니다.
     * @param email 확인할 사용자 이메일
     * @return 중복되지 않았다면 true, 이미 존재하면 false 반환
     */
    boolean isEmailAvailable(String email);
}
