package cn.com.fanyu.service;

import cn.com.fanyu.dao.FyRuleRepository;
import cn.com.fanyu.dao.FyScoreRepository;
import cn.com.fanyu.dao.FyUserRepository;
import cn.com.fanyu.domain.*;
import cn.com.fanyu.domain.templates.NiuNiu;
import cn.com.fanyu.domain.templates.NiuNiuDetail;
import cn.com.fanyu.domain.templates.SamePointDetail;
import cn.com.fanyu.utils.BusinessException;
import cn.com.fanyu.utils.StringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.swagger.client.model.Msg;
import io.swagger.client.model.UserName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TemplatesService {
    @Autowired
    private FyRuleRepository fyRuleRepository;
    @Autowired
    private FyScoreRepository fyScoreRepository;
    @Autowired
    private RestfulService restfulService;

    public void editRule(NiuNiu templates, String groupid) throws BusinessException {
        FyRule rule = fyRuleRepository.getRule(groupid);
        String rulejson = rule.getRule();
        NiuNiu niuNiu = JSON.parseObject(rulejson, NiuNiu.class);
        FyScore banker = fyScoreRepository.findByGroupidAndUsernameAndStatus(groupid, templates.getBanker(), 1);
        if(niuNiu.getMinBankerScore().compareTo(banker.getZintegral())>0){
            throw new BusinessException("庄家庄分不足！");

        }
        niuNiu.setBanker(templates.getBanker());
        niuNiu.setMinBankerScore(templates.getMinBankerScore());
        niuNiu.setTimeout(templates.getTimeout());
        niuNiu.setMinBet(templates.getMinBet());
        niuNiu.setMaxBet(templates.getMaxBet());
        niuNiu.setMinsuoha(templates.getMinsuoha());
        niuNiu.setMaxsuoha(templates.getMaxsuoha());
        niuNiu.setSzcvalue(templates.getSzcvalue());
        niuNiu.setYlcvalue(templates.getYlcvalue());
        niuNiu.setRtcvalue(templates.getRtcvalue());
        niuNiu.setXcType(templates.getXcType());
//        //发送规则变化到app
//        boolean sentRegularFlag = false;
//        if (!StringUtil.isNullOrEmpty(templates.getShangfenContent()) && !templates.getShangfenContent().equals(niuNiu.getShangfenContent())) {
//            //转正则
//            String s = translocationToRegular(templates.getShangfenContent());
//            templates.setShangfenRegular(s);
//            sentRegularFlag = true;
//        }else {
//            templates.setShangfenRegular(niuNiu.getShangfenRegular());
//        }
//        if (!StringUtil.isNullOrEmpty(templates.getXiazhuContent()) && !templates.getXiazhuContent().equals(niuNiu.getXiazhuContent())) {
//            String s = translocationToRegular(templates.getXiazhuContent());
//            templates.setXiazhuRegular(s);
//            sentRegularFlag = true;
//        }else {
//            templates.setXiazhuRegular(niuNiu.getXiazhuRegular());
//        }
//        if (!StringUtil.isNullOrEmpty(templates.getBankerContent()) && !templates.getBankerContent().equals(niuNiu.getBankerContent())) {
//            String s = translocationToRegular(templates.getBankerContent());
//            templates.setBankerReguler(s);
//            sentRegularFlag = true;
//        }else {
//            templates.setBankerReguler(niuNiu.getBankerReguler());
//        }
//        if (!StringUtil.isNullOrEmpty(templates.getSuohaContent()) && !templates.getSuohaContent().equals(niuNiu.getSuohaContent())) {
//            String s = translocationToRegular(templates.getSuohaContent());
//            templates.setSuohaRegular(s);
//            sentRegularFlag = true;
//        }else {
//            templates.setSuohaRegular(niuNiu.getSuohaRegular());
//        }
//        if (sentRegularFlag) {
//            setRegularMes(templates, groupid);
//        }
        rule.setRule(JSON.toJSONString(niuNiu));
        fyRuleRepository.saveAndFlush(rule);
    }

    public String setRegularMes(NiuNiu templates, String groupid) {
        Msg msg = new Msg();
        msg.targetType("chatgroups");
        UserName userName = new UserName();
        userName.add(groupid);
        msg.setTarget(userName);
        msg.setFrom("admin");
        Map msgobj = new HashMap();
        msgobj.put("type", "txt");
        msgobj.put("msg", "正则表达式修改");
        msg.setMsg(msgobj);
        Map ext = new HashMap();
        ext.put("type", "regular");
        Map retr = getRegular(templates);
        ext.put("data", retr);
        msg.setExt(ext);
        String ret = restfulService.sendMessagetoGroup(msg);
        return ret;
    }

    private Map getRegular(NiuNiu templates) {
        Map map = new HashMap();
        map.put("bankerReguler", templates.getBankerReguler());
        map.put("shangfenRegular", templates.getShangfenRegular());
        map.put("xiazhuRegular", templates.getXiazhuRegular());
        map.put("suohaRegular", templates.getSuohaRegular());
        return map;
    }

    private String translocationToRegular(String content) {
        String replaceContent = content.replaceAll("\\+", "").replaceAll("数字", "\\\\d+(\\.\\\\d+)?");
        return "^" + replaceContent + "$";
    }
//
//    public static void main(String[] args) {
//        Pattern p = Pattern.compile("^\\d+(\\.\\d+)?$");
//        Matcher matcher =p.matcher("555.2222");
//        boolean is_matches = matcher.matches();
//        System.out.println(is_matches);
//    }

    public List getAllUser(String groupid) {
        ArrayList list =new ArrayList();
         List<Object[]> scores=fyScoreRepository.findUserInfo(groupid);
        for (Object[] o:scores) {
            Map map=new HashMap();
            map.put("username",o[0]);
            map.put("nickname",o[1]);
            list.add(map);
        }
        return list;
    }

    public String getNiuNiuTemplates(String groupid) {
        FyRule rule = fyRuleRepository.getRule(groupid);
        return rule.getRule();
    }

    public void editSamePoint(SamePointDetail dt, String groupid) {
        FyRule rule = fyRuleRepository.getRule(groupid);
        String rulejson = rule.getRule();
        NiuNiu niuNiu = JSON.parseObject(rulejson, NiuNiu.class);
        List<SamePointDetail> details = niuNiu.getSamePointDetail();
        setSamePointFromDts(details, dt);
        rulejson = JSON.toJSONString(niuNiu);
        rule.setRule(rulejson);
        fyRuleRepository.saveAndFlush(rule);
    }

    private void setSamePointFromDts(List<SamePointDetail> details, SamePointDetail newdt) {
        boolean flag = true;
        for (SamePointDetail dt : details) {
            if (dt.getPoint().intValue()==newdt.getPoint().intValue()) {
                dt.setOption(newdt.getOption());
                flag = false;
            }
        }
        if (flag) {
            details.add(newdt);
        }
    }

    public void editRuleDetail(NiuNiuDetail dt, String groupid) {
        FyRule rule = fyRuleRepository.getRule(groupid);
        String rulejson = rule.getRule();
        NiuNiu niuNiu = JSON.parseObject(rulejson, NiuNiu.class);
        List<NiuNiuDetail> details = niuNiu.getDetails();
        setDtFromDts(details, dt);
        getMaxValue(rule,niuNiu);
        rulejson = JSON.toJSONString(niuNiu);
        rule.setRule(rulejson);
        fyRuleRepository.saveAndFlush(rule);
    }

    private void setDtFromDts(List<NiuNiuDetail> details, NiuNiuDetail dt) {
        boolean flag = true;
        for (NiuNiuDetail nndt : details) {
            if (nndt.getSelectId().equals(dt.getSelectId()) && nndt.getType().intValue() == dt.getType().intValue()) {
                nndt.setName(dt.getName());
                nndt.setValue(dt.getValue());
                flag = false;
            }
        }
        if (flag) {
            details.add(dt);
        }
    }

    public List getRuleDetail(String groupid) {
        FyRule rule = fyRuleRepository.getRule(groupid);
        String rulejson = rule.getRule();
        NiuNiu niuNiu = JSON.parseObject(rulejson, NiuNiu.class);
        List<NiuNiuDetail> details = niuNiu.getDetails();
        List<NiuNiuDetail> retDts = new ArrayList<>();
        for (NiuNiuDetail dt : details) {
            retDts.add(dt);
        }
        return retDts;
    }

    public List getsamePoint(String groupid) {
        FyRule rule = fyRuleRepository.getRule(groupid);
        String rulejson = rule.getRule();
        NiuNiu niuNiu = JSON.parseObject(rulejson, NiuNiu.class);
        List<SamePointDetail> details = niuNiu.getSamePointDetail();
        List<Map> retDts = new ArrayList<>();
        for (SamePointDetail dt : details) {
            Map map =new HashMap();
            map.put("point",dt.getPoint());
            map.put("name","庄闲同为牛"+dt.getPoint());
            if(dt.getOption().intValue()==1){
                map.put("value","庄赢");
            }else if(dt.getOption().intValue()==2){
                map.put("value","跑路");
            }else if(dt.getOption().intValue()==3){
                map.put("value","比金额(同平)");
            }else if(dt.getOption().intValue()==4){
                map.put("value","比金额(庄赢)");
            }else if(dt.getOption().intValue()==5){
                map.put("value","庄输");
            }
            retDts.add(map);
        }
        return retDts;
    }

    public List getRuleDetailSpecial(String groupid) {
        FyRule rule = fyRuleRepository.getRule(groupid);
        String rulejson = rule.getRule();
        NiuNiu niuNiu = JSON.parseObject(rulejson, NiuNiu.class);
        List<NiuNiuDetail> details = niuNiu.getDetails();
        List<NiuNiuDetail> retDts = new ArrayList<>();
        for (NiuNiuDetail dt : details) {
            if (dt.getType().intValue() == 2) {
                retDts.add(dt);
            }
        }
        return retDts;
    }

    public void delRuleDetail(NiuNiuDetail dt, String groupid) {
        FyRule rule = fyRuleRepository.getRule(groupid);
        String rulejson = rule.getRule();
        NiuNiu niuNiu = JSON.parseObject(rulejson, NiuNiu.class);
        List<NiuNiuDetail> details = niuNiu.getDetails();
        Iterator<NiuNiuDetail> iter = details.iterator();
        while (iter.hasNext()) {
            NiuNiuDetail nndt = iter.next();
            if (nndt.getType().intValue() == dt.getType().intValue() && nndt.getSelectId().equals(dt.getSelectId())) {
                iter.remove();
            }
        }
        getMaxValue(rule, niuNiu);
        rulejson = JSON.toJSONString(niuNiu);
        rule.setRule(rulejson);
        fyRuleRepository.saveAndFlush(rule);
    }

    public void delSamePoint(SamePointDetail dt, String groupid) {
        FyRule rule = fyRuleRepository.getRule(groupid);
        String rulejson = rule.getRule();
        NiuNiu niuNiu = JSON.parseObject(rulejson, NiuNiu.class);
        List<SamePointDetail> details = niuNiu.getSamePointDetail();
        Iterator<SamePointDetail> iter = details.iterator();
        while (iter.hasNext()) {
            SamePointDetail nndt = iter.next();
            if (nndt.getPoint().equals(dt.getPoint())) {
                iter.remove();
            }
        }
        rulejson = JSON.toJSONString(niuNiu);
        rule.setRule(rulejson);
        fyRuleRepository.saveAndFlush(rule);
    }

    private void getMaxValue(FyRule rule, NiuNiu niuNiu) {
        BigDecimal maxValue=new BigDecimal(0);
        for (NiuNiuDetail n:niuNiu.getDetails()) {
            if(n.getValue().compareTo(maxValue)>0){
                maxValue=n.getValue();
            }
        }
        rule.setMaxValue(maxValue);
    }

    public void editRuleDetailById(NiuNiuDetail dt, String groupid) {
        FyRule rule = fyRuleRepository.getRule(groupid);
        String rulejson = rule.getRule();
        NiuNiu niuNiu = JSON.parseObject(rulejson, NiuNiu.class);
        List<NiuNiuDetail> details = niuNiu.getDetails();
        for (NiuNiuDetail nndt : details) {
            if (nndt.getSelectId().equals(dt.getSelectId())) {
                nndt.setValue(dt.getValue());
                nndt.setXcvalue(dt.getXcvalue());
                nndt.setStartFrom(dt.getStartFrom());
                nndt.setEndTo(dt.getEndTo());
            }
        }
        rulejson = JSON.toJSONString(niuNiu);
        rule.setRule(rulejson);
        fyRuleRepository.saveAndFlush(rule);
    }

    public Map getRegularByGroupid(String groupid) {
        FyRule rule = fyRuleRepository.getRule(groupid);
        if(rule==null){
            return new HashMap();
        }
        String rulejson = rule.getRule();
        NiuNiu niuNiu = JSON.parseObject(rulejson, NiuNiu.class);
        Map map = getRegular(niuNiu);
        return map;
    }
}
