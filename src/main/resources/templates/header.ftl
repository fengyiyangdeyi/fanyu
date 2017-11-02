<div class="layui-header">
    <div class="layui-logo">泛娱 娱乐</div>
    <!-- 头部区域（可配合layui已有的水平导航） -->
    <ul class="layui-nav layui-layout-left">
        <li class="layui-nav-item"><a>群id:${Session.user.groupid}</a></li>
    </ul>
    <ul class="layui-nav layui-layout-right">
        <li class="layui-nav-item">
            <a href="javascript:;">
                <#--<img src=${Session.user.imgUrl} class="layui-nav-img">-->
                ${Session.user.nickname}
            </a>
        </li>
        <li class="layui-nav-item"><a href="logout">退出</a></li>
    </ul>
</div>