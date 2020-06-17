package com.xbcai.service.user;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xbcai.dao.UserMapper;
import com.xbcai.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService {

    @Override
    public List<User> findAllUsers() {
        return this.baseMapper.findAllUsers();
    }
}
