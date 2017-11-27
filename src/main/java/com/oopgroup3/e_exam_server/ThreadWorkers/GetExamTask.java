/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oopgroup3.e_exam_server.ThreadWorkers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.oopgroup3.e_exam_server.DatabaseManager;
import com.oopgroup3.e_exam_server.Interfaces.GetExamQuery;
import com.oopgroup3.e_exam_server.Interfaces.GetExamQueryImplementation;
import com.oopgroup3.e_exam_server.ResponseClasses.AbstractListResponse;
import com.oopgroup3.e_exam_server.ResponseClasses.ExamQuestion;
import com.oopgroup3.e_exam_server.ResponseClasses.Message;
import com.oopgroup3.e_exam_server.ResponseClasses.ResponseMessage;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Kyle
 */


public class GetExamTask implements Runnable{

    private Message message;
    private ResponseMessage responseMessage;
    private DatabaseManager databaseManager;

    public GetExamTask(Message message, DatabaseManager databaseManager) {
        this.message = message;
        this.databaseManager = databaseManager;
    }
        
    @Override
    public void run()
    {
        String sessionID = message.getSessionID();
        ArrayList<ExamQuestion> examQuestion = new ArrayList<>();

        String[] parameters = message.getParameters();

        ResultSet resultSet;
        GetExamQuery getExamQuery = new GetExamQueryImplementation(Integer.parseInt(parameters[0]), Integer.parseInt(parameters[1]), databaseManager);

        try
        {
            
            resultSet = getExamQuery.queryable();
            if(!resultSet.isBeforeFirst())
            {
                
            }
            else
            {
                while(resultSet.next())
                {
                    examQuestion.add(
                            new ExamQuestion(
                                   resultSet.getInt("QuestionNumber"), 
                                   resultSet.getInt("QuestionType"),
                                   resultSet.getString("ExamQuestion"), 
                                   resultSet.getString("QuestionOne"),
                                   resultSet.getString("QuestionTwo"),
                                   resultSet.getString("QuestionThree"), 
                                   resultSet.getString("QuestionFour")
                            ));

                }

                examQuestion.forEach(q -> {
                    System.out.println("Q#: " + q.getQuestionNumber() + " Question: " + q.getQuestion());
                });
            }
            
        } 
        catch (SQLException sqle) 
        {
            sqle.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
            
            
        String jsonInnerObject;
        String jsonResponse;

        //MessageWithJsonObject mwjoEdit = new MessageWithJsonObject(sessionID, "ExamReturnData", examQuestion);
        AbstractListResponse<ExamQuestion> abstractListResponse = new AbstractListResponse<>();
        abstractListResponse.constuctList(examQuestion);

                            
        try
        {
            Socket sendSocket = new Socket("127.0.0.1",64018);
            
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(sendSocket.getOutputStream()));

            Gson gson = new Gson();
            //jsonInnerObject = gson.toJson(mwjoEdit, MessageWithJsonObject.class);
            Type type = new TypeToken<AbstractListResponse<ExamQuestion>>(){}.getType();
            jsonInnerObject = gson.toJson(abstractListResponse, type);
            
            responseMessage = new ResponseMessage();
            responseMessage.constructJsonResponse("Success", "ExamReturnData", jsonInnerObject);
            
            jsonResponse = gson.toJson(responseMessage, ResponseMessage.class);
            
            bufferedWriter.write(jsonResponse);
            
            bufferedWriter.close();
            
            System.out.println("Response Sent");
                        
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }                                             
                    
    }
    
}
