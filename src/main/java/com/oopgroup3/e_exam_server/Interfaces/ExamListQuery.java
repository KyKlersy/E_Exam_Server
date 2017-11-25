/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oopgroup3.e_exam_server.Interfaces;

import com.oopgroup3.e_exam_server.DatabaseManager;

/**
 *
 * @author Kyle
 */
public abstract class ExamListQuery implements Queryable
{
    private final int userID;
    private final int userType;
    private final DatabaseManager databaseManager;

    public ExamListQuery(int userID, int userType, DatabaseManager databaseManager) 
    {
        this.userID = userID;
        this.userType = userType;
        this.databaseManager = databaseManager;
    }    

    public int getUserID() {
        return userID;
    }

    public int getUserType() {
        return userType;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
    
    
}
