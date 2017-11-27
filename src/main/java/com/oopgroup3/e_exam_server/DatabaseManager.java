/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oopgroup3.e_exam_server;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.commons.io.FileUtils;
import org.hsqldb.cmdline.SqlFile;
import org.hsqldb.cmdline.SqlToolError;

/**
 *
 * @author Kyle
 */
public class DatabaseManager {

    private boolean databaseInitialized;
    private buildPropertiesFile bPropertiesFile;
    private String propValue;
    
    private String DB_URI = null;   
    private String USER = null;   
    private String PASS = null;  
    private Connection connection = null;
    private String JDBC_DRIVER = null; 
    
    public DatabaseManager() 
    {
        bPropertiesFile = new buildPropertiesFile();
    }
    
    public void initDatabase()
    {

        
        bPropertiesFile = new buildPropertiesFile();
        
        if(bPropertiesFile.getPropFileExists())
        {
            
            try
            {
                propValue = bPropertiesFile.getPropByKey("DATABASE_INITIALIZED_ONCE");
                DB_URI = bPropertiesFile.getPropByKey("DB_URI");
                USER = bPropertiesFile.getPropByKey("USER");
                PASS = bPropertiesFile.getPropByKey("PASS");
                JDBC_DRIVER = bPropertiesFile.getPropByKey("JDBC_DRIVER");
                File userDir = new File(System.getProperty("user.dir"));
                File databaseFolder = new File(userDir + File.separator + "/server_resources/database");   
                DB_URI += databaseFolder;
                DB_URI += ";hsqldb.write_delay=false";
                System.out.println("Db uri: " + DB_URI);
                

            } catch (IOException ex) {
                ex.printStackTrace();
            }
            finally
            {
                if(propValue != null)
                {
                    databaseInitialized = Boolean.parseBoolean(propValue);
                }
            }
        } 
        
        buildDatabase(databaseInitialized);
        
        
    }
    
    private void buildDatabase(boolean databaseInitialized)
    {
        if( databaseInitialized == false )
        { 
            System.out.println("Database not initialized");
            try 
            {
                File userDir = new File(System.getProperty("user.dir"));
                File databaseFolder = new File(userDir + File.separator + "server_resources/database/");    
                DB_URI = bPropertiesFile.getPropByKey("DB_URI");
                DB_URI += databaseFolder.getAbsoluteFile();
                JDBC_DRIVER = bPropertiesFile.getPropByKey("JDBC_DRIVER");
                USER = bPropertiesFile.getPropByKey("USER");
                PASS = bPropertiesFile.getPropByKey("PASS");
                
                print("Did we actually load the prop file " + PASS);
                Class.forName(JDBC_DRIVER);
                connection = DriverManager.getConnection(DB_URI, USER, PASS);
                
                File sqlFilePath2 = new File(userDir + File.separator + "server_resources/testUserDB.sql");
                print(sqlFilePath2);
                //File sqlFilePath = new File(userDir + File.separator + "src/main/java/com/oopgroup3/e_exam_server/resources/testUserDB.sql");
                InputStream sqlFileStream = DatabaseManager.class.getResourceAsStream("/testUserDB.sql");
                File tmpFile = File.createTempFile("sqlFile", "temp");
                FileUtils.copyInputStreamToFile(sqlFileStream, tmpFile);

                
                SqlFile sqlFile = new SqlFile(tmpFile);
                print(sqlFile.toString());
                sqlFile.setConnection(connection);
                sqlFile.execute();
                sqlFile.closeReader();
                connection.close();
                bPropertiesFile.databaseInitialized();
            }
            catch (IOException ioe) 
            {
                
            }
            catch (ClassNotFoundException cnfe)
            {
                System.err.println("Class loader failed to find JDBC Driver class.");
            }
            catch (SQLException sqle)
            {
                System.err.println("SQL exception error.");
                sqle.printStackTrace();
            }
            catch (SqlToolError sqlToolError)
            {
                System.err.println("Failure to load and or parse the build sql script");
                sqlToolError.printStackTrace();
            }   
        }
        
        String sqlCommand = "Select * FROM Users";
        
        if(connection != null)
        { 
            System.err.println("Connection is not null");
            
            try 
            {
                Class.forName(JDBC_DRIVER);
                connection = DriverManager.getConnection(DB_URI, USER, PASS);
                Statement statement = connection.createStatement();    
                ResultSet exists = statement.executeQuery(sqlCommand);
                if(!exists.isBeforeFirst())
                {
                    System.err.println("DB doesnt exist");
                }
                else
                {
                    System.out.println("DB exists");
                }
                statement.close();
                exists.close();
                connection.close();
            } catch (Exception e) 
            {
                e.printStackTrace();
            }

        }

    }
  
    
    public Connection getConnection()
        throws SQLException
    {

        return DriverManager.getConnection(DB_URI, USER, PASS);
 
    }      
      
   /* public void closeConnection()
        throws SQLException
    {
        connection.close();
    }*/
    
    private static void print(Object obj)
    {
       System.out.println(obj.toString());
    }
  
    
}
