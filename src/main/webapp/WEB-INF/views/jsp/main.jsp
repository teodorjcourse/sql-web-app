<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>

<html>
<head>
    <title>SQl Web Application</title>

    <%@ include file="/resources/html/bootstrap_headers.html" %>

    <%@ include file="/resources/html/bootstrap_headers.html" %>
    <%@ include file="/resources/html/formvalidation_headers.html" %>
    <%@ include file="/resources/html/bootstrap_dialogs_headers.html" %>

    <script type="text/javascript" src="<c:url value="/resources/js/xml_http_request_helper.js" /> "></script>
</head>
<body>

    <div id="page-container">
        <div class="list-group">
            <a href="/sql-web-app/action/get_tables?p=all" class="list-group-item">Available tables</a>
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
