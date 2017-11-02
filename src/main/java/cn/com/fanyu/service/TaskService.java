package cn.com.fanyu.service;

import cn.com.fanyu.dao.*;
import cn.com.fanyu.domain.*;
import cn.com.fanyu.domain.templates.NiuNiu;
import cn.com.fanyu.domain.templates.NiuNiuDetail;
import cn.com.fanyu.domain.templates.SamePointDetail;
import cn.com.fanyu.utils.DateUtil;
import cn.com.fanyu.utils.TemplatesUtil;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.*;

@Service
public class TaskService {
    @Autowired
    private FyTaskRepository fyTaskRepository;
    @Autowired
    private FyRuleRepository fyRuleRepository;
    @Autowired
    private FyMesBetRepository fyMesBetRepository;
    @Autowired
    private FyDiamondDetailRepository fyDiamondDetailRepository;
    @Autowired
    private FyScoreRepository fyScoreRepository;
    @Autowired
    private FyScoreDetailRepository fyScoreDetailRepository;
    @Autowired
    private FyUserRepository fyUserRepository;


    @Transactional
    public void calculate(FyTask task) {
        StringBuffer sbyxianjia = new StringBuffer("━━━赢━━━\n");
        boolean sbsxianjiaFlag = false;
        StringBuffer sbsxianjia = new StringBuffer("━━━输━━━\n");
        boolean sbyxianjiaFlag = false;
        StringBuffer sbpxianjia = new StringBuffer("━━━跑━━━\n");
        boolean sbpxianjiaFlag = false;
        StringBuffer sbbanker = new StringBuffer("━━━庄家战况━━━\n");
        StringBuffer sbnoBet = new StringBuffer("----未下注抢包------\n");
        StringBuffer sbnoDiamond = new StringBuffer("----下注未抢包------\n");
        //找庄家
        String groupid = task.getGroupid();
        FyRule rule = fyRuleRepository.getRule(groupid);
        String rulejson = rule.getRule();
        NiuNiu niuNiu = JSON.parseObject(rulejson, NiuNiu.class);
        String bankerUsername = niuNiu.getBanker();
        FyUser bankerUser = fyUserRepository.getIMUserByUserName(bankerUsername);
        //找庄家找的红包
        FyMesBet bankerBet = fyMesBetRepository.findByStatusAndTaskIdAndUsername(0, task.getId(), bankerUsername);
        List<FyMesBet> allBet = fyMesBetRepository.findByStatusAndTaskId(0, task.getId());
        if (bankerBet == null) {
            sbbanker.append("[" + bankerUser.getNickname() + "]未抢到红包！");
            sbbanker.append("\n");
            for (FyMesBet bet : allBet) {
                bet.setStatus(1);
                fyMesBetRepository.saveAndFlush(bet);
                BigDecimal[] integral = culcalateScore(bet, new BigDecimal(0), new BigDecimal(0));
                sbsxianjia.append("[" + bet.getNickname() + "] 押:" + bet.getData().intValue()+"上局:" + integral[0] + " 本局:" + integral[1]);
                sbsxianjiaFlag = true;
                sbbanker.append("\n");
            }
        } else {
            NiuNiuDetail bankernndt = getNndt(bankerBet.getPoint(), niuNiu.getDetails());
            String tempstr = "本局庄家[" + bankerUser.getNickname() + "]\n" + "庄包[" + bankerBet.getPoint() + "]" + bankernndt.getName() + "[" + bankernndt.getValue() + "倍]";
            sbbanker.append(tempstr);
            BigDecimal bankerAccount = new BigDecimal(0);
            int zying = 0;//庄家赢数量
            int zshu = 0;//庄家输数量
            int pao = 0;//跑
            BigDecimal xianjiashuifei = new BigDecimal(0);
            for (FyMesBet bet : allBet) {
                if (!bet.getUsername().equals(bankerUsername)) {
                    NiuNiuDetail nndt = getNndt(bet.getPoint(), niuNiu.getDetails());
                    BigDecimal data = bet.getData();
                    //未抢到包的用户
                    if (bet.getPoint().doubleValue() == 0) {
                        BigDecimal[] integral = culcalateScore(bet, new BigDecimal(0), new BigDecimal(0));
                        sbsxianjia.append("[" + bet.getNickname() + "] 押:" + bet.getData().intValue()+"上局:" + integral[0] + " 本局:" + integral[1]);
                        sbnoDiamond.append(bet.getNickname() + ":" + bet.getData());
                        sbnoDiamond.append("\n");
                    } else if (bet.getData().doubleValue() == 0 && !bet.getUsername().equals(bankerUsername)) {
                        //抢包未下注
                        sbnoBet.append(bet.getNickname() + ":" + bet.getPoint());
                        sbnoBet.append("\n");
                    } else {
                        StringBuffer tempsb = new StringBuffer();
                        //计算分数加减
                        tempsb.append("[" + bet.getNickname() + "]" + "\n");
                        tempsb.append("抢:" + bet.getPoint() + " 押:" + data.intValue() + "(" + nndt.getName() + ")");
                        FyScore score = fyScoreRepository.findByGroupidAndUsernameAndStatus(bet.getGroupid(), bet.getUsername(), 1);
                        BigDecimal remainingIntegral = score.getIntegral().add(bet.getData());


                        boolean ztimeout = isTimeout(niuNiu, bankerBet);
                        boolean xtimeout = isTimeout(niuNiu, bet);


                        if (nndt.getSelectId() > bankernndt.getSelectId()) {
                            if (xtimeout) {
                                PaoLu paoLu = new PaoLu(sbpxianjia, tempstr, pao, bet, nndt, tempsb, xtimeout).invoke();
                                sbpxianjiaFlag = paoLu.isSbpxianjiaFlag();
                            } else {
                                ZhuangShu zhuangShu = new ZhuangShu(sbyxianjia, niuNiu, tempstr, bankerAccount, zshu, xianjiashuifei, bet, nndt, data, tempsb, remainingIntegral).invoke();
                                bankerAccount = zhuangShu.getBankerAccount();
                                zshu = zhuangShu.getZshu();
                                xianjiashuifei = zhuangShu.getXianjiashuifei();
                                sbyxianjiaFlag = zhuangShu.isSbyxianjiaFlag();
                            }

                        } else if (nndt.getSelectId() < bankernndt.getSelectId()) {
                            if (ztimeout) {
                                PaoLu paoLu = new PaoLu(sbpxianjia, tempstr, pao, bet, nndt, tempsb, xtimeout).invoke();
                                sbpxianjiaFlag = paoLu.isSbpxianjiaFlag();
                            } else {
                                ZhuangYing zhuangYing = new ZhuangYing(sbsxianjia, bankernndt, tempstr, bankerAccount, zying, bet, data, tempsb, remainingIntegral).invoke();
                                bankerAccount = zhuangYing.getBankerAccount();
                                zying = zhuangYing.getZying();
                                sbsxianjiaFlag = zhuangYing.isSbsxianjiaFlag();
                            }
                        } else {
                            //点数相等
                            List<SamePointDetail> samePointDetail = niuNiu.getSamePointDetail();
                            boolean isdo = false;
                            for (SamePointDetail dt : samePointDetail) {
                                if (dt.getPoint().intValue() == nndt.getSelectId()) {
                                    isdo=true;
                                    switch (dt.getOption().intValue()) {
                                        case 1:
                                            if (ztimeout) {
                                                PaoLu paoLu = new PaoLu(sbpxianjia, tempstr, pao, bet, nndt, tempsb, xtimeout).invoke();
                                                sbpxianjiaFlag = paoLu.isSbpxianjiaFlag();
                                            } else {
                                                ZhuangYing zhuangYing = new ZhuangYing(sbsxianjia, bankernndt, tempstr, bankerAccount, zying, bet, data, tempsb, remainingIntegral).invoke();
                                                bankerAccount = zhuangYing.getBankerAccount();
                                                zying = zhuangYing.getZying();
                                                sbsxianjiaFlag = zhuangYing.isSbsxianjiaFlag();
                                            }
                                            break;
                                        case 2:
                                            PaoLu paoLu1 = new PaoLu(sbpxianjia, tempstr, pao, bet, nndt, tempsb, xtimeout).invoke();
                                            sbpxianjiaFlag = paoLu1.isSbpxianjiaFlag();
                                            break;
                                        case 3:
                                            if (bankerBet.getPoint().compareTo(bet.getPoint()) > 0) {
                                                ZhuangYing zhuangYing = new ZhuangYing(sbsxianjia, bankernndt, tempstr, bankerAccount, zying, bet, data, tempsb, remainingIntegral).invoke();
                                                bankerAccount = zhuangYing.getBankerAccount();
                                                zying = zhuangYing.getZying();
                                                sbsxianjiaFlag = zhuangYing.isSbsxianjiaFlag();

                                            } else if (bankerBet.getPoint().compareTo(bet.getPoint()) < 0) {
                                                //计算账户上的
                                                ZhuangShu zhuangShu = new ZhuangShu(sbyxianjia, niuNiu, tempstr, bankerAccount, zshu, xianjiashuifei, bet, nndt, data, tempsb, remainingIntegral).invoke();
                                                bankerAccount = zhuangShu.getBankerAccount();
                                                zshu = zhuangShu.getZshu();
                                                xianjiashuifei = zhuangShu.getXianjiashuifei();
                                                sbyxianjiaFlag = zhuangShu.isSbyxianjiaFlag();
                                            } else {
                                                PaoLu paoLu = new PaoLu(sbpxianjia, tempstr, pao, bet, nndt, tempsb, xtimeout).invoke();
                                                sbpxianjiaFlag = paoLu.isSbpxianjiaFlag();
                                            }
                                            break;
                                        case 4:
                                            if (bankerBet.getPoint().compareTo(bet.getPoint()) >= 0) {
                                                ZhuangYing zhuangYing = new ZhuangYing(sbsxianjia, bankernndt, tempstr, bankerAccount, zying, bet, data, tempsb, remainingIntegral).invoke();
                                                bankerAccount = zhuangYing.getBankerAccount();
                                                zying = zhuangYing.getZying();
                                                sbsxianjiaFlag = zhuangYing.isSbsxianjiaFlag();

                                            } else if (bankerBet.getPoint().compareTo(bet.getPoint()) < 0) {
                                                //计算账户上的
                                                ZhuangShu zhuangShu = new ZhuangShu(sbyxianjia, niuNiu, tempstr, bankerAccount, zshu, xianjiashuifei, bet, nndt, data, tempsb, remainingIntegral).invoke();
                                                bankerAccount = zhuangShu.getBankerAccount();
                                                zshu = zhuangShu.getZshu();
                                                xianjiashuifei = zhuangShu.getXianjiashuifei();
                                                sbyxianjiaFlag = zhuangShu.isSbyxianjiaFlag();
                                            }
                                            break;
                                        case 5:
                                            ZhuangYing zhuangYing = new ZhuangYing(sbsxianjia, bankernndt, tempstr, bankerAccount, zying, bet, data, tempsb, remainingIntegral).invoke();
                                            bankerAccount = zhuangYing.getBankerAccount();
                                            zying = zhuangYing.getZying();
                                            sbsxianjiaFlag = zhuangYing.isSbsxianjiaFlag();
                                            break;
                                    }
                                }
                            }
                            if (!isdo) {
                                //跑
                                PaoLu paoLu1 = new PaoLu(sbpxianjia, tempstr, pao, bet, nndt, tempsb, xtimeout).invoke();
                                sbpxianjiaFlag = paoLu1.isSbpxianjiaFlag();
                            }
                        }
                    }
                }
                bet.setStatus(1);
                fyMesBetRepository.saveAndFlush(bet);
                //标记收集回来的下注信息已处理
            }
            //庄家总账
            sbbanker.append("\n");
            sbbanker.append("吃" + zying + "家,赔" + zshu + "家,跑"+pao + "家\n");
            //人头水费
            BigDecimal rtcvalue = niuNiu.getRtcvalue().setScale(0, BigDecimal.ROUND_HALF_UP);
            ;
            BigDecimal rtc = rtcvalue.multiply(new BigDecimal(allBet.size() - 1));
            if (bankerAccount.compareTo(new BigDecimal(0)) >= 0) {
                //水费
                sbbanker.append("积分情况:赢" + bankerAccount.intValue() + "\n");
                BigDecimal ylcvalue = niuNiu.getYlcvalue();
                BigDecimal ylshuifei = bankerAccount.multiply(ylcvalue).setScale(0, BigDecimal.ROUND_HALF_UP);
                bankerAccount = bankerAccount.subtract(ylshuifei).subtract(rtc);
                bankerBet.setYlshuifei(ylshuifei);
                bankerBet.setRtcshuifei(rtc);
                sbbanker.append("庄家水费:" + ylshuifei.add(rtc) + "\n");
                sbbanker.append("闲家水费:" + xianjiashuifei + "\n");
                sbbanker.append("红包费用:" + bankerBet.getTotalDiamond() + "\n");
                fyMesBetRepository.saveAndFlush(bankerBet);
                BigDecimal integral = culcalatezScore(bankerBet, bankerAccount, ylshuifei, rtc);
                sbbanker.append("剩余庄分:" + integral.intValue() + "\n");
            } else {
                sbbanker.append("积分情况:输" + bankerAccount.multiply(new BigDecimal(-1)).intValue() + "\n");
                bankerAccount = bankerAccount.subtract(rtc);
                sbbanker.append("庄家水费:" + rtc.intValue() + "\n");
                sbbanker.append("闲家水费:" + xianjiashuifei + "\n");
                sbbanker.append("红包费用:" + bankerBet.getTotalDiamond() + "\n");
                BigDecimal integral = culcalatezScore(bankerBet, bankerAccount, new BigDecimal(0), rtc);
                sbbanker.append("剩余庄分" + integral.intValue() + "\n");
            }
        }
        StringBuffer temp = new StringBuffer();
        if (sbyxianjiaFlag) {
            temp.append(sbyxianjia);
        }
        if (sbsxianjiaFlag) {
            temp.append(sbsxianjia);
        }
        if (sbpxianjiaFlag) {
            temp.append(sbpxianjia);
        }
        task.setMessage("" + temp + sbbanker + sbnoDiamond + sbnoBet);
        task.setStatus(1);
        fyTaskRepository.saveAndFlush(task);
    }

