package com.oopgroup3.e_exam_server.ThreadWorkers;

import com.google.gson.Gson;
import com.oopgroup3.e_exam_server.DatabaseManager;
import com.oopgroup3.e_exam_server.ResponseClasses.JsonResponseInterface;
import com.oopgroup3.e_exam_server.ResponseClasses.Message;
import com.oopgroup3.e_exam_server.ResponseClasses.MessageManager;
import com.oopgroup3.e_exam_server.ResponseClasses.MessageTypes;
import com.oopgroup3.e_exam_server.ResponseClasses.SendableInterface;
import com.oopgroup3.e_exam_server.ResponseClasses.UserData;
import com.oopgroup3.e_exam_server.SQLRequests;
import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Set;
import java.util.UUID;

/**
 *
 * @author Kyle
 */
public class LoginTask implements Runnable {

    private Message message;
    private Set SESSIONID;
    private DatabaseManager databaseManager;
    private MessageManager messageManager = MessageManager.getMessageManagerInstance();
    
    public LoginTask(Message message, Set sessionid, DatabaseManager databaseManager) 
    {
        this.message = message;
        this.SESSIONID = sessionid;
        this.databaseManager = databaseManager;
    }
    
    @Override
    public void run() {
        
        String[] recievedMessageParameters = message.getParameters();
        String jsonInnerObject;
        UUID uuid = UUID.randomUUID();
        SESSIONID.add(uuid.toString());
        UserData user = null;        
        PreparedStatement preparedStatement;
        System.out.println("Size of received message params: " + recievedMessageParameters.length +" "+ recievedMessageParameters[0]);
        try {
            
            Connection connection = databaseManager.getConnection();
            SQLRequests requestType = SQLRequests.authenticateUser;
            preparedStatement = connection.prepareStatement(requestType.getSQLStatement());
            preparedStatement.setString(1, recievedMessageParameters[0]);
            preparedStatement.setString(2, recievedMessageParameters[1]);
            
            ResultSet resultSet = preparedStatement.executeQuery();
            if(!resultSet.isBeforeFirst())
            {
                user = null;
                
            }
            else
            {
                resultSet.next();
                user = new UserData(uuid.toString(), resultSet.getInt("ACCOUNTTYPE"), resultSet.getInt("USERID"), resultSet.getString("USERNAME"));
            }
            //resultSet.next();
             
            

            resultSet.close();
            preparedStatement.close();
            connection.close();
            //databaseManager.closeConnection();
            
        } catch (Exception e) 
        {
            e.printStackTrace();
        }
       
        try
        {
            JsonResponseInterface jsonResponse = (JsonResponseInterface) messageManager.getMessage(MessageTypes.ResponseMessage);
            
            Gson gson = new Gson();
            jsonInnerObject = gson.toJson(user, UserData.class);
            
            if(user != null)
            {
                jsonResponse.constructJsonResponse("Success", "LoginResponseWorker", jsonInnerObject);
            }
            else
            {
                jsonResponse.constructJsonResponse("Failed", "LoginResponseWorker", jsonInnerObject);
            }
            
            SendableInterface sendable = (SendableInterface) jsonResponse;
            sendable.send(new Socket("127.0.0.1",64018));
            
                        
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }             
    }
    
}
