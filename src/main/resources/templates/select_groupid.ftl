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
    <div class="layui-side layui-bg-black">
        <div class="layui-side-scroll">
            <!-- 左侧导航区域（可配合layui已有的垂直导航） -->
            <ul class="layui-nav layui-nav-tree" lay-filter="test">
                <li class="layui-nav-item"><a href="">请选择群</a></li>
            </ul>
        </div>
    </div>
    <div class="layui-body" id="maindiv">
        <!-- 内容主体区域 -->
        <div>
            <table id="select_groupid" lay-filter="select_groupid"></table>
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
<script type="text/html" id="barDemo">
    <a lay-event="selectEvent" class="layui-btn layui-btn-mini">选择</a>
</script>
<script>
    layui.use('table', function () {
        var table = layui.table;
        var tableIns = table.render({ //其它参数在此省略
            elem: '#select_groupid' //或 elem: document.getElementById('test') 等
            , cols: [[{field: 'id', title: 'ID', width: 80, sort: true}
                , {field: 'groupid', title: '群号', width: 160, sort: true}
                , {field: 'code', title: 'code', width: 160, sort: true}
                , {field: 'groupname', title: '名称', width: 160, sort: true}
                , {field: 'nickname', title: '群主', width: 120, sort: true}
                , {fixed: 'right', width: 150, align: 'center', toolbar: '#barDemo'}
            ]]
            , url: 'selectGroup'
        <#--, where: {groupid: '${Session["groupid"]}'}-->
            , method: 'get'
            , page: true //开启分页
            , limit: 20 //默认采用60
            , height: 845
        });

        //监听工具条
        table.on('tool(select_groupid)', function (obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
            var data = obj.data; //获得当前行数据
            layui.use('layer', function () {
                $.post("setGroup"
                        , {"groupid": data.groupid}
                        , function (data) {
                            if (data.retCode == 200) {
                                window.location.href = "group_member";
                            } else {
                                alert("网络错误！");
                            }
                        }
                );
            });
        });
    });
</script>
</body>
</html>