package org.example.SuperMarioBros.View;

import org.example.SuperMarioBros.Controller.GameController;
import org.example.SuperMarioBros.Model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

/**
 * The window on which the rendering is performed.
 * This example uses the modern BufferStrategy approach for double-buffering,
 * actually it performs triple-buffering!
 * For more information on BufferStrategy check out:
 * http://docs.oracle.com/javase/tutorial/extra/fullscreen/bufferstrategy.html
 * http://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferStrategy.html
 */
public class GameFrame extends JFrame
{
    public static final int GAME_HEIGHT = 720;                  // 720p game resolution
    public static final int GAME_WIDTH = 16 * GAME_HEIGHT / 9;  // wide aspect ratio
    //zoom constants
    private static final float
            defaultScale = 2.8f,
            maxZoom = 3.6f,
            minZoom = 2.9f,
            zoomInterval = 0.2f;
    //singleton GameFrame
    private static GameFrame instance;
    //camera position
    private int locX, locY;
    private float scaleX, scaleY;
    private Rectangle viewPort;
    //fps track
    private long lastRender;
    private ArrayList<Float> fpsHistory;
    //buffer for showing in a better way
    private BufferStrategy bufferStrategy;
    private Graphics2D g2d;
    //frame input listener, for zooming in and out
    private GameController.InputListener frameListener;
    private boolean resetPlayerPosition;
    
    private GameFrame()
    {
        super("Super Mario Bros");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(GAME_WIDTH, GAME_HEIGHT);
        setLocationRelativeTo(null); // put frame at center of screen
        locX = locY = 0;
        //2X scale + set viewPort
        scaleX = scaleY = defaultScale;
        resetPlayerPosition = true;
        setViewPort();
        initPosition();
        //
        lastRender = -1;
        fpsHistory = new ArrayList<>(100);
        setVisible(true);
        initBufferStrategy();
    }
    
    private void initPosition()
    {
        setCameraPosition(0, 16 * 8);
        scaleX = scaleY = defaultScale;
        float zoomWidth = GAME_WIDTH / scaleX;
        float zoomHeight = GAME_HEIGHT / scaleY;
        viewPort = new Rectangle(16, 0, (int) zoomWidth, (int) zoomHeight);
    }
    
    private void setViewPort()
    {
        float zoomWidth = GAME_WIDTH / scaleX;
        float zoomHeight = GAME_HEIGHT / scaleY;
        float x = (GAME_WIDTH - zoomWidth) / 2;
        float y = (GAME_HEIGHT - zoomHeight) / 2;
        if(viewPort == null)
            viewPort = new Rectangle((int) x, (int) y, (int) zoomWidth, (int) zoomHeight);
        else
            viewPort = new Rectangle(viewPort.x, viewPort.y, (int) zoomWidth, (int) zoomHeight);
    }
    
    public static GameFrame getInstance()
    {
        if (instance == null)
            instance = new GameFrame();
        return instance;
    }
    
    public void setFrameListener(GameController.InputListener frameListener)
    {
        this.frameListener = frameListener;
    }
    
    /**
     * This must be called once after the JFrame is shown:
     * frame.setVisible(true);
     * and before any rendering is started.
     */
    public void initBufferStrategy()
    {
        // Triple-buffering
        createBufferStrategy(3);
        bufferStrategy = getBufferStrategy();
    }
    
    public void resetPlayerPosition()
    {
        resetPlayerPosition = true;
        initPosition();
    }
    
    //methods relative to camera movement
    
    public void moveCamera(int x, int y)
    {
        locX += x;
        locY += y;
        viewPort.x += x;
        viewPort.y += y;
    }
    
    public void setCameraPosition(int locX, int locY)
    {
        this.locX = locX - GAME_WIDTH / 2;
        this.locY = locY - GAME_HEIGHT / 2;
    }
    
    /**
     * horizontally follows mario with offset
     */
    
    private void followMario()
    {
        Player player = GameState.getMap().getPlayer();
        final int offset = viewPort.width / 3;
        int leftOfScreen = (int) getLeftOfScreen();
        int deltaX = player.getX() - (leftOfScreen + offset);
        if(resetPlayerPosition)
        {
            locX = player.getX() + 12 * 16 - GAME_WIDTH / 2;
            resetPlayerPosition = false;
        }
        else if (deltaX > 0)
        {
            moveCamera(deltaX, 0);
        }
    }
    //
    
    public float getLeftOfScreen()
    {
        return viewPort.x;
    }
    