    private boolean isTimeout(NiuNiu niuNiu, FyMesBet bankerBet) {
        Date receivedDiamondTime = bankerBet.getReceivedDiamondTime();
        Date sendDiamondTime = bankerBet.getSendDiamondTime();
        long interval = (receivedDiamondTime.getTime() - sendDiamondTime.getTime()) / 1000;
        if (niuNiu.getTimeout() < interval) {
            return true;
        } else {
            return false;
        }
    }

    public static void main(String[] args) {
        BigDecimal b = new BigDecimal(1.6);
        System.out.println(b.intValue());
    }

    private int compareTo(BigDecimal bankerPoint, BigDecimal xianjiaPoint) {
        int banker = (int) bankerPoint.doubleValue() * 100;
        int maxBanker = 0;
        while (banker != 0) {
            int temp = banker % 10;
            if (temp > maxBanker) {
                maxBanker = temp;
            }
            banker /= 10;
        }
        int xianjia = (int) xianjiaPoint.doubleValue() * 100;
        int maxXianjia = 0;
        while (xianjia != 0) {
            int temp = xianjia % 10;
            if (temp > maxXianjia) {
                maxXianjia = temp;
            }
            xianjia /= 10;
        }
        if (maxBanker > maxXianjia) {
            return 1;
        } else if (maxBanker < maxXianjia) {
            return -1;
        } else {
            return 0;
        }
    }

