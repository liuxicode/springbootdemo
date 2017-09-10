package com.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class HelloService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Map<String,Object> hello(String name){
        String sql = "select * from sys_user where username = ?";

        Map<String,Object> user = jdbcTemplate.queryForMap(sql,new String[]{name});

        return  user;
    }
}
