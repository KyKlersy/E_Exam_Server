package com.oopgroup3.e_exam_server.Interfaces;

import com.oopgroup3.e_exam_server.DatabaseManager;
import com.oopgroup3.e_exam_server.ResponseClasses.ExamQuestion;
import com.oopgroup3.e_exam_server.SQLRequests;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import org.hsqldb.types.Types;

/**
 * This query is for deleting / inserting and updating teacher exams.
 * 
 * @author Kyle
 */
public class EditExamUpdateQuery 
{
    public void delete(List<ExamQuestion> deleteFromDBList, DatabaseManager dbm, int ExamID)
    {
        if(deleteFromDBList.size() > 0)
        {
            try
            {
                Connection con = dbm.getConnection();
                PreparedStatement deleteStatement = con.prepareStatement(SQLRequests.deleteExamQuestions.getSQLStatement());

                deleteFromDBList.forEach(item -> {
                    try 
                    {
                        deleteStatement.setInt(1, ExamID);
                        deleteStatement.setInt(2, item.getExamQuestionID());
                        deleteStatement.addBatch();

                    } catch (Exception e) 
                    {
                        e.printStackTrace();
                    }            
                });

                deleteStatement.executeBatch();
                deleteStatement.close();
                con.close();

            }
            catch (SQLException sqle)
            {
                sqle.printStackTrace();
            }
        }
    }
    
    public void insert(List<ExamQuestion> insertNewExamQuestionsList, DatabaseManager dbm, int ExamID)
    {
        if(insertNewExamQuestionsList.size() > 0)
        {
            try 
            {
                Connection insertCon = dbm.getConnection();
                PreparedStatement insertStatement = insertCon.prepareStatement(SQLRequests.insertExamQuestions.getSQLStatement());
                
                insertNewExamQuestionsList.forEach(item -> {
                    try 
                    {
                        insertStatement.setInt(1, Types.INTEGER);
                        insertStatement.setInt(2, ExamID);
                        insertStatement.setInt(3, item.getQuestionType());
                        insertStatement.setInt(4, item.getQuestionNumber());
                        insertStatement.setString(5, item.getQuestion());
                        insertStatement.setString(6, item.getQuestion_1());
                        insertStatement.setString(7, item.getQuestion_2());
                        insertStatement.setString(8, item.getQuestion_3());
                        insertStatement.setString(9, item.getQuestion_4());                        
                        insertStatement.addBatch();
                        
                    } catch (Exception e) 
                    {
                        e.printStackTrace();
                    } 
                });
                
                insertStatement.executeBatch();
                insertStatement.close();
                insertCon.close();

            } catch (SQLException sqle) 
            {
                sqle.printStackTrace();
            }

        }  
    }
    
    public void update(List<ExamQuestion> updateExistingValuesList, DatabaseManager dbm, int ExamID)
    {
        
        if(updateExistingValuesList.size() > 0 )
        {
            try 
            {
                Connection updateCon = dbm.getConnection();
                PreparedStatement updateStatement = updateCon.prepareStatement(SQLRequests.insertExamQuestions.getSQLStatement());
                
                updateExistingValuesList.forEach(item -> {

                    try 
                    {
                        updateStatement.setString(1, item.getQuestion());
                        updateStatement.setString(2, item.getQuestion_1());
                        updateStatement.setString(3, item.getQuestion_2());
                        updateStatement.setString(4, item.getQuestion_3());
                        updateStatement.setString(5, item.getQuestion_4());       
                        updateStatement.setInt(6, ExamID);
                        updateStatement.setInt(7, item.getExamQuestionID());
                        updateStatement.addBatch();
                        
                    } catch (Exception e) 
                    {
                        e.printStackTrace();
                    } 
                });
                
                updateStatement.executeBatch();
                updateStatement.close();
                updateCon.close();

            } catch (SQLException sqle) 
            {
                sqle.printStackTrace();
            }            
        }
    }
    
}
