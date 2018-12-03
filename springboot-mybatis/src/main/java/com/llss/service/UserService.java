package com.llss.service;

import com.llss.UserDao;
import com.llss.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    public void addUser(User user){
        userDao.addUser(user);
    }

    public User selectUser(Integer id){
        return userDao.selectUser(id);
    }

    public void updateUser(String name, Integer id){
        userDao.updateUser(name, id);
    }

    public void deleteIUser(Integer id){
        userDao.deleteIUser(id);
    }

}
