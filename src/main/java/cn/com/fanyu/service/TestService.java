package cn.com.fanyu.service;

import cn.com.fanyu.dao.FyGroupRepository;
import cn.com.fanyu.dao.FyScoreRepository;
import cn.com.fanyu.dao.FyUserRepository;
import cn.com.fanyu.dao.UserRepository;
import cn.com.fanyu.domain.FyGroup;
import cn.com.fanyu.domain.FyScore;
import cn.com.fanyu.domain.FyUser;
import cn.com.fanyu.domain.User;
import cn.com.fanyu.utils.BusinessException;
import cn.com.fanyu.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TestService {
    @Autowired
    private TestONeService testONeService;

    public void testone(){
        testONeService.testone();
    }
}
