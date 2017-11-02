<div class="layui-side layui-bg-black">
    <div class="layui-side-scroll">
        <!-- 左侧导航区域（可配合layui已有的垂直导航） -->
        <ul class="layui-nav layui-nav-tree" lay-filter="test">
            <li class="layui-nav-item"><a href="group_member">上下分</a></li>
            <li class="layui-nav-item"><a href="rule">玩法配置</a></li>
            <li class="layui-nav-item"><a href="send_mes">信息发送</a></li>
        <#if "${Session.user.permissions}"=="7">
            <li class="layui-nav-item"><a href="announcement">公告维护</a></li>
        </#if>

        </ul>
    </div>
</div>