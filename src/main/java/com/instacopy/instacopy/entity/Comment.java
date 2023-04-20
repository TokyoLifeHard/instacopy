package com.instacopy.instacopy.entity;

import jakarta.persistence.PrePersist;

import java.time.LocalDateTime;

public class Comment {
    private Long id;
    private Post post;
    private String username;
    private Long UserId;
    private String mesasge;

    private LocalDateTime createDate;

    @PrePersist
    protected void onCreate(){
        this.createDate = LocalDateTime.now();
    }
}
