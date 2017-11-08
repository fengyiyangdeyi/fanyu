package cn.com.fanyu.controller;

import cn.com.fanyu.domain.*;
import cn.com.fanyu.schedule.ScheduleJobs;
import cn.com.fanyu.service.*;
import cn.com.fanyu.servlet.LoginUserVo;
import cn.com.fanyu.servlet.UserVo;
import cn.com.fanyu.utils.*;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Controller
@RequestMapping(value = "/robot")
public class RobotController {
    @Autowired
    private RobotService robotService;
    @Autowired
    private TemplatesService templatesService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private ScheduleJobs scheduleJobs;

    @RequestMapping(value = "/rule")
    public String rule(Map<String, Object> map, HttpSession session) {
        try {
            UserVo userVo = (UserVo) session.getAttribute("user");
            String groupid = userVo.getGroupid();
            FyRule rule = robotService.getRule(groupid);
            if (rule == null) {
                return "add_rule";
            }
//            String json = rule.getRule();
//            Map rulemap = JSON.parseObject(json);
//            map.putAll(rulemap);
//            map.put("id",rule.getId());
            return rule.getPageName();
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:login";
        }
    }

//    @RequestMapping(value = "/addRuleNiuNiu", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
//    public String addRuleNiuNiu(RuleNiuNiu info, HttpSession session) {
//        try {
//            UserVo userVo = (UserVo) session.getAttribute("user");
//            String groupid = userVo.getGroupid();
//            robotService.addRuleNiuNiu(info, groupid);
//            return "redirect:rule";
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "redirect:login";
//        }
//    }

