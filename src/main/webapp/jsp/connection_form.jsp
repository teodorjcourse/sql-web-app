<div class="" id="content-container">
    <div id="modal-form-container">
        <!-- Modal -->
        <div class="modal fade" id="myModal" role="dialog" data-keyboard="false" data-backdrop="static">
            <div class="modal-dialog">
                <div class="modal-content">

                    <div class="modal-header">
                        <%--<button type="button" class="close" data-dismiss="modal">x</button>--%>
                        <h3 class="modal-title" id="modalLabel"><% out.print(I18n.text(Keys.FORM_CONNECT_TITLE)); %></h3>
                    </div>


                    <div class="modal-body">
                        <form id="login_form" name="login_form" >

                            <div class="form-group">
                                <label for="db-user-name"><% out.print(I18n.text(Keys.USERNAME_LABEL)); %></label>
                                <input type="text" class="form-control" id="db-user-name" name="username" placeholder="<% out.print(I18n.text(Keys.USERNAME_TXT)); %>">
                            </div>


                            <div class="form-group">
                                <label for="db-password"><% out.print(I18n.text(Keys.PASSWORD_LABEL)); %></label>
                                <input type="password" class="form-control" id="db-password"  name="password" placeholder="<% out.print(I18n.text(Keys.PASSWORD_TXT)); %>">
                            </div>

                            <button type="submit" class="btn btn-primary btn-block" id="button-submit-connect"><% out.print(I18n.text(Keys.CONNECT_TO_DATABASE_TXT)); %></button>

                        </form>
                    </div>

                </div>
            </div>
        </div>

    </div>
</div>

<script type="text/javascript" src="/sql-web-app/js/xml_http_request_helper.js"></script>

<script>
    $(window).on('load',function(){
        $('#myModal').modal('show');
        $("#myModal").on('hidden.bs.modal', function () {
            $('#login_form').data('formValidation').resetForm(true);
        });
    });

    $(document).ready(function() {
        $("#login_form")
            .formValidation({

                framework: 'bootstrap',
                excluded: ':disabled',
                icon: {
                    valid: 'glyphicon glyphicon-ok',
                    invalid: 'glyphicon gl' +
                    'yphicon-remove',
                    validating: 'glyphicon glyphicon-refresh'
                },
                fields: {
                    username: {
                        validators: {
                            notEmpty: {
                                message: '<% out.print(I18n.text(Keys.USERNAME_REQUIRED_ERROR_TXT)); %>'
                            }
                        }
                    },
                    password: {
                        validators: {
                            notEmpty: {
                                message: "<% out.print(I18n.text(Keys.PASSWORD_REQUIRED_ERROR_TXT)); %>"
                            }
                        }
                    }
                }
            }).on("success.form.fv", function(e) {
            // http://formvalidation.io/examples/form-submit-twice/
            // By default, after pressing the submit button, the plugin will validate fields and
            // then submit the form if all fields are valid.
            // Trigger the success.form.fv event
            // to prevent the plugin from submitting the form automatically,
            // TODO IF the modal will be reopened, it will be possible to submit again !!!!
            $('#button-submit-connect').prop('disabled', true);

            var username = $("#db-user-name").val();
            var password = $("#db-password").val();

            var url = "<% out.print(Links.ACTION_CONNECT_REQUEST); %>"
                + "username=" + username + "&" + "password=" + password;

            sendAsyncPostRequest(url);

            e.preventDefault();
        });


        function sendAsyncPostRequest(url) {
            var request = new XMLHttpRequest();

            try {
                request.onreadystatechange = function() {
                    onGetResponse(this);
                };

                request.open("POST", url, true);
                request.send();
            } catch(e) {
                // TODO Loggin logic should be implemented here !
            }
        }
        // TODO REFACTOR THIS CODE.  ITS MESSY !!!
        // FIXME Remove button first, than wait until modal is hiding and remove form on hidden.bs.modal event
        function onGetResponse(httpRequest) {
            if (httpRequest.readyState == XMLHttpRequestState.DONE) {

                try {
                    var obj = JSON.parse(httpRequest.responseText);

                    if (obj["result"] == "ok") {
                        $("#myModal").on('hidden.bs.modal', function () {
                            $('#myModal').unbind('hidden.bs.modal');
                            $('#login_form').data('formValidation').resetForm(true);
                            $('#button-submit-connect').prop('disabled', false);

                            window.location = obj["url"];
                        });

                        $("#myModal").modal('hide');
                    } else {
                        if (obj["result"] == "error") {
                            $("#myModal").on('hidden.bs.modal', function () {
                                $('#myModal').unbind('hidden.bs.modal');
                                $('#login_form').data('formValidation').resetForm(true);
                            });
//                            $("#login_form").data('formValidation').resetForm();

                            BootstrapDialog.show({
                                message: obj.message,
                                onhide: function () {
                                    $('#button-submit-connect').prop('disabled', false);
                                }
                            });
                        }
                    }

                } catch (err) {
                    console.log(err.message);
                }
            }
        }
    });

</script>