/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oopgroup3.e_exam_server.ThreadWorkers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.oopgroup3.e_exam_server.DatabaseManager;
import com.oopgroup3.e_exam_server.Interfaces.GetAssignedStudentListQuery;
import com.oopgroup3.e_exam_server.Interfaces.GetStudentsListQuery;
import com.oopgroup3.e_exam_server.ResponseClasses.AbstractListResponse;
import com.oopgroup3.e_exam_server.ResponseClasses.AssignedExamData;
import com.oopgroup3.e_exam_server.ResponseClasses.Message;
import com.oopgroup3.e_exam_server.ResponseClasses.MessageManager;
import com.oopgroup3.e_exam_server.ResponseClasses.ResponseMessage;
import com.oopgroup3.e_exam_server.ResponseClasses.UserData;
import static com.oopgroup3.e_exam_server.Utils.printDebug.*;
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
public class GetStudentList implements Runnable {

    private final Message message;
    private final DatabaseManager databaseManager;
    private final MessageManager messageManager = MessageManager.getMessageManagerInstance();
    private ResponseMessage responseMessage;
    private GetStudentsListQuery getStudentsListQuery;
    private GetAssignedStudentListQuery getAssignedStudentListQuery;

    public GetStudentList(Message message, DatabaseManager databaseManager) 
    {
        this.message = message;
        this.databaseManager = databaseManager;
    }

    @Override
    public void run() {
        String sessionID = message.getSessionID();
        
        ArrayList<UserData> assignableUsers = new ArrayList<>();
        ArrayList<AssignedExamData> assignedExamUsers = new ArrayList<>();
        
        String[] parameters = message.getParameters();
        int teacherid = Integer.parseInt(parameters[0]);
        int examid = Integer.parseInt(parameters[1]);
                
        getStudentsListQuery = new GetStudentsListQuery(databaseManager);
        ResultSet studentsResultSet;
        try 
        {
            studentsResultSet = getStudentsListQuery.queryable();
            while(studentsResultSet.next())
            {
                assignableUsers.add(new UserData("", 1, studentsResultSet.getInt("USERID"), studentsResultSet.getString("USERNAME")));
            }            
        } catch (SQLException ex) 
        {
            ex.printStackTrace();
                   
        }
                
        getAssignedStudentListQuery = new GetAssignedStudentListQuery(databaseManager, teacherid, examid);
        ResultSet studentsAssignedResultSet;
        
        try
        {
            
            studentsAssignedResultSet = getAssignedStudentListQuery.queryable();
            while(studentsAssignedResultSet.next())
            {
                assignedExamUsers.add(new AssignedExamData(studentsAssignedResultSet.getString("EXAMNAME"), studentsAssignedResultSet.getString("USERNAME")));
            }
            
            
        } catch (SQLException ex) 
        {
            ex.printStackTrace();
                   
        }

        AbstractListResponse<UserData> abstractListResponse = new AbstractListResponse<>();
        abstractListResponse.constuctList(assignableUsers);
        String jsonInnerObject;
        String jsonResponse;
        
        try
        {
            Socket sendSocket = new Socket("127.0.0.1",64018);
            
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(sendSocket.getOutputStream()));

            Gson gson = new Gson();

            Type type = new TypeToken<AbstractListResponse<UserData>>(){}.getType();
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
     
        
        AbstractListResponse<AssignedExamData> abstractListResponse2 = new AbstractListResponse<>();
        abstractListResponse2.constuctList(assignedExamUsers);
        
        try
        {
            Socket sendSocket = new Socket("127.0.0.1",64018);
            
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(sendSocket.getOutputStream()));

            Gson gson = new Gson();

            Type type = new TypeToken<AbstractListResponse<AssignedExamData>>(){}.getType();
            jsonInnerObject = gson.toJson(abstractListResponse2, type);
            
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
        
    }
    
}
