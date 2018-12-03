package com.llss.controller;


import com.llss.dao.UserDao;
import com.llss.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class JpaController {

    @Autowired
    private UserDao userDao;

    @GetMapping("/save/{name}")
    public void save(@PathVariable String name){
        User u = new User();
        u.setReal_name(name);
        userDao.save(u);
    }
    @GetMapping("/find/{id}")
    public User findonne(@PathVariable int id){
        return userDao.findOne(id);
    }

    @GetMapping("/findAll")
    public Page<User> findAll(@RequestParam() int page, int size){
        //排序方式  和字段  可以多個字段排序
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable p = (Pageable) new PageRequest(page, size, sort);
        Page<User> all = userDao.findAll(p);
        return all;
    }
}
