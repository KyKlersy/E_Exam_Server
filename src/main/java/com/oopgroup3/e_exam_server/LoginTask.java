/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oopgroup3.e_exam_server;

import com.google.gson.Gson;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Set;
import java.util.UUID;

/**
 *
 * @author Kyle
 */
public class LoginTask implements Runnable {

    private Message message;
    private Set SESSIONID;
    
    public LoginTask(Message message, Set sessionid) 
    {
        this.message = message;
        this.SESSIONID = sessionid;

    }
    
    @Override
    public void run() {
        
        String jsonResponse;
        UUID uuid = UUID.randomUUID();
        SESSIONID.add(uuid.toString());
        
        UserData userData = new UserData(256, 2, "Billy", uuid.toString());
        
        try
        {
            Socket sendSocket = new Socket("127.0.0.1",64018);
            
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(sendSocket.getOutputStream()));

            Gson gson = new Gson();
            jsonResponse = gson.toJson(userData, UserData.class);
            
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
