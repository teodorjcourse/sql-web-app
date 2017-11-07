package com.juja.webapp.teodor.components.servlets;
import com.juja.webapp.teodor.Links;
import com.juja.webapp.teodor.WebAppAttributes;
import com.juja.webapp.teodor.controller.UserSession;
import com.juja.webapp.teodor.utils.StringUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

import static com.juja.webapp.teodor.utils.ClassNameUtil.getCurrentClassName;

public class Main extends HttpServletBase  {
    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(getCurrentClassName());

    private final static String REQUEST_TYPE = "action";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        trInfo(logger, session.getId() + ": got request; uri = " + req.getRequestURI());

        UserSession userSession = (UserSession) session.getAttribute(WebAppAttributes.USER_SESSION);

        try {
            if (userSession.connected()) {
                trInfo(logger, session.getId() + ": is connected. Redirect to main.jsp");

                RequestDispatcher dispatcher = req.getRequestDispatcher(Links.MAIN_JSP);
                dispatcher.forward(req, resp);
            } else {
                trInfo(logger, session.getId() + ": doesn't connected. Redirect to connect.jsp");

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

        trInfo(logger, session.getId() + ": got post request; uri = " + req.getRequestURI());

        onGotRequest(req, resp);
    }

    private void onGotRequest(HttpServletRequest req, HttpServletResponse resp) {
        String requestType = StringUtils.removeSlashes(req.getServletPath());
        String actionType = req.getPathInfo();

        HttpSession session = req.getSession();

        trInfo(logger, session.getId() + ": request parameters; uri = " + req.getQueryString());

        switch (requestType) {
            case REQUEST_TYPE: {
                if (actionType != null) {
                    try {
                        trInfo(logger, session.getId() + ": request_type = " + requestType + "; action_type = " + actionType + ".");
                        trInfo(logger, session.getId() + ": redirect to /" + actionType);

                        req.getRequestDispatcher("/" + actionType).forward(req, resp);
                    } catch (ServletException | IOException e) {
                        trError(logger, session.getId() + ": error.", e);
                    }
                } else {
                    trWarn(logger, session.getId() + ": action type is not specified");
                }
            }
            break;

        }
    }
}


