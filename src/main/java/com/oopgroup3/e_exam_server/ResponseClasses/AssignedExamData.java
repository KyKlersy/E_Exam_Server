package com.oopgroup3.e_exam_server.ResponseClasses;

/**
 * Data object class for sending back assigned exam data for students
 * to be able to see.
 * @author Kyle
 */
public class AssignedExamData 
{
    private String examName;
    private String username;

    public AssignedExamData(String examName, String username) {
        this.examName = examName;
        this.username = username;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    
    @Override
    public String toString()
    {
        return ("Username: " + getUsername() + " Exam Name: " + getExamName());
    }
}
