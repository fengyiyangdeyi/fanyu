<form class="layui-form" action="">
    <div class="layui-form-item">
        <label class="layui-form-label">庄家方式：</label>
        <div class="layui-input-block">
            <input type="radio" id="banker" name="banker" value="抢庄" title="抢庄" lay-filter="banker-select">
            <input type="radio" id="banker" name="banker" value="固定庄家" title="固定庄家" lay-filter="banker-fix">
        </div>
    </div>
    <div class="layui-form-item" id="bankerStyle">
        <label class="layui-form-label">格式：</label>
        <div class="layui-input-block">
            <input type="text" name="format" required lay-verify="required" placeholder="请输入抢庄格式" autocomplete="off"
                   class="layui-input" style="width: 460px">
        </div>
    </div>
    <div class="layui-form-item" id="bankerScore">
        <label class="layui-form-label">分数要求>=</label>
        <div class="layui-input-block">
            <input type="text" name="score" required lay-verify="required" placeholder="请输入数字" autocomplete="off"
                   class="layui-input" style="width: 460px">
        </div>
    </div>
    <div class="layui-form-item" id="bankerSelect">
        <label class="layui-form-label">选定庄家：</label>
        <div class="layui-input-block" style="width: 460px">
            <select name="city" lay-verify="" lay-search>
                <option value="010">layer</option>
                <option value="021">form</option>
                <option value="0571" selected>layim</option>
            </select>
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">上分格式：</label>
        <div class="layui-input-block">
            <input type="text" name="format" required lay-verify="required" placeholder="请输入上分格式" autocomplete="off"
                   class="layui-input" style="width: 460px">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">下注格式：</label>
        <div class="layui-input-block">
            <input type="text" name="format" required lay-verify="required" placeholder="请输入下注格式" autocomplete="off"
                   class="layui-input" style="width: 460px">
        </div>
    </div>


</form>

<script>
    layui.use('form', function () {
        var form = layui.form;
        form.on('radio(banker-fix)', function(data){
            $('#bankerStyle').hide();
            $('#bankerScore').hide();
            $('#bankerSelect').show();
        });
        form.on('radio(banker-select)', function(data){
            $('#bankerStyle').show();
            $('#bankerScore').show();
            $('#bankerSelect').hide();
        });
    });
</script>