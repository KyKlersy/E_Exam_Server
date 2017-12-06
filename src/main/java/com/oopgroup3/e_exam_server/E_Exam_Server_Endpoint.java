package com.oopgroup3.e_exam_server;

import com.oopgroup3.e_exam_server.ResponseClasses.MessageManager;
import com.oopgroup3.e_exam_server.ThreadWorkers.ServerTask;
import java.io.IOException;
import java.net.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;


/**
 * This is the main entry point for running the E-Exam server portion of the app
 * It handles creation of the database using database manager and receives all incoming
 * requests on socket port 64023, it then passes this to the serverTask runnable
 * which will further process the received connection.
 * 
 * @author Kyle
 */
public class E_Exam_Server_Endpoint {
    
    static final Set SESSIONIDS = Collections.synchronizedSet(new HashSet<String>());
    /* Limited to 8 threads active at any time could be expanded to fit future needs */
    static final ThreadPoolExecutor EXECUTOR = (ThreadPoolExecutor) Executors.newFixedThreadPool(8);
    
        
    public static void main(String[] args) 
    {

        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.initDatabase();
        
        /* Build the message manager singleton. */
        MessageManager.getMessageManagerInstance();
        
        try 
        {
            ServerSocket sock = new ServerSocket(64023);
            /* now listen for connections */
            while (true)
            {
                Socket client = sock.accept(); 
                //normally bad practice to have a spin infinite while true, sock.accept blocks untill a connection is recieved

                ServerTask serverTask = new ServerTask(client, EXECUTOR, SESSIONIDS, databaseManager);
                
                EXECUTOR.execute(serverTask);
            }
        }
        catch(IOException ioe)
        {
            System.err.println(ioe);
        }  
    }
}
