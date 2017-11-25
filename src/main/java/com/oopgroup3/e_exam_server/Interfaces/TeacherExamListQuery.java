/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oopgroup3.e_exam_server.Interfaces;

import com.oopgroup3.e_exam_server.DatabaseManager;
import com.oopgroup3.e_exam_server.SQLRequests;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Kyle
 */
public class TeacherExamListQuery extends ExamListQuery
{
    private final String queryString;
    private PreparedStatement preparedStatement;
    
    public TeacherExamListQuery(int userID, int userType, DatabaseManager databaseManager)
    {
        super(userID, userType, databaseManager);
        this.queryString = SQLRequests.getTeacherExamList.getSQLStatement();
    }

    @Override
    public ResultSet queryable() 
        throws SQLException
    {
        Connection con = super.getDatabaseManager().getConnection();
        preparedStatement = con.prepareStatement(queryString);
        preparedStatement.setInt(1,super.getUserID());
        
        ResultSet resultSet = preparedStatement.executeQuery();
        
        preparedStatement.close();
        con.close();
        super.getDatabaseManager().closeConnection();
        
        if(!resultSet.isBeforeFirst())
        {
            /* Case where nothing is returned */
            return null;

        }
        else
        {
            return resultSet;
        }
        
    }
    
}
