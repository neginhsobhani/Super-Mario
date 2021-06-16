package org.example.SuperMarioBros.Model;

import java.awt.*;

/**
 * Princess class represents the princess who mario
 * is trying to reach.
 * It holds the princess details relevant in our context.
 */
public class Princess extends FixedGameObject
{
    public Princess(int startX, int startY)
    {
        super(startX, startY, "objects.png", new Rectangle(49, 232, 14, 24));
    }
}
