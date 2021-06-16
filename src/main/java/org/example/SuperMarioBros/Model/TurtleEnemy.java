package org.example.SuperMarioBros.Model;

import java.awt.*;

/**
 * TurtleEnemy represents one kind of enemy .
 * It holds the class details relevant in our context.
 */
public class TurtleEnemy extends Enemy
{
    private boolean isGreen;
    
    public TurtleEnemy(int startX, int startY, boolean isGreen)
    {
        super(startX, startY);
        this.isGreen = isGreen;
        if (isGreen)
        {
            addImage(new Rectangle(96, 6, 16, 25));
            addImage(new Rectangle(96, 6, 16, 25));
        }
        else
        {
            addImage(new Rectangle(96, 71, 16, 25));
            addImage(new Rectangle(96, 71, 16, 25));
        }
        enableGravity();
    }
    
    @Override
    public void destroy()
    {
        super.destroy();
        addScore(200);
        GameState.getMap().addItem(new TurtleShell(getX(), getY(), isGreen));
    }
}
