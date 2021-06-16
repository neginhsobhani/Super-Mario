package org.example.SuperMarioBros.Model;

import org.example.SuperMarioBros.Controller.AudioPlayer;
import org.example.SuperMarioBros.Controller.GameController;
import org.example.SuperMarioBros.View.GameFrame;

import javax.swing.*;
import java.awt.*;

/**
 * Player represents Mario in the game.
 * It holds Mario details relevant in our context.
 */
public class Player extends AnimatedGameObject
{
    
    private int moveX, moveY;
    private int lives;
    private int score;
    private int coin;
    private final int speed;
    private final float jumpSpeed;
    private int activeFireBalls;
    //mario invincibility
    private boolean invincible;
    private final int invincibleTotalTime;
    private int invincibleTime;
    //is mario facing right direction (true) or left (false) ?
    private boolean isRight;
    private int marioSize; //negative number = dead mario, 0 = small mario, 1 = big mario, 2 = chief mario
    private static final int coinToLifeGain = 30;
    //0 = not on teleporting pipe, 1 = on teleporting pipe, 2 = is teleporting
    private int pipeStatus;
    //when teleporting, for animation
    private int pipeTransport;
    private PipeWall destPipe;
    //invincible blink
    private final ImageIcon emptyImage;
    private final int initX, initY;
    
    public Player(int startX, int startY)
    {
        super(startX, startY, "mario.png");
        
        emptyImage = loadImage(new Rectangle(60, 34, 16, 16));
        initX = startX;
        initY = startY;
        
        enableGravity();
        invincibleTotalTime = 50;
        
        coin = 0;
        speed = 2;
        jumpSpeed = 8.5f;
        lives = 3;
        //calling from here for the first time, it's "set" actually!
        reset();
    }
    
    @Override
    public ImageIcon getImage()
    {
        if(invincibleTime % 2 == 1 )
            return emptyImage;
        else
            return super.getImage();
    }
    
    public int getInitX()
    {
        return initX;
    }
    
    public int getInitY()
    {
        return initY;
    }
    
    public void reset()
    {
        activeFireBalls = 0;
        pipeStatus = 0;
        pipeTransport = 0;
        marioSize = 0;
        isRight = true;
        destPipe = null;
        
        //invincibility
        invincible = false;
        invincibleTime = 0;
        applyMarioSize();
    }
    
    //update is called before collisions
    @Override
    public void update()
    {
//        if(getId() == -1)
//            return;
        getInput();
        if (pipeStatus == 2)
        {
            pipeAnimation();
            return;
        }
        
        pipeStatus = 0;
        destPipe = null;
        
        move(moveX, moveY);
        
        if (invincible)
        {
            invincibleTime++;
            if (invincibleTime >= invincibleTotalTime)
            {
                invincibleTime = 0;
                invincible = false;
            }
        }
        super.update();
    }
    
    private void pipeAnimation()
    {
        final int offsetTime = 20;
        if (pipeTransport < getHeight() + offsetTime)
        {
            move(0, 1);
        }
        else if (pipeTransport == getHeight() + offsetTime)
        {
            AudioPlayer.play(AudioPlayer.Pipe);
            int destX = destPipe.getX() + (destPipe.getWidth() - getWidth()) / 2;
            int destY = destPipe.getY() + offsetTime;
            setPosition(destX, destY);
        }
        else if (pipeTransport <= (getHeight() + offsetTime) * 2)
        {
            move(0, -1);
        }
        else
        {
            pipeStatus = 0;
            pipeTransport = 0;
            destPipe = null;
        }
        
        pipeTransport++;
    }
    
    public void addScore(int amount)
    {
        score += amount;
    }
    
    public void addCoin()
    {
        addScore(200);
        coin++;
        if (coin == coinToLifeGain)
        {
            coin = 0;
            lives++;
            AudioPlayer.play(AudioPlayer.LifeGain);
        }
    }
    
    public void decreaseActiveFireBalls()
    {
        if (activeFireBalls > 0)
            activeFireBalls--;
    }
    
    public int getLives()
    {
        return lives;
    }
    
    public int getScore()
    {
        return score;
    }
    
    public boolean isRight()
    {
        return isRight;
    }
    
    private void getInput()
    {
        GameController.InputListener input = GameState.getInputListener();
        
        if (input.crouch)
        {
            //input.crouch = false;
            crouch();
        }
        
        if (pipeStatus == 2)
            return;
        
        if (input.left && getX() > GameFrame.getInstance().getLeftOfScreen())
        {
            moveX = -speed;
            goNextImage();
            isRight = false;
        }
        else if (input.right)
        {
            moveX = speed;
            goNextImage();
            isRight = true;
        }
        else
        {
            moveX = 0;
            goFirstImage();
        }
        
        if (input.shoot && marioSize == 2)
        {
            shoot();
            input.shoot = false;
        }
        if (input.jump)
        {
            if (!isJumping())
                jump();
            input.jump = false;
        }
        
    }
    
    private void jump()
    {
        if (super.jump(jumpSpeed))
            AudioPlayer.play(AudioPlayer.Jump);
    }
    
    private void crouch()
    {
        if (pipeStatus == 1 && destPipe != null)
        {
            pipeStatus = 2;
            AudioPlayer.play(AudioPlayer.Pipe);
        }
    }
    
    private void shoot()
    {
        if (activeFireBalls < 3)
        {
            int fireBallStartX = getX() + (isRight ? getWidth() : 0);
            FireBall fireBall = new FireBall(fireBallStartX, getY() + 2, isRight);
            GameState.getMap().addItem(fireBall);
            activeFireBalls++;
            AudioPlayer.play(AudioPlayer.Shoot);
        }
    }
    
