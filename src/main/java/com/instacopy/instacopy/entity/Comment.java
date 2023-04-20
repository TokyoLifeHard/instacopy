package com.instacopy.instacopy.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    private Post post;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private Long UserId;

    @Column(columnDefinition = "text",nullable = false)
    private String mesasge;

    @Column(updatable = false)
    private LocalDateTime createDate;

    @PrePersist
    protected void onCreate(){
        this.createDate = LocalDateTime.now();
    }
}