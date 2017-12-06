package com.oopgroup3.e_exam_server.ResponseClasses;

import com.google.gson.Gson;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * This is the base class that all other messages inherit from
 * it implements the message types interface which is being used in a way
 * that it should not. Any class that inherits from this parent gets that empty
 * interface allowing them to be cast to messageTypesInterface.
 * @author Kyle
 */
public class Message implements MessageTypesInterface,
        MessageInterface, SendableInterface
{
    
    private String SessionID;
    private String Method;
    private String[] parameters;
    private String jsonObject;
    
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

    public Message(String SessionID, String Method, String[] parameters, String jsonObject) {
        this.SessionID = SessionID;
        this.Method = Method;
        this.parameters = parameters;
        this.jsonObject = jsonObject;
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
    public void putJsonObject(String jsonObject) {
        this.jsonObject = jsonObject;
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

    @Override
    public String getJsonObject() {
        return this.jsonObject;
    }

}
