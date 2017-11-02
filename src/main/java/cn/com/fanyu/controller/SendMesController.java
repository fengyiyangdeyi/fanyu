package cn.com.fanyu.controller;

import cn.com.fanyu.domain.FyScore;
import cn.com.fanyu.domain.templates.NiuNiu;
import cn.com.fanyu.domain.templates.NiuNiuDetail;
import cn.com.fanyu.service.SendMesService;
import cn.com.fanyu.service.TemplatesService;
import cn.com.fanyu.servlet.UserVo;
import cn.com.fanyu.utils.LayuiResultJson;
import cn.com.fanyu.utils.ResultCode;
import cn.com.fanyu.utils.ResultJson;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/sendMes")
public class SendMesController {

    @Autowired
    private SendMesService sendMesService;

    @RequestMapping(value = "/createRuleMes", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String createRuleMes(HttpSession session) {
        try {
            UserVo userVo = (UserVo) session.getAttribute("user");
            String groupid = userVo.getGroupid();
            Map mes= sendMesService.createRuleMes(groupid);
            return new ResultJson(ResultCode.SUCCESS_CODE, "成功", "", JSON.toJSONString(mes)).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultJson(ResultCode.FAILE_CODE, "", e.getMessage(), "").toString();
        }
    }

    @RequestMapping(value = "/refreshBet", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String refreshBet(HttpSession session) {
        try {
            UserVo userVo = (UserVo) session.getAttribute("user");
            String groupid = userVo.getGroupid();
            Map mes= sendMesService.refreshBet(groupid);
            return new ResultJson(ResultCode.SUCCESS_CODE, "成功", "", JSON.toJSONString(mes)).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultJson(ResultCode.FAILE_CODE, "", e.getMessage(), "").toString();
        }
    }

    @RequestMapping(value = "/sendBeginXiazhuMes", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String sendBeginXiazhuMes(HttpSession session,String mes) {
        try {
            UserVo userVo = (UserVo) session.getAttribute("user");
            String groupid = userVo.getGroupid();
            sendMesService.sendBeginXiazhuMes(groupid,mes);
            return new ResultJson(ResultCode.SUCCESS_CODE, "成功", "", "").toString();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultJson(ResultCode.FAILE_CODE, "", e.getMessage(), "").toString();
        }
    }

    @RequestMapping(value = "/sendEndXiazhuMes", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String sendXiazhuMes(HttpSession session,String mes) {
        try {
            UserVo userVo = (UserVo) session.getAttribute("user");
            String groupid = userVo.getGroupid();
            sendMesService.sendEndXiazhuMes(groupid,mes);
            return new ResultJson(ResultCode.SUCCESS_CODE, "成功", "", "").toString();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultJson(ResultCode.FAILE_CODE, "", e.getMessage(), "").toString();
        }
    }

    @RequestMapping(value = "/refreshResult", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String refreshResult(HttpSession session) {
        try {
            UserVo userVo = (UserVo) session.getAttribute("user");
            String groupid = userVo.getGroupid();
            Map mes= sendMesService.refreshResult(groupid);
            return new ResultJson(ResultCode.SUCCESS_CODE, "成功", "", JSON.toJSONString(mes)).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultJson(ResultCode.FAILE_CODE, "", e.getMessage(), "").toString();
        }
    }




}
