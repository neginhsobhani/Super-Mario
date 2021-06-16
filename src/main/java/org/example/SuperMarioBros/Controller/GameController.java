package org.example.SuperMarioBros.Controller;

import org.example.SuperMarioBros.Model.GameState;
import org.example.SuperMarioBros.Network.Client;
import org.example.SuperMarioBros.View.GameFrame;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

//singleton class

/**
 * GameController class represents the controller of mvc model
 * in this project where model and view are connected to each other.
 * It holds the class details relevant in our context.
 */
public class GameController
{
    private static GameController instance;
    private boolean gameOver, win;
    private InputListener inputListener;
    private boolean isMultiPlayer;

    
    public GameController()
    {
        gameOver = false;
        win = false;
        inputListener = new InputListener();
        isMultiPlayer = false;
    }
    
    public static GameController getInstance()
    {
        if (instance == null)
            instance = new GameController();
        return instance;
    }
    
    public void setHostIP(String ip)
    {
        Client.setIP(ip);
        isMultiPlayer = true;
    }
    
    public boolean isGameOver()
    {
        return gameOver;
    }
    
    public boolean isWon()
    {
        return win;
    }
    
    public void update()
    {
        gameOver = GameState.gameOver;
        win = GameState.win;
        GameState.update();
        GameFrame.getInstance().render();
        if(isMultiPlayer)
            NetworkController.getInstance().update();
    }
    
    public InputListener getInputListener()
    {
        return inputListener;
    }
    
    public class InputListener extends KeyAdapter
    {
        //mario inputs
        public boolean
                jump,
                left,
                right,
                shoot,
                pause,
                crouch,
                zoomIn,
                zoomOut;
        
        
        public InputListener()
        {
            jump = left = right = shoot = pause = crouch = zoomIn = zoomOut = false;
        }
        
        @Override
        public void keyPressed(KeyEvent e)
        {
            if (GameState.inputBlocked)
                return;
            switch (e.getKeyCode())
            {
                case KeyEvent.VK_W:
                    jump = true;
                    break;
                case KeyEvent.VK_A:
                    left = true;
                    break;
                case KeyEvent.VK_S:
                    crouch = true;
                    break;
                case KeyEvent.VK_D:
                    right = true;
                    break;
                case KeyEvent.VK_SPACE:
                    shoot = true;
                    break;
                case KeyEvent.VK_ESCAPE:
                    AudioPlayer.play(AudioPlayer.Pause);
                    pause = !pause;
                    break;
                case KeyEvent.VK_R:
                    GameState.restartGame = true;
                    break;
            }
        }
        
        @Override
        public void keyReleased(KeyEvent e)
        {
//            if (GameState.inputBlocked)
//                return;
            switch (e.getKeyCode())
            {
                case KeyEvent.VK_W:
                    jump = false;
                    break;
                case KeyEvent.VK_A:
                    left = false;
                    break;
                case KeyEvent.VK_S:
                    crouch = false;
                    break;
                case KeyEvent.VK_D:
                    right = false;
                    break;
                case KeyEvent.VK_SPACE:
                    shoot = false;
                    break;
                case KeyEvent.VK_ADD:
                case KeyEvent.VK_PLUS:
                case KeyEvent.VK_EQUALS:
                case KeyEvent.VK_UP:
                    zoomIn = true;
                    break;
                case KeyEvent.VK_SUBTRACT:
                case KeyEvent.VK_MINUS:
                case KeyEvent.VK_DOWN:
                    zoomOut = true;
                    break;
            }
        }
    }
}
