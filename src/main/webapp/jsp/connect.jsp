<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ page import="com.juja.webapp.teodor.I18n" %>
<%@ page import="com.juja.webapp.teodor.Keys" %>

<%-- Do not remove this import. It's needed for included connection_form.jsp --%>
<%@ page import="com.juja.webapp.teodor.Links" %>

<html>
<head>
    <title><% out.print(I18n.text(Keys.PAGE_TITLE_CONNECT)); %></title>

    <%@ include file="../html/bootstrap_headers.html" %>
    <%@ include file="../html/formvalidation_headers.html" %>
    <%@ include file="../html/bootstrap_dialogs_headers.html" %>
</head>
<body>
    <%@ include file="connection_form.jsp"%>
</body>
</html>
