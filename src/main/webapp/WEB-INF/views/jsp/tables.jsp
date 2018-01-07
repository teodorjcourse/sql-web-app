<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page errorPage = "error_page.jsp" %>
<html>
<head>
    <title>Available tables</title>
    <%@ include file="/resources/html/bootstrap_headers.html" %>
</head>
<body>
    <div>
       <h1>Available tables</h1>
    </div>

    <div class="list-group">
            <c:forEach items="${tables}" var="table">
                <a href="${pageContext.request.contextPath}/action/select?name=${table}"  class="list-group-item list-group-item-action"  width="200">${table}</a>
            </c:forEach>
    </div>
</body>
</html>

