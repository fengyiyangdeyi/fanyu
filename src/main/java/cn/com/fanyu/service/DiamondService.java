package cn.com.fanyu.service;

import cn.com.fanyu.dao.*;
import cn.com.fanyu.domain.*;
import cn.com.fanyu.utils.BusinessException;
import io.swagger.client.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.*;

@Service
public class DiamondService {
    @Autowired
    private FyDiamondRepository fyDiamondRepository;
    @Autowired
    private FyUserRepository fyUserRepository;
    @Autowired
    private FyDiamondDetailRepository fyDiamondDetailRepository;
    @Autowired
    private FyRuleRepository fyRuleRepository;
    @Autowired
    private FyTaskRepository fyTaskRepository;
    @Autowired
    private FyMesBetRepository fyMesBetRepository;
    @Autowired
    private RestfulService restfulService;
    @Autowired
    private DiamondTaskService diamondTaskService;
    @Autowired
    private FyGoodsRepository fyGoodsRepository;


//    @Transactional
    public Map sendDiamond(FyDiamond fyDiamond) throws BusinessException {
        //保存数据红包数据
        fyDiamond.setDiamondEmainingNum(fyDiamond.getDiamondNum());
        //判断是规则红包
        FyRule rule = fyRuleRepository.getRule(fyDiamond.getGroupid());
        if (rule != null && rule.getQiangbaoStatus().intValue() == 1) {
            fyDiamond.setIsRule(1);
        }
        FyDiamond r_fyDiamond = fyDiamondRepository.saveAndFlush(fyDiamond);
        //扣减用户钻石
        FyUser user = fyUserRepository.getIMUserByUserName(fyDiamond.getUsername());
        diamondTaskService.createFyDiamondAccounts(user,fyDiamond.getDiamondNum().multiply(new BigDecimal(-1)),"发红包");
        Map map = new HashMap();
        map.put("fyDiamondId", r_fyDiamond.getId());
        sendDiamondMes(fyDiamond,user);
        return map;
    }

    public String sendDiamondMes(FyDiamond fyDiamond,FyUser user){
        Msg msg = new Msg();
        msg.targetType("chatgroups");
        UserName userName = new UserName();
        userName.add(fyDiamond.getGroupid());
        msg.setTarget(userName);
        msg.setFrom(fyDiamond.getUsername());
        Map msgobj = new HashMap();
        msgobj.put("type", "txt");
        msgobj.put("msg", "[泛娱钻石包]"+fyDiamond.getRemark());
        msg.setMsg(msgobj);
        Map ext = new HashMap();
        ext.put("count", fyDiamond.getNum());
        ext.put("desc", fyDiamond.getRemark());
        ext.put("diamond", true);
        ext.put("fyDiamondId", fyDiamond.getId());
        ext.put("total", fyDiamond.getDiamondNum());
        ext.put("username",fyDiamond.getUsername());
        ext.put("nickname",user.getNickname());
        ext.put("imgUrl",user.getImgUrl());
        msg.setExt(ext);
        String ret = restfulService.sendMessagetoGroup(msg);
        return ret;
    }

    @Transactional
    public Map receivedDiamond(FyDiamondDetail dt) throws BusinessException {
        FyUser user=fyUserRepository.getIMUserByUserName(dt.getUsername());
        if(user.getDiamondNum().compareTo(new BigDecimal(1))<0){
            throw new BusinessException("钻石不足抢红包失败");
        }
        FyDiamond diamond = fyDiamondRepository.findOne(dt.getFyDiamondId());
//        try {
//            System.out.println("begain===");
//            Thread.sleep(30000L);
//            System.out.println("end========");
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        //抢完了
        if (diamond.getNumPast().intValue() == diamond.getNum().intValue()) {
            Map map = getDiamondInfo(dt);
            return map;
        }
        //已抢过，返回列表
        FyDiamondDetail mydt = fyDiamondDetailRepository.getDt(dt.getUsername(), dt.getFyDiamondId());
        if (mydt != null) {
            return getDiamondInfo(dt);
        }
        dt.setDiamondBeforeNum(diamond.getDiamondEmainingNum());
        //计算红包
        double randomMoney = getRandomMoney(diamond);
        //保存明细
        dt.setDiamondAfterNum(diamond.getDiamondEmainingNum());
        dt.setDiamondNum(new BigDecimal(randomMoney + ""));
        fyDiamondDetailRepository.saveAndFlush(dt);
        //是否是规则红包
        if (diamond.getIsRule().intValue() == 1) {
            FyMesBet bet = fyMesBetRepository.findByStatusAndGroupidAndUsername(0, diamond.getGroupid(), dt.getUsername());
            if (bet == null) {
                //抢包未下注的
                bet = new FyMesBet();
                bet.setData(new BigDecimal(0));
                bet.setUsername(dt.getUsername());
                bet.setNickname(user.getNickname());
                bet.setGroupid(diamond.getGroupid());
                bet.setType(1);
            }
            bet.setPoint(new BigDecimal(randomMoney));
            bet.setDiamondId(diamond.getId());
            bet.setDiamondDetailId(dt.getId());
            bet.setReceivedDiamondTime(dt.getAddTime());
            bet.setTotalDiamond(diamond.getDiamondNum());
            bet.setSendDiamondTime(diamond.getAddTime());
            fyMesBetRepository.saveAndFlush(bet);
        }
        //扣减红包数量
        fyDiamondRepository.saveAndFlush(diamond);
        //增加抢的用户的钻石数量
        diamondTaskService.createFyDiamondAccounts(user,new BigDecimal(randomMoney+"").subtract(new BigDecimal(1)),"抢红包"+randomMoney+"(-1)");
        //
        //计算结果
        if(diamond.getIsEndof().intValue()==1){
            createTask(diamond);
        }
        return getDiamondInfo(dt);
    }


