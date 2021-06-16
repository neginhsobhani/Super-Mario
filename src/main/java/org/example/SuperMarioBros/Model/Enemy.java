package org.example.SuperMarioBros.Model;

import org.example.SuperMarioBros.Controller.AudioPlayer;

/**
 * Enemy class represents the enemies in super mario game.
 * It holds the class details relevant in our context.
 */
public class Enemy extends AnimatedGameObject
{
    private int speed;
    private boolean isLeft; //true if enemy is facing left
    private boolean isUpsideDown; //true if enemy is dead and is upside down
    
    public Enemy(int startX, int startY)
    {
        super(startX, startY, "enemies.png");
        speed = 1;
        isLeft = true;
        isUpsideDown = false;
        enableGravity();
        setAnimationInterval(3);
    }
    
    @Override
    public void onCollision(GameObject other)
    {
        super.onCollision(other);
        if (other instanceof Wall && !isAbove(other) && !isUnder(other))
        {
            flip();
            //no need to go back to prev spot, as super.onCollision does the work
        }
        else if (other instanceof Enemy)
        {
            //go back
            move(isLeft ? speed : -speed, 0);
            flip();
        }
        else if (other instanceof FireBall && !(this instanceof PlantEnemy))
        {
            flipDestroy();
            other.destroy();
        }
        else if (other instanceof TurtleShell)
        {
            TurtleShell turtleShell = (TurtleShell) other;
            if (turtleShell.getMoving() != 0)
            {
                flipDestroy();
            }
            else
            {
                //go back
                move(isLeft ? speed : -speed, 0);
                flip();
            }
        }
        
    }
    
    private void flipDestroy()
    {
        isUpsideDown = true;
        setCollider(0, 0);
        forceJump(5);
        flip();
        AudioPlayer.play(AudioPlayer.ShootKill);
    }
    
    private void flip()
    {
        isLeft = !isLeft;
    }
    
    @Override
    public void update()
    {
        //the gravity check in gameObject
        if (visible == -1)
            destroy();
        if (visible != 0)
            return;
        super.update();
        if (!isUpsideDown)
            goNextImage();
        if (isLeft)
        {
            move(-speed, 0);
        }
        else
        {
            move(speed, 0);
        }
    }
    
    public boolean isLeft()
    {
        return isLeft;
    }
    
    public boolean isUpsideDown()
    {
        return isUpsideDown;
    }
    
    /**
     * a handy tool for adding score
     *
     * @param amount amount of score
     */
    protected void addScore(int amount)
    {
        GameState.getMap().getPlayer().addScore(amount);
    }
    
    @Override
    public void destroy()
    {
        super.destroy();
        if(visible == 0 && !(this instanceof MushroomEnemy))
            AudioPlayer.play(AudioPlayer.Kill);
    }
}
