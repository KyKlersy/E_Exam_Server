/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oopgroup3.e_exam_server.ThreadWorkers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.oopgroup3.e_exam_server.DatabaseManager;
import com.oopgroup3.e_exam_server.ResponseClasses.AbstractListDecoder;
import com.oopgroup3.e_exam_server.ResponseClasses.AbstractListResponse;
import com.oopgroup3.e_exam_server.ResponseClasses.ExamQuestion;
import com.oopgroup3.e_exam_server.ResponseClasses.JsonResponseInterface;
import com.oopgroup3.e_exam_server.ResponseClasses.Message;
import com.oopgroup3.e_exam_server.ResponseClasses.MessageManager;
import com.oopgroup3.e_exam_server.ResponseClasses.MessageTypes;
import com.oopgroup3.e_exam_server.ResponseClasses.SendableInterface;
import com.oopgroup3.e_exam_server.SQLRequests;
import static com.oopgroup3.e_exam_server.Utils.printDebug.*;
import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import org.hsqldb.types.Types;

/**
 *
 * @author Kyle
 */
public class CreateExamTask implements Runnable{

    private Message message;
    private DatabaseManager databaseManager;
    private String eq = "";
    private MessageManager messageManager = MessageManager.getMessageManagerInstance();
    private Integer newExamKeyID = null;
    
    
    public CreateExamTask(Message message, DatabaseManager databaseManager) {
        this.message = message;
        this.databaseManager = databaseManager;
    }
    
    @Override
    public void run()
    {
        
        String sessionID = message.getSessionID();
        ArrayList<ExamQuestion> examQuestion = new ArrayList<>();
        
        String[] parameters = message.getParameters();
        
        print("CreateExamTask called");
        print("Params recieved: " + Arrays.toString(parameters));
        
        AbstractListDecoder ald = new AbstractListDecoder();
        
        TypeToken<AbstractListResponse<ExamQuestion>> typeToken = new TypeToken<AbstractListResponse<ExamQuestion>>(){};
        AbstractListResponse<ExamQuestion> alr = ald.parseAbstractListResponse(message.getJsonObject(), typeToken);
        
        print("Size of returned list: " + alr.getList().size());
        
        try 
        {
            Connection con = databaseManager.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement(SQLRequests.createExamKey.getSQLStatement(),Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setNull(1, Types.INTEGER);      
            preparedStatement.setInt(2, Integer.parseInt(parameters[0]));   
            preparedStatement.setString(3, parameters[1]);
            
            int rowcount = preparedStatement.executeUpdate();
            
            if(rowcount > 0)
            {
                ResultSet generatedID = preparedStatement.getGeneratedKeys();
                generatedID.next();
                newExamKeyID = generatedID.getInt(1);
                print("New Exam KeyID: " + newExamKeyID);

                preparedStatement.close();
                con.close();
                //databaseManager.closeConnection();
                
                Connection con3 = databaseManager.getConnection();
                PreparedStatement insertNewTeacherExam = con3.prepareStatement(SQLRequests.insertTeacherExam.getSQLStatement());
                insertNewTeacherExam.setNull(1, Types.INTEGER);
                insertNewTeacherExam.setInt(2, Integer.parseInt(parameters[0]));
                insertNewTeacherExam.setInt(3, newExamKeyID);

                insertNewTeacherExam.executeUpdate();
                con3.commit();
                con3.close();
                insertNewTeacherExam.close();
                
                Connection con4 = databaseManager.getConnection();
                PreparedStatement test = con4.prepareStatement("SELECT * FROM TEACHEREXAMS WHERE TEACHERID = 1");
                ResultSet testSet = test.executeQuery();
                
                while(testSet.next())
                {
                    print("Exam id: " + testSet.getInt("ExamID")+ " teach id: " + testSet.getInt("TeacherID"));
                }
                con4.close();
                testSet.close();
                
                
                if(alr.getList().size() > 0)
                {
                    examQuestion.addAll(alr.getList());
                    Connection con2 = databaseManager.getConnection();
                    PreparedStatement insertExamQuestions = con2.prepareStatement(SQLRequests.insertExamQuestions.getSQLStatement());
                    
                    
                    examQuestion.forEach(question ->{
                        try 
                        {
                            insertExamQuestions.setNull(1, Types.INTEGER);
                            insertExamQuestions.setInt(2, newExamKeyID);    
                            insertExamQuestions.setInt(3, question.getQuestionType());
                            insertExamQuestions.setInt(4, question.getQuestionNumber());
                            insertExamQuestions.setString(5, question.getQuestion());
                            insertExamQuestions.setString(6, question.getQuestion_1());
                            insertExamQuestions.setString(7, question.getQuestion_2());
                            insertExamQuestions.setString(8, question.getQuestion_3());
                            insertExamQuestions.setString(9, question.getQuestion_4());
                            insertExamQuestions.addBatch();
                        } catch (Exception e) 
                        {
                            
                        }
                    });

                    insertExamQuestions.executeBatch();
                    con2.commit();
                    insertExamQuestions.close();
                    con2.close();
                    //databaseManager.closeConnection();
                }
            }
            
        } catch (SQLException sqle) 
        {
            sqle.printStackTrace();
        }

        try
        {
            JsonResponseInterface jsonResponse = (JsonResponseInterface) messageManager.getMessage(MessageTypes.ResponseMessage);
            
            Gson gson = new Gson();

            
            jsonResponse.constructJsonResponse("Success", "CreateExamResponseWorker", "Success");
            print(jsonResponse.getStatus());
            SendableInterface sendable = (SendableInterface) jsonResponse;
            sendable.send(new Socket("127.0.0.1",64018));
            
                        
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }    
        
    }
    
}
