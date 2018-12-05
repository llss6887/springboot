package com.llss.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "t_role")
@Getter
@Setter
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "name", unique = true)
    private String name;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(name = "t_user_mid_role",
    joinColumns = {@JoinColumn(name = "r_id", referencedColumnName = "id")},
    inverseJoinColumns = {@JoinColumn(name = "u_id", referencedColumnName = "id")})
    private Set<User> users = new HashSet<>();


    @ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER)
    private Set<Permission> pers = new HashSet<>();
}
