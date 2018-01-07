package com.juja.webapp.teodor.components.services;

import com.juja.webapp.teodor.utils.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;


import static com.juja.webapp.teodor.utils.ClassNameUtil.getCurrentClassName;

@Controller
@RequestMapping({"/", "/main", "/index"})
public class MainService {
    private static final Logger logger = new Logger(getCurrentClassName());


    @RequestMapping(method = RequestMethod.GET)
    public String onGetRequest(HttpServletRequest req) {
        logger.info(req.getSession().getId() + ": got request; uri = " + req.getRequestURI());
        return "jsp/main";
    }



}
