package com.juja.webapp.teodor.app.servlets;

import com.juja.webapp.teodor.utils.DebugLoger;
import com.juja.webapp.teodor.Links;
import com.juja.webapp.teodor.WebAppAttributes;
import com.juja.webapp.teodor.controller.UserSession;
import com.juja.webapp.teodor.utils.StringUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

public class Main extends HttpServlet  {
    private final static String REQUEST_TYPE = "action";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        DebugLoger.tr("Got get request from session id = " + session.getId() + req.getRequestURI());

        UserSession userSession = (UserSession) session.getAttribute(WebAppAttributes.USER_SESSION);

        try {
            if (userSession.connected()) {
                DebugLoger.tr("Session id = " + session.getId() + " is connected");
                RequestDispatcher dispatcher = req.getRequestDispatcher(Links.MAIN_JSP);
                dispatcher.forward(req, resp);
            } else {
                DebugLoger.tr("Session id = " + session.getId() + " doesn not connected. Quiet Redirect to " + Links.CONNECT_JSP);
                RequestDispatcher dispatcher = req.getRequestDispatcher(Links.CONNECT_JSP);
                dispatcher.forward(req, resp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        DebugLoger.tr("Got post request from session id = " + session.getId() + req.getRequestURI());
        onGotRequest(req, resp);
    }

    private void onGotRequest(HttpServletRequest req, HttpServletResponse resp) {
        String requestType = StringUtils.removeSlashes(req.getServletPath());
        String actionType = req.getPathInfo();

        HttpSession session = req.getSession();
        DebugLoger.tr("Post request parameters for session id = " + session.getId() + req.getQueryString());


        switch (requestType) {
            case REQUEST_TYPE: {
                if (actionType != null) {
                    try {
                        DebugLoger.tr("session id = " + session.getId() + "redirect to " + "/" + actionType);
                        req.getRequestDispatcher("/" + actionType).forward(req, resp);
                    } catch (ServletException | IOException e) {
                        // TODO
                        e.printStackTrace();
                    }
                } else {
                    // TODO
                }
            }
            break;

        }
    }
}


