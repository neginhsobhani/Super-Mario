package org.example.SuperMarioBros;

import org.example.SuperMarioBros.Controller.AudioPlayer;
import org.example.SuperMarioBros.Controller.ExceptionHandler;
import org.example.SuperMarioBros.MapCreator.*;
import org.example.SuperMarioBros.Model.*;
import org.example.SuperMarioBros.View.InitialMenu;

import java.awt.*;
import java.io.IOException;


public class App
{
    
    public static void main(String[] args)
    {
        //main flow of app (create a map or start the game or both!)
        
        mapCreate(1);
        mapCreate(2);
        start();
    }
    
    private static void start()
    {
        // Initialize the global thread-pool
        ThreadPool.init();
        
        // Show the game menu ...
        EventQueue.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    //create menu and show
                    new InitialMenu();
                }
                catch (IOException e)
                {
                    new ExceptionHandler(e);
                    System.exit(0);
                }
            }
        });
        
        
    }
    
    private static void mapCreate(int level)
    {
        Map map = new Map();
        
        if(level == 1)
            new MapCreator1(map);
        else
            new MapCreator2(map);
        
        try
        {
            map.saveMap(level);
        }
        catch (IOException e)
        {
            new ExceptionHandler(e);
            System.exit(0);
        }
    }
}
