package org.example.SuperMarioBros.Model;

import java.awt.*;

/**
 * FlowerGrowth class represents a power up which helps mario
 * increase in size and also gives him more abilities.
 * It holds the class details relevant in our context.
 */
public class FlowerGrowth extends AnimatedGameObject
{
    private int yAxisAnimation;
    
    public FlowerGrowth(int startX, int startY)
    {
        super(startX, startY, "objects.png");
        addImage(new Rectangle(0, 32, 16, 16));
        addImage(new Rectangle(16, 32, 16, 16));
        addImage(new Rectangle(32, 32, 16, 16));
        addImage(new Rectangle(48, 32, 16, 16));
        
        setAnimationInterval(2);
        yAxisAnimation = 0;
    }
    
    @Override
    public void update()
    {
        goNextImage();
        if (yAxisAnimation < getHeight())
        {
            yAxisAnimation += 1;
            move(0, -1);
        }
    }
}