    /**
     * Game rendering using Drawable interface with triple-buffering using BufferStrategy.
     */
    public void render()
    {
        // Render single frame
        do
        {
            // The following loop ensures that the contents of the drawing buffer
            // are consistent in case the underlying surface was recreated
            do
            {
                // Get a new graphics context every time through the loop
                // to make sure the strategy is validated
                g2d = (Graphics2D) bufferStrategy.getDrawGraphics();
                try
                {
                    //scale graphics
                    if (frameListener.zoomIn && scaleX <= maxZoom)
                    {
                        scaleX += zoomInterval;
                        scaleY += zoomInterval;
                        setViewPort();
                    }
                    else if (frameListener.zoomOut && scaleX >= minZoom)
                    {
                        scaleX -= zoomInterval;
                        scaleY -= zoomInterval;
                        setViewPort();
                    }
                    frameListener.zoomIn = frameListener.zoomOut = false;

                    g2d.scale(scaleX, scaleY);
                    int tx = (GAME_WIDTH - viewPort.width) / 2;
                    int ty = (GAME_HEIGHT - viewPort.height) / 2;
                    g2d.translate(-tx, -ty);
                    //camera positioning
                    followMario();
                    //map rendering
                    doRendering();
                    //HUD rendering
                    hudRender();
                }
                finally
                {
                    // Dispose the graphics
                    g2d.dispose();
                    g2d = null;
                }
                // Repeat the rendering if the drawing buffer contents were restored
            } while (bufferStrategy.contentsRestored());
            
            // Display the buffer
            bufferStrategy.show();
            // Tell the system to do the drawing NOW;
            // otherwise it can take a few extra ms and will feel jerky!
            Toolkit.getDefaultToolkit().sync();
            
            // Repeat the rendering if the drawing buffer was lost
        } while (bufferStrategy.contentsLost());
    }
    
    /**
     * Rendering all game elements based on the game state.
     */
    private void doRendering()
    {
        Map map = GameState.getMap();
//        //camera follows player
//        setCameraPosition(map.getPlayer().getX(), map.getPlayer().getY());
        
        // Draw background
        switch (GameState.getLevel())
        {
            case 1:
                //sky blue
                g2d.setColor(new Color(135, 206, 235));
                g2d.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
                break;
            case 2:
                //orange sky
                g2d.setColor(new Color(45, 55, 79));
                g2d.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
                break;
            default:
                g2d.setColor(Color.GRAY);
                g2d.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
                break;
        }
        //Draw game objects (in order) (last = on top of first)
        for (BackGroundObject backGroundObject : map.backGroundObjects)
            draw(backGroundObject, false, false);
        
        for (GameObject item : map.items)
        {
            if (item instanceof Drawable)
                draw((Drawable) item, false, false);
        }
        
        draw(map.getPlayer(), !map.getPlayer().isRight(), false);
        
        for (Wall wall : map.walls)
            draw(wall, false, false);
        
        for (Enemy enemy : map.enemies)
            draw(enemy, !enemy.isLeft(), enemy.isUpsideDown());
    }
    
    private void draw(Drawable drawable, boolean flip, boolean upsideDown)
    {
        //get coordinates of drawable relative to camera
        int objX = drawable.getX();
        int objY = drawable.getY();
        
        //set visibility value of the game object
        if (drawable instanceof GameObject)
        {
            GameObject gameObject = (GameObject) drawable;
            if (objX < viewPort.x - 32)
            {
                //left of screen
                gameObject.setVisible(-1);
            }
            else if (objX <= viewPort.x + viewPort.width)
            {
                //inside the screen (width)
                gameObject.setVisible(0);
            }
            else
            {
                //right of screen
                gameObject.setVisible(1);
            }
        }
    
        int objectXOnScreen = drawable.getX() - locX;
        int objectYOnScreen = drawable.getY() - locY;
        
        //if the drawable was in the screen, draw it
//        if (objectXOnScreen >= 0 &&
//                objectYOnScreen >= 0 &&
//                objectXOnScreen <= GAME_WIDTH &&
//                objectYOnScreen <= GAME_HEIGHT)
        if(drawable instanceof GameObject && ((GameObject)drawable).visible == 0)
        {
            //draw
            boolean done;
            do
            {
                if(drawable.getImage() == null)
                    return;
                Image image = drawable.getImage().getImage();
                if (flip)
                {
                    if (upsideDown)
                        done = g2d.drawImage(image, objectXOnScreen + image.getWidth(null), objectYOnScreen + image.getHeight(null), -image.getWidth(null), -image.getHeight(null), null);
                    else
                        done = g2d.drawImage(image, objectXOnScreen + image.getWidth(null), objectYOnScreen, -image.getWidth(null), image.getHeight(null), null);
                }
                else
                {
                    if (upsideDown)
                        done = g2d.drawImage(image, objectXOnScreen, objectYOnScreen + image.getHeight(null), image.getWidth(null), -image.getHeight(null), null);
                    else
                        done = g2d.drawImage(image, objectXOnScreen, objectYOnScreen, null);
                }
            } while (!done);
        }
    }
    
