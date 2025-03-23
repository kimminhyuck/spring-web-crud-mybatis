package com.myproject.service;

import com.myproject.model.User;

public interface AuthService {
    // 사용자 인증 메서드
    User authenticate(String username, String password);
    
    // 사용자 이름으로 사용자 정보 조회
    User getUserByUsername(String username);
}
