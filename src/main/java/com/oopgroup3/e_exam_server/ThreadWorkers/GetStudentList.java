/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oopgroup3.e_exam_server.ThreadWorkers;

import com.oopgroup3.e_exam_server.DatabaseManager;
import com.oopgroup3.e_exam_server.ResponseClasses.AbstractListResponse;
import com.oopgroup3.e_exam_server.ResponseClasses.ExamListData;
import com.oopgroup3.e_exam_server.ResponseClasses.ExamQuestion;
import com.oopgroup3.e_exam_server.ResponseClasses.Message;
import com.oopgroup3.e_exam_server.ResponseClasses.MessageManager;
import com.oopgroup3.e_exam_server.ResponseClasses.ResponseMessage;
import com.oopgroup3.e_exam_server.ResponseClasses.UserData;
import java.util.ArrayList;

/**
 *
 * @author Kyle
 */
public class GetStudentList implements Runnable {

    private final Message message;
    private final DatabaseManager databaseManager;
    private final MessageManager messageManager = MessageManager.getMessageManagerInstance();
    private AbstractListResponse<UserData> abstractListResponse;
    private ResponseMessage responseMessage;

    public GetStudentList(Message message, DatabaseManager databaseManager) 
    {
        this.message = message;
        this.databaseManager = databaseManager;
    }

    @Override
    public void run() {
        String sessionID = message.getSessionID();
        ArrayList<UserData> examQuestion = new ArrayList<>();

        String[] parameters = message.getParameters();
        
    }
    
}
