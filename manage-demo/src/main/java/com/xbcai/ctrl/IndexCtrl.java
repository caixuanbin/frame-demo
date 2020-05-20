package com.xbcai.ctrl;

import com.xbcai.model.User;
import com.xbcai.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class IndexCtrl {
    @Autowired
    private UserService tUserService;
    @Autowired
    private ApplicationContext applicationContext;
    @GetMapping("/index")
    public String index(){
        System.out.println(applicationContext.getBean("userxbc"));
        System.out.println(applicationContext.getBean(User.class));
        return "index.html";
    }
    @GetMapping("/findUser")
    public List<User> findUser(){
      return tUserService.findAllUsers();
    }
    public static void main(String[] args) {
        System.out.println(3);
    }
}
