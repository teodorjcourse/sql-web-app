package com.juja.webapp.teodor.components.services;

import com.juja.webapp.teodor.components.managers.ConnectionManager;
import com.juja.webapp.teodor.controller.response.JSONResponse;
import com.juja.webapp.teodor.controller.response.ResponseProcessor;
import com.juja.webapp.teodor.model.dao.ConnectionInfo;
import com.juja.webapp.teodor.model.exceptions.DataBaseRequestException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class ConnectService {
    @Resource(name = "connectManager")
    private ConnectionManager connectionManager;


    @RequestMapping("connect")
    public String connectForm() {
        return "jsp/connect";
    }

    @RequestMapping(value = "action/connect", method = RequestMethod.POST)
    @ResponseBody
    public String connect(
            HttpServletRequest httpRequest, HttpServletResponse httpResponse,
            @RequestParam String username, @RequestParam String password
    ) {
        try {
            connectionManager.createConnection(httpRequest.getSession(), username, password);

            ResponseProcessor responseProcessor = new ResponseProcessor();
            JSONResponse json = responseProcessor.buildSuccessJSON();
            String contextPath = httpRequest.getContextPath();

            json.setKeyValue("url", contextPath);

            return  json.JSONString();
        } catch (DataBaseRequestException e) {
            ResponseProcessor responseProcessor = new ResponseProcessor();
            JSONResponse json = responseProcessor.buildErrorJSON(e, httpRequest, "Connection error");

            return json.JSONString();
        }

    }

    @RequestMapping(value = "action/close", method = RequestMethod.POST)
    @ResponseBody
    public String closeConnection(HttpServletRequest httpRequest) {

        ConnectionInfo connectionInfo = connectionManager.getSessionConnectionInfo(httpRequest.getSession());

        if (connectionInfo != null && connectionInfo.connected()) {
            try {
                connectionManager.closeConnection(httpRequest.getSession());
            } catch (DataBaseRequestException e) {
            }
        }

        ResponseProcessor responseProcessor = new ResponseProcessor();
        JSONResponse json = responseProcessor.buildSuccessJSON();
        String contextPath = httpRequest.getContextPath();

        json.setKeyValue("url", contextPath);

        return json.JSONString();
    }

}