    @RequestMapping(value = "/delRule", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String delRule(HttpSession session) {
        try {
            UserVo userVo = (UserVo) session.getAttribute("user");
            String groupid = userVo.getGroupid();
            robotService.delRule(groupid);
            return new ResultJson(ResultCode.SUCCESS_CODE, "成功", "", "").toString();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultJson(ResultCode.FAILE_CODE, "", e.getMessage(), "").toString();
        }
    }

//    @RequestMapping(value = "/createRule",  produces = "application/json; charset=utf-8")
//    public String createRule(String pageName) {
//        try {
//            return pageName;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "redirect:login";
//        }
//    }

    @RequestMapping(value = "/addIntegral", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String addIntegral(FyScore fyScore, BigDecimal value, HttpSession session) {
        try {
            UserVo userVo = (UserVo) session.getAttribute("user");
            String groupid = userVo.getGroupid();
            robotService.addIntegral(fyScore, value,groupid);
            return new ResultJson(ResultCode.SUCCESS_CODE, "成功", "", "").toString();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultJson(ResultCode.FAILE_CODE, "", e.getMessage(), "").toString();
        }
    }

    @RequestMapping(value = "/addScoreByMes", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String addScoreByMes(FyMesScore mes, BigDecimal value, HttpSession session) {
        try {
            UserVo userVo = (UserVo) session.getAttribute("user");
            String groupid = userVo.getGroupid();
            mes.setGroupid(groupid);
            String ret = robotService.addScoreByMes(mes, value);
            return new ResultJson(ResultCode.SUCCESS_CODE, "成功", "", "").toString();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultJson(ResultCode.FAILE_CODE, "", e.getMessage(), "").toString();
        }
    }

    @RequestMapping(value = "/addzIntegral", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String addzIntegral(FyScore mes, BigDecimal value, HttpSession session) {
        try {
            UserVo userVo = (UserVo) session.getAttribute("user");
            String groupid = userVo.getGroupid();
            robotService.addzIntegral(mes, value,groupid);
            return new ResultJson(ResultCode.SUCCESS_CODE, "成功", "", "").toString();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultJson(ResultCode.FAILE_CODE, "", e.getMessage(), "").toString();
        }
    }



    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    @RequestMapping("/header")
    public String header(Map map) {
        return "header";
    }

    @RequestMapping("/footer")
    public String footer(Map map) {
        return "footer";
    }

    @RequestMapping("/group_member")
    public String groupMember() {
        return "group_member";
    }

    @RequestMapping("/regular_config")
    public String regular_config() {
        return "regular_config";
    }

    @RequestMapping("/select_groupid")
    public String select_groupid(HttpSession session) {
        UserVo user = (UserVo) session.getAttribute("user");
        if (!StringUtil.isNullOrEmpty(user.getGroupid())) {
            return "group_member";
        } else if ("7".equals(user.getPermissions())) {
            return "select_groupid";
        } else {
            return "login";
        }

    }

    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String login(FyUser user, HttpSession session, String groupid, Map map) {
        try {
            UserVo userVo = robotService.login(user, groupid);
            session.setAttribute("user", userVo);
            session.setMaxInactiveInterval(3600);
            return new ResultJson(ResultCode.SUCCESS_CODE, "成功", "", "").toString();
        } catch (Exception e) {
            map.put("mes", e.getMessage());
            e.printStackTrace();
            return new ResultJson(ResultCode.FAILE_CODE, "", e.getMessage(), "").toString();
        }
    }

    @RequestMapping(value = "/setGroup", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String setGroup(String groupid, HttpSession session, Map map) {
        try {
            UserVo user = (UserVo) session.getAttribute("user");
            user.setGroupid(groupid);
            session.setMaxInactiveInterval(3600);
            return new ResultJson(ResultCode.SUCCESS_CODE, "成功", "", "").toString();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultJson(ResultCode.FAILE_CODE, "", e.getMessage(), "").toString();
        }
    }

    @RequestMapping(value = "/logout", produces = "application/json; charset=utf-8")
    public String logout(HttpSession session, Map map) {
        session.invalidate();
        return "redirect:login";
    }

    @RequestMapping(value = "/group_member_json", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String group_member_json( Integer page, Integer limit, HttpSession session,String keyword) {
        try {
            UserVo userVo = (UserVo) session.getAttribute("user");
            String groupid = userVo.getGroupid();
            String ret = robotService.getMemberByGroubId(groupid, page, limit,keyword);
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
            return new LayuiResultJson(1, e.getMessage(), 0, "").toString();
        }
    }

    @RequestMapping(value = "/getMesScore", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String getMesScore(Integer page, Integer limit, HttpSession session) {
        try {
            UserVo userVo = (UserVo) session.getAttribute("user");
            String groupid = userVo.getGroupid();
            String ret = robotService.getMesScore(groupid, page, limit);
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
            return new LayuiResultJson(1, e.getMessage(), 0, "").toString();
        }
    }

    @RequestMapping(value = "/selectGroup", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String selectGroup(Integer page, Integer limit) {
        try {
            String ret = robotService.selectGroup(page, limit);
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
            return new LayuiResultJson(1, e.getMessage(), 0, "").toString();
        }
    }


    @RequestMapping("/announcement")
    public String announcement(Map map) {
        FyAnnouncement a = robotService.findAnnouncement();
        map.put("data", a);
        return "announcement";
    }

    @RequestMapping("/send_mes")
    public String send_mes() {
        return "send_mes";
    }

    @RequestMapping(value = "/updateAnnouncement", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public String updateAnnouncement(FyAnnouncement announcement) {
        try {
            robotService.updateAnnouncement(announcement);
            return "redirect:announcement";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:login";
        }
    }




    @RequestMapping(value = "/getRegularManagement", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String getRegularManagement(HttpSession session) {
        try{
            UserVo userVo = (UserVo) session.getAttribute("user");
            String groupid = userVo.getGroupid();
            List list = robotService.getRegular(groupid);
            return new LayuiResultJson(0, "sucessful", 0, JSON.toJSONString(list)).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return new LayuiResultJson(1, e.getMessage(), 0, "").toString();
        }
    }

    @RequestMapping(value = "/getRegular", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String getRegular(String groupid) {
        try {
            Map map=templatesService.getRegularByGroupid(groupid);
            return new ResultJson(ResultCode.SUCCESS_CODE, "成功", "", JSON.toJSONString(map)).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultJson(ResultCode.FAILE_CODE, "", e.getMessage(), "").toString();
        }
    }

    @RequestMapping(value = "/getRuleDetail", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String getRuleDetail(HttpSession session) {
        try {
            UserVo userVo = (UserVo) session.getAttribute("user");
            String groupid = userVo.getGroupid();
            List list = robotService.getRuleDetail(groupid);
            return new LayuiResultJson(0, "sucessful", 0, JSON.toJSONString(list)).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return new LayuiResultJson(1, e.getMessage(), 0, "").toString();
        }
    }
    @RequestMapping(value = "/getRuleDetailSpecial", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String getRuleDetailSpecial(HttpSession session) {
        try {
            UserVo userVo = (UserVo) session.getAttribute("user");
            String groupid = userVo.getGroupid();
            List list = robotService.getRuleDetailSpecial(groupid);
            return new LayuiResultJson(0, "sucessful", 0, JSON.toJSONString(list)).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return new LayuiResultJson(1, e.getMessage(), 0, "").toString();
        }
    }




    @RequestMapping(value = "/editRuleDetail", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String editRuleDetail(FyRuleDetail dt,HttpSession session) {
        try {
            UserVo user = (UserVo) session.getAttribute("user");
            String groupid = user.getGroupid();
            robotService.editRuleDetail(dt,groupid);
            return new ResultJson(ResultCode.SUCCESS_CODE, "成功", "", "").toString();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultJson(ResultCode.FAILE_CODE, "", e.getMessage(), "").toString();
        }
    }


    @RequestMapping(value = "/editRegular", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String editRegular(FyRegular regular,HttpSession session) {
        try {
            UserVo user = (UserVo) session.getAttribute("user");
            String groupid = user.getGroupid();
            robotService.editRegular(regular,groupid);
            return new ResultJson(ResultCode.SUCCESS_CODE, "成功", "", "").toString();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultJson(ResultCode.FAILE_CODE, "", e.getMessage(), "").toString();
        }
    }

    @RequestMapping(value = "/delRegular", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String delRegular(Long id) {
        try {
            robotService.delRegular(id);
            return new ResultJson(ResultCode.SUCCESS_CODE, "成功", "", "").toString();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultJson(ResultCode.FAILE_CODE, "", e.getMessage(), "").toString();
        }
    }

    @RequestMapping(value = "/delRuleDetail", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String delRuleDetail(Long id) {
        try {
            robotService.delRuleDetail(id);
            return new ResultJson(ResultCode.SUCCESS_CODE, "成功", "", "").toString();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultJson(ResultCode.FAILE_CODE, "", e.getMessage(), "").toString();
        }
    }

    @RequestMapping(value = "/delMesScore", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String delMesScore(Long id) {
        try {
            robotService.delMesScore(id);
            return new ResultJson(ResultCode.SUCCESS_CODE, "成功", "", "").toString();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultJson(ResultCode.FAILE_CODE, "", e.getMessage(), "").toString();
        }
    }



    @RequestMapping(value = "/editRegularById", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String editRegularById(Long id,String regularContent) {
        try {
            robotService.editRegularById(id,regularContent);
            return new ResultJson(ResultCode.SUCCESS_CODE, "成功", "", "").toString();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultJson(ResultCode.FAILE_CODE, "", e.getMessage(), "").toString();
        }
    }

    @RequestMapping(value = "/editRuleDetailById", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String editRuleDetailById(FyRuleDetail dt) {
        try {
            robotService.editRuleDetailById(dt);
            return new ResultJson(ResultCode.SUCCESS_CODE, "成功", "", "").toString();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultJson(ResultCode.FAILE_CODE, "", e.getMessage(), "").toString();
        }
    }

    @RequestMapping(value = "/createRule", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String createRule(FyRule rule,HttpSession session) {
        try {
            UserVo user = (UserVo) session.getAttribute("user");
            String groupid = user.getGroupid();
            robotService.createRule(rule,groupid);
            return new ResultJson(ResultCode.SUCCESS_CODE, "成功", "", "").toString();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultJson(ResultCode.FAILE_CODE, "", e.getMessage(), "").toString();
        }
    }

    @RequestMapping(value = "/setRegularMes", method = RequestMethod.GET,produces = "application/json; charset=utf-8")
    @ResponseBody
    public String setRegularMes(String groupid) {
        try {
            robotService.setRegularMes(groupid);
            return new ResultJson(ResultCode.SUCCESS_CODE, "成功", "", "").toString();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultJson(ResultCode.FAILE_CODE, "", e.getMessage(), "").toString();
        }
    }

    @RequestMapping(value = "/robotMes", method = RequestMethod.POST,produces = "application/json; charset=utf-8")
    @ResponseBody
    public String robotMes(String groupid,String username,String content,String regularType) {
        try {
            robotService.robotMes(groupid,username,content,regularType);
            return new ResultJson(ResultCode.SUCCESS_CODE, "成功", "", "").toString();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultJson(ResultCode.FAILE_CODE, "", e.getMessage(), "").toString();
        }
    }

    @RequestMapping(value = "/QrCodeLogin", method = RequestMethod.POST,produces = "application/json; charset=utf-8")
    @ResponseBody
    public String QrCodeLogin(String groupid, String uuid, @CookieValue(value = "jsessionId", defaultValue = "nocookie") String jsessionId) {
        try {
            UserVo dbuser=robotService.saomalogin(jsessionId,groupid);
            //将登陆信息存入map
            UserVo userVo = LoginUserVo.getLoginUserMap().get(uuid);
            if (userVo == null) {
                LoginUserVo.getLoginUserMap().put(uuid, dbuser);
            }
            return new ResultJson(ResultCode.SUCCESS_CODE, "成功", "", "").toString();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultJson(ResultCode.FAILE_CODE, "", e.getMessage(), "").toString();
        }
    }

    @RequestMapping(value = "/task", method = RequestMethod.GET,produces = "application/json; charset=utf-8")
    @ResponseBody
    public String task() {
        try {
            scheduleJobs.task();
            return new ResultJson(ResultCode.SUCCESS_CODE, "成功", "", "").toString();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultJson(ResultCode.FAILE_CODE, "", e.getMessage(), "").toString();
        }
    }

}
