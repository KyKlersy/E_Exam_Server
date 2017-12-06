package com.oopgroup3.e_exam_server.Interfaces;

import com.oopgroup3.e_exam_server.DatabaseManager;

/**
 * Messing around with abstract class that implements an interface this was an
 * early prototype and its implementation is GetExamQueryImplementation.
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
