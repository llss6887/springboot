package com.llss;

import com.llss.master.dao.UserDao;
import com.llss.master.domain.User;
import com.llss.slave.dao.RoleDao;
import com.llss.slave.domain.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class DataSourceServer {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    public static void main(String[] args) {
        SpringApplication.run(DataSourceServer.class, args);
    }

    @GetMapping("/master")
    public User getUser(){
        return userDao.getUser(2);
    }

    @GetMapping("/slave")
    public Role getRole(){
        return roleDao.getRole(2);
    }

    @GetMapping("/addMaster/{name}")
    public void addMaster(@PathVariable("name") String name){
        User u = new User();
        u.setName(name);
        userDao.addUser(u);
    }

    @GetMapping("/addSlave/{name}")
    public void addSlave(@PathVariable("name") String name){
        Role r = new Role();
        r.setName(name);
        roleDao.addRole(r);
    }
}
