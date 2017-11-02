package cn.com.fanyu.controller;

import cn.com.fanyu.domain.*;
import cn.com.fanyu.domain.templates.NiuNiu;
import cn.com.fanyu.domain.templates.NiuNiuDetail;
import cn.com.fanyu.domain.templates.SamePointDetail;
import cn.com.fanyu.service.RobotService;
import cn.com.fanyu.service.TemplatesService;
import cn.com.fanyu.servlet.UserVo;
import cn.com.fanyu.utils.LayuiResultJson;
import cn.com.fanyu.utils.ResultCode;
import cn.com.fanyu.utils.ResultJson;
import cn.com.fanyu.utils.StringUtil;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/templates")
public class TemplatesController {
    @Autowired
    private TemplatesService templatesService;

    @RequestMapping(value = "/editTemplates", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String editTemplates(HttpSession session,NiuNiu templates) {
        try {
            UserVo userVo = (UserVo) session.getAttribute("user");
            String groupid = userVo.getGroupid();
            templatesService.editRule(templates,groupid);
            return new ResultJson(ResultCode.SUCCESS_CODE, "成功", "", "").toString();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultJson(ResultCode.FAILE_CODE, "", e.getMessage(), "").toString();
        }
    }

    @RequestMapping(value = "/getNiuNiuTemplates", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String getTemplates(HttpSession session) {
        try {
            UserVo userVo = (UserVo) session.getAttribute("user");
            String groupid = userVo.getGroupid();
            String json=templatesService.getNiuNiuTemplates(groupid);
            return new ResultJson(ResultCode.SUCCESS_CODE, "成功", "", json).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultJson(ResultCode.FAILE_CODE, "", e.getMessage(), "").toString();
        }
    }

    @RequestMapping(value = "/getAllUser", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String getAllUser(HttpSession session) {
        try {
            UserVo userVo = (UserVo) session.getAttribute("user");
            String groupid = userVo.getGroupid();
            List<FyScore> list=templatesService.getAllUser(groupid);
            return new ResultJson(ResultCode.SUCCESS_CODE, "成功", "", JSON.toJSONString(list)).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultJson(ResultCode.FAILE_CODE, "", e.getMessage(), "").toString();
        }
    }

    @RequestMapping(value = "/editRuleDetail", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String editRuleDetail(NiuNiuDetail dt, HttpSession session) {
        try {
            UserVo user = (UserVo) session.getAttribute("user");
            String groupid = user.getGroupid();
            templatesService.editRuleDetail(dt,groupid);
            return new ResultJson(ResultCode.SUCCESS_CODE, "成功", "", "").toString();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultJson(ResultCode.FAILE_CODE, "", e.getMessage(), "").toString();
        }
    }

    @RequestMapping(value = "/editSamePoint", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String editSamePoint(SamePointDetail dt, HttpSession session) {
        try {
            UserVo user = (UserVo) session.getAttribute("user");
            String groupid = user.getGroupid();
            templatesService.editSamePoint(dt,groupid);
            return new ResultJson(ResultCode.SUCCESS_CODE, "成功", "", "").toString();
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
            List list = templatesService.getRuleDetail(groupid);
            return new LayuiResultJson(0, "sucessful", 0, JSON.toJSONString(list)).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return new LayuiResultJson(1, e.getMessage(), 0, "").toString();
        }
    }

    @RequestMapping(value = "/getsamePoint", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String getsamePoint(HttpSession session) {
        try {
            UserVo userVo = (UserVo) session.getAttribute("user");
            String groupid = userVo.getGroupid();
            List list = templatesService.getsamePoint(groupid);
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
            List list = templatesService.getRuleDetailSpecial(groupid);
            return new LayuiResultJson(0, "sucessful", 0, JSON.toJSONString(list)).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return new LayuiResultJson(1, e.getMessage(), 0, "").toString();
        }
    }

    @RequestMapping(value = "/delRuleDetail", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String delRuleDetail(NiuNiuDetail dt,HttpSession session) {
        try {
            UserVo userVo = (UserVo) session.getAttribute("user");
            String groupid = userVo.getGroupid();
            templatesService.delRuleDetail(dt,groupid);
            return new ResultJson(ResultCode.SUCCESS_CODE, "成功", "", "").toString();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultJson(ResultCode.FAILE_CODE, "", e.getMessage(), "").toString();
        }
    }

    @RequestMapping(value = "/delSamePoint", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String delSamePoint(SamePointDetail dt,HttpSession session) {
        try {
            UserVo userVo = (UserVo) session.getAttribute("user");
            String groupid = userVo.getGroupid();
            templatesService.delSamePoint(dt,groupid);
            return new ResultJson(ResultCode.SUCCESS_CODE, "成功", "", "").toString();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultJson(ResultCode.FAILE_CODE, "", e.getMessage(), "").toString();
        }
    }


    @RequestMapping(value = "/editRuleDetailById", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String editRuleDetailById(NiuNiuDetail dt,HttpSession session) {
        try {
            UserVo userVo = (UserVo) session.getAttribute("user");
            String groupid = userVo.getGroupid();
            templatesService.editRuleDetailById(dt,groupid);
            return new ResultJson(ResultCode.SUCCESS_CODE, "成功", "", "").toString();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultJson(ResultCode.FAILE_CODE, "", e.getMessage(), "").toString();
        }
    }


}
