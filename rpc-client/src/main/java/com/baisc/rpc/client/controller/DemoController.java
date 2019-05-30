package com.baisc.rpc.client.controller;


import com.basic.rpc.api.IUserService;
import com.basic.rpc.netty.RpcProxy;
import com.basic.rpc.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DemoController {
    @Autowired
    RpcProxy rpcProxy;

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
        IUserService userService = rpcProxy.create(IUserService.class);
        User user = userService.queryById(8);
        return user;
    }

    @GetMapping("/user/all")
    public List<User> getAll(){
        IUserService userService = rpcProxy.create(IUserService.class);
        List<User> users = userService.getAllUsers();
        return users;
    }
}
