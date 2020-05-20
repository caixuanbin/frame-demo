package com.xbcai.dao;

import com.xbcai.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
@Mapper
public interface UserMapper {
     List<User> findAllUsers();
}
