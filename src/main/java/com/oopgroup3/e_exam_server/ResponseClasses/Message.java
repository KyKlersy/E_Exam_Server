/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oopgroup3.e_exam_server.ResponseClasses;

import com.google.gson.Gson;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.Socket;

/**
 *
 * @author Kyle
 */
public class Message implements MessageTypesInterface,
        MessageInterface, SendableInterface
{
    
    private String SessionID;
    private String Method;
    private String[] parameters;
    
    public Message()
    {
        
    }

    public Message(String SessionID, String Method)
    {
        this.SessionID = SessionID;
        this.Method = Method;
    }
    
    public Message(String SessionID, String Method, String[] parameters) {
        this.SessionID = SessionID;
        this.Method = Method;
        this.parameters = parameters;
    }

    @Override
    public void send(Socket sock)
        throws IOException
    {

        
        try (DataOutputStream dataOutputStream = new DataOutputStream(sock.getOutputStream()))
        {
            Gson gson = new Gson();
            
            String jsonString = gson.toJson(this);
            
            dataOutputStream.writeBytes(jsonString);
        }

        sock.close();  
    }

    @Override
    public void putSessionID(String sessionID) {
        this.SessionID = sessionID;
    }

    @Override
    public void putMethodName(String methodName) {
        this.Method = methodName;
    }

    @Override
    public void putParameters(String[] parameters) {
        this.parameters = parameters;
    }

    @Override
    public String getMethodName() {
        return this.Method;
    }

    @Override
    public String getSessionID() {
        return this.SessionID;
    }

    @Override
    public String[] getParameters() {
        return this.parameters;
    }

}
