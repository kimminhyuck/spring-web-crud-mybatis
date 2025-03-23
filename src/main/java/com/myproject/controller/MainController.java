package com.myproject.controller;

import com.myproject.model.User;
import com.myproject.security.JwtUtil;
import com.myproject.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthService authService;

    /**
     * 메인 페이지
     * - JWT 쿠키에서 사용자 정보를 가져와 로그인 상태 유지
     */
    @GetMapping("/")
    public String mainPage(@CookieValue(value = "jwt", required = false) String token, Model model) {
        if (token != null && jwtUtil.validateToken(token)) {
            // JWT에서 사용자명(username) 추출
            String username = jwtUtil.getUsernameFromToken(token);

            // 데이터베이스에서 사용자 정보 조회
            User loggedInUser = authService.getUserByUsername(username);
            model.addAttribute("user", loggedInUser);
        } else {
            // 로그인되지 않은 상태
            model.addAttribute("user", null);
        }

        return "main";  // main.jsp 반환
    }
}
