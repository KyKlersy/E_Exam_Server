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
public class GetAssignedStudentListQuery implements Queryable{

    private DatabaseManager databaseManager;
    private int teacherid;
    private int examid;

    public GetAssignedStudentListQuery(DatabaseManager databaseManager, int teacherid, int examid) {
        this.databaseManager = databaseManager;
        this.teacherid = teacherid;
        this.examid = examid;
    }
    
    @Override
    public ResultSet queryable() throws SQLException 
    {
        ResultSet resultSet;
        
        try(Connection con = databaseManager.getConnection())
        {
            try(PreparedStatement getAssignedStudentsList = con.prepareStatement(SQLRequests.getAssignedStudentsList.getSQLStatement()))
            {
                getAssignedStudentsList.setInt(1, teacherid);
                getAssignedStudentsList.setInt(2, examid);
                resultSet = getAssignedStudentsList.executeQuery();
                
            }
        }
        
        return resultSet;
    }
    
}
