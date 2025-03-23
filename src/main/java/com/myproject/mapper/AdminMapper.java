package com.myproject.mapper;

import com.myproject.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;

import java.util.List;

@Mapper
public interface AdminMapper {
    List<User> getUsers(@Param("offset") int offset, @Param("size") int size, @Param("keyword") String keyword);
    int getTotalUserCount(@Param("keyword") String keyword);
    int updateUser(User user);
    int deleteUser(Long id);
}
