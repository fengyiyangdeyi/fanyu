<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title> 泛娱 后台系统 - FanYu</title>
    <link rel="stylesheet" href="/static/build/css/layui.css" media="all">
    <script src="/static/build/layui.js"></script>
    <script type="text/javascript" src="/static/js/jquery-3.2.1.js"></script>
</head>
<body>
<div class="layui-layout layui-layout-admin">
<#include "header.ftl"/>
<#include "left_sidebar.ftl"/>
    <div class="layui-body" id="maindiv" style="width: 600px;">
        <!-- 内容主体区域 -->
        <div style="padding: 15px;">
            <div class="layui-tab layui-tab-card" lay-filter="docDemoTabBrief">
                <ul class="layui-tab-title">
                    <li class="layui-this">开始下注</li>
                    <li>统计下注信息</li>
                    <li>统计结果</li>
                </ul>
                <div class="layui-tab-content">
                    <div class="layui-tab-item layui-show">
                        <textarea id="ruleMesTextarea" class="layui-textarea" style="height: 710px"></textarea>
                        <button id="createRuleMesButton" class="layui-btn"
                        ">生成开始下注内容</button>
                        <button id="sendMessagetoGroupButton" class="layui-btn"">发送</button>
                    </div>
                    <div class="layui-tab-item">
                        <textarea id="refreshBetTextArea" class="layui-textarea" style="height: 710px"></textarea>
                        <button id="refreshBetButton" class="layui-btn">刷新</button>
                        <button id="sendBetButton" class="layui-btn">发送</button>
                    </div>
                    <div class="layui-tab-item">
                        <textarea id="resultTextArea" class="layui-textarea" style="height: 710px"></textarea>
                        <button id="refreshResultButton" class="layui-btn">刷新</button>
                        <button id="sendResultButton" class="layui-btn">发送</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<#include "footer.ftl"/>
</div>
<script>
    //注意：选项卡 依赖 element 模块，否则无法进行功能性操作
    layui.use(['layer', 'element'], function () {
//        var element = layui.element;
        var layer = layui.layer;

        function createRuleMes() {
            $.post("/sendMes/createRuleMes"
                    , function (data) {
                        if (data.retCode == 200) {
                            var jsonObj = data.data.mes;
                            $("#ruleMesTextarea").val(jsonObj);
                        } else {
                            alert(data.errorMsg);
                        }
                    }
            );
        }

        $("#createRuleMesButton").click(function () {
            createRuleMes();
        });

        function sendMessagetoGroup() {
            comntent=$("#ruleMesTextarea").val();
            if (comntent == null || comntent == undefined || comntent == '') {
                alert("发送内容不能为空！");
                return;
            }
            $.post("/sendMes/sendBeginXiazhuMes"
                    , {"mes": $("#ruleMesTextarea").val()}
                    , function (data) {
                        if (data.retCode == 200) {
                            $("#ruleMesTextarea").val("");
                            layer.msg('发送成功');
                        } else {
                            alert(data.errorMsg);
                        }
                    }
            );
        }

        $("#sendMessagetoGroupButton").click(function () {
            sendMessagetoGroup();
        });

        function sendBeginXiazhuMes() {
            comntent=$("#xiazhuMesTextarea").val();
            if (comntent == null || comntent == undefined || comntent == '') {
                alert("发送内容不能为空！");
                return;
            }
            $.post("/sendMes/sendBeginXiazhuMes"
                    , {"mes": $("#xiazhuMesTextarea").val()}
                    , function (data) {
                        if (data.retCode == 200) {
                            $("#ruleMesTextarea").val("");
                            layer.msg('发送成功');
                        } else {
                            alert(data.errorMsg);
                        }
                    }
            );
        }

        $("#sendBeginXiazhuMesButton").click(function () {
            sendBeginXiazhuMes();
        });

        function sendEndXiazhuMes() {
            comntent=$("#endXiazhuMesTextarea").val();
            if (comntent == null || comntent == undefined || comntent == '') {
                alert("发送内容不能为空！");
                return;
            }
            $.post("/sendMes/sendEndXiazhuMes"
                    , {"mes": $("#endXiazhuMesTextarea").val()}
                    , function (data) {
                        if (data.retCode == 200) {
                            $("#endXiazhuMesTextarea").val("");
                            layer.msg('发送成功');
                        } else {
                            alert(data.errorMsg);
                        }
                    }
            );
        }

        $("#sendEndXiazhuMesButton").click(function () {
            sendEndXiazhuMes();
        });

        function refreshBet() {
            $.get("/sendMes/refreshBet"
                    , function (data) {
                        if (data.retCode == 200) {
                            var jsonObj = data.data.mes;
                            $("#refreshBetTextArea").val(jsonObj);
                        } else {
                            alert(data.errorMsg);
                        }
                    }
            );
        }

        $("#refreshBetButton").click(function () {
            refreshBet();
        });

        function sendBetMes() {
            comntent=$("#refreshBetTextArea").val();
            if (comntent == null || comntent == undefined || comntent == '') {
                alert("发送内容不能为空！");
                return;
            }
            $.post("/sendMes/sendEndXiazhuMes"
                    , {"mes": $("#refreshBetTextArea").val()}
                    , function (data) {
                        if (data.retCode == 200) {
                            $("#refreshBetTextArea").val("");
                            layer.msg('发送成功');
                        } else {
                            alert(data.errorMsg);
                        }
                    }
            );
        }
        $("#sendBetButton").click(function () {
            sendBetMes();
        });

        //获取结果
        function refreshResult() {
            $.get("/sendMes/refreshResult"
                    , {"mes": $("#resultTextArea").val()}
                    , function (data) {
                        if (data.retCode == 200) {
                            var jsonObj = data.data.mes;
                            $("#resultTextArea").val(jsonObj);
                        } else {
                            alert(data.errorMsg);
                        }
                    }
            );
        }
        $("#refreshResultButton").click(function () {
            refreshResult();
        });

        //发送结果
        function sendResult() {
            comntent=$("#resultTextArea").val();
            if (comntent == null || comntent == undefined || comntent == '') {
                alert("发送内容不能为空！");
                return;
            }

            $.post("/rest/sendMessagetoGroup"
                    , {"mes": $("#resultTextArea").val()}
                    , function (data) {
                        if (data.retCode == 200) {
                            $("#resultTextArea").val("");
                            layer.msg('发送成功');
                        } else {
                            alert(data.errorMsg);
                        }
                    }
            );
        }
        $("#sendResultButton").click(function () {
            sendResult();
        });


    });
</script>
</body>
</html>