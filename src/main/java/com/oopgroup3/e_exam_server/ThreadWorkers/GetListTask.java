/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oopgroup3.e_exam_server.ThreadWorkers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.oopgroup3.e_exam_server.DatabaseManager;
import com.oopgroup3.e_exam_server.Interfaces.ExamListQuery;
import com.oopgroup3.e_exam_server.Interfaces.StudentExamListQuery;
import com.oopgroup3.e_exam_server.Interfaces.TeacherExamListQuery;
import com.oopgroup3.e_exam_server.ResponseClasses.AbstractListResponse;
import com.oopgroup3.e_exam_server.ResponseClasses.Message;
import com.oopgroup3.e_exam_server.ResponseClasses.MessageManager;
import com.oopgroup3.e_exam_server.ResponseClasses.MessageTypes;
import com.oopgroup3.e_exam_server.ResponseClasses.ResponseMessage;
import com.oopgroup3.e_exam_server.ResponseClasses.ExamListData;
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


public class GetListTask implements Runnable
{
    
    private final Message message;
    private final DatabaseManager databaseManager;
    private final MessageManager messageManager = MessageManager.getMessageManagerInstance();
    private AbstractListResponse<ExamListData> abstractListResponse;
    private ResponseMessage responseMessage;
    
    public GetListTask(Message message, DatabaseManager databaseManager)
    {
        this.message = message;
        this.databaseManager = databaseManager;
    }

    
    
    @Override
    public void run()
    {
        ArrayList<ExamListData> examListData = new ArrayList<>();
        String[] parameters = message.getParameters();
        String[] resultColumnNames = new String[2];
        String jsonResponse;
        String jsonInnerObject;
        ResultSet resultSet;
        ExamListQuery examListQuery = null;
        
        if(Integer.parseInt(parameters[1]) == 1)
        {
            examListQuery = new StudentExamListQuery(Integer.parseInt(parameters[0]), Integer.parseInt(parameters[1]), databaseManager);
            resultColumnNames[0] = "ExamName";
            resultColumnNames[1] = "StudentExamID";
            
        }
        else if(Integer.parseInt(parameters[1]) == 2)
        {
            examListQuery = new TeacherExamListQuery(Integer.parseInt(parameters[0]), Integer.parseInt(parameters[1]), databaseManager);
            resultColumnNames[0] = "ExamName";
            resultColumnNames[1] = "ExamID";
            
        }
        else
        {
            Thread.interrupted();
        }        
        
        try 
        {
            
            resultSet = examListQuery.queryable();

            while(resultSet.next())
            {
                examListData.add(new ExamListData(resultSet.getString(resultColumnNames[0]), resultSet.getInt(resultColumnNames[1])));
            }

            resultSet.close();

            abstractListResponse = (AbstractListResponse <ExamListData>)messageManager.getMessage(MessageTypes.TeacherListResponse);
            abstractListResponse.constuctList(examListData);
            
        } 
        catch (SQLException sqle) 
        {
            sqle.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }        
        
        try
        {
            Socket sendSocket = new Socket("127.0.0.1",64018);
            
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(sendSocket.getOutputStream()));

            Gson gson = new Gson();

            
            Type type = new TypeToken<AbstractListResponse<ExamListData>>(){}.getType();
            jsonInnerObject = gson.toJson(abstractListResponse, type);
            System.out.println("Json list string: " + jsonInnerObject);
            if(abstractListResponse != null)
            {
                responseMessage = (ResponseMessage)messageManager.getMessage(MessageTypes.ResponseMessage);
                responseMessage.constructJsonResponse("Success","GetListResponseWorker", jsonInnerObject);
                //responseMessage = new ResponseMessage("Success","GetListResponseWorker", jsonInnerObject);
            }
            else
            {
                responseMessage = (ResponseMessage)messageManager.getMessage(MessageTypes.ResponseMessage);
                responseMessage.constructJsonResponse("Failed","GetListResponseWorker", jsonInnerObject);
                //responseMessage = new ResponseMessage("Failed","GetListResponseWorker", jsonInnerObject);
            }
            
            jsonResponse = gson.toJson(responseMessage, ResponseMessage.class);
            
            System.out.println("Json Payload: " + jsonResponse);
            bufferedWriter.write(jsonResponse);
            
            bufferedWriter.close();
            
            System.out.println("Response Sent");
                        
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }    
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
}
