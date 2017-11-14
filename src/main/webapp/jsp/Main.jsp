<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ page import="com.juja.webapp.teodor.Links" %>
<%@ page import="com.juja.webapp.teodor.I18n" %>
<%@ page import="com.juja.webapp.teodor.Keys" %>

<html>
<head>
    <title>SQl Web Application</title>

    <%@ include file="../html/bootstrap_headers.html" %>
    <%@ include file="../html/formvalidation_headers.html" %>
    <%@ include file="../html/bootstrap_dialogs_headers.html" %>

    <script type="text/javascript" src="/sql-web-app/js/xml_http_request_helper.js"></script>
</head>
<body>

    <div id="page-container">
        <div class="list-group">
            <a href="<%= Links.ACTION_GET_TABLES_REQUEST + "p=all" %>" class="list-group-item"><%= I18n.text(Keys.MENU_ITEM_TABLES) %></a>
            <%--<a href="<%= Links.ACTION_GET_TABLES_REQUEST %>" class="list-group-item"><%= I18n.text(Keys.MENU_ITEM_INFO) %></a>--%>
        </div>
    </div>

    <button id="btn-close-connection" type="button" class="btn btn-primary">Log out</button>

    <script>
        $('#btn-close-connection').on('click', function(event) {
            event.preventDefault();

            var request = new XMLHttpRequest();

            try {
                request.onreadystatechange = function() {
                    onGetResponse(this);
                };
                request.open("POST", "/sql-web-app/action/close?", true);
                request.send();
            } catch(e) {
            }

        });

        function onGetResponse(httpRequest) {
            if (httpRequest.readyState == XMLHttpRequestState.DONE) {
                var obj = JSON.parse(httpRequest.responseText);

                console.log(httpRequest.responseTex);
                if(obj["result"] == "ok") {
                    if (!isEmpty(obj["url"])) {
                        window.location = obj["url"];
                    }
                }
            }
        }

        function isEmpty(str) {
            return (!str || 0 === str.length);
        }
    </script>
</body>
</html>
