/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oopgroup3.e_exam_server.ResponseClasses;

import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author Kyle
 */
public interface SendableInterface 
{
    public void send(Socket socket) throws IOException;
}
