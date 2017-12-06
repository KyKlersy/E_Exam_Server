package com.oopgroup3.e_exam_server.Interfaces;

import com.oopgroup3.e_exam_server.DatabaseManager;

/**
 * This abstract class is the parent template for classes that need only to query
 * the database for a resultset and not do any kind of deletion or insertion.
 * 
 * Any child class that inherits from this parent must implement the queryable interface
 * which defines a query that returns a result set.
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
