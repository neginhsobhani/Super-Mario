package org.example.SuperMarioBros.Controller;

import java.io.*;
import java.util.Date;

/**
 * ExceptionHandler class is a class which saves all exceptions in
 * log.txt for feedback.
 */
public class ExceptionHandler
{
    private String customMessage;
    
    public ExceptionHandler(Exception e)
    {
        this.customMessage = null;
        init(e);
    }
    
    public ExceptionHandler(Exception e, String customMessage)
    {
        this.customMessage = customMessage;
        init(e);
    }
    
    private void init(Exception e)
    {
        //for developing
        e.printStackTrace();
        //for release
        log(e);
    }
    
    private void log(Exception e)
    {
        File logFile = new File("Files/log.txt");
        try
        {
            if (!logFile.exists())
                logFile.createNewFile();
            PrintWriter logWriter = new PrintWriter(new FileOutputStream(logFile, true));
            //string written to log file
            logWriter.println(new Date().toString());
            String msg = e.getMessage();
            if (customMessage != null)
                logWriter.println("Custom Message: " + customMessage);
            if (msg == null)
                logWriter.println("(No Message)");
            else
                logWriter.println("Message: " + e.getMessage());
            logWriter.println("Stack Trace:");
            e.printStackTrace(logWriter);
            logWriter.println();
            //
            logWriter.close();
        }
        catch (IOException ioerror)
        {
            System.out.println("IO Error in Exception Handler:");
            ioerror.printStackTrace();
        }
    }
}
