package com.example.meetu.controller;


import com.example.meetu.dao.UserDao;
import com.example.meetu.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
@RestController
public class test {
//    @RequestMapping("/")
//    @ResponseBody
//    public String sayHello(){
//        return "hello Beijing";
//    }

    @Autowired
    UserDao userDao;

    @GetMapping("/")
    public List<User> hello() {
        User user = new User();
        user.setUsername("sjq");
        user.setPassword("sjq");

        userDao.save(user);

        return userDao.findAll();
    }
}
