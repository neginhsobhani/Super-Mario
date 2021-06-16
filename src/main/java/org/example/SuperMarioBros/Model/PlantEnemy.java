package org.example.SuperMarioBros.Model;

import java.awt.*;

/**
 * PlantEnemy represents one kind of enemy .
 * It holds the class details relevant in our context.
 */
public class PlantEnemy extends Enemy
{
    public PlantEnemy(int startX, int startY)
    {
        super(startX, startY);
        addImage(new Rectangle(192, 8, 16, 24));
        addImage(new Rectangle(208, 8, 16, 24));
    }
    
    @Override
    public void destroy()
    {
        //do nothing
    }
}
