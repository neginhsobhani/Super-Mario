package org.example.SuperMarioBros.Model;

import java.awt.*;

/**
 * MushroomGrowth class represents a power up which helps mario
 * increase in size and also gives him more abilities.
 * It holds the class details relevant in our context.
 */
public class MushroomGrowth extends FixedGameObject
{
    private int yAxisAnimation;
    private boolean isRight;
    private final int speed;
    private Wall bumpingWall;
    
    public MushroomGrowth(int startX, int startY)
    {
        super(startX, startY, "objects.png", new Rectangle(0, 0, 16, 16));
        
        yAxisAnimation = 0;
        isRight = true;
        //moves faster than mario
        speed = 3;
        setGravityMagnitude(0.8f);
    }
    
    @Override
    public void update()
    {
        if (yAxisAnimation < getHeight())
        {
            yAxisAnimation += 1;
            move(0, -1);
        }
        else
        {
            enableGravity();
            if (isRight)
                move(speed, 0);
            else
                move(-speed, 0);
        }
        super.update();
        
    }
    
    @Override
    public void onCollision(GameObject other)
    {
        super.onCollision(other);
        
        if (other.equals(bumpingWall))
        {
            while (collide(other))
                move(0, -1);
            return;
        }
        //the method is actually only called when other is instance of Wall
        if (other instanceof Wall)
        {
            //on side collisions (no need to check isUnder as the mushroom doesn't jump)
            if (!isAbove(other) && !other.equals(bumpingWall))
                isRight = !isRight;
        }
    }
    
    /**
     * on collision to a bumping wall, this is called
     */
    public void jumpFlip(Wall other)
    {
        if (bumpingWall != null)
            return;
        bumpingWall = other;
        isRight = !isRight;
        jump(5);
    }
    
    // called by wall
    public void clearBumpingWall()
    {
        bumpingWall = null;
    }
}
