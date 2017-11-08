package cn.com.fanyu.service;

import cn.com.fanyu.dao.FyGlobalValueRepository;
import cn.com.fanyu.dao.FyGroupRepository;
import cn.com.fanyu.dao.FyScoreRepository;
import cn.com.fanyu.dao.FyUserRepository;
import cn.com.fanyu.domain.FyGlobalValue;
import cn.com.fanyu.domain.FyGroup;
import cn.com.fanyu.domain.FyScore;
import cn.com.fanyu.domain.FyUser;
import cn.com.fanyu.huanxin.api.impl.EasemobChatGroup;
import cn.com.fanyu.huanxin.api.impl.EasemobIMUsers;
import cn.com.fanyu.huanxin.api.impl.EasemobSendMessage;
import cn.com.fanyu.utils.BusinessException;
import cn.com.fanyu.utils.IdGen;
import cn.com.fanyu.utils.MD5Tools;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.swagger.client.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class RestfulService {
    @Autowired
    private FyScoreRepository fyScoreRepository;
    @Autowired
    private FyUserRepository fyUserRepository;
    @Autowired
    private FyGroupRepository fyGroupRepository;
    private EasemobChatGroup ecg = new EasemobChatGroup();
    private EasemobIMUsers eiu = new EasemobIMUsers();
    private EasemobSendMessage easemobSendMessage = new EasemobSendMessage();
    @Autowired
    private FyGlobalValueRepository fyGlobalValueRepository;


    public Map register(User user) throws BusinessException {
        Map map = new HashMap<>();
        String nickname=user.getUsername();
        user.setUsername(user.getUsername().toLowerCase());
        FyUser imUserByUserName = fyUserRepository.getIMUserByUserName(user.getUsername());
        if (imUserByUserName == null) {
            createFyUser(user,nickname);
        }
        try {
            RegisterUsers list = new RegisterUsers();
            list.add(user);
            eiu.createNewIMUserSingle(list);
        } catch (Exception e) {
            e.printStackTrace();
            if (imUserByUserName != null) {
                throw new BusinessException("用户已注册");
            }
        }
        return map;
    }

    private void createFyUser(User user,String nickname) {
        FyUser fu = new FyUser();
        fu.setAddTime(new Date());
        fu.setPassword(MD5Tools.MD5(user.getPassword()));
        fu.setUsername(user.getUsername());
        fu.setNickname(nickname);
        fyUserRepository.saveAndFlush(fu);
    }

    @Transactional
    public Map createChatGroup(Group group) throws Exception {
        List<FyGroup> fyGroups = fyGroupRepository.ownerChatGroup(group.getOwner());
        if(fyGroups.size()>0){
            throw new Exception("不能重复建群组");
        }
        FyGroup fyGroup = createFyGroup(group);
        group.setPublic(true);
        Object chatGroup = ecg.createChatGroup(group);
        JSONObject jsonObject = JSON.parseObject(chatGroup.toString());

        JSONObject data = jsonObject.getJSONObject("data");
        String groupid = data.getString("groupid");
        fyGroup.setGroupid(groupid);
        fyGroupRepository.saveAndFlush(fyGroup);
        Map map = new HashMap<>();
        map.put("groupid", groupid);
        map.put("code", fyGroup.getCode());
        try {
            FyScore f = new FyScore();
            f.setGroupid(groupid);
            f.setUsername(group.getOwner());
            addUserToGroup(f);
        } catch (Exception e) {
        }
        return map;
    }

    private FyGroup createFyGroup(Group group) {
        FyGroup fg = new FyGroup();
        fg.set_public(group.getPublic());
        fg.setAddTime(new Date());
        fg.setApproval(group.getApproval());
        fg.setRemark(group.getDesc());
        fg.setGroupname(group.getGroupname());
        fg.setMaxusers(group.getMaxusers());
        fg.setOwner(group.getOwner());
        String code;
        int i = 0;
        FyGroup g;
        do {
            i++;
            code = getCode();
            g = fyGroupRepository.findByCode(code);
        } while (i < 100 && g != null);
        fg.setCode(code);
        fyGroupRepository.saveAndFlush(fg);
        return fg;
    }

    private String getCode() {
        String str = "0123456789";
        StringBuilder sb = new StringBuilder(8);
        for (int i = 0; i < 8; i++) {
            char ch = str.charAt(new Random().nextInt(str.length()));
            sb.append(ch);
        }
        return sb.toString();
    }

    public Boolean ownerChatGroup(String username) {
        List<FyGroup> fyGroups = fyGroupRepository.ownerChatGroup(username);
        if (fyGroups.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public Map getIMUserByUserName(FyUser user) {
        Map map = new HashMap();
        //Object imUserByUserName = eiu.getIMUserByUserName(username);
        //JSONObject jsonObject = JSON.parseObject(imUserByUserName.toString());
        //JSONObject data = jsonObject.getJSONObject("data");
        FyUser f = fyUserRepository.getIMUserByUserName(user.getUsername());
        if (f != null) {
            map.put("imgUrl", f.getImgUrl());
            map.put("diamondNum", f.getDiamondNum());
            map.put("nickname", f.getNickname());
            map.put("username",f.getUsername());
            map.put("id",f.getId());
            List<FyGroup> fyGroups = fyGroupRepository.ownerChatGroup(user.getUsername());
            if(fyGroups.size()>0){
                map.put("hasCreateGroup",1);
            }else {
                map.put("hasCreateGroup",0);
            }
        }
        return map;
    }

    @Transactional
    public void addUserToGroup(FyScore fyScore) {
        FyScore byParam = fyScoreRepository.findByGroupidAndUsernameAndStatus(fyScore.getGroupid(), fyScore.getUsername(),1);
        if(byParam!=null){
            throw new BusinessException("用户已在群里");
        }
        fyScore.setAddTime(new Date());
        fyScoreRepository.saveAndFlush(fyScore);
        FyGroup group = fyGroupRepository.getChatGroup(fyScore.getGroupid());
        //调环信
        addSingUserToGroup(fyScore.getGroupid(),fyScore.getUsername());
    }

    public String sendMessagetoGroup(Msg msg) {
        Object o = easemobSendMessage.sendMessage(msg);
        return o.toString();
    }

    public String sendMessagetoGroup(String mes, String groupid) {
        Msg msg = new Msg();
        msg.targetType("chatgroups");
        UserName userName = new UserName();
        userName.add(groupid);
        msg.setTarget(userName);
        msg.setFrom("admin");
        Map msgobj = new HashMap();
        msgobj.put("type", "txt");
        msgobj.put("msg", mes);
        msg.setMsg(msgobj);
        String s = sendMessagetoGroup(msg);
        return s;
    }

    public String sendMessagetoUser(String mes, String username,String sendUser) {
        Msg msg = new Msg();
        msg.targetType("users");
        UserName userName = new UserName();
        userName.add(username);
        msg.setTarget(userName);
        msg.setFrom(sendUser);
        Map msgobj = new HashMap();
        msgobj.put("type", "txt");
        msgobj.put("msg", mes);
        msg.setMsg(msgobj);
        String s = sendMessagetoGroup(msg);
        return s;
    }

    @Transactional
    public Map wxLogin(FyUser user) throws BusinessException {
        Map map = new HashMap<>();
        user.setUsername(user.getUsername().toLowerCase());
        FyUser dbuser = fyUserRepository.findByWxid(user.getUsername());
        if (dbuser == null) {
//            String password = user.getPassword();
//            user.setPassword(MD5Tools.MD5(password));
//            user.setWxid(user.getUsername());
//            dbuser=fyUserRepository.saveAndFlush(user);
//            RegisterUsers list = new RegisterUsers();
//            User u=new User();
//            u.setUsername(user.getUsername());
//            u.setPassword(password);
//            list.add(u);
//            eiu.createNewIMUserSingle(list);
            map.put("has","0");
            return map;

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
        map.put("has","1");

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


    public void addSingUserToGroup(String groupid,String username) {
        ecg.addSingleUserToChatGroup(groupid,username);
    }
    public void removeSingleUserFromChatGroup(String groupid,String username) {
        ecg.removeSingleUserFromChatGroup(groupid,username);
    }

    public void deleteChatGroup(String groupid) {
        ecg.deleteChatGroup(groupid);
    }
    public void modifyChatGroup(String groupid, String groupname, String remark) {
        ModifyGroup m=new ModifyGroup();
        m.setGroupname(groupname);
        m.setDescription(remark);
        ecg.modifyChatGroup(groupid,m);
    }
}
