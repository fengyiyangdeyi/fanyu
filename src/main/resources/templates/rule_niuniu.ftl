<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title> 泛娱 后台系统 - FanYu</title>
    <link rel="stylesheet" href="/static/build/css/layui.css" media="all">
    <script type="text/javascript" src="/static/js/jquery-3.2.1.js"></script>
    <script src="/static/build/layui.js"></script>
    <script src="/templates/js/rule_niuniu.js"></script>
</head>
<body>
<div class="layui-layout layui-layout-admin">
<#include "header.ftl"/>
<#include "left_sidebar.ftl"/>
    <div class="layui-body" id="maindiv">
        <!-- 内容主体区域 -->
        <div style="padding: 15px;">
            <form class="layui-form">
                <fieldset class="layui-elem-field" style="width:600px">
                    <legend>基本设置</legend>
                    <div class="layui-form-item" id="bankerDiv">
                        <label class="layui-form-label">选定庄家：</label>
                        <div class="layui-input-block" style="width: 460px">
                            <select id="banker" lay-verify="banker" lay-search>
                            </select>
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label">上庄分数要求：</label>
                        <span class="layui-input-inline">
                        <input id="minBankerScore" placeholder="请输入" autocomplete="off"
                               class="layui-input" style="width:460px">
                    </span>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label">红包超时(秒)：</label>
                        <span class="layui-input-inline">
                        <input id="timeout" placeholder="请输入" autocomplete="off"
                               class="layui-input" style="width:460px">
                    </span>
                    </div>
                    <div class="layui-form-item layui-inline">
                        <label class="layui-form-label">下注范围：</label>
                        <div class="layui-input-inline" style="width: 100px;">
                            <input id="minBet" placeholder="￥" autocomplete="off" class="layui-input">
                        </div>
                        <div class="layui-form-mid">-</div>
                        <div class="layui-input-inline" style="width: 100px;">
                            <input id="maxBet" placeholder="￥" autocomplete="off" class="layui-input">
                        </div>
                    </div>
                    <br>
                    <div class="layui-form-item layui-inline">
                        <label class="layui-form-label">梭哈范围：</label>
                        <div class="layui-input-inline" style="width: 100px;">
                            <input id="minsuoha" placeholder="￥" autocomplete="off" class="layui-input">
                        </div>
                        <div class="layui-form-mid">-</div>
                        <div class="layui-input-inline" style="width: 100px;">
                            <input id="maxsuoha" placeholder="￥" autocomplete="off" class="layui-input">
                        </div>
                    </div>
                </fieldset>
                <hr class="layui-bg-green" style="width:600px">

                <fieldset class="layui-elem-field" style="width:600px">
                    <legend>抽水设置</legend>
                    <div class="layui-field-box">
                        <div class="layui-form-item">
                            <label class="layui-form-label">上庄抽水：</label>
                            <span class="layui-input-inline">
                        <input id="szcvalue" placeholder="请输入" autocomplete="off"
                               class="layui-input" style="width:460px">
                    </span>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">盈利抽水：</label>
                            <span class="layui-input-inline">
                        <input id="ylcvalue" placeholder="请输入" autocomplete="off"
                               class="layui-input" style="width:460px">
                    </span>
                        </div>
                        <div class="layui-form-item">
                            <label class="layui-form-label">每人头抽庄家积分：</label>
                            <span class="layui-input-inline">
                        <input id="rtcvalue" placeholder="请输入" autocomplete="off"
                               class="layui-input" style="width:460px">
                    </span>
                        </div>
                    </div>
                </fieldset>
                <hr class="layui-bg-green" style="width:600px">

                <fieldset class="layui-elem-field" style="width:600px">
                    <legend>赔率设置</legend>
                    <div class="layui-field-box">
                        <div class="layui-form-item">
                            <label class="layui-form-label">闲抽水：</label>
                            <div class="layui-input-block">
                                <input type="radio" name="xcType" value="1" title="百分比" checked>
                                <input type="radio" name="xcType" value="2" title="固定积分">
                            </div>
                        </div>
                        <div style="padding-left: 15px;">
                            <div>
                                <table id="rule_list" lay-filter="rule_list"></table>
                            </div>
                        </div>
                    </div>
                </fieldset>





            </form>
            <hr class="layui-bg-green" style="width:600px">
            <fieldset class="layui-elem-field" style="width:600px">
                <legend>同点数设置</legend>
                <div class="layui-field-box">
                    <div class="layui-form-item">
                        <label class="layui-form-label">庄闲同为牛</label>
                        <span class="layui-input-inline" style="width:60px">
                        <input id="point" placeholder="请输入" autocomplete="off"
                               class="layui-input">
                    </span>
                        <div class="layui-input-inline" style="width: 120px">
                            <select class="layui-select" id="sameoption">
                                <option selected="" value="1">庄赢</option>
                                <option value="2">跑路</option>
                                <option value="3">比金额(同平)</option>
                                <option value="4">比金额(庄赢)</option>
                                <option value="5">庄输</option>
                            </select>
                        </div>
                        <button id="editSamePoint" class="layui-btn layui-input-inline">添加</button>
                    </div>
                    <div style="padding-left: 15px">
                        <div>
                            <table id="tongdian_list" lay-filter="tongdian_list"></table>
                        </div>
                    </div>
                </div>
            </fieldset>
            <hr class="layui-bg-green" style="width:600px">
            <div style="padding-left: 15px;">
                <button class="layui-btn" onclick="editTemplates()">保存</button>
                <button class="layui-btn layui-btn-danger" onclick="delRule()">删除整个模版</button>
            </div>
        </div>
    </div>
