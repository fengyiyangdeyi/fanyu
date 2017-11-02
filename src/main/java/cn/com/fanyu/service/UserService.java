package cn.com.fanyu.service;

import cn.com.fanyu.dao.*;
import cn.com.fanyu.domain.FyGlobalValue;
import cn.com.fanyu.domain.FyGroup;
import cn.com.fanyu.domain.FyScore;
import cn.com.fanyu.domain.FyUser;
import cn.com.fanyu.utils.BusinessException;
import cn.com.fanyu.utils.StringUtil;
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
}