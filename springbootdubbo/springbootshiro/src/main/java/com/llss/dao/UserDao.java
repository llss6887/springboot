package com.llss.dao;

import com.llss.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface UserDao extends JpaRepository<User, Integer> {

    User findByName(String name);
}
