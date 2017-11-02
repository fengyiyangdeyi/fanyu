<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>登录</title>
    <link rel="stylesheet" href="/static/build/css/layui.css" media="all">
    <style type="text/css">
        /*body {*/
            /*background-image: url(/static/images/backgroup1.png);*/
        /*}*/

        .login {
            padding-top: 15%;
            width: 26%;
        }

        .btn-center {
            text-center: center;
            margin: 0 auto;
        }
    </style>
</head>
<body style="text-align:center">
<div style="margin:0 auto">
    <img src="/static/images/backgroup1.png">
</div>
<div class="clear box layui-main login" style="position: absolute;top: 0px;left: 50%;">
    <div hidden id="mes" style="color:red;"></div>
    <fieldset class="layui-elem-field" style="width: 350px;border-radius:8px" hidden id="usernameLoginDiv">
        <legend>登录</legend>
        <div class="formbox">
            <div class="layui-form-item">
                <label class="layui-form-label">帐号</label>
                <div class="layui-input-inline">
                    <input type="text" id="username" name="username" required lay-verify="required" placeholder="帐号"
                           autocomplete="off" class="layui-input" value="admin">
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">密码</label>
                <div class="layui-input-inline">
                    <input type="password" id="password" name="password" required lay-verify="required" placeholder="请输入密码"
                           autocomplete="off" class="layui-input" value="fanyu1">
                </div>
                <div class="layui-form-mid layui-word-aux"></div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">群号id</label>
                <div class="layui-input-inline">
                    <input type="text" id="groupid" name="groupid" placeholder="群号id"
                           autocomplete="off" class="layui-input" value="">
                </div>
            </div>
            <div class="layui-form-item">
                <div class="layui-input-block">
                    <button class="layui-btn" lay-submit lay-filter="reg" onclick="userLogin()">登录</button>
                    <button type="reset" class="layui-btn layui-btn-primary">重置</button>
                </div>
            </div>
        </div>
    </fieldset>
    <div id="QrCodeLoginDiv" style="text-align:center">
        <img id="QrCodeImg"/>
    </div>
    <div style="text-align:center;font-size: 18px;">
        <a onclick="switchLogin()" id="swichLable" style="color:#ffffff">切换到密码登录</a>
    </div>
</div>
<script src="/static/build/layui.js"></script>
<script type="text/javascript" src="/static/js/jquery-3.2.1.js"></script>
<script type="text/javascript">
    var falg = true;
    var uuid;
    $(document).ready(function () {
        $.get("/QrCodeLoginPro/GetQrCodeServlet", function (data, status) {
            var obj = eval("(" + data + ")");
            //存储UUID
            uuid = obj.uuid;
            //显示二维码
            $("#QrCodeImg").attr("src", obj.qrCodeImg);
            //开始验证登录
            validateLogin();
        });

        validateLogin = function validateLogin() {
            if (!falg) {
                return;
            }
            $.get("/QrCodeLoginPro/LongConnectionCheckServlet?uuid=" + uuid, function (data, status) {
                if (data == "") {
                    validateLogin();
                } else {
//                    var obj = eval("(" + data + ")");
//                    alert("登录成功了:" + obj.uname);
                    $(location).attr('href', 'group_member');
                }
            });
        }

    });

    function switchLogin() {
        if (falg) {
            $("#usernameLoginDiv").show();
            $("#QrCodeLoginDiv").hide();
            falg = false;
            $("#swichLable").text("切换到扫码登录");
        } else {
            $("#QrCodeLoginDiv").show();
            $("#usernameLoginDiv").hide();
            falg = true;
            $("#swichLable").text("切换到密码登录");
            $.get("/QrCodeLoginPro/GetQrCodeServlet", function (data, status) {
                var obj = eval("(" + data + ")");
                console.log(obj);
                //存储UUID
                uuid = obj.uuid;
                //显示二维码
                $("#QrCodeImg").attr("src", obj.qrCodeImg);
                //开始验证登录
                validateLogin();
            });
        }
    }

    function userLogin() {
        $.post("login"
                , {"username": $('#username').val(),"password": $('#password').val(),"groupid":$('#groupid').val()}
                , function (data) {
                    if(data.retCode==200){
                        window.location.href="select_groupid";
                    }else {
                        $('#mes').show();
                        $('#mes').text(data.errorMsg);
                    }
                }
        );
    }
</script>
</body>
</html>