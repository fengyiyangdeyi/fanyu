package cn.com.fanyu.controller;

import cn.com.fanyu.domain.*;
import cn.com.fanyu.service.RobotService;
import cn.com.fanyu.service.UserService;
import cn.com.fanyu.servlet.UserVo;
import cn.com.fanyu.utils.BusinessException;
import cn.com.fanyu.utils.ResultCode;
import cn.com.fanyu.utils.ResultJson;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.Map;

@Controller
@RequestMapping(value = "/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private RobotService robotService;
    

    @RequestMapping(value = "/updateUser", method = RequestMethod.POST,produces = "application/json; charset=utf-8")
    @ResponseBody
    public String updateUser(String username,String imgUrl,String nickname) {
        try {
            userService.updateUser(username,imgUrl,nickname);
            return new ResultJson(ResultCode.SUCCESS_CODE, "成功", "", "").toString();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultJson(ResultCode.FAILE_CODE, "", e.getMessage(), "").toString();
        }
    }

//    @RequestMapping(value = "/getLoginUser", method = RequestMethod.GET,produces = "application/json; charset=utf-8")
//    @ResponseBody
//    public String getLoginUser(HttpSession session) {
//        try {
//            UserVo userVo = (UserVo) session.getAttribute("user");
//            String groupid = userVo.getGroupid();
//            userService.updateUser(username,imgUrl,nickname);
//            return new ResultJson(ResultCode.SUCCESS_CODE, "成功", "", "").toString();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new ResultJson(ResultCode.FAILE_CODE, "", e.getMessage(), "").toString();
//        }
//    }



    @RequestMapping(value = "/findAnnouncement", method = RequestMethod.GET,produces = "application/json; charset=utf-8")
    @ResponseBody
    public String updateUser() {
        try {
            Map announcement = robotService.findAnnouncementPOJO();
            return new ResultJson(ResultCode.SUCCESS_CODE, "成功", "", JSON.toJSONString(announcement)).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultJson(ResultCode.FAILE_CODE, "", e.getMessage(), "").toString();
        }
    }

    @RequestMapping(value = "/getGroupCode", method = RequestMethod.GET,produces = "application/json; charset=utf-8")
    @ResponseBody
    public String getGroupCode(String groupid) {
        try {
            Map code = userService.getGroupCode(groupid);
            return new ResultJson(ResultCode.SUCCESS_CODE, "成功", "", JSON.toJSONString(code)).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultJson(ResultCode.FAILE_CODE, "", e.getMessage(), "").toString();
        }
    }

    @RequestMapping(value = "/getGroupid", method = RequestMethod.GET,produces = "application/json; charset=utf-8")
    @ResponseBody
    public String getGroupid(String code) {
        try {
            Map groupid = userService.getGroupid(code);
            return new ResultJson(ResultCode.SUCCESS_CODE, "成功", "", JSON.toJSONString(groupid)).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultJson(ResultCode.FAILE_CODE, "", e.getMessage(), "").toString();
        }
    }

    @RequestMapping(value = "/editGroupinfo", method = RequestMethod.POST,produces = "application/json; charset=utf-8")
    @ResponseBody
    public String editGroupinfo(FyGroup group) {
        try {
            userService.editGroupinfo(group);
            return new ResultJson(ResultCode.SUCCESS_CODE, "成功", "", "").toString();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultJson(ResultCode.FAILE_CODE, "", e.getMessage(), "").toString();
        }
    }

    @RequestMapping(value = "/deleteGroup", method = RequestMethod.POST,produces = "application/json; charset=utf-8")
    @ResponseBody
    public String deleteGroup(String groupid) {
        try {
            userService.deleteGroup(groupid);
            return new ResultJson(ResultCode.SUCCESS_CODE, "成功", "", "").toString();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultJson(ResultCode.FAILE_CODE, "", e.getMessage(), "").toString();
        }
    }

    @RequestMapping(value = "/addD", method = RequestMethod.GET,produces = "application/json; charset=utf-8")
    @ResponseBody
    public String deleteGroup(Long id,@RequestParam(defaultValue = "10000") BigDecimal num) {
        try {
            userService.addD(id,num);
            return new ResultJson(ResultCode.SUCCESS_CODE, "成功", "", "").toString();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultJson(ResultCode.FAILE_CODE, "", e.getMessage(), "").toString();
        }
    }

    @RequestMapping(value = "/testT", method = RequestMethod.GET,produces = "application/json; charset=utf-8")
    @ResponseBody
    public String testT() {
        try {
            userService.testT();
            return new ResultJson(ResultCode.SUCCESS_CODE, "成功", "", "").toString();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultJson(ResultCode.FAILE_CODE, "", e.getMessage(), "").toString();
        }
    }

    @RequestMapping(value = "/removeSingleUserFromChatGroup", method = RequestMethod.POST,produces = "application/json; charset=utf-8")
    @ResponseBody
    public String removeSingleUserFromChatGroup(String groupid, String username) {
        try {
            userService.removeSingleUserFromChatGroup(groupid,username);
            return new ResultJson(ResultCode.SUCCESS_CODE, "成功", "", "").toString();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultJson(ResultCode.FAILE_CODE, "", e.getMessage(), "").toString();
        }
    }

    @RequestMapping(value = "/editRelate", method = RequestMethod.POST,produces = "application/json; charset=utf-8")
    @ResponseBody
    public String editRelate(HttpSession session,String relateNickname,Long id) {
        try {
            UserVo userVo = (UserVo) session.getAttribute("user");
            String groupid = userVo.getGroupid();
            userService.editRelate(groupid,relateNickname,id);
            return new ResultJson(ResultCode.SUCCESS_CODE, "成功", "", "").toString();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultJson(ResultCode.FAILE_CODE, "", e.getMessage(), "").toString();
        }
    }

    @RequestMapping(value = "/getVersion", method = RequestMethod.GET,produces = "application/json; charset=utf-8")
    @ResponseBody
    public String getVersion(String type) {
        try {
            Map version = userService.getVersion(type);
            return new ResultJson(ResultCode.SUCCESS_CODE, "成功", "", JSON.toJSONString(version)).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultJson(ResultCode.FAILE_CODE, "", e.getMessage(), "").toString();
        }
    }

    @RequestMapping(value = "/getVersionStatus", method = RequestMethod.GET,produces = "application/json; charset=utf-8")
    @ResponseBody
    public String getVersionStatus(FyVersionStatus fyVersionStatus) {
        try {
            Map version = userService.getVersionStatus(fyVersionStatus);
            return new ResultJson(ResultCode.SUCCESS_CODE, "成功", "", JSON.toJSONString(version)).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultJson(ResultCode.FAILE_CODE, "", e.getMessage(), "").toString();
        }
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST,produces = "application/json; charset=utf-8")
    @ResponseBody
    public String register(String phone,String pwd,HttpSession session,String code) {
        try {
            String vcode = (String) session.getAttribute(phone);
            if(!code.equals(vcode)){
                throw new BusinessException("验证码不误！");
            }
            Map map=userService.register(phone,pwd);
            return new ResultJson(ResultCode.SUCCESS_CODE, "成功", "", JSON.toJSONString(map)).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultJson(ResultCode.FAILE_CODE, "", e.getMessage(), "").toString();
        }
    }

    @RequestMapping(value = "/forgetpwd", method = RequestMethod.POST,produces = "application/json; charset=utf-8")
    @ResponseBody
    public String forgetpwd(String phone,String code,String pwd,HttpSession session) {
        try {
            String vcode = (String) session.getAttribute(phone);
            if(!code.equals(vcode)){
                throw new BusinessException("验证码不误！");
            }
            userService.forgetpwd(phone,pwd);
            return new ResultJson(ResultCode.SUCCESS_CODE, "成功", "", "").toString();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultJson(ResultCode.FAILE_CODE, "", e.getMessage(), "").toString();
        }
    }

    @RequestMapping(value = "/phoneLogin", method = RequestMethod.POST,produces = "application/json; charset=utf-8")
    @ResponseBody
    public String phoneLogin(String phone,String pwd) {
        try {
            userService.phoneLogin(phone,pwd);
            return new ResultJson(ResultCode.SUCCESS_CODE, "成功", "", "").toString();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultJson(ResultCode.FAILE_CODE, "", e.getMessage(), "").toString();
        }
    }

}
