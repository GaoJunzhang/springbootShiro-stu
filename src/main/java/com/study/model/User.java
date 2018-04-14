package com.study.model;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "user")
public class User implements Serializable{
    private static final long serialVersionUID = -8736616045315083846L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 账号
     */
    private String username;

    private String password;

    /**
     * 是否启用
     */
    private Integer enable;

    /**
     * 设备号
     */
    private String equipmentno;

    /**
     * 级别（0-学生，1-教师,2-普通用户）
     */
    private String level;

    /**
     * 学校名称
     */
    private String organization;

    /**
     * 真实姓名
     */
    private String realname;

    private String mobile;

    private String mac;

    /**
     * 资源类别(0-vive,1-一体机）
     */
    private String category;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取账号
     *
     * @return username - 账号
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置账号
     *
     * @param username 账号
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取是否启用
     *
     * @return enable - 是否启用
     */
    public Integer getEnable() {
        return enable;
    }

    /**
     * 设置是否启用
     *
     * @param enable 是否启用
     */
    public void setEnable(Integer enable) {
        this.enable = enable;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", enable=" + enable +
                '}';
    }

    public String getEquipmentno() {
        return equipmentno;
    }

    /**
     * 设置设备号
     *
     * @param equipmentno 设备号
     */
    public void setEquipmentno(String equipmentno) {
        this.equipmentno = equipmentno;
    }

    /**
     * 获取级别（0-学生，1-教师,2-普通用户）
     *
     * @return level - 级别（0-学生，1-教师,2-普通用户）
     */
    public String getLevel() {
        return level;
    }

    /**
     * 设置级别（0-学生，1-教师,2-普通用户）
     *
     * @param level 级别（0-学生，1-教师,2-普通用户）
     */
    public void setLevel(String level) {
        this.level = level;
    }

    /**
     * 获取学校名称
     *
     * @return organization - 学校名称
     */
    public String getOrganization() {
        return organization;
    }

    /**
     * 设置学校名称
     *
     * @param organization 学校名称
     */
    public void setOrganization(String organization) {
        this.organization = organization;
    }

    /**
     * 获取真实姓名
     *
     * @return realname - 真实姓名
     */
    public String getRealname() {
        return realname;
    }

    /**
     * 设置真实姓名
     *
     * @param realname 真实姓名
     */
    public void setRealname(String realname) {
        this.realname = realname;
    }

    /**
     * @return mobile
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * @param mobile
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * @return mac
     */
    public String getMac() {
        return mac;
    }

    /**
     * @param mac
     */
    public void setMac(String mac) {
        this.mac = mac;
    }

    /**
     * 获取资源类别(0-vive,1-一体机）
     *
     * @return category - 资源类别(0-vive,1-一体机）
     */
    public String getCategory() {
        return category;
    }

    /**
     * 设置资源类别(0-vive,1-一体机）
     *
     * @param category 资源类别(0-vive,1-一体机）
     */
    public void setCategory(String category) {
        this.category = category;
    }
}