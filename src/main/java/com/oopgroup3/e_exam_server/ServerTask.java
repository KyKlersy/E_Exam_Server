/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oopgroup3.e_exam_server;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Set;
import java.util.concurrent.Executor;

/**
 *
 * @author Kyle
 */
public class ServerTask implements Runnable{

    private Socket client;
    private Executor EXECUTOR;
    private Set SESSIONIDS;
    
    public ServerTask(Socket request, Executor executor, Set sessionids)
    {
        this.client = request;
        this.EXECUTOR = executor;
        this.SESSIONIDS = sessionids;
    }
    
    @Override
    public void run() {
       
        Message message;
        String jsonString = "";
        try
        {
            InputStream in = client.getInputStream();
            BufferedReader bufferedInput = new BufferedReader(new InputStreamReader(in));
            jsonString = bufferedInput.readLine();
            client.close();
        }
        catch(IOException ioe)
        {
            System.err.println(ioe);
        }

        Gson gson = new Gson();
        if(jsonString.equals(""))
        {    
            Thread.interrupted();
        }
        
        message = gson.fromJson(jsonString, Message.class);
        
        
        /*******************************************************************
        *                                                                  *
        * Begin block for parsing message for method to handle             *
        * on match execute new task using the Thread pool Executor         *
        *                                                                  *
        ********************************************************************/
        
        if(message.getMethod().equals("Login"))
        {
            EXECUTOR.execute(new LoginTask(message, SESSIONIDS));
        }
        
        /*
            To Do add more condition tests for handling other method calls
            each will follow the same pattern.
        */
        
        
        
        
        System.out.println(message.getMethod());
        
        /*
        
        Simple loop for parsing parameter array.
        for(String string : message.getParameters())
        {
            System.out.println("Parameter: " + string);
        }
        
        */

        
    }
    
}
