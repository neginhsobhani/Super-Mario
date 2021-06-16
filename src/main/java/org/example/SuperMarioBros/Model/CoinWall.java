package org.example.SuperMarioBros.Model;

import org.example.SuperMarioBros.Controller.AudioPlayer;

import javax.swing.*;
import java.awt.*;

/**
 * CoinWall class represents the walls with ? on them which have coins
 * in them.
 * It holds the class details relevant in our context.
 */
public class CoinWall extends Wall
{
    //magic constants
    public static final int POWERUP = -1;
    public static final int EMPTY = 0;
    
    //image handling
    private ImageIcon emptyImage;
    private CoinWallImageHandler imageHandler;
    
    //number of coins (1, 2, 3 ...) or power up (-1) or empty (0)
    private int coin;
    private int bumpAnim;
    //the mushroom powerup which this wall is bumped to
    private MushroomGrowth bumpedToMushroomGrowth;
    //mushroom created by this CoinWall
    private MushroomGrowth myMushroom;
    
    public CoinWall(int startX, int startY)
    {
        super(startX, startY, "tiles.png", new Rectangle(432, 0, 16, 16));
        init();
        coin = 1;
    }
    
    /**
     * creates a coin wall
     *
     * @param startX     start x
     * @param startY     start y
     * @param coinNumber number of coins inside, 0 (CoinWall.EMPTY) =  empty, negative number (CoinWall.POWERUP) = power up
     */
    public CoinWall(int startX, int startY, int coinNumber)
    {
        super(startX, startY, "tiles.png", new Rectangle(432, 0, 16, 16));
        init();
        
        if (coinNumber < -1)
            coinNumber = -1;
        coin = coinNumber;
        if (coin == EMPTY)
            setImage(emptyImage);
    }
    
    private void init()
    {
        bumpAnim = -1;
        emptyImage = getImage();
        imageHandler = new CoinWallImageHandler();
    }
    
    //this class is created because all walls extends from FixedGameObject!
    private class CoinWallImageHandler extends AnimatedGameObject
    {
        
        public CoinWallImageHandler()
        {
            super(0, 0, "tiles.png");
            
            addImage(new Rectangle(384, 0, 16, 16));
            addImage(new Rectangle(384, 0, 16, 16));
            addImage(new Rectangle(384, 0, 16, 16));
            addImage(new Rectangle(384, 0, 16, 16));
            
            addImage(new Rectangle(400, 0, 16, 16));
            addImage(new Rectangle(416, 0, 16, 16));
            
            setAnimationInterval(3);
        }
        
        @Override
        /**
         * update is called by the owner (coin wall), not Map
         */
        public void update()
        {
            goNextImage();
        }
    }
    
    /**
     * no items, just bumping the wall
     */
    private void bumpOnly()
    {
        if (bumpAnim == -1)
            bumpAnim = 0;
    }
    
    @Override
    public void update()
    {
        //no need to call super (no physics)
        //super.update();
        
        if (coin != EMPTY)
        {
            setImage(imageHandler.getImage());
            imageHandler.update();
        }
        
        //bump animation
        if (bumpAnim > -1)
        {
            final int totalMove = 5;
            bumpAnim++;
            if (bumpAnim <= totalMove)
                move(0, -1);
            else if (bumpAnim <= totalMove * 2)
                move(0, 1);
            else
            {
                bumpAnim = -1;
                //clear mushroom growth if bumped to
                if (bumpedToMushroomGrowth != null)
                    bumpedToMushroomGrowth.clearBumpingWall();
            }
        }
    }
    
    @Override
    public void onCollision(GameObject other)
    {
        //no need to call super (no physics)
        //super.onCollision(other);
        if (bumpAnim == -1)
            return;
        if (other instanceof MushroomGrowth)
        {
            if (!other.equals(myMushroom))
            {
                bumpedToMushroomGrowth = (MushroomGrowth) other;
                bumpedToMushroomGrowth.jumpFlip(this);
            }
        }
        else if (other instanceof CoinObject)
        {
            ((CoinObject) other).getCoinByWall(this);
        }
    }
    
    /**
     * gets what's inside the CoinWall
     */
    public void getCoin()
    {
        if (coin == EMPTY)
            AudioPlayer.play(AudioPlayer.WallBump);
        else
            bumpOnly();
        
        if (coin == POWERUP)
        {
            int marioSize = GameState.getMap().getPlayer().getMarioSize();
            if (marioSize == 0)
            {
                myMushroom = new MushroomGrowth(getX(), getY());
                GameState.getMap().addItem(myMushroom);
            }
            else
                GameState.getMap().addItem(new FlowerGrowth(getX(), getY()));
            AudioPlayer.play(AudioPlayer.PowerUpAppear);
            coin = EMPTY;
            setImage(emptyImage);
        }
        else if (coin > EMPTY)
        {
            coin--;
            GameState.getMap().addItem(new CoinObject(getX() + 4, getY() - 17,
                    new Rectangle(getCollider().x, getCollider().y, getCollider().width, getCollider().height)));
            GameState.getMap().getPlayer().addCoin();
            if (coin == EMPTY)
                setImage(emptyImage);
            AudioPlayer.play(AudioPlayer.Coin);
        }
    }
    
}
