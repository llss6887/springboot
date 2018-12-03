package com.llss;

import com.llss.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class DaoTest {

    @Autowired
    private UserDao userDao;

    @Test
    public void addTest(){
        User u = new User();
        u.setRealName("liss");
        userDao.addUser(u);
    }
}
