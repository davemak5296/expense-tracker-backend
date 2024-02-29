package com.codewithflow.exptracker.dto;

import java.time.LocalDateTime;

public class UserRespDTO {
    private Long id;
    private String email;
    private String alias;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

    public UserRespDTO() {
    }

    public UserRespDTO(Long id, String email, String alias, LocalDateTime createdDate, LocalDateTime lastModifiedDate) {
        this.id = id;
        this.email = email;
        this.alias = alias;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}