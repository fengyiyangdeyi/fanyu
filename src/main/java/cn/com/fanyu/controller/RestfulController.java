package cn.com.fanyu.controller;

import cn.com.fanyu.domain.FyGroup;
import cn.com.fanyu.domain.FyScore;
import cn.com.fanyu.domain.FyUser;
import cn.com.fanyu.huanxin.comm.TokenUtil;
import cn.com.fanyu.service.RestfulService;
import cn.com.fanyu.servlet.UserVo;
import cn.com.fanyu.utils.*;
import com.alibaba.fastjson.JSON;
import io.swagger.client.model.Group;
import io.swagger.client.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/rest")
public class RestfulController {
    @Autowired
    private RestfulService restfulService;

    @RequestMapping("/token")
    @ResponseBody
    public String token() {
        try {
            String accessToken = TokenUtil.getAccessToken();
            return new ResultJson(ResultCode.SUCCESS_CODE, "成功", "", accessToken).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultJson(ResultCode.FAILE_CODE, "", e.getMessage(), "").toString();
        }
    }

    /**
     * 创建单个用户
     * @param user
     * @return
     */
    @RequestMapping(value = "/createNewIMUserSingle", method = RequestMethod.POST,produces = "application/json; charset=utf-8")
    @ResponseBody
    public String createNewIMUserSingle(User user) {
        try {
            if(StringUtil.isNullOrEmpty(user.getUsername())){
                throw new BusinessException("username is null");
            }
            if(StringUtil.isNullOrEmpty(user.getPassword())){
                throw new BusinessException("password is null");
            }
            Map map = restfulService.register(user);
            return new ResultJson(ResultCode.SUCCESS_CODE, "成功", "", JSON.toJSONString(map)).toString();
        } catch (BusinessException e) {
            e.printStackTrace();
            return new ResultJson(ResultCode.FAILE_CODE, "", e.getMessage(), "").toString();
        }
    }

    /**
     * 创建单个用户
     * @param user
     * @return
     */
    @RequestMapping(value = "/wxLogin", method = RequestMethod.POST,produces = "application/json; charset=utf-8")
    @ResponseBody
    public String wxLogin(FyUser user,
                          HttpServletResponse response
                          ) {
        try {
            Map map = restfulService.wxLogin(user);
            String jsessionId=(String) map.get("uuid");
            response.addHeader("jsessionId",jsessionId);
            Cookie cookie=new Cookie("jsessionId",jsessionId);
            response.addCookie(cookie);
            return new ResultJson(ResultCode.SUCCESS_CODE, "成功", "", JSON.toJSONString(map)).toString();
        } catch (BusinessException e) {
            e.printStackTrace();
            return new ResultJson(ResultCode.FAILE_CODE, "", e.getMessage(), "").toString();
        }
    }


    /**
     * 查询用户-本地非环信用户
     * @param user
     * @return
     */
    @RequestMapping(value = "/getIMUserByUserName", method = RequestMethod.GET,produces = "application/json; charset=utf-8")
    @ResponseBody
    public String getIMUserByUserName(FyUser user) {
        try {
            Map map = restfulService.getIMUserByUserName(user);
            return new ResultJson(ResultCode.SUCCESS_CODE, "成功", "", JSON.toJSONString(map)).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultJson(ResultCode.FAILE_CODE, "", e.getMessage(), "").toString();
        }
    }

    /**
     * 创建群组
     * @param group
     * @return
     */
    @RequestMapping(value = "/createChatGroup", method = RequestMethod.POST,produces = "application/json; charset=utf-8")
    @ResponseBody
    public String createChatGroup(Group group) {
        try {
            //Group group = JSON.parseObject(data, Group.class);
            Map map = restfulService.createChatGroup(group);
            return new ResultJson(ResultCode.SUCCESS_CODE, "成功", "", JSON.toJSONString(map)).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultJson(ResultCode.FAILE_CODE, "", e.getMessage(), "").toString();
        }
    }

    /**
     * 通过群主查询群组
     * @param username
     * @return
     */
    @RequestMapping(value = "/ownerChatGroup", method = RequestMethod.GET,produces = "application/json; charset=utf-8")
    @ResponseBody
    public String ownerChatGroup(String username) {
        try {
            Boolean aBoolean = restfulService.ownerChatGroup(username);
            return new ResultJson(ResultCode.SUCCESS_CODE, "成功", "", JSON.toJSONString(aBoolean)).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultJson(ResultCode.FAILE_CODE, "", e.getMessage(), "").toString();
        }
    }

    /**
     * 把计算结果发到群里
     * @param
     * @return
     */
    @RequestMapping(value = "/sendMessagetoGroup", method = RequestMethod.POST,produces = "application/json; charset=utf-8")
    @ResponseBody
    public String sendMessagetoGroup(String mes,HttpSession session) {
        try {
            UserVo userVo = (UserVo) session.getAttribute("user");
            String groupid = userVo.getGroupid();
            String ret= restfulService.sendMessagetoGroup(mes,groupid);
            return new ResultJson(ResultCode.SUCCESS_CODE, "成功", "", "").toString();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultJson(ResultCode.FAILE_CODE, "", e.getMessage(), "").toString();
        }
    }

    /**
     * app拉人进群时调用，保存于数据库（这里不调环信）
     * @param fyScore
     * @return
     */
    @RequestMapping(value = "/addUserToGroup", method = RequestMethod.POST,produces = "application/json; charset=utf-8")
    @ResponseBody
    public String addUserToGroup(FyScore fyScore) {
        try {
            restfulService.addUserToGroup(fyScore);
            return new ResultJson(ResultCode.SUCCESS_CODE, "成功", "", "").toString();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultJson(ResultCode.FAILE_CODE, "", e.getMessage(), "").toString();
        }
    }

    @RequestMapping(value = "/addSingUserToGroup", method = RequestMethod.GET,produces = "application/json; charset=utf-8")
    @ResponseBody
    public String addSingUserToGroup(String groupid,String username) {
        try {
            restfulService.addSingUserToGroup(groupid,username);
            return new ResultJson(ResultCode.SUCCESS_CODE, "成功", "", "").toString();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultJson(ResultCode.FAILE_CODE, "", e.getMessage(), "").toString();
        }
    }






}
