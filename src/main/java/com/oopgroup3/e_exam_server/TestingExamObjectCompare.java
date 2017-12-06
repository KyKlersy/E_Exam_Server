/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oopgroup3.e_exam_server;

import com.oopgroup3.e_exam_server.ResponseClasses.ExamGrade;
import com.oopgroup3.e_exam_server.ResponseClasses.ExamQuestion;
import java.util.HashSet;
import java.util.Set;
import static com.oopgroup3.e_exam_server.Utils.printDebug.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Consumer;

/**
 *
 * @author Kyle
 */
public class TestingExamObjectCompare 
{
    private ExamQuestion eq1, eq2, eq3, eq4, eq5, eq6, eq7,eq8, eq9;
    
    public TestingExamObjectCompare()
    {
        
        eq1 = new ExamQuestion(11,1, 1, "duc", "goose", "pidgeon", "quail", "swan");
        eq2 = new ExamQuestion(12,2, 1, "duck", "goose", "pidgeon", "quail", "swan");
        eq3 = new ExamQuestion(13,1, 1, "duc", "goose", "pidgeon", "quail", "swan"); //remvoe and place into the delete array
        eq4 = new ExamQuestion(15,2, 1, "yuck", "goose", "pidgeon", "quail", "swan");
        
        eq5 = new ExamQuestion(11,1, 1, "duc", "goose", "pidgeon", "quail", "swan");
        eq6 = new ExamQuestion(12,2, 1, "duck", "goose", "pidgeon", "quail", "swan");
        eq7 = new ExamQuestion(15,1, 1, "duc", "goose", "pidgeon", "quail", "swan");
        eq8 = new ExamQuestion(22,2, 1, "yuck", "goose", "pidgeon", "quail", "swan"); //remove and place into insert array
        eq9 = new ExamQuestion(-1,2, 1, "yuck", "goose", "pidgeon", "quail", "swan"); //remove and place into insert array
        
    }
    
    public static void main(String[] args) 
    {
        try {
            
        
        TestingExamObjectCompare testingExamObjectCompare = new TestingExamObjectCompare();
        int test;
        test = testingExamObjectCompare.eq1.compareTo(testingExamObjectCompare.eq2);
        
        System.out.println("Compare Val: " + test);
        
        Set<Integer> eqSet1 = new HashSet<>();
        eqSet1.add(testingExamObjectCompare.eq1.getExamQuestionID());
        eqSet1.add(testingExamObjectCompare.eq2.getExamQuestionID());
        eqSet1.add(testingExamObjectCompare.eq3.getExamQuestionID());
        eqSet1.add(testingExamObjectCompare.eq4.getExamQuestionID());
        
        Set<Integer> eqSet2 = new HashSet<>();
        eqSet2.add(testingExamObjectCompare.eq5.getExamQuestionID());
        eqSet2.add(testingExamObjectCompare.eq6.getExamQuestionID());
        eqSet2.add(testingExamObjectCompare.eq7.getExamQuestionID());
        eqSet2.add(testingExamObjectCompare.eq8.getExamQuestionID());
        eqSet2.add(testingExamObjectCompare.eq9.getExamQuestionID());
        
        Set<Integer> eqSet3 = new HashSet<>();
        eqSet3.addAll(eqSet1);
        
        eqSet1.removeAll(eqSet2);
        eqSet2.removeAll(eqSet3);
        
        print("Val missing, delete from database");
        eqSet1.forEach(val -> {
            System.out.println("Exam Question ID: " + val);
        });
        
        print("Val added, add new val to database");
        eqSet2.forEach(val -> {
            System.out.println("Exam Question ID: " + val);
        });
        
        List<Integer> deleteCase = new ArrayList<>();
        deleteCase.addAll(eqSet1);
        
        List<Integer> addCase = new ArrayList<>();
        addCase.addAll(eqSet2);
        
        //addCase.forEach(ac -> {print(ac);});
        
        List<ExamQuestion> list1 = new ArrayList<>();
        list1.add(testingExamObjectCompare.eq1);
        list1.add(testingExamObjectCompare.eq2);
        list1.add(testingExamObjectCompare.eq3);
        list1.add(testingExamObjectCompare.eq4);
        

        ListIterator<ExamQuestion> li = list1.listIterator();
        
        while(li.hasNext())
        {
            ExamQuestion examQuestion = li.next();
            deleteCase.forEach(de -> {

                if(examQuestion.getExamQuestionID() == de)
                {
                    li.remove();
                }
            });
        }

            print("list 1");
        list1.forEach(eq -> {print(eq.getExamQuestionID());});
        
        
      
        
        
        List<ExamQuestion> list2 = new ArrayList<>();
        list2.add(testingExamObjectCompare.eq5);
        list2.add(testingExamObjectCompare.eq6);
        list2.add(testingExamObjectCompare.eq7);
        list2.add(testingExamObjectCompare.eq8);
        list2.add(testingExamObjectCompare.eq9);

        ListIterator<ExamQuestion> li2 = list2.listIterator();

        
        while(li2.hasNext())
        {
                ExamQuestion examQuestion = li2.next();
            addCase.forEach(ac -> {


                if(examQuestion.getExamQuestionID() == ac)
                {
                    li2.remove();
                }
            });
        }
        
            print("List 2");
        list2.forEach(l2 -> {print(l2.getExamQuestionID());});
        
        list1.forEach(new Consumer<ExamQuestion>() {
            @Override
            public void accept(ExamQuestion ent) {
                list2.forEach(ent2 -> {
                    int compareVal = ent.compareTo(ent2);
                    
                    if(compareVal == -1)
                    {
                        print("Val changed between two cases: " + ent.getQuestion() + " " + ent2.getQuestion());
                    }
                    else if(compareVal == 0)
                    {
                        print("No val change");
                    }
                });
            }
        });
        
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        ExamGrade examGrade = new ExamGrade(.85f, "Exam 3");
        
        print(examGrade.toString());
        
    }
}
