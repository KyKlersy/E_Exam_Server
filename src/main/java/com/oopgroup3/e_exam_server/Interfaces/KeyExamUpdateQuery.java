/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oopgroup3.e_exam_server.Interfaces;

import com.oopgroup3.e_exam_server.DatabaseManager;
import com.oopgroup3.e_exam_server.ResponseClasses.ExamAnswer;
import com.oopgroup3.e_exam_server.SQLRequests;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Kyle
 */
public class KeyExamUpdateQuery 
{
    
    public void updateExamKeys(List<ExamAnswer> examAnswersList, DatabaseManager dbm, int ExamID) throws SQLException
    {
        if(examAnswersList.size() > 0)
        {
            try(Connection con = dbm.getConnection())
            {
                try(PreparedStatement updateKeys = con.prepareStatement(SQLRequests.keyExam.getSQLStatement()))
                {
                    
                    examAnswersList.forEach( answer ->{
                        try 
                        {
                            updateKeys.setInt(1, answer.getAnswerChoice());
                            updateKeys.setInt(2, ExamID);
                            updateKeys.setInt(3, answer.getExamQuestionID());
                            updateKeys.addBatch();
                            
                        } 
                        catch (SQLException ex)
                        {
                            ex.printStackTrace();
                        }
                    });
                    
                    updateKeys.executeBatch();
                    
                }
            } 
        }             
    }
}
