package com.myproject.mapper;

import com.myproject.model.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    // 사용자 이름으로 사용자 조회
    User findByUsername(String username);

    // 사용자 ID로 사용자 조회
    User findById(Long id);

    // 이메일로 사용자 조회
    User findByEmail(String email);

    // 새로운 사용자 등록
    int insertUser(User user);

    // 사용자 정보 수정
    int updateUser(User user);

    // 사용자 삭제
    int deleteUser(Long id);
}
