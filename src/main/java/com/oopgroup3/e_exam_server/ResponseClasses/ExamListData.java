package com.oopgroup3.e_exam_server.ResponseClasses;

/**
 * Data object for sending back Exam list data, each instance of this class
 * represents one exam that is packaged in a Abstractlistresponse to send the
 * entire list of exams to the client.
 * @author Kyle
 */
public class ExamListData 
{
    private String ExamName;
    private int  ExamID;

    public ExamListData(String ExamName, int ExamID) {
        this.ExamName = ExamName;
        this.ExamID = ExamID;
    }

    public String getExamName() {
        return ExamName;
    }

    public void setExamName(String ExamName) {
        this.ExamName = ExamName;
    }

    public int getExamID() {
        return ExamID;
    }

    public void setExamID(int ExamID) {
        this.ExamID = ExamID;
    }
}
