<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="static com.juja.webapp.teodor.utils.ClassNameUtil.getCurrentClassName" %>
<%@ page errorPage = "ErrorPage.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>${tableName}</title>
    <%@ include file="../html/bootstrap_headers.html" %>
    <%@ include file="../html/bootstrap_dialogs_headers.html" %>


    <script type="text/javascript" src="/sql-web-app/js/xml_http_request_helper.js"></script>
    <script type="text/javascript" src="/sql-web-app/js/RequestController.js"></script>
</head>
<body>
    <%!
        public static final Logger logger  = Logger.getLogger(getCurrentClassName());
    %>
    <h1>Режим редактирования таблиц: ${tableName}</h1>

    <div class="modal fade" id="modal-form" role="dialog" data-keyboard="false" data-backdrop="static">
        <div class="modal-dialog">
            <div class="modal-content">

                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">x</button>
                    <h3 class="modal-title" id="modalLabel">Header</h3>
                </div>


                <div class="modal-body">
                    <form id="login_form" name="login_form" >

                        <div class="form-group hide" id="template">
                            <label for="fake">fsefsefsf</label>
                            <input type="text" class="form-control" id="fake"  name="" placeholder="fake">
                        </div>

                        <div id="fields">

                        </div>

                        <button type="button" class="btn btn-primary btn-block" id="button-update">Update</button>
                        <button type="button" class="btn btn-primary btn-block" id="button-close" data-dismiss="modal">Close</button>

                    </form>
                </div>

            </div>
        </div>
    </div>


    <div>
        <button id="button-new" type="button" class="btn btn-primary" data-toggle="modal" data-target="#modal-form">New</button>
        <button id="button-edit" type="button" class="btn btn-primary" data-toggle="modal" data-target="#modal-form">Edit</button>
        <button id="button-delete" type="button" class="btn btn-primary" data-toggle="modal" data-target="#modal-form">Delete</button>
    </div>

    <table id="table" class="table table-bordered" cellspacing="0" width="100%">

    </table>

    <script type="text/javascript" src="/sql-web-app/js/xml_http_request_helper.js"></script>

    <script>
        var rowId;
        var tableColumns;

        $(document).ready(function() {
            $('#button-edit').prop('disabled', true);
            $('#button-delete').prop('disabled', true);

            $("#modal-form").on('hidden.bs.modal', function () {
            });

            $('#table').on('click', '.clickable-row', function(event) {
                if($(this).hasClass('bg-primary')){
                    rowId = null;

                    $(this).removeClass('bg-primary');

                    $('#button-edit').prop('disabled', true);
                    $('#button-delete').prop('disabled', true);
                } else {
                    rowId = this.id;

                    $(this).addClass('bg-primary').siblings().removeClass('bg-primary');

                    $('#button-edit').prop('disabled', false);
                    $('#button-delete').prop('disabled', false);
                }
            });

            $('#button-new').on('click', onCreateRowButtonClick);
            $('#button-edit').on('click', onEditButtonClick);
            $('#button-delete').on('click', onDeleteRowButtonClick);

            updateAsyncData();
        });

        function updateAsyncData() {
            var request = new XMLHttpRequest();

            try {
                <%
                    logger.info(session.getId() + ": call update for table " + request.getAttribute("tableName"));
                %>

                request.onreadystatechange = function() {
                    <%
                        logger.info(session.getId() + ": got response");
                    %>

                    console.log(this.responseText);

                    updateTable(this);
                };

                request.open("POST", "${pageContext.request.contextPath}/action/select?name=${tableName}", true);
                request.send(null);
            } catch(e) {
                console.log(e.message);
            }
        }

        function updateTable(httpRequest) {
            if (httpRequest.readyState == XMLHttpRequestState.DONE) {
                try {
                    var obj = JSON.parse(httpRequest.responseText);

                    if (obj["result"] == "ok") {
                        $("#table").empty();

                        var tableDataArray = obj["table_data"];
                        if (tableDataArray) {
                            $("#table").append(tableDataArray);
                        }

                        if (obj["columns"]) {
                            tableColumns = obj["columns"].split(",");
                        }
                    }
                } catch (error) {

                }

            }
        }

        function onEditButtonClick(event) {
            event.preventDefault();

            var params = {};
            params["action_button"] = {
                label: "Update",
                callback: sendAsyncUpdate
            };

            params["table_data"] = [];

            var $row = $('#' + rowId);
            var columns = $row.children();

            for (var i = 0; i < columns.length; i++) {
                var data = {};
                data[columns[i].attributes["name"].value] = columns[i].innerHTML;
                params["table_data"].push(data)
            }

            createEntryForm(params);
        }

        function onCreateRowButtonClick(event) {
            event.preventDefault();

            var params = {};
            params["action_button"] = {
                label: "Create",
                callback: sendAsyncCreate
            };

            params["table_data"] = [];

            for (var i = 0; i < tableColumns.length; i++) {
                var data = {};
                data[tableColumns[i]] = "";
                params["table_data"].push(data)
            }


            createEntryForm(params);
        }

        function onDeleteRowButtonClick(event) {
            event.preventDefault();

            var params = {};
            params["action_button"] = {
                label: "Delete",
                callback: sendAsyncDelete
            };

            params["table_data"] = [];

            var $row = $('#' + rowId);
            var columns = $row.children();

            for (var i = 0; i < columns.length; i++) {
                var data = {};
                data[columns[i].attributes["name"].value] = columns[i].innerHTML;
                params["table_data"].push(data)
            }

            createEntryForm(params);
        }

        function createEntryForm(params) {
            $("#fields").empty();

            var template = $('#template');

            for (var i = 0; i < params["table_data"].length; i++) {
                var col_name = Object.keys(params["table_data"][i])[0];
                var col_value = params["table_data"][i][col_name];

                var $clone = template
                    .clone()
                    .removeClass('hide')
                    .removeAttr('id')
                    .insertBefore(template);

                var $labelElement = $clone.find('label');
                var $inputElement = $clone.find('input');

                $labelElement.html(col_name);
                $labelElement.attr("for", col_name);

                $inputElement.attr("id", col_name);
                $inputElement.attr("name", col_name);
                $inputElement.attr("placeholder", col_value);

                $("#fields").append($clone);
            }

            if (params != null) {
                if (params["action_button"] != null) {

                    var $btn = $('#button-update');

                    $btn.text(params["action_button"]["label"]);

                    $btn.off('click');

                    $btn.on('click', params["action_button"]["callback"]);
                }
            }
        }


        function sendAsyncUpdate(event) {
            event.preventDefault();

            $('#modal-form').modal('hide');

            var request = new XMLHttpRequest();

            try {
                request.onreadystatechange = function () {
                    if (this.readyState == XMLHttpRequestState.DONE) {
                        try {
                            var obj = JSON.parse(this.responseText);
                            console.log(this.responseText);
                        } catch (error) {
                        }
                    }
                }
            } catch(error) {

            }

            var queryString = "";
            var $selectedRow = $("#" + rowId);

            $("#fields :input").each(function(){
                if (queryString != "") {
                    queryString+="&";
                }

                var $input = $(this);

                $selectedRow.find("td[name='" +  $input.attr("id")+ "']").text($input.val());

                queryString += $input.attr("id") + "=" + $input.val();
            });

            var keyValue =  rowId.split("_")[1];


            request.open("POST", "${pageContext.request.contextPath}/action/update?name=${tableName}&updateData=" +  encodeURIComponent(queryString) + "&uid=" + keyValue, true);
            request.send(null);
        }

        function sendAsyncCreate(event) {
            event.preventDefault();

            $('#modal-form').modal('hide');

            var request = new XMLHttpRequest();

            try {
                request.onreadystatechange = function() {
                    if (this.readyState == XMLHttpRequestState.DONE) {
                        try {
                            var obj = JSON.parse(this.responseText);

                            if (obj != null) {
                                if (obj["result"] == "ok") {
                                    $("#new").removeAttr('style');
                                    $("#new").attr('class', obj["class"] || '');
                                    $("#new").attr('id', obj["uid"] || '');

                                } else if (obj["result"] == "error") {
                                    $("#new").css('background-color', 'red');

                                    BootstrapDialog.show({
                                        message: obj.message,
                                        onhide: function () {
                                            $("#new").remove();
                                        }
                                    });
                                }
                            }

                        } catch (error) {
                        }
                    }
                };


                var queryString = "";

                var newElement = "<tr id='new'>";

                $("#fields :input").each(function(){
                    if (queryString != "") {
                        queryString+="&";
                    }

                    var $input = $(this);

                    newElement += "<td name='" + $input.attr("id") + "'>";
                    newElement += $input.val();
                    newElement += "</td>";

                    queryString += $input.attr("id") + "=" + $input.val();
                });
                newElement += "</tr>";

                console.log(newElement);

                $("#table").find("tbody").append(newElement);
                $("#new").css('background-color', 'green');

                request.open("POST", "${pageContext.request.contextPath}/action/insert?name=${tableName}&cols=" +  encodeURIComponent(queryString), true);
                request.send(null);
            } catch(e) {
                console.log(e.message);
            }
        }

        function sendAsyncDelete(event) {
            $('#modal-form').modal('hide');

            var request = new XMLHttpRequest();

            try {
                request.onreadystatechange = function () {
                    if (this.readyState == XMLHttpRequestState.DONE) {
                        try {
                            var obj = JSON.parse(this.responseText);

                            if (obj != null) {
                                $("#row_" + obj["uid"]).remove();
                            }
                        } catch (error) {
                        }
                    }
                }
            } catch (error) {

            }

            var cond = "uid=" + rowId.split("_")[1];

            request.open("POST", "${pageContext.request.contextPath}/action/delete?name=${tableName}&where=" + encodeURIComponent(cond), true);
            request.send(null);
        }

    </script>


</body>
</html>
