package com.juja.webapp.teodor.components.interceptors;

import com.juja.webapp.teodor.components.managers.ConnectionManager;
import com.juja.webapp.teodor.model.dao.ConnectionInfo;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class RequestsInterceptorHanlder extends HandlerInterceptorAdapter {
    @Resource(name = "connectManager")
    private ConnectionManager connectionManager;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession httpSession = request.getSession();
        ConnectionInfo sessionConnectionInfo = connectionManager.getSessionConnectionInfo(httpSession);


        if (connectPageIsRequested(request) || sessionConnectionInfo != null && sessionConnectionInfo.connected()) {
            return super.preHandle(request, response, handler);
        } else {
            response.sendRedirect(request.getContextPath() + "/connect");
            return false;
        }
    }

    private boolean connectPageIsRequested(HttpServletRequest request) {
        return request.getServletPath().startsWith("/connect") || request.getServletPath().startsWith("/action/connect");
    }
}
