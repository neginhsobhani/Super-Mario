package org.example.SuperMarioBros.Model;

import java.awt.*;

/**
 * PipeWall class represents the piped shape walls in game.
 * It holds the class details relevant in our context.
 */
public class PipeWall extends Wall
{
    private static final Rectangle
            size1Rect = new Rectangle(0, 0, 32, 32),
            size2Rect = new Rectangle(32, 0, 32, 48),
            size3Rect = new Rectangle(64, 0, 32, 64);
    
    private PipeWall dest;
    
    /**
     * creates a normal pipe wall
     *
     * @param startX x
     * @param startY y
     * @param size   size
     */
    public PipeWall(int startX, int startY, int size)
    {
        super(startX, startY, "pipes.png",
                switch (size)
                        {
                            case 2:
                                yield size2Rect;
                            case 3:
                                yield size3Rect;
                                //case 1:
                            default:
                                yield size1Rect;
                        }
        );
    }
    
    /**
     * creates a teleporting pipe wall
     *
     * @param startX x
     * @param startY y
     * @param size   size
     * @param dest   the destination pipe wall in teleporting
     */
    public PipeWall(int startX, int startY, int size, PipeWall dest)
    {
        super(startX, startY, "pipes.png",
                switch (size)
                        {
                            case 2:
                                yield size2Rect;
                            case 3:
                                yield size3Rect;
                                //case 1:
                            default:
                                yield size1Rect;
                        }
        );
        this.dest = dest;
    }
    
    public PipeWall getDestination()
    {
        return dest;
    }
}