    /**
     * @param bet
     * @param mul
     * @return
     */
    private BigDecimal[] culcalateScore(FyMesBet bet, BigDecimal mul, BigDecimal xc) {
        BigDecimal[] rt = new BigDecimal[2];
        FyScore score = fyScoreRepository.findByGroupidAndUsernameAndStatus(bet.getGroupid(), bet.getUsername(), 1);
        rt[0] = score.getIntegral().add(bet.getData());
        FyScoreDetail scoreDt = new FyScoreDetail();
        scoreDt.setAddTime(new Date());
        scoreDt.setScoreId(score.getId());
        scoreDt.setGroupid(score.getGroupid());
        scoreDt.setUsername(score.getUsername());
        scoreDt.setFreeBefore(score.getFreeIntegral());
        scoreDt.setIntegralBefore(score.getIntegral());
        String remark;
        if (mul.compareTo(new BigDecimal(0)) > 0) {
            remark = "闲家赢";
        } else if (mul.compareTo(new BigDecimal(0)) < 0) {
            remark = "闲家输";
        } else {
            remark = "闲家跑路";
        }
        scoreDt.setReMark(remark + mul.toString());
        score.setIntegral(score.getIntegral().add(mul).add(bet.getData()));
        score.setFreeIntegral(score.getFreeIntegral().subtract(bet.getData()));
        scoreDt.setIntegral(mul);
        scoreDt.setFreeAfter(score.getFreeIntegral());
        scoreDt.setIntegralAfter(score.getIntegral());
        scoreDt.setXc(xc);
        scoreDt.setType(3);
        scoreDt.setBetId(bet.getId());
        scoreDt.setTaskId(bet.getTaskId());
        fyScoreRepository.saveAndFlush(score);
        fyScoreDetailRepository.saveAndFlush(scoreDt);
        rt[1] = score.getIntegral();
        return rt;
    }