    public double getRandomMoney(FyDiamond _leftMoneyPackage) {
        // remainSize 剩余的红包数量
        int remainSize = _leftMoneyPackage.getNum() - _leftMoneyPackage.getNumPast().intValue();
        // remainMoney 剩余的钱
        if (remainSize == 1) {
            double lastmoney = (double) Math.round(_leftMoneyPackage.getDiamondEmainingNum().multiply(new BigDecimal("100")).doubleValue()) / 100;
            _leftMoneyPackage.setNumPast(_leftMoneyPackage.getNumPast() + 1);
            _leftMoneyPackage.setDiamondPastNum(_leftMoneyPackage.getDiamondNum());
            _leftMoneyPackage.setDiamondEmainingNum(new BigDecimal(0));
            _leftMoneyPackage.setIsEndof(1);
            return lastmoney;
        }
        Random r = new Random();
        double min = 0.01; //
        double max = _leftMoneyPackage.getDiamondEmainingNum().doubleValue() / remainSize * 2;
        double money = r.nextDouble() * max;
        money = money <= min ? 0.01 : money;
        money = Math.floor(money * 100) / 100;
        _leftMoneyPackage.setNumPast(_leftMoneyPackage.getNumPast() + 1);
        _leftMoneyPackage.setDiamondPastNum(_leftMoneyPackage.getDiamondPastNum().add(new BigDecimal(money + "")));
        _leftMoneyPackage.setDiamondEmainingNum(_leftMoneyPackage.getDiamondEmainingNum().subtract(new BigDecimal(money + "")));
        return money;
    }

    private void createTask(FyDiamond _leftMoneyPackage) {
        if (_leftMoneyPackage.getIsRule().intValue() == 1) {
            FyTask task = new FyTask();
            task.setAddTime(new Date());
            task.setDiamondId(_leftMoneyPackage.getId());
            task.setGroupid(_leftMoneyPackage.getGroupid());
            fyTaskRepository.saveAndFlush(task);
            FyRule rule = fyRuleRepository.getRule(_leftMoneyPackage.getGroupid());
            rule.setQiangbaoStatus(0);
            List<FyMesBet> bets = fyMesBetRepository.findByStatusAndGroupid(0, _leftMoneyPackage.getGroupid());
            for (FyMesBet bet:bets) {
                bet.setTaskId(task.getId());
                fyMesBetRepository.saveAndFlush(bet);
            }
        }
    }

    public Map checkReceive(FyDiamondDetail dt) {
        FyDiamondDetail dbdt = fyDiamondDetailRepository.getDt(dt.getUsername(), dt.getFyDiamondId());
        Map map = new HashMap();
        if (dbdt != null) {
            map.put("isReceive", 1);
        } else {
            map.put("isReceive", 0);
        }
        FyDiamond fd = fyDiamondRepository.findOne(dt.getFyDiamondId());
        map.put("endof",fd.getIsEndof());
        return map;
    }

    public Map getDiamondInfo(FyDiamondDetail dt) {
        Map map = new HashMap();
        FyDiamondDetail mydt = fyDiamondDetailRepository.getDt(dt.getUsername(), dt.getFyDiamondId());
        if (mydt != null) {
            map.put("diamondNum", mydt.getDiamondNum());//抢到的数量
        }
        FyDiamond one = fyDiamondRepository.findOne(dt.getFyDiamondId());
        map.put("username", one.getUsername());
        FyUser sendUser = fyUserRepository.getIMUserByUserName(one.getUsername());
        map.put("nickname",sendUser.getNickname());
        map.put("imgUrl",sendUser.getImgUrl());
        map.put("remark", one.getRemark());
        map.put("endof",one.getIsEndof());
        StringBuffer sb = new StringBuffer();
        sb.append("已领取");
        sb.append(one.getNumPast());
        sb.append("/");
        sb.append(one.getNum() + ",共");
        sb.append(one.getDiamondPastNum());
        sb.append("/");
        sb.append(one.getDiamondNum());
        map.put("info", sb);
        List<Object[]> list = fyDiamondDetailRepository.getDtAndUserinfoByDiamondId(dt.getFyDiamondId());
        List listPoJo = new ArrayList();
        for (Object[] d : list) {
            Map mapPoJo = new HashMap();
            mapPoJo.put("username", d[0]);
            mapPoJo.put("addTime", d[1]);
            mapPoJo.put("diamondNum", d[2]);
            mapPoJo.put("nickname", d[3]);
            mapPoJo.put("imgUrl", d[4]);
            listPoJo.add(mapPoJo);
        }
        map.put("list", listPoJo);
        return map;
    }

    public Map receivedDiamondLoop(FyDiamondDetail dt) throws Exception {
        for (int i = 0; i < 3; i++) {
            try {
//                System.out.println("================我又来抢了" + i);
                return receivedDiamond(dt);
            } catch (ObjectOptimisticLockingFailureException e) {
//                System.out.println("version=============");
            }
        }
//        throw new Exception("未抢到钻石！");
        Map map = getDiamondInfo(dt);
        return map;
    }

    public List getGoods() {
        List<FyGoods> all = fyGoodsRepository.findAll();
        List list=new ArrayList();
        for (FyGoods g:all) {
            Map map=new HashMap();
            map.put("id",g.getId());
            map.put("price",g.getPrice());
            map.put("diamondNum",g.getDiamondNum());
            map.put("imgUrl",g.getImgUrl());
            map.put("appleid",g.getAppleid());
            map.put("givingDiamondNum",g.getGivingDiamondNum());
            list.add(map);
        }
        return list;
    }
}
