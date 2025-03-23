package com.myproject.controller;

import com.myproject.model.User;
import com.myproject.service.AdminService;
import com.myproject.util.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * AdminController - 관리자 전용 회원 관리 컨트롤러
 */
@Controller // RestController → Controller 변경
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    /**
     * 회원 목록 조회 (페이징 & 검색)
     */
    @Secured("ADMIN") // 관리자 권한 필수
    @GetMapping("/users")
    public String getUsers(@RequestParam(defaultValue = "1") int page,
                           @RequestParam(defaultValue = "10") int size,
                           @RequestParam(required = false) String keyword,
                           Model model) {
        int totalUsers = adminService.getTotalUserCount(keyword);
        Pagination pagination = new Pagination(totalUsers, page, size);

        List<User> users = adminService.getUsers(page, size, keyword);
        model.addAttribute("users", users);
        model.addAttribute("pagination", pagination);
        model.addAttribute("keyword", keyword);
        
        return "admin/admin_dashboard"; // JSP 파일 (WEB-INF/views/admin/user_list.jsp)
    }

    /**
     * 회원 정보 수정
     */
    @Secured("ADMIN")
    @PostMapping("/users/update")
    public String updateUser(@ModelAttribute User user) {
        int result = adminService.updateUser(user);
        return result > 0 ? "redirect:/admin/users" : "admin/error"; // JSP 에러 페이지로 이동
    }

    /**
     * 회원 삭제
     */
    @Secured("ADMIN")
    @PostMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        int result = adminService.deleteUser(id);
        return result > 0 ? "redirect:/admin/users" : "admin/error"; // JSP 에러 페이지로 이동
    }
}
