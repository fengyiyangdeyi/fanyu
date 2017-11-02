package cn.com.fanyu.service;

import cn.com.fanyu.dao.UserRepository;
import cn.com.fanyu.domain.User;
import cn.com.fanyu.utils.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class TestONeService {
    @Autowired
    private UserRepository userRepository;
    @Transactional
    public void testone(){
        User user = userRepository.findUser("555");
        user.setAge(8);
        userRepository.saveAndFlush(user);
        throw new BusinessException("fuck");
    }
}
