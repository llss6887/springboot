package com.llss;

import com.llss.domain.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserDao {

    //@Insert("insert into t_user (real_name) values(#{realName})")
    public void addUser(User user);

    @Select("select * from t_user where id = #{id}")
    public User selectUser(@Param("id") Integer id);

    @Update("update t_user set real_name = #{real_name} where id = #{id}")
    public void updateUser(@Param("real_name") String name, @Param("id") Integer id);

    @Delete("delete from t_user where id = #{id}")
    public void deleteIUser(@Param("id") Integer id);
}
