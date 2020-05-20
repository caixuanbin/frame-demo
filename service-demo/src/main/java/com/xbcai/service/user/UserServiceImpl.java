package com.xbcai.service.user;

import com.xbcai.dao.UserMapper;
import com.xbcai.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public List<User> findAllUsers() {
        return userMapper.findAllUsers();
    }
}
