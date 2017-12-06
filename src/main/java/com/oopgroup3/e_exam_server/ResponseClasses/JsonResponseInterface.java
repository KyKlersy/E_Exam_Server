package com.oopgroup3.e_exam_server.ResponseClasses;

/**
 * Response interface for message classes that handle sending json data.
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
