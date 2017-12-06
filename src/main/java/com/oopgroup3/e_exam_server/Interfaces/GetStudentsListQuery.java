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
public class GetStudentsListQuery implements Queryable{

    private DatabaseManager databaseManager;
    
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
