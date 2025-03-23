package com.myproject.service;

import com.myproject.model.User;

import java.util.List;

public interface AdminService {
    List<User> getUsers(int page, int size, String keyword); // 회원 목록 조회 (페이징 & 검색)
    int getTotalUserCount(String keyword); // 전체 회원 수 조회 (검색 포함)
    int updateUser(User user); // 회원 정보 수정
    int deleteUser(Long id); // 회원 삭제
}
