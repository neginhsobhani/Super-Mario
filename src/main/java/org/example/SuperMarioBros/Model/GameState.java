package org.example.SuperMarioBros.Model;

import org.example.SuperMarioBros.Controller.AudioPlayer;
import org.example.SuperMarioBros.Controller.ExceptionHandler;
import org.example.SuperMarioBros.Controller.GameController;
import org.example.SuperMarioBros.View.GameFrame;
import org.example.SuperMarioBros.View.InitialMenu;

import java.awt.*;
import java.io.IOException;

/**
 * GameState class represents the current state of the game.
 * It holds the class details relevant in our context.
 */
public class GameState
{
    //don't show pause is for pausing the game without showing it to the user (e.g. mario dies, waiting for die audio to end)
    public static boolean pause, gameOver, win, inputBlocked, restartGame, restartMap, goNextLevel;
    private static Map map;
    private static GameController.InputListener input;
    private static int level;
    
    static
    {
        pause = gameOver = win = inputBlocked = restartGame = restartMap = goNextLevel = false;
        level = 0;
    }
    
    private GameState()
    {
        //prevent from instantiating
    }
    
    public static void loadMap(int level)
    {
        try
        {
            map = Map.loadMap(level);
        }
        catch (IOException | ClassNotFoundException e)
        {
            System.err.println("Create a map as level " + level + " (again?)");
            new ExceptionHandler(e);
            System.exit(0);
        }
    }
    
    public static int getLevel()
    {
        return level;
    }
    
    public static void goNextLevel()
    {
        if (level > 0)
            AudioPlayer.playWait(AudioPlayer.WinLevel);
        level++;
        prepareMap(true);
    }
    
    private static void restartMap()
    {
        prepareMap(true);
    }
    
    private static void restartGame()
    {
        level = 1;
        map = null;
        prepareMap(false);
    }
    
    private static void prepareMap(boolean savePlayer)
    {
        inputBlocked = true;
        
        AudioPlayer.stop(AudioPlayer.Background);
        
        //player stats isn't changed when level changes
        Player player = null;
        int initX = 0;
        int initY = 0;
        if (map != null) //level > 0
        {
            player = map.getPlayer();
            initX = player.getInitX();
            initY = player.getInitY();
        }
        
        loadMap(level);
        
        if(level == 1)
            AudioPlayer.playWithLoop(AudioPlayer.Background);
        else if (level == 2)
            AudioPlayer.playWithLoop(AudioPlayer.Background2);
        
        if (player != null && savePlayer)
        {
            player.reset();
            player.setPosition(initX, initY);
            map.setPlayer(player);
        }
        
        GameFrame.getInstance().resetPlayerPosition();
        
        inputBlocked = false;
    }
    
    /**
     * The method which updates the game state.
     */
    public static void update()
    {
        if(gameOver)
        {
            GameFrame.getInstance().render();
            AudioPlayer.playWait(AudioPlayer.GameOver);
        }
        else if (restartGame)
        {
            restartGame();
            restartGame = false;
        }
        else if (win)
        {
            win();
        }
        else if (restartMap)
        {
            restartMap();
            restartMap = false;
        }
        else if (goNextLevel)
        {
            goNextLevel();
            goNextLevel = false;
        }
        else
        {
            pause = input.pause;
            if (pause)
                return;
            map.update();
        }
    }
    
    public static GameController.InputListener getInputListener()
    {
        return input;
    }
    
    public static void setInputListener(GameController.InputListener inputListener)
    {
        input = inputListener;
    }
    
    public static Map getMap()
    {
        return map;
    }
    
    private static void win()
    {
        inputBlocked = true;
        GameFrame.getInstance().render();
        AudioPlayer.playWait(AudioPlayer.WinWorld);
    }
}