    private BigDecimal culcalatezScore(FyMesBet bet, BigDecimal mul, BigDecimal ylc, BigDecimal rct) {
        FyScore score = fyScoreRepository.findByGroupidAndUsernameAndStatus(bet.getGroupid(), bet.getUsername(), 1);
        FyScoreDetail scoreDt = new FyScoreDetail();
        scoreDt.setAddTime(new Date());
        scoreDt.setScoreId(score.getId());
        scoreDt.setGroupid(score.getGroupid());
        scoreDt.setUsername(score.getUsername());
        scoreDt.setIntegralBefore(score.getZintegral());
        score.setZintegral(score.getZintegral().add(mul));
        scoreDt.setIntegral(mul);
        scoreDt.setIntegralAfter(score.getZintegral());
        scoreDt.setYlc(ylc);
        scoreDt.setRtc(rct);
        scoreDt.setType(4);
        String remark;
        if (mul.compareTo(new BigDecimal(0)) >= 0) {
            remark = "庄家赢";
        } else {
            remark = "庄家输";
        }
        scoreDt.setReMark(remark);
        scoreDt.setBetId(bet.getId());
        scoreDt.setTaskId(bet.getTaskId());
        fyScoreRepository.saveAndFlush(score);
        fyScoreDetailRepository.saveAndFlush(scoreDt);
        return score.getZintegral();
    }

