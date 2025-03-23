package com.myproject.service;

import com.myproject.mapper.AdminMapper;
import com.myproject.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Override
    public List<User> getUsers(int page, int size, String keyword) {
        int offset = (page - 1) * size;
        return adminMapper.getUsers(offset, size, keyword);
    }

    @Override
    public int getTotalUserCount(String keyword) {
        return adminMapper.getTotalUserCount(keyword);
    }

    @Override
    public int updateUser(User user) {
        return adminMapper.updateUser(user);
    }

    @Override
    public int deleteUser(Long id) {
        return adminMapper.deleteUser(id);
    }
}
