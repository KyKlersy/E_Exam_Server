/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oopgroup3.e_exam_server.ThreadWorkers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.oopgroup3.e_exam_server.DatabaseManager;
import com.oopgroup3.e_exam_server.Interfaces.EditExamUpdateQuery;
import com.oopgroup3.e_exam_server.Interfaces.GetExamQuery;
import com.oopgroup3.e_exam_server.Interfaces.GetExamQueryImplementation;
import com.oopgroup3.e_exam_server.ResponseClasses.AbstractListDecoder;
import com.oopgroup3.e_exam_server.ResponseClasses.AbstractListResponse;
import com.oopgroup3.e_exam_server.ResponseClasses.ExamQuestion;
import com.oopgroup3.e_exam_server.ResponseClasses.JsonResponseInterface;
import com.oopgroup3.e_exam_server.ResponseClasses.Message;
import com.oopgroup3.e_exam_server.ResponseClasses.MessageManager;
import com.oopgroup3.e_exam_server.ResponseClasses.MessageTypes;
import com.oopgroup3.e_exam_server.ResponseClasses.SendableInterface;
import static com.oopgroup3.e_exam_server.Utils.printDebug.print;
import java.io.IOException;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;



/**
 *
 * @author Kyle
 */
public class EditExamTask implements Runnable{


    private List<ExamQuestion> databaseExamList;
    private Set<Integer> databaseExamIDSet;
    
    private List<ExamQuestion> clientExamList;
    private Set<Integer> clientExamIDSet;
    
    private Set<Integer> duplicateDataBaseExamID;
    

    private List<ExamQuestion> deleteFromDatabaseList;
    private List<ExamQuestion> addToDatabaseList;
    private List<ExamQuestion> updateDatabaseList;
    
    private Message message;
    private DatabaseManager databaseManager;
    private EditExamUpdateQuery editExamSQLController;
    private MessageManager messageManager = MessageManager.getMessageManagerInstance();
    
    public EditExamTask(Message message, DatabaseManager databaseManager) 
    {
        this.message = message;
        this.databaseManager = databaseManager;
        editExamSQLController = new EditExamUpdateQuery();
        
        databaseExamList = new ArrayList<>();
        databaseExamIDSet = new HashSet<>();
        
        clientExamList = new ArrayList<>();
        clientExamIDSet = new HashSet<>();
        
        duplicateDataBaseExamID = new HashSet<>();

        deleteFromDatabaseList = new ArrayList<>();
        addToDatabaseList = new ArrayList<>();
        updateDatabaseList = new ArrayList<>();
    }


    

    
    @Override
    public void run() 
    {

        
        
        String sessionID = message.getSessionID();
        String[] params = message.getParameters();
        int Examid = Integer.parseInt(params[1]);
        ResultSet resultSet;
        GetExamQuery getExamQuery = new GetExamQueryImplementation(Integer.parseInt(params[0]), Integer.parseInt(params[1]), databaseManager);
        
        try 
        {
            
            resultSet = getExamQuery.queryable();
            
            while(resultSet.next())
            {
                databaseExamList.add(new ExamQuestion(
                        
                    resultSet.getInt("ExamQuestionID"),
                    resultSet.getInt("QuestionNumber"),
                    resultSet.getInt("QuestionType"),
                    resultSet.getString("ExamQuestion"),
                    resultSet.getString("QuestionOne"),
                    resultSet.getString("QuestionTwo"),
                    resultSet.getString("QuestionThree"),
                    resultSet.getString("QuestionFour")                       
                ));
                
                databaseExamIDSet.add(resultSet.getInt("ExamQuestionID"));
            }
            
        }
        catch (SQLException sqle) 
        {
            sqle.printStackTrace();
        }
        
        AbstractListDecoder ald = new AbstractListDecoder();
        
        TypeToken<AbstractListResponse<ExamQuestion>> typeToken = new TypeToken<AbstractListResponse<ExamQuestion>>(){};
        AbstractListResponse<ExamQuestion> alr = ald.parseAbstractListResponse(message.getJsonObject(), typeToken);
        
        print("Size of returned list: " + alr.getList().size());
        clientExamList.addAll(alr.getList());
        print("List recieved from client: ");
        alr.getList().forEach(item -> {print(item.getExamQuestionID());});
        
        clientExamList.forEach(exam -> {
            clientExamIDSet.add(exam.getExamQuestionID());
        });
        
        duplicateDataBaseExamID.addAll(databaseExamIDSet);

        databaseExamIDSet.removeAll(clientExamIDSet);
        clientExamIDSet.removeAll(duplicateDataBaseExamID);
        
        print("Val missing, delete from database");
        databaseExamIDSet.forEach(val -> {
            print("Exam Question ID: " + val);
        });
        
        print("Val added, add new val to database");
        clientExamIDSet.forEach(val -> {
            print("Exam Question ID: " + val);
        });
        
        for(int i = (clientExamList.size() - clientExamIDSet.size()); i < clientExamList.size(); i++)
        {
            addToDatabaseList.add(clientExamList.get(i));
               
        }
        
        for(ExamQuestion eq : databaseExamList)
        {
            for(Integer integer : databaseExamIDSet)
            {
                if(eq.getExamQuestionID() == integer)
                {
                    deleteFromDatabaseList.add(eq);
                }
            }
        }
        
        
        print("List of things to add to the database: ");
        addToDatabaseList.forEach(p ->{print(p.getExamQuestionID());});

        print("List of things to delete from the database");
        deleteFromDatabaseList.forEach(p ->{print(p.getExamQuestionID());});
        
        clientExamList.removeAll(deleteFromDatabaseList);
        clientExamList.removeAll(addToDatabaseList);
        
        print("List of things to update in the database");
        clientExamList.forEach(p->{print(p.getExamQuestionID());});
        
        editExamSQLController.delete(deleteFromDatabaseList, databaseManager, Examid); 
        editExamSQLController.insert(addToDatabaseList, databaseManager, Examid );
        editExamSQLController.update(clientExamList, databaseManager, Examid);
        
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
