/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oopgroup3.e_exam_server.ThreadWorkers;

import com.google.gson.Gson;
import com.oopgroup3.e_exam_server.DatabaseManager;
import com.oopgroup3.e_exam_server.ResponseClasses.JsonResponseInterface;
import com.oopgroup3.e_exam_server.ResponseClasses.Message;
import com.oopgroup3.e_exam_server.ResponseClasses.MessageManager;
import com.oopgroup3.e_exam_server.ResponseClasses.MessageTypes;
import com.oopgroup3.e_exam_server.ResponseClasses.SendableInterface;
import com.oopgroup3.e_exam_server.SQLRequests;
import static com.oopgroup3.e_exam_server.Utils.printDebug.*;
import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

/**
 *
 * @author Kyle
 */
public class AssignStudentExamTask implements Runnable{

    private final Message message;
    private final DatabaseManager databaseManager;
    private final MessageManager messageManager = MessageManager.getMessageManagerInstance();

    public AssignStudentExamTask(Message message, DatabaseManager databaseManager) {
        this.message = message;
        this.databaseManager = databaseManager;
    }
    
    
    
    @Override
    public void run() {
        
        String[] params = message.getParameters();
        int studentID = Integer.parseInt(params[0]);
        int teacherID = Integer.parseInt(params[1]);
        int examID = Integer.parseInt(params[2]);
        
        print("Recieved params: Studentid" + studentID + " teacherid: " + teacherID + " examid: " + examID );
        
        boolean rowupdated = false;

        try(Connection con = databaseManager.getConnection())
        {
            try(PreparedStatement insertNewAssignedExam = con.prepareStatement(SQLRequests.insertTeacherAssignedExam.getSQLStatement()))
            {
                insertNewAssignedExam.setNull(1, Types.INTEGER);  
                insertNewAssignedExam.setInt(2, examID);
                insertNewAssignedExam.setInt(3, studentID);
                insertNewAssignedExam.setInt(4, teacherID);

                rowupdated = insertNewAssignedExam.execute();
            }
        }catch(SQLException sqle)
        {
            sqle.printStackTrace();
        }
        
        try(Connection con2 = databaseManager.getConnection())
        {
            try(PreparedStatement insertNewStudentAssignedExam = con2.prepareStatement(SQLRequests.insertStudentAssignedExam.getSQLStatement()))
            {
                insertNewStudentAssignedExam.setNull(1, Types.INTEGER);  
                insertNewStudentAssignedExam.setInt(2, studentID);
                insertNewStudentAssignedExam.setInt(3, examID);
                insertNewStudentAssignedExam.setInt(4, 0);
                insertNewStudentAssignedExam.setNull(5, Types.FLOAT);

                rowupdated = insertNewStudentAssignedExam.execute();
            }
        }catch(SQLException sqle)
        {
            sqle.printStackTrace();
        }

        try
        {
            JsonResponseInterface jsonResponse = (JsonResponseInterface) messageManager.getMessage(MessageTypes.ResponseMessage);

            Gson gson = new Gson();


            jsonResponse.constructJsonResponse("Success", "CreateExamResponseWorker", "Success");
            print(jsonResponse.getStatus());
            SendableInterface sendable = (SendableInterface) jsonResponse;
            sendable.send(new Socket("127.0.0.1",64018));

        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }            
    }
}
