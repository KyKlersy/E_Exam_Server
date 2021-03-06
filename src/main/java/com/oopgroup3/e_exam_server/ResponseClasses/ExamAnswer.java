package com.oopgroup3.e_exam_server.ResponseClasses;

/**
 * Data object for sending and receiving exam question objects
 * which represent the exam question form shown to the user in the client.
 * @author Kyle
 */
public class ExamAnswer {
    
    private int ExamQuestionID;
    private int QuestionNumber;
    private int QuestionType;
    private int AnswerChoice;

    public ExamAnswer(int ExamQuestionID, int QuestionNumber, int QuestionType, int AnswerChoice) {
        this.ExamQuestionID = ExamQuestionID;
        this.QuestionNumber = QuestionNumber;
        this.QuestionType = QuestionType;
        this.AnswerChoice = AnswerChoice;
    }

    public int getExamQuestionID() {
        return ExamQuestionID;
    }

    public void setExamQuestionID(int ExamQuestionID) {
        this.ExamQuestionID = ExamQuestionID;
    }

    public int getQuestionNumber() {
        return QuestionNumber;
    }

    public void setQuestionNumber(int QuestionNumber) {
        this.QuestionNumber = QuestionNumber;
    }

    public int getQuestionType() {
        return QuestionType;
    }

    public void setQuestionType(int QuestionType) {
        this.QuestionType = QuestionType;
    }

    public int getAnswerChoice() {
        return AnswerChoice;
    }

    public void setAnswerChoice(int AnswerChoice) {
        this.AnswerChoice = AnswerChoice;
    }
    
    @Override
    public String toString()
    {
        String output = "\nExam Question ID: " + getExamQuestionID() 
                        +"\nExam Question Number: " + getQuestionNumber()
                        +"\nExam Question Type: " + getQuestionType()
                        +"\nAnswer Choice: " + getAnswerChoice();
                
        return output;
    }

}
