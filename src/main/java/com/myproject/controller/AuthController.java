package com.myproject.controller;

import com.myproject.model.User;
import com.myproject.security.JwtUtil;
import com.myproject.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * AuthController ──────────────────────────────── - 로그인 (JWT Access Token &
 * Refresh Token 발급) - 현재 로그인된 사용자 조회 - Access Token 만료 시 자동 갱신 (Refresh Token
 * 사용) - 로그아웃 (JWT 쿠키 삭제)
 */
@Controller
@RequestMapping("/user")
public class AuthController {

	@Autowired
	private AuthService authService;

	@Autowired
	private JwtUtil jwtUtil;

	/**
	 * 🔹 로그인 페이지 (GET) 요청 처리
	 */
	@GetMapping("/login")
	public String loginPage() {
		return "user/login"; // 로그인 페이지 JSP 또는 HTML로 리턴
	}

	/**
	 * 🔹 로그인 엔드포인트 (POST) 사용자 인증 후 Access Token 및 Refresh Token을 쿠키에 저장
	 */
	@PostMapping("/login")
	public String login(@RequestParam String username, @RequestParam String password, HttpServletResponse response,
			HttpSession session) {
		// 사용자 인증: 서비스에서 username과 password로 사용자 확인
		User user = authService.authenticate(username, password);
		if (user == null) {
			return "redirect:/login"; // 로그인 실패 시 로그인 페이지로 리디렉션
		}

		// JWT Access Token 및 Refresh Token 생성
		String accessToken = jwtUtil.generateToken(user.getUsername());
		String refreshToken = jwtUtil.generateRefreshToken(user.getUsername());

		// JWT Access Token & Refresh Token을 쿠키로 저장
		ResponseCookie accessCookie = jwtUtil.createJwtCookie(accessToken);
		ResponseCookie refreshCookie = jwtUtil.createRefreshCookie(refreshToken);

		response.addHeader("Set-Cookie", accessCookie.toString());
		response.addHeader("Set-Cookie", refreshCookie.toString());

		// 사용자 정보를 세션에 저장
		session.setAttribute("user", user); // 로그인 성공 시 세션에 사용자 정보 저장

		// 로그인 성공 후 메인 페이지로 리디렉션
		return "redirect:/"; // 메인 페이지로 리디렉션
	}

	/**
	 * 🔹 현재 로그인한 사용자 정보 조회 Access Token을 쿠키에서 가져와서 인증
	 */
	@GetMapping("/me")
	public ResponseEntity<?> getUser(@CookieValue(value = "jwt", required = false) String token) {
		if (token == null || !jwtUtil.validateToken(token)) {
			return ResponseEntity.status(401).body("Unauthorized");
		}

		String username = jwtUtil.getUsernameFromToken(token);
		User user = authService.getUserByUsername(username);
		return ResponseEntity.ok(user); // 인증된 사용자 정보 반환
	}

	/**
	 * 🔹 Access Token 자동 재발급 (Refresh Token 사용) Access Token이 만료되었을 때, Refresh
	 * Token을 확인하여 새로운 Access Token 발급
	 */
	@PostMapping("/refresh")
	public ResponseEntity<?> refreshAccessToken(
			@CookieValue(value = "refreshToken", required = false) String refreshToken, HttpServletResponse response) {
		if (refreshToken == null || !jwtUtil.validateToken(refreshToken)) {
			return ResponseEntity.status(401).body("Invalid or expired refresh token");
		}

		// Refresh Token에서 사용자 정보 가져오기
		String username = jwtUtil.getUsernameFromToken(refreshToken);
		User user = authService.getUserByUsername(username);
		if (user == null) {
			return ResponseEntity.status(401).body("User not found");
		}

		// 새로운 Access Token 발급
		String newAccessToken = jwtUtil.generateToken(username);
		ResponseCookie newAccessCookie = jwtUtil.createJwtCookie(newAccessToken);
		response.addHeader("Set-Cookie", newAccessCookie.toString());

		return ResponseEntity.ok("New access token issued");
	}

	/**
	 * 🔹 로그아웃 (쿠키 삭제) Access Token & Refresh Token 제거
	 */
	@PostMapping("/logout")
	public String logout(HttpServletResponse response, HttpSession session) {
		// JWT 쿠키 삭제
		ResponseCookie accessCookie = jwtUtil.deleteCookie("jwt");
		ResponseCookie refreshCookie = jwtUtil.deleteCookie("refreshToken");

		response.addHeader("Set-Cookie", accessCookie.toString());
		response.addHeader("Set-Cookie", refreshCookie.toString());

		// 세션에서 사용자 정보 삭제
		session.invalidate();

		// 메인 페이지로 리디렉션
		return "redirect:/"; // 메인 페이지로 리디렉션
	}
}
