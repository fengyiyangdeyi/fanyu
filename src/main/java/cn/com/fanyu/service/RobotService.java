package cn.com.fanyu.service;

import cn.com.fanyu.dao.*;
import cn.com.fanyu.domain.*;
import cn.com.fanyu.domain.templates.NiuNiu;
import cn.com.fanyu.servlet.UserVo;
import cn.com.fanyu.utils.*;
import com.alibaba.fastjson.JSON;
import io.swagger.client.model.Msg;
import io.swagger.client.model.UserName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class RobotService {
    @Autowired
    private FyRuleRepository fyRuleRepository;
    @Autowired
    private FyScoreRepository fyScoreRepository;
    @Autowired
    private FyUserRepository fyUserRepository;
    @Autowired
    private FyAnnouncementRepository fyAnnouncementRepository;
    @Autowired
    private FyMesScoreRepository fyMesScoreRepository;
    @Autowired
    private FyRegularRepository fyRegularRepository;
    @Autowired
    private FyGroupRepository fyGroupRepository;
    @Autowired
    private FyRuleTemplatesRepository fyRuleTemplatesRepository;
    @Autowired
    private FyScoreDetailRepository fyScoreDetailRepository;
    @Autowired
    private FyRuleDetailRepository fyRuleDetailRepository;
    @Autowired
    private RestfulService restfulService;
    @Autowired
    private FyMesBetRepository fyMesBetRepository;
    @Autowired
    private TemplatesService templatesService;

    public FyRule getRule(String groupId) {
        FyRule rule = fyRuleRepository.getRule(groupId);
        if (rule == null) {
            return null;
        }
        return rule;
    }

    @Transactional
    public void addIntegral(FyScore fyScore, BigDecimal value,String groupid) {
        FyScore score=fyScoreRepository.findByGroupidAndId(groupid,fyScore.getId());
        if(score.getIntegral().add(value).compareTo(new BigDecimal(0))<0){
            throw new BusinessException("处理失败，请检查输入！");
        }
        String remark;
        if(value.compareTo(new BigDecimal(0))>=0){
            remark="上分";
        }else {
            remark="下分";
        }
        createScoreDetail(score, value,remark);
        score.setIntegral(score.getIntegral().add(value));
        fyScoreRepository.saveAndFlush(score);
    }

    public UserVo login(FyUser user, String groupid) throws Exception {
        FyUser dbuser = fyUserRepository.getIMUserByUserName(user.getUsername());
        String encryption = MD5Tools.MD5(user.getPassword());
        if (dbuser == null) {
            throw new Exception("用户不存在！");
        }
        if (!encryption.equals(dbuser.getPassword())) {
            throw new Exception("密码有误！");
        }
        if (!StringUtil.isNullOrEmpty(groupid)) {
            FyGroup chatGroup = fyGroupRepository.getChatGroup(groupid);
            if (chatGroup == null) {
                throw new Exception("群id有误！");
            }
        } else {
            if (!"7".equals(dbuser.getPermissions())) {
                throw new Exception("groupid不能为空！");
            }
        }
        UserVo v = new UserVo();
        v.setUsername(dbuser.getUsername());
        v.setGroupid(groupid);
        v.setImgUrl(dbuser.getImgUrl());
        v.setPermissions(dbuser.getPermissions());
        v.setNickname(dbuser.getNickname());
        return v;
    }

    public String getMemberByGroubId(String groupid, Integer page, Integer limit,String keyword) {
        Integer pageIndex = (page - 1) * limit;
        Integer pageSize = limit;
        long count = 0;
        List<Object[]> list=new ArrayList<>();
        if(StringUtil.isNullOrEmpty(keyword)){
            count = fyScoreRepository.count((root, query, cb) -> {
                query.where(cb.equal(root.get("groupid"), groupid),
                        cb.equal(root.get("status"), 1)); //这里可以设置任意条查询条件
                return null;
            });
            list = fyScoreRepository.getMemberByGroubidNative(groupid, pageIndex, pageSize);
        }else {
            count=fyScoreRepository.countMemberByGroubidAndNickNative(groupid,"%"+keyword+"%");
            list = fyScoreRepository.getMemberByGroubidAndNickNative(groupid, pageIndex, pageSize,"%"+keyword+"%");
        }
        List relist = new ArrayList();
        for (Object[] f : list) {
            Map map = new HashMap();
            map.put("id", f[0]);
            map.put("username", f[1]);
            map.put("integral", f[2]);
            map.put("nickname", f[3]);
            map.put("relateNickname", f[4]);
            map.put("zintegral", f[5]);
            relist.add(map);
        }
        return new LayuiResultJson(0, "sucesssful", count, JSON.toJSONString(relist)).toString();
    }

    public String getMemberByGroubId2(FyGroup group, Integer page, Integer pageSize) {

//        PageRequest pageRequest = new PageRequest(page - 1, pageSize, Sort.Direction.DESC, "createDate");
        PageRequest pageRequest = new PageRequest(page - 1, pageSize);
        Page<FyScore> list = fyScoreRepository.findAll((root, query, cb) -> {
            query.where(cb.equal(root.get("groupId"), group.getGroupid())); //这里可以设置任意条查询条件
            return null;
        }, pageRequest);

        List relist = new ArrayList();
        for (FyScore f : list) {
            Map map = new HashMap();
            map.put("id", f.getId());
            map.put("username", f.getUsername());
            map.put("integral", f.getIntegral());
            map.put("operation", "<button class=\"layui-btn layui-btn-small\">上下分</button>");
            relist.add(map);
        }
        return new LayuiResultJson(0, "sucesssful", 5, JSON.toJSONString(relist)).toString();
    }

    public void updateAnnouncement(FyAnnouncement announcement) {
        announcement.setAddTime(new Date());
        fyAnnouncementRepository.saveAndFlush(announcement);
    }

    public FyAnnouncement findAnnouncement() {
        List<FyAnnouncement> all = fyAnnouncementRepository.findAll();
        if (all.size() > 0) {
            return all.get(0);
        } else {
            FyAnnouncement f = new FyAnnouncement();
            f.setContent("");
            f.setAddTime(new Date());
            FyAnnouncement fyAnnouncement = fyAnnouncementRepository.saveAndFlush(f);
            return fyAnnouncement;
        }
    }

    public Map findAnnouncementPOJO() {
        FyAnnouncement announcement = findAnnouncement();
        Map map = new HashMap();
        map.put("content", announcement.getContent());
        map.put("appContent", announcement.getAppContent());
        return map;
    }

    public List getRegular(String groupid) {
        List<FyRegular> list = fyRegularRepository.getRegular(groupid);
        return list;
    }

    private List getRegularList(List<FyRegular> list) {
        List retr = new ArrayList();
        for (FyRegular r : list) {
            Map rPojo = new HashMap();
            rPojo.put("rType", r.getRegularType());
            rPojo.put("rContent", r.getRegularContent());
            rPojo.put("rName", r.getName());
            rPojo.put("r", r.getRegular());
            rPojo.put("rGroupid", r.getGroupid());
            rPojo.put("id", r.getId());
            retr.add(rPojo);
        }
        return retr;
    }

    public String selectGroup(Integer page, Integer pageSize) {
        Integer pageIndex = (page - 1) * pageSize;
        long count = fyGroupRepository.count();
        List<Object[]> list = fyGroupRepository.findAllWithUserinfo(pageIndex, pageSize);
        List relist = new ArrayList();
        for (Object[] f : list) {
            Map map = new HashMap();
            map.put("id", f[0]);
            map.put("groupid", f[1]);
            map.put("groupname", f[2]);
            map.put("nickname", f[3]);
            map.put("code",f[4]);
            relist.add(map);
        }
        return new LayuiResultJson(0, "sucesssful", count, JSON.toJSONString(relist)).toString();
    }

    public List<FyRuleTemplates> getTemplates() {
        return fyRuleTemplatesRepository.findAll();
    }

    public String getTemplatesByName(String pageName) {
        FyRuleTemplates t = fyRuleTemplatesRepository.getTemplatesByName(pageName);
        return t.getTemplates();
    }

    public void delRule(String id) {
        fyRuleRepository.deleteByGroupid(id);
    }

    @Transactional
    public void editRegular(FyRegular regular, String groupid) {
        FyRegular by = fyRegularRepository.findByRegularTypeAndGroupid(regular.getRegularType(), groupid);
        if (by == null) {
            by = new FyRegular();
            by.setAddTime(new Date());
            by.setGroupid(groupid);
            by.setRegularType(regular.getRegularType());
        }
        by.setRegularContent(regular.getRegularContent());
        //转正则
        String content = regular.getRegularContent();
        //下注+数字
        String replaceContent = content.replaceAll("\\+", "").replaceAll("数字", "\\\\d+");
        by.setRegular("^" + replaceContent + "$");
        by.setName(regular.getName());
        fyRegularRepository.saveAndFlush(by);
        setRegularMes(groupid);
    }

    @Transactional
    public void delRegular(Long id) {
        FyRegular one = fyRegularRepository.findOne(id);
        String groupid = one.getGroupid();
        fyRegularRepository.delete(id);
        setRegularMes(one.getGroupid());
    }

    @Transactional
    public void editRegularById(Long id, String regularContent) {
        FyRegular by = fyRegularRepository.findOne(id);
        by.setRegularContent(regularContent);
        //转正则
        String replaceContent = regularContent.replaceAll("\\+", "").replaceAll("数字", "\\\\d+");
        by.setRegular("^" + replaceContent + "$");
        fyRegularRepository.saveAndFlush(by);
        setRegularMes(by.getGroupid());
    }

    public List<FyRuleDetail> getRuleDetail(String groupid) {
        FyRule rule = fyRuleRepository.getRule(groupid);
        if (rule != null) {
            return fyRuleDetailRepository.findByTypeAndGroupidAndRuleId(1, groupid, rule.getId());
        } else {
            return null;
        }
    }

    public List<FyRuleDetail> getRuleDetailSpecial(String groupid) {
        FyRule rule = fyRuleRepository.getRule(groupid);
        if (rule != null) {
            return fyRuleDetailRepository.findByTypeAndGroupidAndRuleId(2, groupid, rule.getId());
        } else {
            return null;
        }
    }

    @Transactional
    public void editRuleDetail(FyRuleDetail dt, String groupid) {
        FyRule rule = getRule(groupid);
        if (rule == null) {
            rule = createRule(groupid, "page_niuniu", "牛牛");
        }
        FyRuleDetail by = fyRuleDetailRepository.findByTypeAndGroupidAndSelectIdAndRuleId(dt.getType(), groupid, dt.getSelectId(), rule.getId());
        if (by == null) {
            by = new FyRuleDetail();
            by.setAddTime(new Date());
            by.setGroupid(groupid);
            by.setType(dt.getType());
            by.setPageName(dt.getPageName());
            by.setSelectId(dt.getSelectId());
            by.setRuleId(rule.getId());
        }
        by.setName(dt.getName());
        by.setValue(dt.getValue());
        fyRuleDetailRepository.saveAndFlush(by);
    }

    private FyRule createRule(String groupid, String page_niuniu, String name) {
        FyRule rule = new FyRule();
        rule.setAddTime(new Date());
        rule.setName(name);
        rule.setPageName(page_niuniu);
        rule.setGroupId(groupid);
        return fyRuleRepository.saveAndFlush(rule);
    }

    public void delRuleDetail(Long id) {
        fyRuleDetailRepository.delete(id);
    }

    public void editRuleDetailById(FyRuleDetail dt) {
        FyRuleDetail by = fyRuleDetailRepository.findOne(dt.getId());
        by.setValue(dt.getValue());
        fyRuleDetailRepository.saveAndFlush(by);
    }

    @Transactional
    public void createRule(FyRule rule, String groupid) {
        rule.setAddTime(new Date());
        rule.setGroupId(groupid);
        FyRule fixRule = fyRuleRepository.findByName(rule.getName() + "固定模版");
        rule.setRule(fixRule.getRule());
        NiuNiu niuNiu = JSON.parseObject(fixRule.getRule(), NiuNiu.class);
        templatesService.setRegularMes(niuNiu,groupid);
        fyRuleRepository.saveAndFlush(rule);
    }

    public String getMesScore(String groupid, Integer page, Integer pageSize) {
        Integer pageIndex = (page - 1) * pageSize;
        long count = fyMesScoreRepository.count((root, query, cb) -> {
            query.where(cb.equal(root.get("groupid"), groupid)); //这里可以设置任意条查询条件
            return null;
        });
        List<Object[]> list = fyMesScoreRepository.findMesScore(groupid, pageIndex, pageSize);
        List relist = new ArrayList();
        for (Object[] f : list) {
            Map map = new HashMap();
            map.put("id", f[0]);
            map.put("username", f[1]);
            map.put("content", f[2]);
//            map.put("addTime",DateUtil.format(f[3].toString()));
            map.put("nickname", f[4]);
            relist.add(map);
        }
        return new LayuiResultJson(0, "sucesssful", count, JSON.toJSONString(relist)).toString();
    }

    public void delMesScore(Long id) {
        fyMesScoreRepository.delete(id);
    }

    @Transactional
    public String addScoreByMes(FyMesScore mes, BigDecimal value) {
        FyScore fi = fyScoreRepository.findByGroupidAndUsernameAndStatus(mes.getGroupid(), mes.getUsername(),1);
        String r1;
        if(value.compareTo(new BigDecimal(0))>0){
            r1="上分";
        }else {
            r1="下分";
        }
        createScoreDetail(fi, value,r1);
        fi.setIntegral(fi.getIntegral().add(value));
        fyScoreRepository.saveAndFlush(fi);
        fyMesScoreRepository.delete(mes);
        return "";
    }

    @Transactional
    public void addzIntegral(FyScore mes, BigDecimal value,String groupid) {
        FyScore fi = fyScoreRepository.findByGroupidAndId(groupid,mes.getId());
        BigDecimal temp = fi.getIntegral().subtract(value);
        if(temp.compareTo(new BigDecimal(0))<0){
            throw new BusinessException("积分不足无法上庄分！");
        }
        String r1;
        String r2;
        if(value.compareTo(new BigDecimal(0))>0){
            r1="上庄分";
            r2="上庄分,扣积分";
            createScoreDetail(fi, value.multiply(new BigDecimal("-1")),r2);
            fi.setIntegral(temp);
            BigDecimal realValue=createzScoreDetail(fi, value,r1);
            fi.setZintegral(fi.getZintegral().add(realValue));
            fyScoreRepository.saveAndFlush(fi);
        }else {
            r1="下庄分";
            r2="下庄分,增加到积分中";
            createScoreDetail(fi, value.multiply(new BigDecimal("-1")),r2);
            fi.setIntegral(temp);
            createxzScoreDetail(fi, value,r1);
            fi.setZintegral(fi.getZintegral().add(value));
            fyScoreRepository.saveAndFlush(fi);
        }
    }

    private void createScoreDetail(FyScore fi, BigDecimal value,String remark) {
        FyScoreDetail dt = new FyScoreDetail();
        dt.setAddTime(new Date());
        dt.setGroupid(fi.getGroupid());
        dt.setIntegral(value);
        dt.setScoreId(fi.getId());
        dt.setUsername(fi.getUsername());
        dt.setIntegralBefore(fi.getIntegral());
        dt.setIntegralAfter(fi.getIntegral().add(value));
        dt.setType(1);
        dt.setReMark(remark);
        fyScoreDetailRepository.saveAndFlush(dt);
    }

    private BigDecimal createzScoreDetail(FyScore fi, BigDecimal value,String remark) {
        FyRule rule = fyRuleRepository.getRule(fi.getGroupid());
        String rulejson = rule.getRule();
        NiuNiu niuNiu = JSON.parseObject(rulejson, NiuNiu.class);
        BigDecimal szcvalue = niuNiu.getSzcvalue();
        BigDecimal szc=szcvalue.multiply(value);
        BigDecimal realValue=value.subtract(szc);
        FyScoreDetail dt = new FyScoreDetail();
        dt.setYlc(szc);
        dt.setAddTime(new Date());
        dt.setGroupid(fi.getGroupid());
        dt.setIntegral(realValue);
        dt.setScoreId(fi.getId());
        dt.setUsername(fi.getUsername());
        dt.setIntegralBefore(fi.getZintegral());
        dt.setIntegralAfter(fi.getZintegral().add(realValue));
        dt.setType(2);
        dt.setReMark(remark);
        fyScoreDetailRepository.saveAndFlush(dt);
        return realValue;
    }

    private void createxzScoreDetail(FyScore fi, BigDecimal value,String remark) {
        FyScoreDetail dt = new FyScoreDetail();
        dt.setAddTime(new Date());
        dt.setGroupid(fi.getGroupid());
        dt.setIntegral(value);
        dt.setScoreId(fi.getId());
        dt.setUsername(fi.getUsername());
        dt.setIntegralBefore(fi.getZintegral());
        dt.setIntegralAfter(fi.getZintegral().add(value));
        dt.setType(2);
        dt.setReMark(remark);
        fyScoreDetailRepository.saveAndFlush(dt);
    }

    public String setRegularMes(String groupid) {
        List<FyRegular> list = fyRegularRepository.findByGroupid(groupid);
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
        List retr = getRegularList(list);
        ext.put("data", retr);
        msg.setExt(ext);
        String ret = restfulService.sendMessagetoGroup(msg);
        return ret;
    }

    @Transactional
    public void robotMes(String groupid, String username, String content, String regularType) throws BusinessException {
        if (regularType.equals("shangfenRegular")) {
            FyMesScore s = new FyMesScore();
            s.setAddTime(new Date());
            s.setContent(content);
            s.setGroupid(groupid);
            s.setUsername(username);
            fyMesScoreRepository.saveAndFlush(s);
        } else if (regularType.equals("xiazhuRegular")||regularType.equals("suohaRegular")) {
            FyRule rule = fyRuleRepository.getRule(groupid);
            String rulejson = rule.getRule();
            NiuNiu niuNiu = JSON.parseObject(rulejson, NiuNiu.class);
            if (rule.getXiazhuFlag().intValue() == 0) {
                throw new BusinessException("非下注时间,下注失败");
            }
            BigDecimal xiazhuDate = new BigDecimal(0);
            if(regularType.equals("xiazhuRegular")){
                xiazhuDate = getXiazhuDate(rule, content);
            }else if(regularType.equals("suohaRegular")){
                xiazhuDate = getSuohaDate(rule, content);
                if (niuNiu.getMinsuoha()!=null&&niuNiu.getMinsuoha().compareTo(xiazhuDate)>0) {
                    throw new BusinessException("最低梭哈金额"+niuNiu.getMinsuoha());
                }
                if(niuNiu.getMaxsuoha().compareTo(xiazhuDate)<0){
                    throw new BusinessException("最大梭哈金额"+niuNiu.getMaxsuoha());
                }
            }
            if (niuNiu.getMinBet()!=null&&niuNiu.getMinBet().compareTo(xiazhuDate)>0) {
                throw new BusinessException("最低下注金额"+niuNiu.getMinBet());
            }
            if(niuNiu.getMaxBet().compareTo(xiazhuDate)<0){
                throw new BusinessException("最大下注金额"+niuNiu.getMaxBet());
            }
            if (xiazhuDate.compareTo(BigDecimal.ZERO) <= 0) {
                throw new BusinessException( "下注金额有问题");
            }
            if (StringUtil.isNullOrEmpty(niuNiu.getBanker())) {
                throw new BusinessException( "庄家未确定，下注失败");
            }
            if (username.equals(niuNiu.getBanker())) {
                throw new BusinessException( "庄家不能下注！！！");
            }
            FyMesBet checkBet = fyMesBetRepository.findByStatusAndGroupidAndUsername(0, groupid, username);
            if (checkBet != null) {
                throw new BusinessException( "不能重复下注！");
            }
            //冻结分数
            FyScore score = fyScoreRepository.findByGroupidAndUsernameAndStatus(groupid,username,1);
            //最大金额从这里控制
//            BigDecimal maxValue = rule.getMaxValue();
//            fyRuleRepository.getRule(groupid);
//            if (regularType.equals("xiazhuRegular")&&score.getIntegral().compareTo(xiazhuDate.multiply(maxValue)) < 0) {
//                throw new BusinessException( "金额不足下注失败！");
//            }
//            if (regularType.equals("suohaRegular")&&score.getIntegral().compareTo(xiazhuDate) < 0) {
//                throw new BusinessException( "金额不足下注失败！");
//            }
            if (score.getIntegral().compareTo(xiazhuDate) < 0) {
                throw new BusinessException("金额不足下注失败！");
            }
            FyScoreDetail scoreDt = new FyScoreDetail();
            scoreDt.setIntegralBefore(score.getIntegral());
            scoreDt.setFreeBefore(score.getFreeIntegral());
            score.setFreeIntegral(xiazhuDate);
            score.setIntegral(score.getIntegral().subtract(xiazhuDate));
            scoreDt.setIntegralAfter(score.getIntegral());
            scoreDt.setFreeAfter(score.getFreeIntegral());
            fyScoreRepository.saveAndFlush(score);
            fyScoreDetailRepository.saveAndFlush(scoreDt);
            FyMesBet s = new FyMesBet();
            s.setAddTime(new Date());
            s.setContent(content);
            s.setGroupid(groupid);
            s.setUsername(username);
            FyUser user = fyUserRepository.getIMUserByUserName(username);
            s.setNickname(user.getNickname());
            s.setData(xiazhuDate);
            if(regularType.equals("suohaRegular")){
                s.setRegularType(1);
            }
            fyMesBetRepository.saveAndFlush(s);
        } else if (regularType.equals("bankerReguler")) {
            throw new BusinessException("正则类型不匹配");
        }
    }

    private BigDecimal getXiazhuDate(FyRule rule, String content) {
        String rulejson = rule.getRule();
        NiuNiu niuNiu = JSON.parseObject(rulejson, NiuNiu.class);
        String s = niuNiu.getXiazhuRegular().replaceAll("\\\\d\\+", "(\\\\\\d\\+)");
        System.out.println(s);
        Pattern p = Pattern.compile(s);
        Matcher m = p.matcher(content);
        while (m.find()) {
            String group = m.group(1);
            return new BigDecimal(group);
        }
        return null;
    }

    private BigDecimal getSuohaDate(FyRule rule, String content) {
        String rulejson = rule.getRule();
        NiuNiu niuNiu = JSON.parseObject(rulejson, NiuNiu.class);
//        String s = niuNiu.getSuohaRegular().replaceAll("\\\\d\\+(\\.\\\\d+)?", "(\\\\\\d\\+(\\.\\\\\\d+)?)");
        String s = niuNiu.getSuohaRegular().replaceAll("\\\\d\\+", "(\\\\\\d\\+)");
        System.out.println(s);
        Pattern p = Pattern.compile(s);
        Matcher m = p.matcher(content);
        while (m.find()) {
            String group = m.group(1);
            return new BigDecimal(group);
        }
        return null;
    }


//    public static void main(String[] args) {
////        String content = "上分102";
////
////        String pattern = "^上分\\d+$|^下分\\d+$";
////
////        boolean isMatch = Pattern.matches(pattern, content);
////        System.out.println("字符串中是否包含了 'runoob' 子字符串? " + isMatch);
//
//        String str = "我要下注10";
//        Pattern p = Pattern.compile("^我要下注(\\d+)$");
//        Matcher m = p.matcher(str);
//        ArrayList<String> strs = new ArrayList<String>();
//        while (m.find()) {
//            strs.add(m.group(1));
//        }
//        for (String s : strs){
//            System.out.println(s);
//        }
//    }
}



