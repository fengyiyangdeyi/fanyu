package cn.com.fanyu.service;

import cn.com.fanyu.dao.*;
import cn.com.fanyu.domain.*;
import cn.com.fanyu.domain.templates.NiuNiu;
import cn.com.fanyu.domain.templates.NiuNiuDetail;
import cn.com.fanyu.utils.BusinessException;
import cn.com.fanyu.utils.StringUtil;
import com.alibaba.fastjson.JSON;
import io.swagger.client.model.Msg;
import io.swagger.client.model.UserName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class SendMesService {

    @Autowired
    private FyRuleRepository fyRuleRepository;
    @Autowired
    private RestfulService restfulService;
    @Autowired
    private FyMesBetRepository fyMesBetRepository;
    @Autowired
    private FyTaskRepository fyTaskRepository;
    @Autowired
    private FyUserRepository fyUserRepository;
    @Autowired
    private FyScoreRepository fyScoreRepository;


    public Map createRuleMes(String groupid) {
        StringBuffer sb = new StringBuffer();
        FyRule rule = fyRuleRepository.getRule(groupid);
        if (rule == null) {
            throw new BusinessException("未定义规则");
        }
        String rulejson = rule.getRule();
        NiuNiu niuNiu = JSON.parseObject(rulejson, NiuNiu.class);
        if(StringUtil.isNullOrEmpty(niuNiu.getBanker())){
            throw new BusinessException("请选定庄家");
        }
        FyUser user = fyUserRepository.getIMUserByUserName(niuNiu.getBanker());
        String bancker=user.getNickname();
        sb.append("本局庄家:"+bancker+"\n");
        FyScore score = fyScoreRepository.findByGroupidAndUsernameAndStatus(groupid, user.getUsername(), 1);
        sb.append("本局庄费:"+score.getZintegral()+"\n");
        sb.append("￣￣￣￣￣￣￣￣￣￣"+"\n");
        sb.append("最低押注:"+niuNiu.getMinBet().intValue()+"\n");
        sb.append("最高押注:"+niuNiu.getMaxBet().intValue()+"\n");
        sb.append("最低梭哈:"+niuNiu.getMinsuoha().intValue()+"\n");
        sb.append("最高梭哈:"+niuNiu.getMaxsuoha().intValue()+"\n");
        sb.append("┄┄┄┄┄┄┄┄┄┄"+"\n");
        sb.append("☞请下注☜"+"\n");
        Map map =new HashMap();
        map.put("mes",sb.toString());
        return map;
    }

    public void sendBeginXiazhuMes(String groupid,String mes) {
        FyRule rule = fyRuleRepository.getRule(groupid);
        rule.setXiazhuFlag(1);
        fyRuleRepository.saveAndFlush(rule);
        restfulService.sendMessagetoGroup(mes,groupid);
    }

    public void sendEndXiazhuMes(String groupid, String mes) {
        FyRule rule = fyRuleRepository.getRule(groupid);
        rule.setXiazhuFlag(0);
        rule.setQiangbaoStatus(1);
        fyRuleRepository.saveAndFlush(rule);
        restfulService.sendMessagetoGroup(mes,groupid);
    }

    public Map refreshBet(String groupid) {
        StringBuffer sb = new StringBuffer();
        List<FyMesBet> list=fyMesBetRepository.findByStatusAndGroupid(new Integer(0),groupid);
        sb.append("───────────");
        sb.append("\n");
        BigDecimal sum =new BigDecimal(0);
        for (FyMesBet dt : list) {
            sb.append("‣["+dt.getNickname()+"]" + dt.getContent());//认
            sb.append("\n");
            sum=sum.add(dt.getData());
        }
        Map map =new HashMap();
        String head="㊒效下注:➠"+list.size()+"人"+"\n";
        StringBuffer tail=new StringBuffer();
        tail.append("───────────\n");
        tail.append("当局总数："+sum.intValue()+"注\n");
        FyRule rule = fyRuleRepository.getRule(groupid);
        String rulejson = rule.getRule();
        NiuNiu niuNiu = JSON.parseObject(rulejson, NiuNiu.class);
        String bankerNickname="";
        if(!StringUtil.isNullOrEmpty(niuNiu.getBanker())){
            FyUser banker = fyUserRepository.getIMUserByUserName(niuNiu.getBanker());
            bankerNickname=banker.getNickname();
        }
        tail.append("本局庄家："+bankerNickname+"\n");
        tail.append("━━━停止下注━━━");
        map.put("mes",head+sb.toString()+tail);
        return map;
    }

    public Map refreshResult(String groupid) {
        Map map =new HashMap();
        FyRule rule = fyRuleRepository.getRule(groupid);
        if (rule.getXiazhuFlag().intValue()==1||rule.getQiangbaoStatus().intValue()==1){
            map.put("mes","");
        }else {
            FyTask task=fyTaskRepository.findLashTask(groupid);
            if(task!=null){
                map.put("mes",task.getMessage());
            }
        }

        return map;
    }
}
