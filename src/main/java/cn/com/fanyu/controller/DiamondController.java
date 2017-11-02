package cn.com.fanyu.controller;

import cn.com.fanyu.domain.FyDiamond;
import cn.com.fanyu.domain.FyDiamondDetail;
import cn.com.fanyu.huanxin.comm.TokenUtil;
import cn.com.fanyu.service.DiamondService;
import cn.com.fanyu.service.RestfulService;
import cn.com.fanyu.utils.ResultCode;
import cn.com.fanyu.utils.ResultJson;
import com.alibaba.fastjson.JSON;
import io.swagger.client.model.Group;
import io.swagger.client.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/diamond")
public class DiamondController {
    @Autowired
    private DiamondService diamondService;

    @RequestMapping(value = "/sendDiamond",method = RequestMethod.POST,produces = "application/json; charset=utf-8")
    @ResponseBody
    public String sendDiamond(FyDiamond fyDiamond) {
        try {
            Map map = diamondService.sendDiamond(fyDiamond);
            return new ResultJson(ResultCode.SUCCESS_CODE, "成功", "", JSON.toJSONString(map)).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultJson(ResultCode.FAILE_CODE, "", e.getMessage(), "").toString();
        }
    }

    @RequestMapping(value = "/receivedDiamond",method = RequestMethod.POST,produces = "application/json; charset=utf-8")
    @ResponseBody
    public String receivedDiamond(FyDiamondDetail dt) {
        try {
            Map map = diamondService.receivedDiamondLoop(dt);
            return new ResultJson(ResultCode.SUCCESS_CODE, "成功", "", JSON.toJSONString(map)).toString();
        }catch (Exception e) {
            e.printStackTrace();
            return new ResultJson(ResultCode.FAILE_CODE, "", e.getMessage(), "").toString();
        }
    }

    @RequestMapping(value = "/checkReceive",method = RequestMethod.GET,produces = "application/json; charset=utf-8")
    @ResponseBody
    public String checkSend(FyDiamondDetail dt) {
        try {
            Map map = diamondService.checkReceive(dt);
            return new ResultJson(ResultCode.SUCCESS_CODE, "成功", "", JSON.toJSONString(map)).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultJson(ResultCode.FAILE_CODE, "", e.getMessage(), "").toString();
        }
    }

    @RequestMapping(value = "/getDiamondInfo",method = RequestMethod.GET,produces = "application/json; charset=utf-8")
    @ResponseBody
    public String getDiamondInfo(FyDiamondDetail dt) {
        try {
            Map map = diamondService.getDiamondInfo(dt);
            return new ResultJson(ResultCode.SUCCESS_CODE, "成功", "", JSON.toJSONString(map)).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultJson(ResultCode.FAILE_CODE, "", e.getMessage(), "").toString();
        }
    }

    @RequestMapping(value = "/sendDiamondMes",method = RequestMethod.GET,produces = "application/json; charset=utf-8")
    @ResponseBody
    public String sendDiamondMes(FyDiamond d) {
        try {
//            diamondService.sendDiamondMes(d);
            return new ResultJson(ResultCode.SUCCESS_CODE, "成功", "", "").toString();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultJson(ResultCode.FAILE_CODE, "", e.getMessage(), "").toString();
        }
    }

    @RequestMapping(value = "/getGoods",method = RequestMethod.GET,produces = "application/json; charset=utf-8")
    @ResponseBody
    public String getGoods() {
        try {
            List list=diamondService.getGoods();
            return new ResultJson(ResultCode.SUCCESS_CODE, "成功", "", JSON.toJSONString(list)).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultJson(ResultCode.FAILE_CODE, "", e.getMessage(), "").toString();
        }
    }




}
