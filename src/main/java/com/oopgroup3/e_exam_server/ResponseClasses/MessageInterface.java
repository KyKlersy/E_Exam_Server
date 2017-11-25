/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oopgroup3.e_exam_server.ResponseClasses;

/**
 *
 * @author Kyle
 */
public interface MessageInterface 
{
    public void putSessionID(String sessionID);
    public void putMethodName(String methodName);
    public void putParameters(String[] parameters);
    
    public String getSessionID();
    public String getMethodName();
    public String[] getParameters();
}
