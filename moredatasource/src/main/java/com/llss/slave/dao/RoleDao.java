package com.llss.slave.dao;

import com.llss.slave.domain.Role;
import org.apache.ibatis.annotations.*;
import org.mybatis.spring.annotation.MapperScan;

@MapperScan
public interface RoleDao {

    @Select("select * from role where id = #{id}")
    public Role getRole(@Param("id") int id);

    @Insert("insert into role(name) values(#{name})")
    public void addRole(Role role);

    @Delete("delete from role where id = #{id}")
    public void delete(@Param("id") int id);

    @Update("update role set name = #{name} where id = #{id}")
    public void Update(@Param("id") int id, @Param("name") String name);
}
