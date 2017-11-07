/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oopgroup3.e_exam_server;

/**
 *
 * @author Kyle
 */
public class UserData {

    private int userID;
    private int userType;
    private String username;
    private String uuid;

    public UserData(int userid, int usertype, String username, String uuid) 
    {
        this.userID = userid;
        this.userType = usertype;
        this.username = username;
        this.uuid = uuid;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    } 
}
