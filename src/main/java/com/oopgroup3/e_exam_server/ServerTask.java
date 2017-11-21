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
import java.util.ArrayList;
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
    private DatabaseManager databaseManager;
    
    public ServerTask(Socket request, Executor executor, Set sessionids, DatabaseManager databaseManager)
    {
        this.client = request;
        this.EXECUTOR = executor;
        this.SESSIONIDS = sessionids;
        this.databaseManager = databaseManager;
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
            EXECUTOR.execute(new LoginTask(message, SESSIONIDS, databaseManager));
        }
        
        if(message.getMethod().equals("Register"))
        {
            
            EXECUTOR.execute(new RegisterUserTask(message, SESSIONIDS, databaseManager));
        }
        
        if(message.getMethod().equals("CreateExam"))
        {

            MessageWithJsonObject mwjo = gson.fromJson(jsonString, MessageWithJsonObject.class);
            
            mwjo.printExamQuestionList();
            
        }
        
        
        if(message.getMethod().equals("EditExam"))
        {        
            EXECUTOR.execute(new EditExamTask(message));   
        }
        
        if(message.getMethod().equals("DeleteExam"))
        {
            
            
        }
        
        if(message.getMethod().equals("GetExam"))
        {
            
            
            
        }

        System.out.println(message.getMethod());
        
        
    }
    
}
