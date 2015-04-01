package com.dexcoder.assistant.persistence;

import java.util.Date;

import com.dexcoder.assistant.pager.Pageable;

/**
 * Created by liyd on 3/2/15.
 */
public class User extends Pageable {

    private Long    userId;

    private String  userName;

    private Integer userAge;

    private Date    gmtCreate;

    private Date    gmtModify;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getUserAge() {
        return userAge;
    }

    public void setUserAge(Integer userAge) {
        this.userAge = userAge;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModify() {
        return gmtModify;
    }

    public void setGmtModify(Date gmtModify) {
        this.gmtModify = gmtModify;
    }
}
