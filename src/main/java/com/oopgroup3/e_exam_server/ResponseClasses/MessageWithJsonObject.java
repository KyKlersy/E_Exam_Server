/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oopgroup3.e_exam_server.ResponseClasses;

/**
 *
 * @author Kyle
 */
public class MessageWithJsonObject extends Message{
    
    private final String jsonObject;
    
    public MessageWithJsonObject(String SessionID, String Method, String[] parameters ,String jsonObject)
    {
        super(SessionID, Method, parameters);

        this.jsonObject = jsonObject;
    }
      
    public MessageWithJsonObject(String SessionID, String Method,String jsonObject)
    {
        super(SessionID, Method);

        this.jsonObject = jsonObject;
    }
    
    public String getJsonObject()
    {
        return this.jsonObject;
    }
}
