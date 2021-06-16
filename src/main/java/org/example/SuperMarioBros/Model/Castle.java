package org.example.SuperMarioBros.Model;

/**
 * Castle represents the castle which mario wins if
 * he reaches it.
 * It holds the class details relevant in our context.
 */
public class Castle extends FixedGameObject
{
    public Castle(int startX, int startY)
    {
        super(startX, startY, "castle.png");
    }
}
