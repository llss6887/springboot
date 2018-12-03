package com.llss.controller;


import com.llss.domain.User;
import com.llss.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/spring_mybatis")
public class SpringBootMybatis {

    @Autowired
    private UserService userService;


    @GetMapping("/addUser/{name}")
    public void addUser(@PathVariable("name") String name){
        User user = new User();
        user.setRealName(name);
        userService.addUser(user);
    }

    @GetMapping("/findUser/{id}")
    public User findUser(@PathVariable("id") String id){
        return userService.selectUser(Integer.parseInt(id));
    }

    @GetMapping("/updateUser/{id}/{name}")
    public void updateUser(@PathVariable("id") String id, @PathVariable("name") String name){
        userService.updateUser(name, Integer.parseInt(id));
    }

    @GetMapping("/deleteUser/{id}")
    public void deleteUser(@PathVariable("id") String id){
        userService.deleteIUser(Integer.parseInt(id));
    }

}
