package com.juja.webapp.teodor.controller.commands;

import com.juja.webapp.teodor.controller.response.JSONResponse;
import com.juja.webapp.teodor.controller.response.ResponseProcessor;
import com.juja.webapp.teodor.model.exceptions.DataBaseRequestException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class DeleteRowCommand extends Command {

    public DeleteRowCommand(HttpServletRequest req, HttpServletResponse resp) {
        super("delete", req, resp);
    }

    @Override
    protected void executeInternal()
            throws DataBaseRequestException
    {
        String tableName = httpRequest.getParameter("name");
        String condition = null;
        try {
            condition = URLDecoder.decode(httpRequest.getParameter("where"), "UTF-8");
            String[] cond = condition.split("&|=");

            int result = databaseManager().deleteRows(connection(), tableName, cond);

            ResponseProcessor rp = createResponseProcessor();

            JSONResponse json = rp.buildSuccessJSON();
            json.setKeyValue("uid", cond[1]);
            rp.send(httpResponse, json);
        } catch (UnsupportedEncodingException e) {
            // NOP
        }
    }
}
