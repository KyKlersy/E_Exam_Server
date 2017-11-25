/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oopgroup3.e_exam_server.ResponseClasses;

import com.google.gson.Gson;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 *
 * @author Kyle
 */
public class ResponseMessage implements MessageTypesInterface,
    JsonResponseInterface, SendableInterface
{
    
    private String workerThreadName;
    private String jsonObject;
    private String status;

    public ResponseMessage()
    {
        /* Default constructor */
    }
    
    @Override
    public void constructJsonResponse(String status, String workerThreadName, String jsonObjectString) {
        this.status = status;
        this.workerThreadName = workerThreadName;
        this.jsonObject = jsonObjectString;
        
    }
    
    @Override
    public void putWorkerThreadName(String workerThreadName) {
        this.workerThreadName = workerThreadName;
    }

    @Override
    public void putJsonObjectString(String jsonObjectString) {
        this.jsonObject = jsonObjectString;
    }

    @Override
    public void putStatus(String status) {
        this.status = status;
    }

    @Override
    public String getWorkerThreadName() {
        return this.workerThreadName;
    }

    @Override
    public String getJsonObjectString() {
        return this.jsonObject;
    }

    @Override
    public String getStatus() {
        return this.status;
    }

    @Override
    public void send(Socket socket)
        throws IOException
    {
        
        try (BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) 
        {
            Gson gson = new Gson();
            String jsonResponse = gson.toJson(this, ResponseMessage.class);
            
            bufferedWriter.write(jsonResponse);
        }
            
        System.out.println("Response Sent");
        
    }    
}