    private FyMesBet getBet(List<FyMesBet> allBets, FyDiamondDetail dt) {
        for (FyMesBet bet : allBets) {
            if (bet.getUsername().equals(dt.getUsername())) {
                return bet;
            }
        }
        return null;
    }

    private NiuNiuDetail getNndt(BigDecimal bankerDiamon, List<NiuNiuDetail> details) {
        int option = getOption(bankerDiamon);
        for (NiuNiuDetail nndt : details) {
            if (nndt.getSelectId().equals(option)) {
                return nndt;
            }
        }
        NiuNiuDetail dt = new NiuNiuDetail();
        dt.setName(TemplatesUtil.niuniuTemplates(option));
        dt.setValue(new BigDecimal(1));
        dt.setSelectId(option);
        return dt;
    }


    private Integer getOption(BigDecimal p) {
        int pp = p.multiply(new BigDecimal("100")).intValue();
        int[] ch = new int[3];
        ch[2] = pp % 10;
        ch[1] = pp / 10 % 10;
        ch[0] = pp / 100 % 10;
        //豹子
        if (ch[0] == ch[1] && ch[1] == ch[2]) {
            return TemplatesUtil.baozi;
        }
        if (ch[1] == 0 && ch[2] == 0) {
            return TemplatesUtil.manniu;
        }
        if (ch[0] + 1 == ch[1] && ch[1] + 1 == ch[2]) {
            return TemplatesUtil.shuzi;
        }
        if (ch[0] - 1 == ch[1] && ch[1] - 1 == ch[2]) {
            return TemplatesUtil.daoshun;
        }
        if (ch[1] == ch[2] && ch[0] == 0) {
            return TemplatesUtil.duizi;
        }
        if (ch[2] == 0 && ch[0] == 0) {
            return TemplatesUtil.jinniu;
        }
        if ((ch[0] + ch[1] + ch[2]) % 10 == 0) {
            return TemplatesUtil.niuniu;
        }
        return (ch[0] + ch[1] + ch[2]) % 10;
    }

