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
public class MessageManager
{
    private static MessageManager messageManagerInstance;
    private MessageFactory messageFactory;
    
    
    private MessageManager()
    {
        messageFactory = new MessageFactory();
        
    }
    
    public static MessageManager getMessageManagerInstance()
    {
        if(messageManagerInstance == null)
        {
            messageManagerInstance = new MessageManager();
        }
        
        return messageManagerInstance;
    }

    public MessageTypesInterface getMessage(MessageTypes messageClassRequest)
    {
        return messageFactory.getMessageClass(messageClassRequest);
    }  
}
