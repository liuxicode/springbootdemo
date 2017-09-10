package com.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/hello")
public class HelloController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @RequestMapping("/helloworld")
    public Map<String,Object> hello(@RequestParam("name") String name){

         String sql = "select * from sys_user where username = ?";

        Map<String,Object> user = jdbcTemplate.queryForMap(sql,new String[]{name});

        return user;
    }
}
