<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title> 泛娱 后台系统 - FanYu</title>
    <link rel="stylesheet" href="/static/build/css/layui.css" media="all">
    <script type="text/javascript" src="/static/js/jquery-3.2.1.js"></script>
</head>
<body>
<div class="layui-layout layui-layout-admin">
<#include "header.ftl"/>
<#include "left_sidebar.ftl"/>
    <div class="layui-body" id="maindiv">
        <!-- 内容主体区域 -->
        <div style="padding-top: 15px;padding-left: 15px;">
            <input id="keyword" class="layui-input" style="width: 200px;float:left;">
            <button id="searchButton" class="layui-btn">搜索</button>
        </div>

        <div style="padding-left: 15px;float: left">
            <table id="addScoreMes" lay-filter="addScoreMes"></table>
        </div>
        <div style="padding-left: 15px;float: left">
            <table id="test" lay-filter="test"></table>
        </div>
    </div>
<#include "footer.ftl"/>
</div>
<script src="/static/build/layui.js"></script>
<script type="text/html" id="addScoreMesarDemo">
    <a class="layui-btn layui-btn-mini" lay-event="addIntegral">上下分</a>
    <a class="layui-btn layui-btn-mini layui-btn-danger" lay-event="ignore">忽略</a>
</script>
<script type="text/html" id="barDemo">
    <a class="layui-btn layui-btn-mini" lay-event="addIntegral">上下分</a>
    <a class="layui-btn layui-btn-mini" lay-event="addzIntegral">庄分</a>
</script>
<script>
    layui.use('table', function () {
        var table = layui.table;

        var tableIns1 = table.render({ //其它参数在此省略
            elem: '#addScoreMes' //或 elem: document.getElementById('test') 等
            , cols: [[{field: 'id', title: 'ID', width: 60, sort: true}
                , {field: 'nickname', title: '昵称', width: 120, sort: true}
                , {field: 'content', title: '内容', width: 120, sort: true}
                , {field: 'addTime', title: '时间', width: 180, sort: true}
                , {fixed: 'right', width: 150, align: 'center', toolbar: '#addScoreMesarDemo'}
            ]]
            , url: 'getMesScore'
        <#--, where: {groupid: '${Session["groupid"]}'}-->
            , method: 'get'
            , page: true //开启分页
            , limit: 30 //默认采用60
            , height: 800
        });

        //监听工具条
        table.on('tool(addScoreMes)', function (obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
            var data = obj.data; //获得当前行数据
            var layEvent = obj.event; //获得 lay-event 对应的值
            if (layEvent === 'ignore') { //删除
                layer.confirm('真的忽略行么', function (index) {
                    obj.del(); //删除对应行（tr）的DOM结构
                    layer.close(index);
                    //向服务端发送删除指令
                    $.post("delMesScore"
                            , {"id": data.id}
                            , function (data) {
                                if (data.retCode == 200) {
                                } else {
                                    alert("网络错误！");
                                }
                            }
                    );
                });
            }

            if (layEvent === 'addIntegral') {
                layer.prompt(function (value, index, elem) {
                    $.post("addScoreByMes"
                            , {"username": data.username, "value": value, "id": data.id}
                            , function (data) {
                                if (data.retCode == 200) {
                                    $(".layui-laypage-btn").click();
                                } else {
                                    alert("网络错误！");
                                }
                            }
                    );
                    layer.close(index);
                });
            }
        });
        //===============================
        var tableIns2 = table.render({ //其它参数在此省略
            elem: '#test' //或 elem: document.getElementById('test') 等
            , cols: [[{field: 'id', title: 'ID', width: 60, sort: true}
                , {field: 'nickname', title: '昵称', width: 120, sort: true}
                , {field: 'integral', title: '分数', width: 120, sort: true}
                , {field: 'zintegral', title: '庄分', width: 120, sort: true}
                , {field: 'relateNickname', title: '拉手(可编辑)', width: 120, sort: true, edit: 'text'}
                , {fixed: 'right', width: 150, align: 'center', toolbar: '#barDemo'}
            ]]
            , url: 'group_member_json'
        <#--, where: {groupid: '${Session["groupid"]}'}-->
            , method: 'get'
            , page: true //开启分页
            , limit: 30 //默认采用60
            , height: 800
        });

        //监听工具条
        table.on('tool(test)', function (obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
            var data = obj.data; //获得当前行数据
            var layEvent = obj.event; //获得 lay-event 对应的值
            if (layEvent === 'addIntegral') {
                layui.use('layer', function () {
                    var layer = layui.layer;
                    layer.prompt(function (value, index, elem) {
                        $.post("addIntegral"
                                , {"id": data.id, "value": value}
                                , function (data) {
                                    if (data.retCode == 200) {
                                        $(".layui-laypage-btn").click();
                                    } else {
                                        alert(data.errorMsg);
                                    }
                                }
                        );
                        layer.close(index);
                    });
                });
            }
            if (layEvent === 'addzIntegral') {
                layer.prompt(function (value, index, elem) {
                    $.post("addzIntegral"
                            , {"value": value, "id": data.id}
                            , function (data) {
                                if (data.retCode == 200) {
                                    $(".layui-laypage-btn").click();
                                } else {
                                    alert(data.errorMsg);
                                }
                            }
                    );
                    layer.close(index);
                });
            }
        });


        //监听单元格编辑
        table.on('edit(test)', function (obj) {
            var value = obj.value //得到修改后的值
                    , objdata = obj.data //得到所在行所有键值
                    , field = obj.field; //得到字段rule
            layer.confirm('行已修改，确定要提交修改吗？', function (index) {
                layer.close(index);
                //同步更新缓存对应的值
                $.post("/user/editRelate"
                        , {"id": objdata.id, "relateNickname": value}
                        , function (data) {
                            if (data.retCode == 200) {
                            } else {
                                alert(data.errorMsg);
                                $(".layui-laypage-btn").click();
                            }
                        }
                );
            });
        });

        //===========detail===================
        function edit() {
            var keyword = $("#keyword").val();
            tableIns2.reload({
                where: { //设定异步数据接口的额外参数，任意设
                    keyword: keyword
                }
            });
        }

        $("#searchButton").click(function () {
            edit();
        });

    });
</script>
</body>
</html>