package com.demo.controller;

import org.springframework.session.ExpiringSession;
import org.springframework.session.SessionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("session")
public class SessionController {

    @Resource(name="sessionRepository")
    private SessionRepository<ExpiringSession> sessionRepository;

    @RequestMapping("delSession")
    public Integer delSession(@RequestParam("sessionid") String sessionid){

        sessionRepository.delete(sessionid);

        return 1;
    }

    @RequestMapping("set")
    public String setSession(@RequestParam("name") String name,
                             HttpServletRequest request){


        HttpSession session = request.getSession();

        session.setAttribute("name",name);

        return name;
    }

    @RequestMapping("get")
    public Object getSession(HttpServletRequest request){


        HttpSession session = request.getSession();

        return session.getAttribute("name");
    }

    @RequestMapping("getSessionId")
    public Object getSessionId(HttpServletRequest request){


        HttpSession session = request.getSession();

        return session.getId();
    }


}
