package org.example.SuperMarioBros.Model;

import java.awt.*;

/**
 * TurtleShell represents the shell of turtle enemy which appears when
 * mario jumps on a turtle.
 * This shell enables mario to kill enemies.
 * It holds the class details relevant in our context.
 */
public class TurtleShell extends FixedGameObject
{
    //-1 = moving left, 0 = fixed on position, 1 = moving right
    private int moving;
    private int speed;
    
    public TurtleShell(int startX, int startY, boolean isGreen)
    {
        super(startX, startY, "enemies.png", isGreen ?
                new Rectangle(161, 15, 14, 14) :
                new Rectangle(161, 79, 14, 14));
        moving = 0;
        speed = 5;
        enableGravity();
    }
    
    @Override
    public void update()
    {
        if (visible != 0)
        {
            destroy();
            return;
        }
        super.update();
        move(moving * speed, 0);
    }
    
    @Override
    public void onCollision(GameObject other)
    {
        super.onCollision(other);
        if (other instanceof Wall)
        {
            if (!isAbove(other))
            {
                //go back
                move(-moving * speed, 0);
                moving = -moving;
            }
        }
    }
    
    /**
     * initiates shooting mode
     *
     * @param goRight 1 = right, 0 = fixed, -1 = left
     */
    public void shootShell(int goRight)
    {
        if (goRight > 1)
            goRight = 1;
        if (goRight < -1)
            goRight = -1;
        moving = goRight;
    }
    
    public int getMoving()
    {
        return moving;
    }
}
