package com.oopgroup3.e_exam_server;

import com.google.gson.Gson;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
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
    private ResponseMessage responseMessage;
    
    
    public LoginTask(Message message, Set sessionid, DatabaseManager databaseManager) 
    {
        this.message = message;
        this.SESSIONID = sessionid;
        this.databaseManager = databaseManager;
    }
    
    @Override
    public void run() {
        
        String[] recievedMessageParameters = message.getParameters();
        String jsonResponse;
        String jsonInnerObject;
        UUID uuid = UUID.randomUUID();
        SESSIONID.add(uuid.toString());
        User user = null;        
        PreparedStatement preparedStatement;
        System.out.println("Size of received message params: " + recievedMessageParameters.length + recievedMessageParameters[0]);
        try {
            
            Connection connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement("SELECT USERID, USERNAME, PASSWORD,ACCOUNTTYPE FROM USERS WHERE USERNAME = ? AND PASSWORD = ?");
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
                user = new User(uuid.toString(), resultSet.getInt("ACCOUNTTYPE"), resultSet.getInt("USERID"), resultSet.getString("USERNAME"));
            }
            //resultSet.next();
             
            

            resultSet.close();
            preparedStatement.close();
            connection.close();
            databaseManager.closeConnection();
            
        } catch (Exception e) 
        {
            e.printStackTrace();
        }
       
        try
        {
            Socket sendSocket = new Socket("127.0.0.1",64018);
            
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(sendSocket.getOutputStream()));

            Gson gson = new Gson();
            jsonInnerObject = gson.toJson(user, User.class);
            
            if(user != null)
            {
                responseMessage = new ResponseMessage("Success","LoginResponseWorker", jsonInnerObject);
            }
            else
            {
                responseMessage = new ResponseMessage("Failed","LoginResponseWorker", jsonInnerObject);
            }
            
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
