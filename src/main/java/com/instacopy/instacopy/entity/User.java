package com.instacopy.instacopy.entity;

import jakarta.persistence.PrePersist;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class User {

    private Long id;
    private String name;
    private String lastName;
    private String userName;
    private String email;
    private String biography;
    private String password;

    private Set<Role> roles = new HashSet<>();
    private List<Post> posts = new ArrayList<>();
    private LocalDateTime createDate;

    @PrePersist
    protected void onCreate(){
        this.createDate = LocalDateTime.now();
    }
}
