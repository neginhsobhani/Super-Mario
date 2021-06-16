package org.example.SuperMarioBros.Controller;

import org.example.SuperMarioBros.Model.GameState;
import org.example.SuperMarioBros.View.GameFrame;

/**
 * A very simple structure for the main game loop.
 * THIS IS NOT PERFECT, but works for most situations.
 * Note that to make this work, none of the 2 methods
 * in the while loop (update() and render()) should be
 * long running! Both must execute very quickly, without
 * any waiting and blocking!
 * <p>
 * Detailed discussion on different game loop design
 * patterns is available in the following link:
 * http://gameprogrammingpatterns.com/game-loop.html
 *
 * @author Seyed Mohammad Ghaffarian
 */
public class GameLoop implements Runnable
{
    
    /**
     * Frame Per Second.
     * Higher is better, but any value above 24 is fine.
     */
    private static final int FPS = 30;
    
    private static GameLoop instance;
    private boolean isMultiplayer;
    
    private GameLoop()
    {
        //create game frame
        GameFrame.getInstance();
        isMultiplayer = false;
        GameController.InputListener kl = GameController.getInstance().getInputListener();
        GameFrame.getInstance().setFrameListener(kl);
        GameFrame.getInstance().addKeyListener(kl);
        GameState.setInputListener(kl);
        GameState.goNextLevel();
    }
    
    public static GameLoop getInstance()
    {
        if (instance == null)
            instance = new GameLoop();
        return instance;
    }
    
    public void setHostIP(String ip)
    {
        GameController.getInstance().setHostIP(ip);
        isMultiplayer = true;
    }
    
    @Override
    public void run()
    {
        if(isMultiplayer)
            NetworkController.getInstance().update();
        
        boolean gameOver = false;
        boolean win = false;
        while (!gameOver && !win)
        {
            try
            {
                long start = System.currentTimeMillis();
                //
                GameController.getInstance().update();
                gameOver = GameController.getInstance().isGameOver();
                win = GameController.getInstance().isWon();
                //
                long delay = (1000 / FPS) - (System.currentTimeMillis() - start);
                if (delay > 0)
                    Thread.sleep(delay);
            }
            catch (InterruptedException ex)
            {
                new ExceptionHandler(ex);
            }
        }
        
        //game over or win screen
        GameFrame.getInstance().render();
        if(isMultiplayer)
            NetworkController.getInstance().update();
    }
}
