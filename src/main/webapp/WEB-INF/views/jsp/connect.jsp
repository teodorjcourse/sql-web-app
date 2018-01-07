<%@ page
        contentType="text/html;charset=UTF-8"
        language="java"
        pageEncoding="UTF-8"
%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>

<html>
<head>
    <title>Connect Page Title</title>

    <%@ include file="/resources/html/bootstrap_headers.html" %>
    <%@ include file="/resources/html/formvalidation_headers.html" %>
    <%@ include file="/resources/html/bootstrap_dialogs_headers.html" %>
    <script type="text/javascript" src="<c:url value="/resources/js/xml_http_request_helper.js"/>"></script>
</head>
<body>
    <%@ include file="connection_form.jsp"%>
</body>
</html>
