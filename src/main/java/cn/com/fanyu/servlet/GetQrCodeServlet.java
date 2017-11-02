package cn.com.fanyu.servlet;


import cn.com.fanyu.utils.IdGen;
import cn.com.fanyu.utils.PropUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;

import java.io.PrintWriter;

import java.util.Date;

import javax.servlet.ServletException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;


/**
 * 生成二维码图片以及uuid
 *
 * @author zijuntang
 */
@WebServlet(urlPatterns = "/QrCodeLoginPro/GetQrCodeServlet", description = "xxx")
public class GetQrCodeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    @Autowired
    private PropUtil propUtil;

    @Value("${qrcode-url}")
    private String qrcodeurl;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        //生成唯一ID
//        int uuid = (int)(Math.random() * 100000);
        String uuid = IdGen.uuid();

        //二维码内容
        String content = uuid;
        //生成二维码
        String imgName = uuid + "_" + (int) (new Date().getTime() / 1000) + ".png";
        String imgPath = qrcodeurl + imgName;
        TwoDimensionCode handler = new TwoDimensionCode();
        handler.encoderQRCode(content, imgPath, "png");
        //生成的图片访问地址
        String qrCodeImg = propUtil.getImgUrl() + "/QrCodeLogin/" + imgName;
        String jsonStr = "{\"uuid\":\"" + uuid + "\",\"qrCodeImg\":\"" + qrCodeImg + "\"}";
        out.print(jsonStr);
        out.flush();
        out.close();
    }

}