<#include "footer.ftl"/>
</div>
<script>
    layui.use('table', function () {
        var table = layui.table;
        var tableIns = table.render({ //其它参数在此省略
            elem: '#rule_list' //或 elem: document.getElementById('test') 等
            , cols: [[{field: 'selectId', title: 'ID', width: 80, sort: true}
                , {field: 'name', tle: '名称', width: 120, sort: true}
                , {field: 'value', title: '赔率', width: 100, sort: true, edit: 'text'}
                , {field: 'xcvalue', title: '闲抽', width: 100, sort: true, edit: 'text'}
            ]]
            , url: '/templates/getRuleDetail'
            , method: 'get'
            , width: 405
        });

        //监听单元格编辑
        table.on('edit(rule_list)', function (obj) {
            var data = obj.data;
            var field = obj.field;//得到字段rule
            var xcvalue = data.xcvalue;
            var value = data.value;
            var startFrom = data.startFrom;
            var endTo = data.endTo;

            if (field == "xcvalue") {
                xcvalue = obj.value
            } else if (field == "value") {
                value = obj.value
            } else if (field == "startFrom") {
                startFrom = obj.value
            } else if (field == "endTo") {
                endTo = obj.value
            }
            layer.confirm('行已修改，确定要提交修改吗？', function (index) {
                layer.close(index);
                //同步更新缓存对应的值
                $.post("/templates/editRuleDetailById"
                        , {
                            "selectId": data.selectId,
                            "value": value,
                            "xcvalue": xcvalue,
                            "startFrom": startFrom,
                            "endTo": endTo
                        }
                        , function (data) {
                            if (data.retCode == 200) {
                            } else {
                                alert("网络错误！");
                            }
                        }
                );
            });
        });

        //监听工具条
        table.on('tool(tongdian_list)', function (obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
            var data = obj.data; //获得当前行数据
            var layEvent = obj.event; //获得 lay-event 对应的值
            if (layEvent === 'delete') { //删除
                layer.confirm('真的删除行么', function (index) {
                    obj.del(); //删除对应行（tr）的DOM结构
                    layer.close(index);
                    //向服务端发送删除指令
                    $.post("/templates/delSamePoint"
                            , {"point": data.point}
                            , function (data) {
                                if (data.retCode == 200) {
                                } else {
                                    alert("网络错误！");
                                }
                            }
                    );
                });
            }
        });

        //=======================
        var tableIns2 = table.render({ //其它参数在此省略
            elem: '#tongdian_list' //或 elem: document.getElementById('test') 等
            , cols: [[{field: 'name', width: 150, sort: true}
                , {field: 'value', width: 150, sort: true}
                , {fixed: 'right', width: 150, align: 'center', toolbar: '#barDemo'}
            ]]
            , url: '/templates/getsamePoint'
            , method: 'get'
            , width: 455
        });


        function editSamePoint() {
            $.post("/templates/editSamePoint"
                    , {
                        "point": $('#point').val(),
                        "option": $("#sameoption").find("option:selected").val()
                    }
                    , function (data) {
                        if (data.retCode == 200) {
                            tableIns2.reload({});
                        } else {
                            alert("网络错误！");
                        }
                    }
            );
        }

        $("#editSamePoint").click(function (event) {
            event.stopPropagation();
            editSamePoint();
        });
    });
</script>
<script type="text/html" id="barDemo">
    <a class="layui-btn layui-btn-mini" lay-event="delete">删除</a>
</script>
</body>
</html>