    private class ZhuangShu {
        private StringBuffer sbyxianjia;
        private NiuNiu niuNiu;
        private String tempstr;
        private BigDecimal bankerAccount;
        private int zshu;
        private BigDecimal xianjiashuifei;
        private FyMesBet bet;
        private NiuNiuDetail nndt;
        private BigDecimal data;
        private StringBuffer tempsb;
        private BigDecimal remainingIntegral;
        private boolean sbyxianjiaFlag;

        public ZhuangShu(StringBuffer sbyxianjia, NiuNiu niuNiu, String tempstr, BigDecimal bankerAccount, int zshu, BigDecimal xianjiashuifei, FyMesBet bet, NiuNiuDetail nndt, BigDecimal data, StringBuffer tempsb, BigDecimal remainingIntegral) {
            this.sbyxianjia = sbyxianjia;
            this.niuNiu = niuNiu;
            this.tempstr = tempstr;
            this.bankerAccount = bankerAccount;
            this.zshu = zshu;
            this.xianjiashuifei = xianjiashuifei;
            this.bet = bet;
            this.nndt = nndt;
            this.data = data;
            this.tempsb = tempsb;
            this.remainingIntegral = remainingIntegral;
        }

        public boolean isSbyxianjiaFlag() {
            return sbyxianjiaFlag;
        }

        public BigDecimal getBankerAccount() {
            return bankerAccount;
        }

        public int getZshu() {
            return zshu;
        }

        public BigDecimal getXianjiashuifei() {
            return xianjiashuifei;
        }

        public ZhuangShu invoke() {
            //计算账户上的
            BigDecimal peilv = new BigDecimal("1");//赔率
            if (bet.getRegularType().intValue() == 0) {
                peilv = nndt.getValue();
            }
            BigDecimal mul = data.multiply(peilv);
            if (mul.compareTo(remainingIntegral) > 0) {
                mul = remainingIntegral;
            }
            bankerAccount = bankerAccount.subtract(mul);
            //水费
            Integer xcType = niuNiu.getXcType();
            BigDecimal xcvalue = nndt.getXcvalue();
            BigDecimal shuifei = new BigDecimal(0);
            if (xcType.intValue() == 1) {
                shuifei = xcvalue.multiply(mul);
            } else {
                shuifei = xcvalue;
            }
            shuifei = shuifei.setScale(0, BigDecimal.ROUND_HALF_UP);
            mul = mul.subtract(shuifei);
            bet.setXshuifei(shuifei);
            BigDecimal[] integral = culcalateScore(bet, mul, shuifei);
            tempsb.append(" " + peilv + "倍 赢" + mul.intValue() + "\n");
            tempsb.append("上局:" + integral[0].intValue() + " 本局:" + integral[1].intValue());
            bet.setRemark(tempsb.toString() + ";" + tempstr);
            bet.setResult(mul);
            zshu++;
            xianjiashuifei = xianjiashuifei.add(shuifei);
            tempsb.append("\n");
            sbyxianjia.append(tempsb);
            sbyxianjiaFlag = true;
            return this;
        }
    }

    private class ZhuangYing {
        private StringBuffer sbsxianjia;
        private NiuNiuDetail bankernndt;
        private String tempstr;
        private BigDecimal bankerAccount;
        private int zying;
        private FyMesBet bet;
        private BigDecimal data;
        private StringBuffer tempsb;
        private BigDecimal remainingIntegral;
        private boolean sbsxianjiaFlag;

