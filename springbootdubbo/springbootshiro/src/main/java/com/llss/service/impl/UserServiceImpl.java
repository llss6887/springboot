package com.llss.service.impl;

import com.llss.dao.UserDao;
import com.llss.domain.User;
import com.llss.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    public User findUserByName(String name){
        return userDao.findByName(name);
    }
}
