package com.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class HelloService {

    /*@Autowired
    private JdbcTemplate jdbcTemplate;

    public Map<String,Object> hello(String name){
        String sql = "select * from sys_user where username = ?";

        Map<String,Object> user = jdbcTemplate.queryForMap(sql,new String[]{name});

        return  user;
    }

    @Transactional(isolation = Isolation.DEFAULT)
    public int update(String name,String password){

        String selectSql = "select * from sys_user where username = ? for update";

        jdbcTemplate.queryForMap(selectSql,new String[]{name});

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String sql = "update sys_user set password = ? where username = ?";

        int status = jdbcTemplate.update(sql,new String[]{password,name});

        int i = 1/0;

        return status;
    }

    @Transactional
    public int update2(String name,String password){
        String sql = "update sys_user set password = ? where username = ?";

        int status = jdbcTemplate.update(sql,new String[]{password,name});

        return status;
    }*/
}
