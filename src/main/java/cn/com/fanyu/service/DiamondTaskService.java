package cn.com.fanyu.service;

import cn.com.fanyu.dao.FyDiamondAccountsRepository;
import cn.com.fanyu.dao.FyDiamondRepository;
import cn.com.fanyu.dao.FyUserRepository;
import cn.com.fanyu.domain.FyDiamond;
import cn.com.fanyu.domain.FyDiamondAccounts;
import cn.com.fanyu.domain.FyUser;
import cn.com.fanyu.utils.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class DiamondTaskService {

    @Autowired
    private FyDiamondRepository fyDiamondRepository;
    @Autowired
    private FyUserRepository fyUserRepository;
    @Autowired
    private RestfulService restfulService;
    @Autowired
    private FyDiamondAccountsRepository fyDiamondAccountsRepository;

    @Transactional
    public void diamondTaskJob(){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        List<FyDiamond> list=fyDiamondRepository.findByIsTimeOutAndAddTimeLessThan(0, cal.getTime());
        for (FyDiamond f:list) {
            try {
                if(f.getIsEndof().intValue()==0){
                    FyUser user=fyUserRepository.getIMUserByUserName(f.getUsername());
                    createFyDiamondAccounts(user,f.getDiamondEmainingNum(),"红包超时退还");
                    String mes="钻石红包超时退还："+f.getDiamondEmainingNum()+"克拉";
                    restfulService.sendMessagetoUser(mes,f.getUsername(),"系统通知");
                }
                f.setIsTimeOut(1);
                fyDiamondRepository.saveAndFlush(f);
            }catch (Exception e){
                f.setIsTimeOut(2);
                fyDiamondRepository.saveAndFlush(f);
            }

        }
    }

    public void createFyDiamondAccounts(FyUser user, BigDecimal diamondNum,String remark) throws BusinessException {
        BigDecimal add = user.getDiamondNum().add(diamondNum);
        if (add.doubleValue()<0) {
            throw new BusinessException("钻石数量不足！");
        }
        FyDiamondAccounts a=new FyDiamondAccounts();
        a.setDiamondNum(diamondNum);
        a.setRemark(remark);
        a.setUsername(user.getUsername());
        a.setUserBeforeNum(user.getDiamondNum());
        user.setDiamondNum(add);
        a.setUserAfterNum(add);
        fyUserRepository.saveAndFlush(user);
        fyDiamondAccountsRepository.saveAndFlush(a);
    }
}
