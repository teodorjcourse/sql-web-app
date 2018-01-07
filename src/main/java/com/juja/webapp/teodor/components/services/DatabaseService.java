package com.juja.webapp.teodor.components.services;

import com.juja.webapp.teodor.components.managers.ConnectionManager;
import com.juja.webapp.teodor.controller.response.JSONResponse;
import com.juja.webapp.teodor.controller.response.ResponseProcessor;
import com.juja.webapp.teodor.data.view.HTMLTableFormatter;
import com.juja.webapp.teodor.model.dao.ConnectionInfo;
import com.juja.webapp.teodor.model.dao.DataBaseManager;
import com.juja.webapp.teodor.model.dao.requests.QueryActionResult;
import com.juja.webapp.teodor.model.dao.table.Column;
import com.juja.webapp.teodor.model.dao.table.Table;
import com.juja.webapp.teodor.model.exceptions.DataBaseRequestException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Controller
public class DatabaseService {

    @Resource(name = "databaseManager")
    private DataBaseManager databaseManager;

    @Resource(name = "connectManager")
    private ConnectionManager connectionManager;

    @RequestMapping(value = "/action/get_tables", method = RequestMethod.GET)
    public String selectTable(HttpServletRequest httpRequest, ModelMap model) throws DataBaseRequestException {

        ConnectionInfo sessionConnectionInfo = connectionManager.getSessionConnectionInfo(httpRequest.getSession());
        List<String> tables = databaseManager.getTables(sessionConnectionInfo.connection());

        model.addAttribute("tables", tables);

        return "jsp/tables";
    }

    @RequestMapping(value = "/action/select", method = RequestMethod.GET)
    public String selectOnGet(HttpServletRequest httpRequest, @RequestParam String name) {
        httpRequest.setAttribute("tableName", name);
        return "/jsp/table";
    }

    @RequestMapping(value = "/action/select", method = RequestMethod.POST)
    @ResponseBody
    public String selectOnPost(HttpServletRequest httpRequest, @RequestParam String name) throws DataBaseRequestException {
        Table table = databaseManager.selectTable(connectionManager.getSessionConnectionInfo(httpRequest.getSession()).connection(), name);

        ResponseProcessor responseProcessor = new ResponseProcessor();
        JSONResponse json = responseProcessor.buildSuccessJSON();

        json.setKeyValue("table_data", new HTMLTableFormatter(table).HTMLString());

        StringBuilder cols = new StringBuilder();
        Iterator<Column> it = table.columns().iterator();

        for (Column c : table.columns()) {
            // FIXME
            if (c.name().equals("uid")) {
                continue;
            }

            if (cols.length() > 0) {
                cols.append(",");
            }
            cols.append(c.name());
        }

        json.setKeyValue("columns", cols.toString());

        return json.JSONString();
    }

    @RequestMapping(value = "/action/insert", method = RequestMethod.POST)
    @ResponseBody
    public String insert(@RequestParam Map<String, String> requestParams, HttpServletRequest httpRequest) throws DataBaseRequestException {
        Connection connection = connectionManager.getSessionConnectionInfo(httpRequest.getSession()).connection();
        String tableName = requestParams.get("name");

        try {
            String columnsKP = URLDecoder.decode(requestParams.get("cols"), "UTF-8");
            String[] keyValuePairs = columnsKP.split("&|=");
            boolean uidTableExists = false;

            QueryActionResult result;

            try {
                DatabaseMetaData md = connection.getMetaData();
                ResultSet rs = md.getColumns(null, null, tableName, "uid");
                uidTableExists = rs.next();
                rs.close();
            } catch (SQLException e) {
                // NOP
            }

            if (uidTableExists) {
                result = databaseManager.insertRow(connection, tableName, keyValuePairs, new String[] {"uid"});
            } else {
                result = databaseManager.insertRow(connection, tableName, keyValuePairs);
            }

            ResponseProcessor rp = new ResponseProcessor();
            JSONResponse json = rp.buildSuccessJSON();

            if (!result.isEmpty()) {
                json.setKeyValue("uid", "row_" + result.result());
                json.setKeyValue("class", "clickable-row");
            } else {
                json.setKeyValue("class", "not-clickable-row");
            }

            return json.JSONString();
        } catch (UnsupportedEncodingException e) {
            // NOP
        }

        return "{}";
    }

    @RequestMapping(value = "/action/update", method = RequestMethod.POST)
    @ResponseBody
    public String update(@RequestParam Map<String, String> requestParams, HttpServletRequest httpRequest) throws DataBaseRequestException, UnsupportedEncodingException {
        Connection connection = connectionManager.getSessionConnectionInfo(httpRequest.getSession()).connection();

        String tableName = requestParams.get("name");
        String uid = requestParams.get("uid");
        String updateData = URLDecoder.decode(requestParams.get("updateData"), "UTF-8");

        int result = databaseManager.updateRows(connection, tableName, "uid", uid, updateData.split("&|="));

        JSONResponse json = new ResponseProcessor().buildSuccessJSON();

        return json.JSONString();
    }

    @RequestMapping(value = "/action/delete", method = RequestMethod.POST)
    @ResponseBody
    public String delete(@RequestParam Map<String, String> requestParams, HttpServletRequest httpRequest) throws DataBaseRequestException, UnsupportedEncodingException {
        Connection connection = connectionManager.getSessionConnectionInfo(httpRequest.getSession()).connection();

        String tableName = requestParams.get("name");

        try {
            String condition = URLDecoder.decode(requestParams.get("where"), "UTF-8");
            String[] cond = condition.split("&|=");

            int result = databaseManager.deleteRows(connection, tableName, cond);

            ResponseProcessor rp = new ResponseProcessor();

            JSONResponse json = rp.buildSuccessJSON();
            json.setKeyValue("uid", cond[1]);

            return json.JSONString();
        } catch (UnsupportedEncodingException e) {
            return "{}";
        }
    }

}
