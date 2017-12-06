package com.oopgroup3.e_exam_server.Interfaces;

import com.oopgroup3.e_exam_server.DatabaseManager;
import com.oopgroup3.e_exam_server.SQLRequests;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This sql class gets all accounts of type student for the teacher list of
 * students to be able to pick from.
 * @author Kyle
 */
public class GetStudentsListQuery implements Queryable{

    private final DatabaseManager databaseManager;
    
    public GetStudentsListQuery(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }
  
    @Override
    public ResultSet queryable() throws SQLException 
    {
        ResultSet results;
        try(Connection con = databaseManager.getConnection())
        {
            try(PreparedStatement getStudentsList = con.prepareStatement(SQLRequests.getStudentsList.getSQLStatement()))
            {
                results = getStudentsList.executeQuery();
                
            } 
        }
        
        return results;
        
    }
    
}
