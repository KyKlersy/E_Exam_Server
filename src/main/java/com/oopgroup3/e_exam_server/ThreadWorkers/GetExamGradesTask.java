/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oopgroup3.e_exam_server.ThreadWorkers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.oopgroup3.e_exam_server.DatabaseManager;
import com.oopgroup3.e_exam_server.Interfaces.GetExamGradesQuery;
import com.oopgroup3.e_exam_server.ResponseClasses.AbstractListResponse;
import com.oopgroup3.e_exam_server.ResponseClasses.ExamGrade;
import com.oopgroup3.e_exam_server.ResponseClasses.ExamQuestion;
import com.oopgroup3.e_exam_server.ResponseClasses.Message;
import com.oopgroup3.e_exam_server.ResponseClasses.ResponseMessage;
import static com.oopgroup3.e_exam_server.Utils.printDebug.*;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kyle
 */
public class GetExamGradesTask implements Runnable{

    private Message message;
    private ResponseMessage responseMessage;
    private DatabaseManager databaseManager;
    private GetExamGradesQuery getExamGradesQuery;

    public GetExamGradesTask(Message message, DatabaseManager databaseManager) {
        this.message = message;
        this.databaseManager = databaseManager;
    }

    @Override
    public void run() 
    {
        String[] parameters = message.getParameters();
        
        int userId = Integer.parseInt(parameters[0]);
        float grade;
        String examName;
        ArrayList<ExamGrade> examGrades = new ArrayList<>();
        
        getExamGradesQuery = new GetExamGradesQuery(userId, databaseManager);
        
        try(ResultSet results = getExamGradesQuery.queryable())
        {
            while(results.next())
            {
                grade = results.getFloat("Grade");
                
                if(results.wasNull())
                {
                    grade = -1.0f;
                }
                
                examName = results.getString("ExamName");
                
                examGrades.add(new ExamGrade(grade, examName));

            }    
            
        } catch (SQLException ex) {
           
            ex.printStackTrace();
            Thread.interrupted();
        }
        
        String jsonInnerObject;
        String jsonResponse;


        AbstractListResponse<ExamGrade> abstractListResponse = new AbstractListResponse<>();
        abstractListResponse.constuctList(examGrades);

                            
        try
        {
            Socket sendSocket = new Socket("127.0.0.1",64018);
            
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(sendSocket.getOutputStream()));

            Gson gson = new Gson();

            Type type = new TypeToken<AbstractListResponse<ExamGrade>>(){}.getType();
            jsonInnerObject = gson.toJson(abstractListResponse, type);
            
            responseMessage = new ResponseMessage();
            responseMessage.constructJsonResponse("Success", "GetExamGrades", jsonInnerObject);
            
            jsonResponse = gson.toJson(responseMessage, ResponseMessage.class);
            
            bufferedWriter.write(jsonResponse);
            
            bufferedWriter.close();
            
            print("Response Sent");
                        
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }                                             
                    
    
    
        
        
        examGrades.forEach(p -> {print(p.toString());});
        
    }
    
}
