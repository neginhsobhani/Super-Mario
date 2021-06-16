package org.example.SuperMarioBros.Model;

import java.awt.*;

/**
 * SolidWall class represents a kind of wall in game.
 * It holds the SolidWall wall details in our context.
 */
public class SolidWall extends Wall
{
    
    public SolidWall(int startX, int startY)
    {
        super(startX, startY, "tiles.png", new Rectangle(0, 16, 16, 16));
    }
}
