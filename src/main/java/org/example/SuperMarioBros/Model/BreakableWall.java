package org.example.SuperMarioBros.Model;

import org.example.SuperMarioBros.Controller.AudioPlayer;
import org.example.SuperMarioBros.Controller.ExceptionHandler;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Random;

/**
 * BreakableWall class represents a background object which can be destroyed ba mario.
 * It holds the class details relevant in our context.
 */
public class BreakableWall extends Wall
{
    //number of items hidden in breakable wall
    private int items;
    private ImageIcon emptyCoinWall;
    private boolean hasPowerUp;
    private boolean hasCoin;
    private int bumpAnim;
    //the mushroom powerup which this wall is bumped to
    private MushroomGrowth bumpedToMushroomGrowth;
    //mushroom created by this breakable wall
    private MushroomGrowth myMushroom;
    
    public BreakableWall(int startX, int startY)
    {
        super(startX, startY, "tiles.png", new Rectangle(16, 0, 16, 16));
        //break after bump (if mario is big)
        this.items = -1;
        bumpAnim = -1;
    }
    
    public BreakableWall(int startX, int startY, int items)
    {
        super(startX, startY, "tiles.png", new Rectangle(16, 0, 16, 16));
        bumpAnim = -1;
        if (items < 0)
            items = -1;
        this.items = items;
        try
        {
            emptyCoinWall = loadImage("tiles.png", new Rectangle(432, 0, 16, 16));
        }
        catch (IOException e)
        {
            new ExceptionHandler(e);
            System.exit(0);
        }
        
        Random random = new Random();
        //50% chance to have coin
        hasCoin = random.nextBoolean();
        //20% chance to have power up
        int chance = random.nextInt(5);
        hasPowerUp = (chance == 0);
    }
    
    public void bump(int marioSize)
    {
        //normal breakable wall
        if (items == -1 && marioSize > 0)
        {
            //initiate animation of destroying wall
            Map map = GameState.getMap();
            map.addItem(new WallPiece(true, true));
            map.addItem(new WallPiece(true, false));
            map.addItem(new WallPiece(false, true));
            map.addItem(new WallPiece(false, false));
            map.getPlayer().addScore(50);
            destroy();
        }
        //only bump or get item
        else
        {
            if (items == -1)
            {
                AudioPlayer.play(AudioPlayer.WallBump);
                bumpOnly();
            }
            else
                getItem(marioSize);
        }
    }
    
    
    @Override
    public void destroy()
    {
        //TODO: start destroy animation (taken apart to 4 pieces!)
        if (getId() != -1)
            AudioPlayer.play(AudioPlayer.WallBreak);
        super.destroy();
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
     * gets item inside, if any
     */
    private void getItem(int marioSize)
    {
        if (items == 0)
        {
            AudioPlayer.play(AudioPlayer.WallBump);
            //not needed, but implemented for when the breakable wall starts with item = 0!!!
            setImage(emptyCoinWall);
        }
        else
        {
            if (items == 1 && hasPowerUp)
            {
                //create power up
                if (marioSize == 0)
                    GameState.getMap().addItem(myMushroom = new MushroomGrowth(getX(), getY()));
                else
                    GameState.getMap().addItem(new FlowerGrowth(getX(), getY()));
                AudioPlayer.play(AudioPlayer.PowerUpAppear);
            }
            else if (hasCoin)
            {
                //create dynamic coin and add coin
                GameState.getMap().addItem(new CoinObject(getX() + 4, getY() - 17,
                        new Rectangle(getCollider().x, getCollider().y, getCollider().width, getCollider().height)));
                GameState.getMap().getPlayer().addCoin();
                AudioPlayer.play(AudioPlayer.Coin);
            }
            else
                AudioPlayer.play(AudioPlayer.WallBump);
            
            bumpOnly();
            
            items--;
            if (items == 0)
                setImage(emptyCoinWall);
        }
    }
    
    private class WallPiece extends AnimatedGameObject
    {
        //move on x axis
        private int speedX;
        
        /**
         * creates a piece of wall in breaking animation (which contains 4 pieces of wall)
         *
         * @param isRight is this animation for the right pieces?
         * @param isUp    is this animation for the upper pieces?
         */
        public WallPiece(boolean isRight, boolean isUp)
        {
            super(BreakableWall.this.getX(), BreakableWall.this.getY(), "objects.png");
            addImage(new Rectangle(64, 16, 16, 16));
            addImage(new Rectangle(64, 32, 16, 16));
            
            setCollider(0, 0);
            setAnimationInterval(4);
            setGravityMagnitude(1.0f);
            enableGravity();
            
            final int jumpUp = 10, jumpDown = 5, speedRight = 2,
                    moveX = 8, moveY = 8;
            
            //set jump and move based on booleans
            if (isRight)
            {
                speedX = speedRight;
                move(moveX, 0);
            }
            else
            {
                speedX = -speedRight;
                move(-moveX, 0);
            }
            
            if (isUp)
            {
                jump(jumpUp);
                move(0, -moveY);
            }
            else
            {
                jump(jumpDown);
                move(0, moveY);
            }
        }
        
        @Override
        public void update()
        {
            goNextImage();
            move(speedX, 0);
            super.update();
        }
    }
}
