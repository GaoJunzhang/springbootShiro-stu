package com.study.model;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "file_role")
public class FileRole implements Serializable{
    private static final long serialVersionUID = -916411139749530670L;

    @Column(name = "fileid")
    private String fileid;

    @Column(name = "roleid")
    private String roleid;

    public String getFileid() {
        return fileid;
    }

    public void setFileid(String fileid) {
        this.fileid = fileid;
    }

    public String getRoleid() {
        return roleid;
    }

    public void setRoleid(String roleid) {
        this.roleid = roleid;
    }
}