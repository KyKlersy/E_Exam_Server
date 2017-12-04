/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oopgroup3.e_exam_server.ThreadWorkers;

import com.google.gson.Gson;
import com.oopgroup3.e_exam_server.DatabaseManager;
import com.oopgroup3.e_exam_server.ResponseClasses.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Set;
import java.util.concurrent.Executor;
import static com.oopgroup3.e_exam_server.Utils.printDebug.*;
/**
 *
 * @author Kyle
 */
public class ServerTask implements Runnable{

    private final Socket client;
    private final Executor EXECUTOR;
    private final Set SESSIONIDS;
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
        
        if(message.getMethodName().equals("Login"))
        {
            EXECUTOR.execute(new LoginTask(message, SESSIONIDS, databaseManager));
        }
        
        if(message.getMethodName().equals("Register"))
        {
            
            EXECUTOR.execute(new RegisterUserTask(message, SESSIONIDS, databaseManager));
        }
        
        if(message.getMethodName().equals("CreateExam"))
        {

            EXECUTOR.execute(new CreateExamTask(message, databaseManager));
             
        }
        
        
        if(message.getMethodName().equals("EditExam"))
        {        
          
            EXECUTOR.execute(new EditExamTask(message, databaseManager));
            
        }
        
        if(message.getMethodName().equals("DeleteExam"))
        {
            EXECUTOR.execute(new DeleteExamTask(message, databaseManager));
            
        }
        
        if(message.getMethodName().equals("GetExam"))
        {
            
            EXECUTOR.execute(new GetExamTask(message, databaseManager));   
            
        }
        
        if(message.getMethodName().equals("GetTeacherExamList"))
        {

            EXECUTOR.execute(new GetListTask(message, databaseManager));       
        }

        if(message.getMethodName().equals("KeyExam"))
        {
            EXECUTOR.execute(new KeyExamTask(message, databaseManager));
            
        }
        
        if(message.getMethodName().equals("SubmitStudentExam"))
        {
            EXECUTOR.execute(new SubmitAndGradeStudentExamTask(message, databaseManager));
        }
        
        if(message.getMethodName().equals("GetExamGrades"))
        {
            EXECUTOR.execute(new GetExamGradesTask(message, databaseManager));
        }
        
        print(message.getMethodName());
        
        
    }
    
}
