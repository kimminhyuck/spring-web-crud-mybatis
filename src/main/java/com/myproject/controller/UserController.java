package com.myproject.controller;

import com.myproject.model.User;
import com.myproject.security.JwtUtil;
import com.myproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    //  회원가입 폼
    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("user", new User());
        return "user/signup";
    }

    // 🔹 회원가입 처리
    @PostMapping("/signup")
    public String signup(@ModelAttribute("user") User user, Model model) {
        user.setPassword(passwordEncoder.encode(user.getPassword())); // 비밀번호 암호화
        int result = userService.registerUser(user);
        if (result > 0) {
            return "redirect:user/login";
        } else {
            model.addAttribute("error", "회원가입 실패");
            return "user/signup";
        }
    }

    //  회원 정보 수정 (비밀번호 변경 포함)
    @PostMapping("/update")
    public String updateProfile(@ModelAttribute("user") User user, 
                                @CookieValue(value = "jwt", required = false) String token,
                                HttpServletResponse response,
                                Model model) {
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        int result = userService.updateUser(user);
        if (result > 0) {
            //  프로필 업데이트 후 새로운 JWT 발급
            if (token != null) {
                String newToken = jwtUtil.generateToken(user.getUsername());
                Cookie jwtCookie = new Cookie("jwt", newToken);
                jwtCookie.setHttpOnly(true);
                jwtCookie.setSecure(false);
                jwtCookie.setPath("/");
                jwtCookie.setMaxAge(3600);
                response.addCookie(jwtCookie);
            }
            return "redirect:/user/profile";
        } else {
            model.addAttribute("error", "프로필 업데이트 실패");
            return "user/profile";
        }
    }

    // 🔹 회원 탈퇴 (JWT 쿠키 삭제)
    @PostMapping("/delete")
    public String deleteUser(@RequestParam("userId") Long userId, 
                             HttpServletResponse response, 
                             Model model) {
        int result = userService.deleteUser(userId);
        if (result > 0) {
            //  회원 탈퇴 후 JWT 쿠키 삭제
            Cookie jwtCookie = new Cookie("jwt", null);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setSecure(false);
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge(0);
            response.addCookie(jwtCookie);

            return "redirect:/login";
        } else {
            model.addAttribute("error", "회원 탈퇴 실패");
            return "user/profile";
        }
    }
}
