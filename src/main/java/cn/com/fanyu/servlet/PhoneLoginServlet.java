package cn.com.fanyu.servlet;

import cn.com.fanyu.domain.FyUser;
import cn.com.fanyu.service.RobotService;
import cn.com.fanyu.utils.PropUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 * 二维码手机端登录
 *
 * @author zijuntang
 */
@WebServlet(urlPatterns="/QrCodeLoginPro/PhoneLoginServlet")
public class PhoneLoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    @Autowired
    private RobotService robotService;

    public PhoneLoginServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uuid = request.getParameter("uuid");
        String uname = request.getParameter("uname");
        String upwd = request.getParameter("upwd");
        String groupid = request.getParameter("groupid");
        System.out.println(uuid);
        System.out.println(uname);
        System.out.println(upwd);
        System.out.println(groupid);
        FyUser user=new FyUser();
        user.setUsername(uname);
        user.setPassword(upwd);
        boolean bool = true;
        if (bool) {
            //验证密码是否正确
            UserVo dbuser=new UserVo();
            try {
                dbuser=robotService.login(user,groupid);
            }catch (Exception e){
                bool=false;
            }

            //将登陆信息存入map
            UserVo userVo = LoginUserVo.getLoginUserMap().get(uuid);
            if (userVo == null) {
                LoginUserVo.getLoginUserMap().put(uuid, dbuser);
            }
        }
        PrintWriter out = response.getWriter();
        //手机扫码后密码不对这里返回给手机提示
        out.print(bool);
        out.flush();
        out.close();
    }

}
