package org.example.SuperMarioBros.Model;

import java.awt.*;

/**
 * Wall class represents different kind of walls in super mario game.
 * It holds the wall details relevant in our context.
 */
public class Wall extends FixedGameObject
{
    
    public Wall(int startX, int startY, String imageFileName, Rectangle crop)
    {
        super(startX, startY, imageFileName, crop);
    }
}
