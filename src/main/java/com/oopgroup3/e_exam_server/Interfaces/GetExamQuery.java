/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oopgroup3.e_exam_server.Interfaces;

import com.oopgroup3.e_exam_server.DatabaseManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Kyle
 */
public abstract class GetExamQuery implements Queryable
{
    private int userID;
    private int examID;
    private DatabaseManager databaseManager;

    public GetExamQuery(int userID, int examID, DatabaseManager databaseManager)
    {
        this.userID = userID;
        this.examID = examID;
        this.databaseManager = databaseManager;
    }

    public int getUserID() {
        return userID;
    }

    public int getExamID() {
        return examID;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
}
