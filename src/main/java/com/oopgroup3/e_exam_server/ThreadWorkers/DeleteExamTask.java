/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oopgroup3.e_exam_server.ThreadWorkers;

import com.google.gson.Gson;
import com.oopgroup3.e_exam_server.DatabaseManager;
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
import java.sql.SQLException;

/**
 *
 * @author Kyle
 */
public class DeleteExamTask implements Runnable {

    
    private Message message;
    private DatabaseManager databaseManager;
    private MessageManager messageManager = MessageManager.getMessageManagerInstance();
    
    public DeleteExamTask(Message message, DatabaseManager databaseManager)
    {
        this.message = message;
        this.databaseManager = databaseManager;
    }
    
    
    @Override
    public void run() 
    {
        String sessionID = message.getSessionID();
        String[] params = message.getParameters();
        
        print(params[0] + " " + params[1]);
        int rowcount = 0;
        try 
        {
            Connection con = databaseManager.getConnection();    
            PreparedStatement preparedStatement = con.prepareStatement(SQLRequests.deleteTeacherExam.getSQLStatement());
            preparedStatement.setInt(1, Integer.parseInt(params[0]));
            preparedStatement.setInt(2, Integer.parseInt(params[1]));
            
            rowcount = preparedStatement.executeUpdate();
            
            preparedStatement.close();
            con.close();

            
        }
        catch (SQLException sqle) 
        {
            sqle.printStackTrace();
        }
        
        try
        {
            JsonResponseInterface jsonResponse = (JsonResponseInterface) messageManager.getMessage(MessageTypes.ResponseMessage);
            
            if(rowcount > 0)
            {
                jsonResponse.constructJsonResponse("Success", "DeleteExamResponseWorker", "Success");
            }
            else
            {
                jsonResponse.constructJsonResponse("Failed", "DeleteExamResponseWorker", "Failed");
            }
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
