package com.basic.rpc.server.controller;

import com.basic.rpc.api.IUserService;
import com.basic.rpc.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class IndexController {

    @Autowired
    IUserService userService;


    /**
     * first request confirm
     * @return
     */
    @GetMapping(("/"))
    public String index(){
        return "Hello,World!";
    }


    @GetMapping("/user/{id}")
    public User queryById(@PathVariable(value = "id") Integer id){
        return userService.queryById(id);
    }


    @GetMapping("/user/all")
    public List<User> list(){
        return userService.getAllUsers();
    }

}