    @Override
    public void onCollision(GameObject other)
    {
//        if(getId() == -1)
//            return;
        if (pipeStatus == 2)
            return;
        super.onCollision(other);
        
        if (other instanceof Wall)
        {
            wallCollision((Wall) other);
        }
        else if (other instanceof MushroomGrowth)
        {
            marioSize = 1;
            applyMarioSize();
            addScore(1000);
            AudioPlayer.playWait(AudioPlayer.PowerUpGet);
            other.destroy();
        }
        else if (other instanceof FlowerGrowth)
        {
            if (marioSize == 1)  //has eaten mushroom before
            {
                marioSize = 2;
                applyMarioSize();
            }
            
            addScore(1000);
            AudioPlayer.playWait(AudioPlayer.PowerUpGet);
            other.destroy();
        }
        else if (other instanceof Castle)
        {
            GameState.goNextLevel = true;
        }
        else if (other instanceof Princess)
        {
            GameState.win = true;
        }
        else if (other instanceof CoinObject)
        {
            addCoin();
            AudioPlayer.play(AudioPlayer.Coin);
            other.destroy();
        }
        else if (other instanceof Enemy)
        {
            enemyCollision((Enemy) other);
        }
        else if (other instanceof TurtleShell)
        {
            TurtleShell turtleShell = (TurtleShell) other;
            goLastMove();
    
            if (isAbove(other))
            {
                forceJump(jumpSpeed * 1 / 3);
                turtleShell.shootShell(0);
            }
            else if (!isUnder(other))
            {
                if(turtleShell.getMoving() == 0)
                {
                    move(-getLastMoveX(), 0);
                    if (getX() + getWidth() / 2 < other.getX() + other.getWidth() / 2)
                        turtleShell.shootShell(1);
                    else
                        turtleShell.shootShell(-1);
                }
            }
            else if(!invincible)
            {
                loseSize();
            }
            
            
        }
        
    }
    
    private void wallCollision(Wall other)
    {
        if (other instanceof BreakableWall)
        {
            BreakableWall breakableWall = (BreakableWall) other;
            if (isUnder(other))
            {
                breakableWall.bump(marioSize);
            }
        }
        else if (other instanceof CoinWall)
        {
            if (isUnder(other))
            {
                CoinWall coinWall = (CoinWall) other;
                coinWall.getCoin();
            }
        }
        else if (other instanceof PipeWall)
        {
            final int offset = 1;
            if (isAbove(other) &&
                    //if player is in between of pipe wall
                    getX() + offset > other.getX() &&
                    getX() + getWidth() - offset < other.getX() + other.getWidth())
            {
                destPipe = ((PipeWall) other).getDestination();
                if (destPipe != null)
                    pipeStatus = 1;
            }
        }
    }
    
    private void enemyCollision(Enemy other)
    {
        if (invincible)
            return;
        goLastMove();
        if (isAbove(other)) //mario's Y is higher than the other's
        {
            forceJump(jumpSpeed * 1 / 3);
            if(other instanceof HedgeHogEnemy || other instanceof PlantEnemy)
                loseSize();
            else
                other.destroy();
            /*
            //TODO: change it to other.destroy(); only!
            if (other instanceof TurtleEnemy)
            {
                other.destroy();
                //TurtleShell newTurtleShell = new TurtleShell(getX() + getWidth(), 0);
                //GameState.getMap().addItem(newTurtleShell);
            }
            else if (other instanceof RedTurtleEnemy)
            {
                //addScore(100);
                other.destroy();
//                TurtleShell newTurtleShell = new TurtleShell(getX() + getWidth(), 0);
//                GameState.getMap().addItem(newTurtleShell);
                //TODO complete shoot
            }
            else if (other instanceof HedgeHogEnemy)
            {
                destroy();
            }
            else if (other instanceof PlantEnemy)
            {
                destroy();
            }
            else if (other instanceof MushroomEnemy)
            {
//                addScore(100);
                other.destroy(); //the mushroom destroys
            }
            
             */
        }
        else
        {
            //horizontal collision
            loseSize();
        }
    }
    
    @Override
    //die
    public void destroy()
    {
        //super.destroy();
        AudioPlayer.stop(AudioPlayer.Background);
        AudioPlayer.playWait(AudioPlayer.Die);
        lives--;
        if (lives == 0)
        {
            GameState.gameOver = true;
            
        }
        else
        {
            GameState.restartMap = true;
        }
        applyMarioSize();
    }
    
    public void loseSize()
    {
        marioSize--;
        if (marioSize < 0)
            destroy();
        else
        {
            applyMarioSize();
            invincible = true;
            AudioPlayer.playWait(AudioPlayer.Pipe);
        }
    }
    
    public void applyMarioSize()
    {
        resetImages();
        if(marioSize == 0)
        {
            addImage(new Rectangle(80, 34, 16, 16));
            addImage(new Rectangle(97, 34, 16, 16));
            addImage(new Rectangle(114, 34, 16, 16));
            addImage(new Rectangle(131, 34, 16, 16));
        }
        else if (marioSize == 1)
        {
            addImage(new Rectangle(80,1,16,32));
            addImage(new Rectangle(97,1,16,32));
            addImage(new Rectangle(114,1,16,32));
            addImage(new Rectangle(131,1,16,32));
        }
        else
        {
            //TODO: Fire mario
            addImage(new Rectangle(80, 129, 16, 32));
            addImage(new Rectangle(97, 129, 16, 32));
            addImage(new Rectangle(114, 129, 16, 32));
            addImage(new Rectangle(131, 129, 16, 32));
        }
    }
    
    public int getCoin()
    {
        return coin;
    }
    
    public int getMarioSize()
    {
        return marioSize;
    }
    
}
