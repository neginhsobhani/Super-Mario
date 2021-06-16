package org.example.SuperMarioBros.Model;

import java.awt.*;

/**
 * HedgeHogEnemy represents one kind of enemy .
 * It holds the class details relevant in our context.
 */
public class HedgeHogEnemy extends Enemy
{
    public HedgeHogEnemy(int startX, int startY)
    {
        super(startX, startY);
        addImage(new Rectangle(480, 82, 14, 14));
        addImage(new Rectangle(496, 83, 14, 14));
    }
    
    
    @Override
    public void destroy()
    {
        super.destroy();
        addScore(500);
    }
}
