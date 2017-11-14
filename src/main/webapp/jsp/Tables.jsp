<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.juja.webapp.teodor.I18n" %>
<%@ page import="com.juja.webapp.teodor.Keys" %>

<%@ page errorPage = "ErrorPage.jsp" %>
<html>
<head>
    <title><%= I18n.text(Keys.PAGE_TITLE_TABLES) %></title>
    <%@ include file="../html/bootstrap_headers.html" %>
</head>
<body>
    <div>
       <h1>Доступные таблицы</h1>
    </div>

    <div class="list-group">
            <c:forEach items="${tables}" var="table">
                <a href="${pageContext.request.contextPath}/action/select?name=${table}"  class="list-group-item list-group-item-action"  width="200">${table}</a>
            </c:forEach>
    </div>
</body>
</html>

