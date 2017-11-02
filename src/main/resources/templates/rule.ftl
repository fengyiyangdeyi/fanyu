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
        <#--${tmp}-->
        <#include "rule_niuniu.ftl"/>
    </div>
<#include "footer.ftl"/>
</div>
<script src="/static/build/layui.js"></script>
<script>
    //JavaScript代码区域
    layui.use('element', function () {
        var element = layui.element;
    });
</script>
<script type="text/javascript" src="/static/js/jquery-3.2.1.js"></script>
</body>
</html>