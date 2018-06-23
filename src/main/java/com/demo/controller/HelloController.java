package com.demo.controller;

import com.demo.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/hello")
public class HelloController {

    @Autowired
    private HelloService helloService;

    @RequestMapping("/test")
    public String test(@RequestParam("name") String name){

        return "test1: hello world"+ name;
    }

    @RequestMapping("/update")
    public int update(@RequestParam("name") String name,
                      @RequestParam("password") String password){
        return helloService.update(name,password);
    }

    @RequestMapping("/update2")
    public int update2(@RequestParam("name") String name,
                      @RequestParam("password") String password){
        return helloService.update2(name,password);
    }
}