    private void hudRender()
    {
        //texts margin when showing on screen
        final int margin = 20;
        String str;
        g2d.setColor(Color.BLACK);
    
        if(GameState.gameOver)
        {
            g2d.fillRect(0, 0, GAME_WIDTH,GAME_HEIGHT);
            g2d.setColor(Color.WHITE);
            g2d.setFont(g2d.getFont().deriveFont(50.0f));
            str = "Game Over";
            drawString(str, getCenterX(str), getCenterY());
        }
        else if (GameState.pause)
        {
            g2d.setFont(g2d.getFont().deriveFont(50.0f));
            str = "Paused";
            drawString(str, getCenterX(str), getCenterY());
        }
        else if (GameState.win)
        {
            g2d.setColor(Color.GREEN);
            g2d.setFont(g2d.getFont().deriveFont(50.0f));
            str = "You Won, Congrats!";
            drawString(str, getCenterX(str), getCenterY());
        }
        
        g2d.setFont(g2d.getFont().deriveFont(14.0f));
        Player player = GameState.getMap().getPlayer();
        
        str = "score: " + player.getScore();
        drawString(str, margin, margin);
        
        str = "coins: " + player.getCoin();
        drawString(str, margin, margin * 2);
        
        str = "World " + GameState.getLevel();
        drawString(str, getCenterX(str), margin);
        
        str = "mario x" + player.getLives();
        int strWidth = g2d.getFontMetrics().stringWidth(str);
        drawString(str, viewPort.width - strWidth - margin, margin);
    }
    
    private void drawString(String str, int x, int y)
    {
//        int zoomWidth = (int) (GAME_WIDTH / scaleX);
//        int zoomHeight = (int) (GAME_HEIGHT / scaleY);
        //transform for scale
        int tx = (GAME_WIDTH - viewPort.width) / 2;
        int ty = (GAME_HEIGHT - viewPort.height) / 2;
        final int offsetY = 10;
        g2d.drawString(str, x + tx, y + ty + offsetY);
    }
    
    private int getCenterX(String str)
    {
        int strWidth;
        if (str == null)
            strWidth = 0;
        else
            strWidth = g2d.getFontMetrics().stringWidth(str);
        return (viewPort.width - strWidth) / 2;
    }
    
    /**
     * only use this to get center of screen for "string" drawing
     *
     * @return center of screen considering g2d font
     */
    private int getCenterY()
    {
        //int strHeight = g2d.getFontMetrics().getHeight();
        //return (viewPort.height - strHeight) / 2;
        return viewPort.height / 2;
    }
    
    /*
    old code:
     // Game rendering with triple-buffering using BufferStrategy.
     
     
    public void render(GameState state) {
        // Render single frame
        do {
            // The following loop ensures that the contents of the drawing buffer
            // are consistent in case the underlying surface was recreated
            do {
                // Get a new graphics context every time through the loop
                // to make sure the strategy is validated
                Graphics2D graphics = (Graphics2D) bufferStrategy.getDrawGraphics();
                try {
                    doRendering(graphics, state);
                } finally {
                    // Dispose the graphics
                    graphics.dispose();
                }
                // Repeat the rendering if the drawing buffer contents were restored
            } while (bufferStrategy.contentsRestored());
            
            // Display the buffer
            bufferStrategy.show();
            // Tell the system to do the drawing NOW;
            // otherwise it can take a few extra ms and will feel jerky!
            Toolkit.getDefaultToolkit().sync();
            
            // Repeat the rendering if the drawing buffer was lost
        } while (bufferStrategy.contentsLost());
    }
    
    
     // Rendering all game elements based on the game state.
     
    private void doRendering(Graphics2D g2d, GameState state) {
        // Draw background
        g2d.setColor(Color.GRAY);
        g2d.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
        // Draw ball
        g2d.setColor(Color.BLACK);
        //g2d.fillOval(state.locX, state.locY, state.diam, state.diam);
        
        //		g2d.drawImage(image,state.locX,state.locY,null);
        
        
        // Print FPS info
        long currentRender = System.currentTimeMillis();
        if (lastRender > 0) {
            fpsHistory.add(1000.0f / (currentRender - lastRender));
            if (fpsHistory.size() > 100) {
                fpsHistory.remove(0); // remove oldest
            }
            float avg = 0.0f;
            for (float fps : fpsHistory) {
                avg += fps;
            }
            avg /= fpsHistory.size();
            String str = String.format("Average FPS = %.1f , Last Interval = %d ms",
                    avg, (currentRender - lastRender));
            g2d.setColor(Color.CYAN);
            g2d.setFont(g2d.getFont().deriveFont(18.0f));
            int strWidth = g2d.getFontMetrics().stringWidth(str);
            int strHeight = g2d.getFontMetrics().getHeight();
            g2d.drawString(str, (GAME_WIDTH - strWidth) / 2, strHeight+50);
        }
        lastRender = currentRender;
        // Print user guide
        String userGuide
                = "Use the MOUSE or ARROW KEYS to move the BALL. "
                + "Press ESCAPE to end the game.";
        g2d.setFont(g2d.getFont().deriveFont(18.0f));
        g2d.drawString(userGuide, 10, GAME_HEIGHT - 10);
        // Draw GAME OVER
        if (state.pause) {
            String str = "Paused";
            g2d.setColor(Color.WHITE);
            g2d.setFont(g2d.getFont().deriveFont(Font.BOLD).deriveFont(64.0f));
            int strWidth = g2d.getFontMetrics().stringWidth(str);
            g2d.drawString(str, (GAME_WIDTH - strWidth) / 2, GAME_HEIGHT / 2);
        }
    }
    
     */
    
}

