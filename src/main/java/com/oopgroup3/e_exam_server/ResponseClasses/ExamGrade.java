package com.oopgroup3.e_exam_server.ResponseClasses;

/**
 * Data object for sending back student exam grades.
 * @author Kyle
 */
public class ExamGrade
{
    private float grade;
    private String ExamName;

    public ExamGrade(float grade, String ExamName) {
        this.grade = grade;
        this.ExamName = ExamName;
    }

    public float getGrade() {
        return (grade * 100);
    }

    public void setGrade(float grade) {
        this.grade = grade;
    }

    public String getExamName() {
        return ExamName;
    }

    public void setExamName(String ExamName) {
        this.ExamName = ExamName;
    }
    
    
    @Override
    public String toString()
    {
        if(getGrade() == -100.0f)
        {
            return ("Exam name: " + getExamName() + " Grade: N/A");
        }
        else
        {
            return ("Exam name: " + getExamName() + " Grade: " + getGrade());
        }
        
    } 
}
