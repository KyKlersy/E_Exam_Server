package com.oopgroup3.e_exam_server.ResponseClasses;

/**
 * This extension of message allows a message to return a json message with a json
 * object packed inside of it.
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
