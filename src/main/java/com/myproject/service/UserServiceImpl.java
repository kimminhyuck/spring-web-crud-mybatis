package com.myproject.service;

import com.myproject.dao.UserDAO;
import com.myproject.model.User;
import com.myproject.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * UserServiceImpl
 * ────────────────────────────────
 * UserService 인터페이스의 구현체로, MyBatis 기반의 UserDAO를 사용하여
 * 사용자 등록, 조회, 수정, 삭제 및 중복 체크 등의 비즈니스 로직을 수행합니다.
 * 
 * 또한 Spring Security의 UserDetailsService를 구현하여
 * SecurityContextHolder에 인증 정보를 설정할 수 있도록 함.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private PasswordEncoder passwordEncoder; // 비밀번호 암호화를 위한 PasswordEncoder 추가

    /**
     * 신규 사용자 등록 (비밀번호 암호화 포함)
     * @param user 등록할 사용자 객체
     * @return 등록된 행의 수 (정상 등록 시 1 반환)
     */
    @Override
    @Transactional
    public int registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword())); // 비밀번호 암호화
        return userDAO.insertUser(user);
    }

    /**
     * 사용자 이름(username)을 기반으로 사용자 정보를 조회합니다.
     * @param username 조회할 사용자 이름
     * @return 조회된 User 객체, 없을 경우 null 반환
     */
    @Override
    public User findByUsername(String username) {
        return userDAO.findByUsername(username);
    }

    /**
     * 회원정보 수정 (비밀번호 변경 포함)
     * @param user 수정할 사용자 객체
     * @return 수정된 행의 수 (정상 수정 시 1 반환)
     */
    @Override
    @Transactional
    public int updateUser(User user) {
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword())); // 비밀번호 변경 시 암호화
        }
        return userDAO.updateUser(user);
    }

    /**
     * 회원정보 삭제
     * @param id 삭제할 사용자의 고유 ID
     * @return 삭제된 행의 수 (정상 삭제 시 1 반환)
     */
    @Override
    @Transactional
    public int deleteUser(Long id) {
        return userDAO.deleteUser(id);
    }

    /**
     * 아이디 중복 여부를 체크합니다.
     * @param username 확인할 사용자 아이디
     * @return 중복되지 않았다면 true, 이미 존재하면 false 반환
     */
    @Override
    public boolean isUsernameAvailable(String username) {
        return userDAO.findByUsername(username) == null;
    }

    /**
     * 이메일 중복 여부를 체크합니다.
     * @param email 확인할 사용자 이메일
     * @return 중복되지 않았다면 true, 이미 존재하면 false 반환
     */
    @Override
    public boolean isEmailAvailable(String email) {
        return userDAO.findByEmail(email) == null;
    }

    /**
     * Spring Security에서 사용하는 UserDetailsService 구현
     * JWT 인증 시 UserDetails를 로드하는 역할 수행
     * @param username 조회할 사용자 아이디
     * @return UserDetails (CustomUserDetails)
     * @throws UsernameNotFoundException 해당 아이디가 없을 경우 예외 발생
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username);
        }
        return new CustomUserDetails(user);
    }
}
