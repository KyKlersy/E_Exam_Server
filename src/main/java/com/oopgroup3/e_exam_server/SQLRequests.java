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
                        "WHERE STUDENTUSERID = ?"
                       );
    
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
