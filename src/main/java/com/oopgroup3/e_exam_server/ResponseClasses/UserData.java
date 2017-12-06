
package com.oopgroup3.e_exam_server.ResponseClasses;

/**
 *
 * @author tri.le
 */
public class UserData {
    
    private String sessionID;
    private int userType;
    private int userID;
    private String userFirstName;

    
    public UserData()
    {
        /* Provide default constructor for composition inside User class */
    }
    
    public UserData(String sessionID, int userType, int userID, String userFirstName) {
        this.sessionID = sessionID;
        this.userType = userType;
        this.userID = userID;
        this.userFirstName = userFirstName;
    }
    
    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

}
