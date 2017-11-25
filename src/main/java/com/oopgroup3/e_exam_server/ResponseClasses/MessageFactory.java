/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oopgroup3.e_exam_server.ResponseClasses;

import java.util.HashMap;

/**
 *
 * @author Kyle
 */
public class MessageFactory 
{
    private static HashMap<MessageTypes, MessageTypesInterface> messageClassMap;

    public MessageFactory() 
    {
        messageClassMap = new HashMap<>();
        messageClassMap.put(MessageTypes.Message, new Message());
        messageClassMap.put(MessageTypes.MessageWithResponse, new MessageWithResponse());
        messageClassMap.put(MessageTypes.ResponseMessage, new ResponseMessage());
        messageClassMap.put(MessageTypes.TeacherListResponse, new AbstractListResponse<ExamListData>());
    }
    

    public MessageTypesInterface getMessageClass(MessageTypes messageType)
    {
        if(!messageClassMap.containsKey(messageType))
        {
            throw new IllegalArgumentException("Invalid message type or message type not initialized");
        }
        
        return messageClassMap.get(messageType);
    }
    
}
