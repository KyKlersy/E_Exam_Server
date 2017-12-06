/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oopgroup3.e_exam_server.ThreadWorkers;


import com.google.gson.reflect.TypeToken;
import com.oopgroup3.e_exam_server.DatabaseManager;
import com.oopgroup3.e_exam_server.Interfaces.KeyExamUpdateQuery;
import com.oopgroup3.e_exam_server.ResponseClasses.AbstractListDecoder;
import com.oopgroup3.e_exam_server.ResponseClasses.AbstractListResponse;
import com.oopgroup3.e_exam_server.ResponseClasses.ExamAnswer;
import com.oopgroup3.e_exam_server.ResponseClasses.JsonResponseInterface;
import com.oopgroup3.e_exam_server.ResponseClasses.Message;
import com.oopgroup3.e_exam_server.ResponseClasses.MessageManager;
import com.oopgroup3.e_exam_server.ResponseClasses.MessageTypes;
import com.oopgroup3.e_exam_server.ResponseClasses.SendableInterface;
import static com.oopgroup3.e_exam_server.Utils.printDebug.*;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Arrays;

/**
 *
 * @author Kyle
 */
public class KeyExamTask implements Runnable{

    private final Message message;
    private final DatabaseManager databaseManager;
    private final MessageManager messageManager = MessageManager.getMessageManagerInstance();
    private KeyExamUpdateQuery keyExamUpdateQuery;

    public KeyExamTask(Message message, DatabaseManager databaseManager) 
    {
        this.message = message;
        this.databaseManager = databaseManager;
        keyExamUpdateQuery = new KeyExamUpdateQuery();
        
    }
    
    
    
    @Override
    public void run() {
        String sessionID = message.getSessionID();
        
        String[] parameters = message.getParameters();
        
        print("Params recieved: " + Arrays.toString(parameters));
        print("param 1: " + parameters[1]);
        
        AbstractListDecoder ald = new AbstractListDecoder();
        
        TypeToken<AbstractListResponse<ExamAnswer>> typeToken = new TypeToken<AbstractListResponse<ExamAnswer>>(){};
        AbstractListResponse<ExamAnswer> alr = ald.parseAbstractListResponse(message.getJsonObject(), typeToken);
        
        print("List items recived from client to key exam with: ");
        alr.getList().forEach(item ->{
            print(item.toString());

        });
        
        try 
        {
            keyExamUpdateQuery.updateExamKeys(alr.getList(), databaseManager, Integer.parseInt(parameters[1]));
        } 
        catch (SQLException sqle)
        {
            sqle.printStackTrace();
        }
        
        try
        {
            JsonResponseInterface jsonResponse = (JsonResponseInterface) messageManager.getMessage(MessageTypes.ResponseMessage);
            
            jsonResponse.constructJsonResponse("Success", "KeyExamTask", "Success");
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
