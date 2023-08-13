package com.example.zbsmaster.controller;

import com.example.zbsmaster.dao.UserMapper;

import com.example.zbsmaster.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserMapper userMapper;

    @GetMapping("/queryUserList")
    public List<User> queryUserList(){
        List<User> users = userMapper.queryUserList();
        for (User user :
                users) {
            System.out.println(user);
        }
        return users;
    }
    @GetMapping("/queryOne/{id}")
    public User queryOne(@PathVariable String id){
        User user = userMapper.queryUserById(id);
        System.out.println(user);
        return user;
    }
    @PostMapping("/addUser")
    public int addUser(@RequestBody User user){
        int i = userMapper.addUser(user);
        System.out.println(i);
        return i;
    }
    @PostMapping("/updateUser")
    public int updateUser(@RequestBody User user){
        int i = userMapper.updateUser(user);
        System.out.println(i);
        return i;
    }
    @GetMapping("/deleteUser/{id}")
    public int deleteUser(@PathVariable String id){
        return userMapper.deleteUser(id);
    }
}
