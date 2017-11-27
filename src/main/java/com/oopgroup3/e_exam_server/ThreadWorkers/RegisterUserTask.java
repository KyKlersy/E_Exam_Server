package com.oopgroup3.e_exam_server.ThreadWorkers;

import com.google.gson.Gson;
import com.oopgroup3.e_exam_server.DatabaseManager;
import com.oopgroup3.e_exam_server.ResponseClasses.Message;
import com.oopgroup3.e_exam_server.ResponseClasses.ResponseMessage;
import com.oopgroup3.e_exam_server.ResponseClasses.User;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Set;
import java.util.UUID;
import java.sql.Types;
/**
 *
 * @author Kyle
 */


public class RegisterUserTask implements Runnable {

    private Message message;
    private Set SESSIONID;
    private DatabaseManager databaseManager;
    private ResponseMessage responseMessage;
    
    
    public RegisterUserTask(Message message, Set sessionid, DatabaseManager databaseManager) 
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
            Integer userIDKey = null;
            Connection connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement("INSERT INTO Users(UserID,Username,Password,AccountType) VALUES(?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setNull(1, Types.NULL);
            preparedStatement.setString(2, recievedMessageParameters[0]);
            preparedStatement.setString(3, recievedMessageParameters[1]);
            preparedStatement.setInt(4, Integer.parseInt(recievedMessageParameters[2]));
            
            System.out.println("recieved parameters: " + recievedMessageParameters[0] + " " + recievedMessageParameters[1] + " " + recievedMessageParameters[2]);
            
            int successInsert = preparedStatement.executeUpdate();
            System.err.println("Success on insert val: " + successInsert);
            if(successInsert <= 0)
            {
                System.out.println("user already exists");
                user = null;
                
            }
            else
            {
                try(ResultSet generatedKey = preparedStatement.getGeneratedKeys())
                {
                    
                    if(generatedKey.next())
                    {
                        userIDKey = generatedKey.getInt(1);
                    }
                        
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
                if(userIDKey != null)
                {
                    user = new User(uuid.toString(), Integer.parseInt(recievedMessageParameters[2]), userIDKey, recievedMessageParameters[1]);
                }
                else
                {
                    System.err.println("Error retreiving inserted user key.");
                }
                
            }
            //resultSet.next();
             
            System.out.println("uuid: " + user.getSessionID() + " username: " + user.getUserFirstName() + " userid: " + user.getUserID() + " usertype: " + user.getUserType());
            
            preparedStatement.close();
            connection.close();
            //databaseManager.closeConnection();
            
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
                responseMessage = new ResponseMessage();
                responseMessage.constructJsonResponse("Success","LoginResponseWorker", jsonInnerObject);
            }
            else
            {
                responseMessage = new ResponseMessage();
                responseMessage.constructJsonResponse("Failed","LoginResponseWorker", jsonInnerObject);
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

