package com.oopgroup3.e_exam_server.Interfaces;

import com.oopgroup3.e_exam_server.DatabaseManager;
import com.oopgroup3.e_exam_server.SQLRequests;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * This sql class is used to get the list of exams that students have
 * avaliable to them.
 * @author Kyle
 */
public class StudentExamListQuery extends ExamListQuery 
{
    private final String queryString;       
    private PreparedStatement preparedStatement;
    
    public StudentExamListQuery(int userID, int userType, DatabaseManager databaseManager)
    {
        super(userID, userType, databaseManager);
        this.queryString = SQLRequests.getStudentExamList.getSQLStatement();
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
