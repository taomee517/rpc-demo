package com.basic.rpc.api;

import com.basic.rpc.pojo.User;

import java.util.List;

public  interface IUserService {
    User queryById(Integer id);
    List<User> getAllUsers();
    Boolean insertUser(User user);
}
