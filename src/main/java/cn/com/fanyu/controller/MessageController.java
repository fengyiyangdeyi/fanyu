package cn.com.fanyu.controller;


import cn.com.fanyu.utils.BusinessException;
import cn.com.fanyu.utils.ResultCode;
import cn.com.fanyu.utils.ResultJson;
import cn.com.fanyu.utils.SmsDemo;
import com.alibaba.fastjson.JSON;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Controller
@RequestMapping(value = "/message")
public class MessageController {


    @RequestMapping(value = "/sent", method = RequestMethod.POST)
    @ResponseBody
    public String sent(HttpSession session,String phone)  {
        try {
            Random ne=new Random();//实例化一个random的对象ne
            int x=ne.nextInt(9999-1000+1)+1000;//为变量赋随机值1000-9999
            SendSmsResponse response = SmsDemo.sendSms(phone,x);
            System.out.println("短信接口----------------"+x);
            if("OK".equals(response.getCode())){
                session.setAttribute(phone,x+"");
                session.setMaxInactiveInterval(60*5);//五分钟
                return new ResultJson(ResultCode.SUCCESS_CODE, "成功", "", "").toString();
            }else {
                StringBuffer sb =new StringBuffer();
                sb.append("Code=" + response.getCode());
                sb.append(",Message=" + response.getMessage());
                sb.append(",RequestId=" + response.getRequestId());
                sb.append(",BizId=" + response.getBizId());
                throw new BusinessException(sb.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultJson(ResultCode.FAILE_CODE, "", e.getMessage(), "").toString();
        }

    }


}
