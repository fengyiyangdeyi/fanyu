package cn.com.fanyu.service;

import cn.com.fanyu.dao.*;
import cn.com.fanyu.domain.*;
import cn.com.fanyu.huanxin.api.impl.EasemobIMUsers;
import cn.com.fanyu.utils.BusinessException;
import cn.com.fanyu.utils.IdGen;
import cn.com.fanyu.utils.MD5Tools;
import cn.com.fanyu.utils.StringUtil;
import io.swagger.client.model.RegisterUsers;
import io.swagger.client.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.*;

@Service
public class UserService {
    @Autowired
    private FyUserRepository fyUserRepository;
    @Autowired
    private FyGroupRepository fyGroupRepository;
    @Autowired
    private FyScoreRepository fyScoreRepository;
    @Autowired
    private RestfulService restfulService;
    @Value("${add-diamond}")
    private boolean addDiamond;
    @Autowired
    private FyGlobalValueRepository fyGlobalValueRepository;
    private EasemobIMUsers eiu = new EasemobIMUsers();
    @Autowired
    private FyVersionStatusRepository fyVersionStatusRepository;

    public void updateUser(String username, String imgUrl, String nickname) throws Exception {
        FyUser user = fyUserRepository.getIMUserByUserName(username);
        if (!StringUtil.isNullOrEmpty(imgUrl)) {
            user.setImgUrl(imgUrl);
        }
        if (!StringUtil.isNullOrEmpty(imgUrl)) {
            user.setNickname(nickname);
        }
        fyUserRepository.saveAndFlush(user);
    }

    public Map getGroupCode(String groupid) {
        Map map = new HashMap();
        FyGroup chatGroup = fyGroupRepository.getChatGroup(groupid);
        if (chatGroup != null) {
            map.put("code", chatGroup.getCode());
            map.put("comfirmStatus", chatGroup.getComfirmStatus());
            List<Object[]> userInfos = fyScoreRepository.findUserInfo(groupid);
            List list = new ArrayList();
            for (Object[] u : userInfos) {
                Map mapdt = new HashMap();
                mapdt.put("username", u[0]);
                mapdt.put("nickname", u[1]);
                mapdt.put("imgUrl", u[2]);
                list.add(mapdt);
            }
            map.put("groupmember", list);
        }
        return map;
    }

    public Map getGroupid(String code) {
        Map map = new HashMap();
        FyGroup chatGroup = fyGroupRepository.findByCode(code);
        if (chatGroup == null) {
            throw new BusinessException("房间不存在!");
        }
        map.put("groupid", chatGroup.getGroupid());
        map.put("comfirmStatus", chatGroup.getComfirmStatus());

        return map;
    }

    @Transactional
    public void deleteGroup(String groupid) {
        FyGroup group = fyGroupRepository.getChatGroup(groupid);
        fyGroupRepository.delete(group);
        restfulService.deleteChatGroup(groupid);
    }

    public void addD(Long id, BigDecimal num) throws BusinessException {
        FyUser user = fyUserRepository.findOne(id);
        if (user == null) {
            throw new BusinessException("user not exist");
        }

        if (!addDiamond) {
            throw new BusinessException("can not edit!status is unactive");
        }
        user.setDiamondNum(user.getDiamondNum().add(num));
        fyUserRepository.saveAndFlush(user);
    }

    public void editGroupinfo(FyGroup group) {
        FyGroup dbgroup = fyGroupRepository.getChatGroup(group.getGroupid());
        if (!StringUtil.isNullOrEmpty(group.getRemark())) {
            dbgroup.setRemark(group.getRemark());
        }
        if (!StringUtil.isNullOrEmpty(group.getGroupname())) {
            dbgroup.setGroupname(group.getGroupname());
        }
        Integer comfirmStatus = group.getComfirmStatus();
        if (comfirmStatus != null) {
            dbgroup.setComfirmStatus(comfirmStatus);
        }
        fyGroupRepository.saveAndFlush(dbgroup);
        restfulService.modifyChatGroup(group.getGroupid(), dbgroup.getGroupname(), dbgroup.getRemark());
    }

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void testT() throws BusinessException {
        cn.com.fanyu.domain.User u = new cn.com.fanyu.domain.User();
        u.setName("aaa");
        userRepository.saveAndFlush(u);
        throw new BusinessException("fuck");
    }

