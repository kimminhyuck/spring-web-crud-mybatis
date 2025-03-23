package com.myproject.service;

import com.myproject.mapper.UserMapper;
import com.myproject.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserMapper userMapper; // DB 조회를 위한 UserMapper 주입

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); // 비밀번호 암호화 객체 생성

    /**
     * 사용자 인증 메서드
     * - 사용자 이름과 비밀번호를 통해 인증을 수행하고, 인증된 사용자를 반환합니다.
     */
    @Override
    public User authenticate(String username, String password) {
        User user = userMapper.findByUsername(username);  // 사용자 이름으로 사용자 정보 조회
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {  // 비밀번호 확인
            return user;  // 인증 성공, 사용자 반환
        }
        return null;  // 인증 실패, null 반환
    }

    /**
     * 사용자 이름으로 사용자 정보 조회
     * - 사용자의 이름을 통해 DB에서 사용자 정보를 조회하고 반환합니다.
     */
    @Override
    public User getUserByUsername(String username) {
        return userMapper.findByUsername(username);  // 사용자 이름으로 사용자 정보 반환
    }
}
