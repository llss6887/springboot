package com.llss.dao;

import com.llss.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;


public interface UserDao extends JpaRepository<User, Integer> {

    @Override
    void delete(Integer id);

    @Override
    User findOne(Integer id);

    @Override
    User save(User u);



    @Query("select u from User u where u.real_name = ?1")
    public User queryByReal_name(String name);

    @Modifying
    @Query("update User u set u.real_name = ?1 where u.id = ?2")
    public User updateById(String name, int id);

    @Override
    Page<User> findAll(Pageable pageable);
}
