package com.majiang.community.service;

import com.majiang.community.mapper.UserMapper;
import com.majiang.community.model.User;
import com.majiang.community.model.UserExample;
import org.apache.catalina.realm.UserDatabaseRealm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public void createOrUpdate(User user) {
        UserExample userExample =new UserExample();
        userExample.createCriteria().andAccountIdEqualTo(user.getAccountId());
        List<User> users = userMapper.selectByExample(userExample);
        if (users.size() == 0) {
//        插入
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
        } else {
//        更新
            User dbuser = users.get(0);
            User updateuser =new User();
            UserExample Example =new UserExample();
            Example.createCriteria().andIdEqualTo(dbuser.getId());
            updateuser.setAvatarUrl(user.getAvatarUrl());
            updateuser.setToken(user.getToken());
            updateuser.setGmtModified(System.currentTimeMillis());
            updateuser.setName(user.getName());
            userMapper.updateByExampleSelective(updateuser,Example);
        }
    }
}
