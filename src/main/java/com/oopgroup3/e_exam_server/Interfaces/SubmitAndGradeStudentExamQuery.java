package com.oopgroup3.e_exam_server.Interfaces;

import com.oopgroup3.e_exam_server.DatabaseManager;
import com.oopgroup3.e_exam_server.ResponseClasses.ExamAnswer;
import com.oopgroup3.e_exam_server.SQLRequests;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.hsqldb.types.Types;

/**
 * This sql class handles submission of an exam by the student,
 * as well as grading the exam based on the exam key and storing the
 * students exam grade.
 * @author Kyle
 */
public class SubmitAndGradeStudentExamQuery 
{
    private int numberOfQuestions = 0;
    private int numberOfCorrectAnswers = 0;
    
    public void submitStudentExamAnswers(List<ExamAnswer> studentExamAnswersList, DatabaseManager dbm, int UserID, int ExamID) throws SQLException
    {
        
        if(studentExamAnswersList.size() > 0)
        {
            try(Connection con = dbm.getConnection())
            {
                try(PreparedStatement insertStudentSubmission = con.prepareStatement(SQLRequests.insertStudentSubmission.getSQLStatement()))
                {
            
                    studentExamAnswersList.forEach(answer ->{
                        try 
                        {
                            insertStudentSubmission.setNull(1, Types.INTEGER);
                            insertStudentSubmission.setInt(2, ExamID); System.out.println("ExamID: " + ExamID);
                            insertStudentSubmission.setInt(3, UserID); System.out.println("UserId: " + UserID);
                            insertStudentSubmission.setInt(4, answer.getExamQuestionID()); System.out.println("ExamQuestionID: " + answer.getExamQuestionID());
                            insertStudentSubmission.setInt(5, answer.getAnswerChoice()); System.out.println("AnswerChoice: " + answer.getAnswerChoice());
                            insertStudentSubmission.addBatch();

                        }
                        catch (SQLException sqle) 
                        {
                            sqle.printStackTrace();
                        } 
                    });
                    
                    insertStudentSubmission.executeBatch();
                }
            }

            try(Connection con2 = dbm.getConnection())
            {
                try(PreparedStatement updateExamTaken = con2.prepareStatement(SQLRequests.updateExamTakenStatus.getSQLStatement()))
                {
                    updateExamTaken.setInt(1, UserID);
                    updateExamTaken.setInt(2, ExamID);
                    
                    updateExamTaken.execute();
                }
            }
            
        }
    }
    
    public float gradeStudentExam(List<ExamAnswer> studentExamAnswersList, DatabaseManager dbm, int UserID, int ExamID) throws SQLException
    {
        ResultSet examKeySet;
        float grade;
        
        if(studentExamAnswersList.size() > 0)
        {
            try(Connection con = dbm.getConnection())
            {
                try(PreparedStatement getExamKey = con.prepareStatement(SQLRequests.getAnswerKey.getSQLStatement()))
                {
            
                    getExamKey.setInt(1, ExamID);
                    examKeySet = getExamKey.executeQuery();
                    
                }
            }

            studentExamAnswersList.forEach(answer ->{
                try 
                {
                    examKeySet.next();
                    numberOfQuestions += 1;
                    if(answer.getAnswerChoice() == examKeySet.getInt("ExamCorrectAnswer") && answer.getExamQuestionID() == examKeySet.getInt("ExamQuestionID"))
                    {
                        numberOfCorrectAnswers += 1;
                    }
                    
                } catch (Exception e) 
                {
                    e.printStackTrace();
                }
            });
            
            grade = (numberOfCorrectAnswers / numberOfQuestions);
            
            try(Connection con = dbm.getConnection())
            {
                try(PreparedStatement saveStudentData = con.prepareStatement(SQLRequests.updateStudentExamData.getSQLStatement()))
                {
                    saveStudentData.setFloat(1, grade);
                    saveStudentData.setBoolean(2, true);
                    saveStudentData.setInt(3, UserID);
                    saveStudentData.setInt(4, ExamID);
                    saveStudentData.execute();
                }
            }
            
            return grade;     
        }
        return 0.0f;
    }
}
