package com.xbcai;

import com.xbcai.aspect.DataCon;
import com.xbcai.model.Station;
import com.xbcai.model.User;
import com.xbcai.service.user.UserService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RestController
public class IndexCtrl {
    @Autowired
    private UserService tUserService;
    @Autowired
    private ApplicationContext applicationContext;
    @GetMapping("/index")
    public String index(){
        //System.out.println(applicationContext.getBean("userxbc"));
        //System.out.println(applicationContext.getBean(User.class));
        return "index.html";
    }
    @DataCon
    @GetMapping("/findUser")
    public List<User> findUser(String name,User user){
        System.out.println("findUser beging------");
        List<User> allUsers = tUserService.findAllUsers();
        System.out.println("findUser end------");
        return allUsers;
    }
    @DataCon
    @PostMapping("/saveStaion")
    public List<Station> saveStation(@RequestBody Station station){
        List<Station> list = new ArrayList<>();
        list.add(station);
        return list;
    }
    public static void main(String[] args) {
        System.out.println(3);
    }
}
