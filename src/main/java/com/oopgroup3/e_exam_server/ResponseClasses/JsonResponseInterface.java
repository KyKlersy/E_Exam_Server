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
public interface JsonResponseInterface 
{
    public void constructJsonResponse(String status, String workerThreadName, String jsonObjectString);
    public void putWorkerThreadName(String workerThreadName);
    public void putJsonObjectString(String jsonObjectString);
    public void putStatus(String status);
            
    public String getWorkerThreadName();
    public String getJsonObjectString();
    public String getStatus();     
}
