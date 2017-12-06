package com.oopgroup3.e_exam_server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * This class handles creating the properties file if it doesn't exist
 * as well as the folder structure.
 * 
 * @author Kyle
 */
public class buildPropertiesFile {
     
    private boolean propFileExists = false;
    
    public buildPropertiesFile()
    {
        buildPropFile();
    }

    
    public final void buildPropFile()
    {
        Properties properties;
        OutputStream outputStream = null;
        boolean success = false;
        
        try 
        {
            properties = new Properties();   
            
            File f = new File(System.getProperty("user.dir"));
            File folderPath = new File(f.getAbsolutePath() + File.separator + "server_resources");
            //System.out.println("Folder Path: "+ folderPath.getAbsolutePath());
            
            if(!folderPath.exists())
            {
                System.out.println("Folder Doesnt Exists");

                try 
                {
                    success = (folderPath.mkdir());
                    System.out.println("Success Folder Created");

                } catch (Exception e) 
                {
                    e.printStackTrace();
                }
            }

            File propertiesFile = new File(folderPath + File.separator + "config.properties");
            if(!propertiesFile.exists())
            {

                outputStream = new FileOutputStream(propertiesFile);

                properties.setProperty("JDBC_DRIVER", "org.hsqldb.jdbc.JDBCDriver");
                properties.setProperty("DB_URI", "jdbc:hsqldb:file:");
                properties.setProperty("USER", "SA");
                properties.setProperty("PASS", "SA");
                properties.setProperty("DATABASE_INITIALIZED_ONCE", "false");

                properties.setProperty("DB_FOLDER_NAME", "E_Exam_Database"); 
                properties.setProperty("SQL_FILE_NAME", "buildExamTables.sql");


                properties.store(outputStream, null);
            }  
            
            propFileExists = true;
        }
        catch (IOException io) 
        {
            propFileExists = false;
            io.printStackTrace();
        }
        finally
        {
            if(outputStream != null)
            {
                try
                {
                    outputStream.close();
                }
                catch (IOException ioe) 
                {
                    ioe.printStackTrace();
                }
            }
        }
    }
    
    public String getPropByKey(String propName) throws IOException
    {
        
        Properties properties = null;
        InputStream inputStream = null;
        
        try 
        {  
            
            File userDir = new File(System.getProperty("user.dir"));
            File configFile = new File(userDir + File.separator + "server_resources/config.properties");
            //System.out.println(configFile.getAbsolutePath());
            
            if(configFile.exists())
            {
                inputStream = new FileInputStream(configFile);
                //System.out.println(inputStream.available());

                properties = new Properties();

                properties.load(inputStream);    
                
            }
        }  
        finally
        {
            if(inputStream != null)
            {
                try 
                {
                    inputStream.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            
            if(properties != null)
            {
                return properties.getProperty(propName);
            }
        }
        
        return null;
    }
    
    public void databaseInitialized()
    {
        Properties properties = null;
        OutputStream outputStream = null;
        
        try 
        {  
            
            File userDir = new File(System.getProperty("user.dir"));
            File configFile = new File(userDir + File.separator + "server_resources/config.properties");
            //System.out.println(configFile.getAbsolutePath());
            
            outputStream = new FileOutputStream(configFile);

            properties = new Properties();
            
            properties.setProperty("JDBC_DRIVER", "org.hsqldb.jdbc.JDBCDriver");
            properties.setProperty("DB_URI", "jdbc:hsqldb:file:");
            properties.setProperty("USER", "SA");
            properties.setProperty("PASS", "SA");
            properties.setProperty("DATABASE_INITIALIZED_ONCE", "true");

            properties.setProperty("DB_FOLDER_NAME", "E_Exam_Database"); 
            properties.setProperty("SQL_FILE_NAME", "buildExamTables.sql");

            properties.store(outputStream, null);                    
            
        }  
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
        finally
        {
            if(outputStream != null)
            {
                try 
                {
                    outputStream.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        } 
    }
    
    
    public boolean getPropFileExists()
    {
        return this.propFileExists;
    }

}
