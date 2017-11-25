package com.study.model;

import javax.persistence.*;

@Table(name = "user_file")
public class UserFile {
    @Column(name = "userId")
    private Integer userId;

    @Column(name = "fileId")
    private Integer fileId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getFileId() {
        return fileId;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }
}