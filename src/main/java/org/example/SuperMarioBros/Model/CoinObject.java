package org.example.SuperMarioBros.Model;

import org.example.SuperMarioBros.Controller.AudioPlayer;

import java.awt.*;

/**
 * CoinObject class represents the coins in super mario game.
 * It holds the class details relevant in our context.
 */
public class CoinObject extends AnimatedGameObject
{
    private static final int jumpPower = 10;
    private static final int gravityMagnitude = 1;
    
    private Rectangle creator;
    
    /**
     * creates a static coin
     *
     * @param startX x
     * @param startY y
     */
    public CoinObject(int startX, int startY)
    {
        super(startX, startY, "objects.png");
        initFixed();
        setAnimationInterval(4);
    }
    
    /**
     * creates a dynamic coin ( regardless of the class name :) )
     *
     * @param creator the coin wall created this coin (used to detect when to end animation)
     */
    public CoinObject(int startX, int startY, Rectangle creator)
    {
        super(startX, startY, "objects.png");
        changeToDynamicCoin(creator);
    }
    
    private void initFixed()
    {
        addImage(new Rectangle(3, 98, 10, 14));
        addImage(new Rectangle(3, 98, 10, 14));
        
        addImage(new Rectangle(19, 98, 10, 14));
        
        addImage(new Rectangle(35, 98, 10, 14));
    }
    
    private void initDynamic()
    {
        addImage(new Rectangle(4, 113, 8, 14));
        
        addImage(new Rectangle(20, 113, 8, 14));
        addImage(new Rectangle(20, 113, 8, 14));
        
        addImage(new Rectangle(36, 113, 8, 14));
        
        addImage(new Rectangle(52, 113, 8, 14));
    }
    
    private void changeToDynamicCoin(Rectangle creator)
    {
        resetImages();
        initDynamic();
        setAnimationInterval(1);
        
        enableGravity();
        setCollider(0, 0);
        this.creator = creator;
        setGravityMagnitude(gravityMagnitude);
        jump(jumpPower);
    }
    
    @Override
    public void update()
    {
        super.update();
        goNextImage();
        if (creator != null)
        {
            if (creator.intersects(new Rectangle(getX(), getY(), getWidth(), getHeight())))
            {
                if (getId() != -1)
                    GameState.getMap().getPlayer().addScore(100);
                destroy();
            }
        }
    }
    
    public void getCoinByWall(Wall other)
    {
        if (getId() == -1)
            return;
        GameState.getMap().getPlayer().addCoin();
        AudioPlayer.play(AudioPlayer.Coin);
        Rectangle wallCollider = new Rectangle(
                other.getCollider().x,
                other.getCollider().y,
                other.getCollider().width,
                other.getCollider().height);
        move(4, -5);
        changeToDynamicCoin(wallCollider);
    }
}
