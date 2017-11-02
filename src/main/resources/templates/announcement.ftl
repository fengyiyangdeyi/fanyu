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
            <form id="textForm" action="updateAnnouncement" method="post">
                <div class="layui-form-item">
                    <input type="hidden" id="id" name="id" value=${data.id}>
                </div>
                <label>公告</label>
                <div class="layui-form-item">
                    <textarea id="content" name="content" class="layui-textarea">${data.content}</textarea>
                </div>
                <hr class="layui-bg-green">
                <label>APP公告</label>
                <div class="layui-form-item">
                    <textarea id="appContent" name="appContent" class="layui-textarea">${data.appContent}</textarea>
                </div>
                <div class="layui-form-item">
                    <input type="submit" value="保存" class="layui-btn">
                </div>
            </form>
        </div>
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