        public ZhuangYing(StringBuffer sbsxianjia, NiuNiuDetail bankernndt, String tempstr, BigDecimal bankerAccount, int zying, FyMesBet bet, BigDecimal data, StringBuffer tempsb, BigDecimal remainingIntegral) {
            this.sbsxianjia = sbsxianjia;
            this.bankernndt = bankernndt;
            this.tempstr = tempstr;
            this.bankerAccount = bankerAccount;
            this.zying = zying;
            this.bet = bet;
            this.data = data;
            this.tempsb = tempsb;
            this.remainingIntegral = remainingIntegral;
        }

        public boolean isSbsxianjiaFlag() {
            return sbsxianjiaFlag;
        }

        public BigDecimal getBankerAccount() {
            return bankerAccount;
        }

        public int getZying() {
            return zying;
        }

        public ZhuangYing invoke() {
            //计算账户上的
            BigDecimal peilv = new BigDecimal("1");//赔率
            if (bet.getRegularType().intValue() == 0) {
                peilv = bankernndt.getValue();
            }
            BigDecimal mul = data.multiply(peilv);
            if (mul.compareTo(remainingIntegral) > 0) {
                mul = remainingIntegral;
            }
            BigDecimal[] integral = culcalateScore(bet, mul.multiply(new BigDecimal(-1)), new BigDecimal(0));
            tempsb.append(" " + peilv + "倍 输" + mul.intValue() + "\n");
            tempsb.append("上局:" + integral[0].intValue() + " 本局:" + integral[1].intValue());
            bet.setRemark(tempsb.toString() + ";" + tempstr);
            bet.setResult(mul);
            bankerAccount = bankerAccount.add(mul);
            zying++;
            tempsb.append("\n");
            sbsxianjia.append(tempsb);
            sbsxianjiaFlag = true;
            return this;
        }
    }

    private class PaoLu {
        private StringBuffer sbpxianjia;
        private String tempstr;
        private int pao;
        private FyMesBet bet;
        private NiuNiuDetail nndt;
        private StringBuffer tempsb;
        private boolean xtimeout;
        private boolean sbpxianjiaFlag;

        public PaoLu(StringBuffer sbpxianjia, String tempstr, int pao, FyMesBet bet, NiuNiuDetail nndt, StringBuffer tempsb, boolean xtimeout) {
            this.sbpxianjia = sbpxianjia;
            this.tempstr = tempstr;
            this.pao = pao;
            this.bet = bet;
            this.nndt = nndt;
            this.tempsb = tempsb;
            this.xtimeout = xtimeout;
        }

        public boolean isSbpxianjiaFlag() {
            return sbpxianjiaFlag;
        }

        public int getPao() {
            return pao;
        }

        public PaoLu invoke() {
            //计算账户上的
            BigDecimal peilv = new BigDecimal("1");//赔率
            if (bet.getRegularType().intValue() == 0) {
                peilv = nndt.getValue();
            }
            BigDecimal[] integral = culcalateScore(bet, new BigDecimal(0), new BigDecimal(0));
            tempsb.append(" " + peilv + "倍 跑");
            if (xtimeout) {
                Date receivedDiamondTime = bet.getReceivedDiamondTime();
                Date sendDiamondTime = bet.getSendDiamondTime();
                long interval = (receivedDiamondTime.getTime() - sendDiamondTime.getTime()) / 1000;
                tempsb.append(" 超" + interval + " 抢包时间" + DateUtil.format(bet.getReceivedDiamondTime(), "HH:mm:ss"));
            }
            tempsb.append("\n");
            tempsb.append("上局:" + integral[0].intValue() + " 本局:" + integral[1].intValue());
            bet.setRemark(tempsb.toString() + ";" + tempstr);
            bet.setResult(new BigDecimal(0));
            pao++;
            tempsb.append("\n");
            sbpxianjia.append(tempsb);
            sbpxianjiaFlag = true;
            return this;
        }
    }
}
