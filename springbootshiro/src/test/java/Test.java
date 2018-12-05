import com.ShiroApplication;
import com.llss.dao.UserDao;
import com.llss.domain.Permission;
import com.llss.domain.Role;
import com.llss.domain.User;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ShiroApplication.class)
public class Test {

    @Autowired
    private UserDao userDao;

    @org.junit.Test
    public void test(){
        User lisi = userDao.findByName("lisi");
        List<Role> roles = lisi.getRoles();
        roles.forEach(role -> {
            Set<Permission> pers = role.getPers();
            pers.forEach(permission -> {
                System.out.printf(permission.getName());
            });
        });
    }
}
