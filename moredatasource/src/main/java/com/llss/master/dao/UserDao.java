package com.llss.master.dao;

import com.llss.master.domain.User;
import org.apache.ibatis.annotations.*;
import org.mybatis.spring.annotation.MapperScan;

@MapperScan
public interface UserDao {

    @Select("select * from user where id = #{id}")
    public User getUser(@Param("id") int id);

    @Insert("insert into user(name) values(#{name})")
    public void addUser(User role);

    @Delete("delete from user where id = #{id}")
    public void delete(@Param("id") int id);

    @Update("update user set name = #{name} where id = #{id}")
    public void Update(@Param("id") int id, @Param("name") String name);
}
