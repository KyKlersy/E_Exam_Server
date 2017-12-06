package com.oopgroup3.e_exam_server.Interfaces;

import com.oopgroup3.e_exam_server.DatabaseManager;
import com.oopgroup3.e_exam_server.SQLRequests;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import static com.oopgroup3.e_exam_server.Utils.printDebug.*;

/**
 * Implementation of the abstract parent GetExamQuery
 * This Sql class handles getting an exam from the database.
 * @author Kyle
 */
public class GetExamQueryImplementation extends GetExamQuery
{
    private final String queryString;       
    private PreparedStatement preparedStatement;

    public GetExamQueryImplementation(int userID, int examID, DatabaseManager databaseManager) {
        super(userID, examID, databaseManager);
        queryString = SQLRequests.getExam.getSQLStatement();
    }

    @Override
    public ResultSet queryable() 
        throws SQLException
    {
           
        try 
        {

        Connection con = super.getDatabaseManager().getConnection();
        preparedStatement = con.prepareStatement(queryString);
        preparedStatement.setInt(1,super.getExamID());
        print(super.getExamID());
        
        ResultSet resultSet = preparedStatement.executeQuery();
        
        preparedStatement.close();
        con.close();

        return resultSet;     
        }
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        
        return null;
    }
    
}
