<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title> 泛娱 后台系统 - FanYu</title>
    <link rel="stylesheet" href="/static/build/css/layui.css" media="all">
</head>
<body>
<div class="layui-layout layui-layout-admin">
<#include "header.ftl"/>
<#include "left_sidebar.ftl"/>
    <div class="layui-body" id="maindiv">
        <!-- 内容主体区域 -->
        <div style="padding: 15px;">
            <select id="headSelect" class="layui-select" style="float: left">
                <option value="rule_niuniu">牛牛</option>
            </select>
            <button class="layui-btn" onclick="edit()">添加新模板</button>
        </div>
    </div>
<#include "footer.ftl"/>
</div>
<script src="/static/build/layui.js"></script>
<script type="text/javascript" src="/static/js/jquery-3.2.1.js"></script>
<script>
    function edit() {
        $.post("createRule"
                , {
                    "pageName": $('#headSelect').val(),
                    "name": $("#headSelect").find("option:selected").text()
                }
                , function (data) {
                    if (data.retCode == 200) {
                        window.location.href = "rule";
                    } else {
                        alert("网络错误！");
                    }
                }
        );
    }
</script>
</body>
</html>