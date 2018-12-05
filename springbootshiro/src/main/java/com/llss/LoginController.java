package com.llss;

import com.llss.domain.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @GetMapping("/login")
    public String getUser(User user){
        UsernamePasswordToken upt = new UsernamePasswordToken();
        upt.setUsername(user.getName());
        upt.setPassword(user.getPassword().toCharArray());
        Subject subject = SecurityUtils.getSubject();
        String reValue = "OK";
        try{
            subject.login(upt);
        }catch (Exception e){
            e.printStackTrace();
            reValue = "fail";
        }
        String username = (String)subject.getPrincipal();
        return reValue;
    }
}
