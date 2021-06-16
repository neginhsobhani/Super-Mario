package org.example.SuperMarioBros.Model;

import java.awt.*;

/**
 * PlatForm wall represents a kind of wall.
 * It holds the class details relevant in our context.
 */
public class PlatformWall extends Wall
{
    public PlatformWall(int startX, int startY)
    {
        super(startX, startY, "tiles.png", new Rectangle(0, 0, 16, 16));
    }
}
