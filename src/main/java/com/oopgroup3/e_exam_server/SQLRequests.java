/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oopgroup3.e_exam_server;

/**
 *
 * @author Kyle
 */
public enum SQLRequests 
{
    authenticateUser("SELECT USERID, USERNAME, PASSWORD,ACCOUNTTYPE FROM USERS WHERE USERNAME = ? AND PASSWORD = ?"),
    
    registerUser("INSERT INTO Users(UserID,Username,Password,AccountType) VALUES(?, ?, ?, ?)"),
    
    getTeacherExamList(
                        "SELECT TEACHEREXAMS.EXAMID, EXAMKEYS.EXAMNAME\n" +
                        "FROM TEACHEREXAMS \n" +
                        "LEFT JOIN EXAMKEYS ON TEACHEREXAMS.EXAMID= EXAMKEYS.EXAMKEYID\n" +
                        "WHERE TEACHERID= ?"
                       ),
    
    getStudentExamList(
                        "SELECT STUDENTEXAMS.STUDENTUSERID, STUDENTEXAMS.STUDENTEXAMID, EXAMKEYS.EXAMNAME FROM STUDENTEXAMS\n" +
                        "LEFT JOIN EXAMKEYS ON STUDENTEXAMS.STUDENTEXAMID = EXAMKEYS.EXAMKEYID\n" +
                        "WHERE STUDENTUSERID = ? AND STUDENTEXAMS.EXAMTAKEN = 0"
                       ),
    
    getExam("SELECT * FROM EXAMQUESTIONS WHERE EXAMID = ?"),
    
    createExamKey("INSERT INTO EXAMKEYS(ExamKeyID, ExamTeacherID, ExamName) VALUES(?,?,?)"),
    
    keyExam("UPDATE EXAMQUESTIONS SET ExamCorrectAnswer = ? WHERE EXAMID = ? AND ExamQuestionID = ?"),
    
    insertExamQuestions(
                        "INSERT INTO ExamQuestions(ExamQuestionID, ExamID, QuestionType, QuestionNumber, " +
                        "ExamQuestion, QuestionOne, QuestionTwo, QuestionThree, QuestionFour) VALUES(?,?,?,?,?,?,?,?,?)"
                       ),
    
    updateExamQuestions(
                        "UPDATE EXAMQUESTIONS SET ExamQuestion = ?, QuestionOne = ?, QuestionTwo = ?, QuestionThree = ? " +
                        "QuestionFour = ?, WHERE ExamID = ? AND ExamQuestionID = ?"
                       ),
    
    deleteExamQuestions("DELETE FROM EXAMQUESTIONS WHERE ExamID = ? AND ExamQuestionID = ?"),
    
    insertTeacherExam("INSERT INTO TeacherExams(ExamNumber,TeacherID,ExamID) VALUES(?,?,?)"),
    
    deleteTeacherExam("DELETE FROM TEACHEREXAMS WHERE TEACHERID = ? AND EXAMID = ?"),
    
    insertStudentSubmission("INSERT INTO StudentExamSubmittedQuestions(SubmittedQuestionID," +
                            "LinkedExamKeyID, SubmittedQuestionUser, SubmittedQuestionNumber," +
                            "SubmittedAnswerChoice) VALUES(?,?,?,?,?)"
                           ),
    getAnswerKey("SELECT ExamQuestionID, ExamCorrectAnswer FROM ExamQuestions WHERE ExamID = ?"),
    
    updateStudentExamData("UPDATE StudentExams SET Grade = ?, ExamTaken = ? WHERE StudentUserID = ? AND StudentExamID = ?"),
    
    getStudentsExamList("SELECT "),
    
    getExamGrades("SELECT GRADE, EXAMNAME FROM STUDENTEXAMS\n" +
                  "RIGHT JOIN EXAMKEYS ON STUDENTEXAMID = EXAMKEYID\n" +
                  "WHERE STUDENTUSERID = ?");
   
   
    private final String sqlString;

    private SQLRequests(String sqlString ) 
    {
        this.sqlString = sqlString;
    }
    
    public String getSQLStatement()
    {
        return this.sqlString;
    }
}
