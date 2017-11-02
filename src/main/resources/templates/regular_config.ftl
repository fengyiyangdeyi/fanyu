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
                <option value="1">上下分</option>
                <option value="2">下注</option>
            </select>
            <input id="headRegularContent" class="layui-input" style="width: 200px;float:left;" value="我的+数字">
            <button class="layui-btn" onclick="edit()">添加</button>
        </div>
        <div>
            <table id="regular_list" lay-filter="regular_list"></table>
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
    <a class="layui-btn layui-btn-small layui-btn-danger" lay-event="del">删除</a>
</script>
<script>
    layui.use('table', function () {
        var table = layui.table;
        var tableIns = table.render({ //其它参数在此省略
            elem: '#regular_list' //或 elem: document.getElementById('test') 等
            , cols: [[{field: 'id', title: 'ID', width: 80, sort: true}
                , {field: 'name', title: '名称', width: 120, sort: true}
                , {field: 'regularContent', title: '格式', width: 200, sort: true, edit: 'text'}
                , {fixed: 'right', width: 150, align: 'center', toolbar: '#barDemo'}
            ]]
            , url: 'getRegular'    <#--, where: {groupid: '${Session["groupid"]}'}-->
            , method: 'get'
            , page: false //开启分页
            , limit: 10 //默认采用60
        });

        //监听单元格编辑
        table.on('edit(regular_list)', function(obj){
            var value = obj.value //得到修改后的值
                    ,data = obj.data //得到所在行所有键值
                    ,field = obj.field; //得到字段
//            layer.msg('[ID: '+ data.id +'] ' + field + ' 字段更改为：'+ value);
            layer.confirm('行已修改，确定要提交修改吗？', function (index) {
                layer.close(index);
                //同步更新缓存对应的值
                $.post("editRegularById"
                        , {"id": data.id, "regularContent": value}
                        , function (data) {
                            if (data.retCode == 200) {
//                                    $(".layui-laypage-btn").click();
                            } else {
                                alert("网络错误！");
                            }
                        }
                );
            });
        });

        //监听工具条
        table.on('tool(regular_list)', function (obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
            var data = obj.data; //获得当前行数据
            var layEvent = obj.event; //获得 lay-event 对应的值
            if (layEvent === 'del') { //删除
                layer.confirm('真的删除行么', function (index) {
                    obj.del(); //删除对应行（tr）的DOM结构
                    layer.close(index);
                    //向服务端发送删除指令
                    $.post("delRegular"
                            , {"id": data.id}
                            , function (data) {
                                if (data.retCode == 200) {
//                                    $(".layui-laypage-btn").click();
                                } else {
                                    alert("网络错误！");
                                }
                            }
                    );
                });
            }
        });
    });
</script>

<script>
    function edit() {
        $.post("editRegular"
                , {
                    "regularType": $('#headSelect').val(),
                    "regularContent": $('#headRegularContent').val(),
                    "name": $("#headSelect").find("option:selected").text()
                }
                , function (data) {
                    if (data.retCode == 200) {
                        window.location.href = 'regular_config';
                    } else {
                        alert("网络错误！");
                    }
                }
        );
    }
</script>
</body>
</html>