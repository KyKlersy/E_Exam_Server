/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oopgroup3.e_exam_server.Interfaces;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Interface Querable used by classes that need only simple requests to the database
 * that return a result set.
 * @author Kyle
 */
public interface Queryable
{
    public ResultSet queryable() throws SQLException;
    
}
