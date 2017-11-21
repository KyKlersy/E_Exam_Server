/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oopgroup3.e_exam_server;

import com.google.gson.Gson;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author Kyle
 */
public class EditExamTask implements Runnable{

    private Message message;
    private ResponseMessage responseMessage;

    public EditExamTask(Message message) {
        this.message = message;
        
    }
    
    
    @Override
    public void run()
    {
            String sessionID = message.getSessionID();
            ArrayList<ExamQuestion> examQuestion = new ArrayList<>();
            examQuestion.add(new ExamQuestion(1, 1, "The Duck", "", "", "", ""));
            examQuestion.add(new ExamQuestion(2, 2, "Thinks hard", "", "", "", ""));
            examQuestion.add(new ExamQuestion(3, 1, "Why?", "", "", "", ""));
            
            String jsonInnerObject;
            String jsonResponse;
            
            MessageWithJsonObject mwjoEdit = new MessageWithJsonObject(sessionID, "ExamReturnData", examQuestion);
                            
        try
        {
            Socket sendSocket = new Socket("127.0.0.1",64018);
            
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(sendSocket.getOutputStream()));

            Gson gson = new Gson();
            jsonInnerObject = gson.toJson(mwjoEdit, MessageWithJsonObject.class);
            responseMessage = new ResponseMessage(sessionID, "ExamReturnData", jsonInnerObject);
            
            jsonResponse = gson.toJson(responseMessage, ResponseMessage.class);
            
            bufferedWriter.write(jsonResponse);
            
            bufferedWriter.close();
            
            System.out.println("Response Sent");
                        
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }                                             
                    
    }
    
}