    @Transactional
    public void removeSingleUserFromChatGroup(String groupid, String username) {
        FyScore s = fyScoreRepository.findByGroupidAndUsernameAndStatus(groupid, username, 1);
        s.setStatus(0);
        fyScoreRepository.saveAndFlush(s);
        restfulService.removeSingleUserFromChatGroup(groupid, username);
    }

    public void editRelate(String groupid, String relateNickname, Long id) {
        List<String> objects = fyScoreRepository.findRelateNicknameNative(groupid, relateNickname);
        if(objects.size()!=1){
            throw new BusinessException("找不到用户");
        }
        FyScore one = fyScoreRepository.findOne(id);
        one.setRelateUsername(objects.get(0));
        fyScoreRepository.saveAndFlush(one);
    }

    public Map getVersion(String type) {
        FyGlobalValue value= fyGlobalValueRepository.findByStatusAndGlobalKey(1, type);
        Map map =new HashMap();
        map.put("type",type);
        map.put("version",value.getGlobalValue());
        return map;
    }

    public Map register(String phone, String pwd) {
        Map map = new HashMap<>();
        FyUser user=new FyUser();
        user.setUsername(phone);
        user.setPhone(phone);
        FyUser dbuser = fyUserRepository.getIMUserByUserName(user.getUsername());
        if (dbuser == null) {
            user.setPassword(MD5Tools.MD5(pwd));
            dbuser=fyUserRepository.saveAndFlush(user);
            RegisterUsers list = new RegisterUsers();
            User u=new User();
            u.setUsername(user.getUsername());
            u.setPassword("fanyu");
            list.add(u);
            eiu.createNewIMUserSingle(list);
        }else {
            throw new BusinessException("用户已注册！");
        }
        FyUser byUuid;
        String uuid;
        do {
            uuid = IdGen.uuid();
            byUuid = fyUserRepository.findByUuid(uuid);
        }while (byUuid!=null);
        dbuser.setUuid(uuid);
        fyUserRepository.saveAndFlush(dbuser);


        map.put("id",dbuser.getId());
        map.put("username",dbuser.getUsername());
        map.put("imgUrl",dbuser.getImgUrl());
        map.put("nickname",dbuser.getNickname());
        map.put("diamondNum",dbuser.getDiamondNum());
        map.put("uuid",dbuser.getUuid());

        //支付
        FyGlobalValue payStatus = fyGlobalValueRepository.findByStatusAndGlobalKey(1, "payStatus");
        map.put("payStatus",payStatus.getGlobalValue());
        //转盘
        FyGlobalValue rotaryTable = fyGlobalValueRepository.findByStatusAndGlobalKey(1, "rotaryTable");
        if(rotaryTable!=null){
            map.put("rotaryTable",rotaryTable.getGlobalValue());
        }else {
            map.put("rotaryTable","");
        }

        List<FyGroup> fyGroups = fyGroupRepository.ownerChatGroup(user.getUsername());
        if(fyGroups.size()>0){
            map.put("hasCreateGroup",1);
        }else {
            map.put("hasCreateGroup",0);
        }

        return map;
    }

    public Map getVersionStatus(FyVersionStatus fyVersionStatus) {
        FyVersionStatus status=fyVersionStatusRepository.findByVersionValueAndType(fyVersionStatus.getVersionValue(),fyVersionStatus.getType());
        Map map =new HashMap();
        if(status!=null){
            map.put("status",1);
        }else {
            map.put("status",0);
        }
        return map;
    }

    public void forgetpwd(String phone, String newpwd) {
        FyUser user = fyUserRepository.getIMUserByUserName(phone);
        if(user==null){
            throw new BusinessException("用户不存在！");
        }
        user.setPassword(MD5Tools.MD5(newpwd));
        fyUserRepository.saveAndFlush(user);
    }

    public void phoneLogin(String phone, String pwd) {
        FyUser user = fyUserRepository.getIMUserByUserName(phone);
        if(user==null){
            throw new BusinessException("用户未注册");
        }
        String md5 = MD5Tools.MD5(pwd);
        if(!user.getPassword().equals(md5)){
            throw new BusinessException("密码有误！");
        }
    }
}
