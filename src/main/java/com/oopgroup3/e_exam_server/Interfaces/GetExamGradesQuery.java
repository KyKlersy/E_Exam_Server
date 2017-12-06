package com.oopgroup3.e_exam_server.Interfaces;

import com.oopgroup3.e_exam_server.DatabaseManager;
import com.oopgroup3.e_exam_server.SQLRequests;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This Sql class gets the exam grades for a student for all exams taken.
 * @author Kyle
 */
public class GetExamGradesQuery implements Queryable{

    private DatabaseManager databaseManager;
    private int userID;
    
    
    public GetExamGradesQuery(int userID, DatabaseManager databaseManager) 
    {
        this.userID = userID;
        this.databaseManager = databaseManager;
    }

    @Override
    public ResultSet queryable() throws SQLException
    {
        ResultSet resultSet;
        try(Connection con = databaseManager.getConnection())
        {
            try(PreparedStatement getExamsStatement = con.prepareStatement(SQLRequests.getExamGrades.getSQLStatement()))
            {
                getExamsStatement.setInt(1, userID);
                resultSet = getExamsStatement.executeQuery();
            }
        }
        
        return resultSet;
    }
}
