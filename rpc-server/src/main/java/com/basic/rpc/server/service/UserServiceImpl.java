package com.basic.rpc.server.service;

import com.basic.rpc.annotation.RpcService;
import com.basic.rpc.api.IUserService;
import com.basic.rpc.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;


@RpcService(IUserService.class)
public class UserServiceImpl implements IUserService {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public User queryById(Integer id) {
        String sql = "select * from tb_user where id = ?";
        User user = (User)jdbcTemplate.queryForObject(sql,new Object[]{id},new BeanPropertyRowMapper(User.class));
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        String sql = "select * from tb_user";
        List<User> users = jdbcTemplate.query(sql,new BeanPropertyRowMapper(User.class));
        return users;
    }

    @Override
    public Boolean insertUser(User user) {
        String sql = "insert into tb_user values(null, ?, ?)";
        int result = jdbcTemplate.update(sql, user.getName(), user.getAge());
        return result > 0;
    }
}
