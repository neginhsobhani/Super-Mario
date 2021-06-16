package org.example.SuperMarioBros.Model;

import java.awt.*;

/**
 * BackGroundObject represents the objects which are used for showing the
 * background of the map and don't have much functionality.
 * It holds the class details relevant in our context.
 */
public class BackGroundObject extends FixedGameObject
{
    public static final int
            cloud1 = 0,
            cloud2 = 1,
            bush1 = 2,
            bush2 = 3,
            stoneWall = 4; //platform wall
    
    public BackGroundObject(int startX, int startY, int model)
    {
        super(startX, startY, "tiles.png",
                switch (model)
                        {
                            //TODO magic constants
                            case cloud2:
                                //level 2 cloud
                                yield new Rectangle(6, 384, 35, 25);
                            case bush1:
                                //level 1 bush
                                yield new Rectangle(184, 144, 33, 17);
                            case bush2:
                                //level 2 bush - orange
                                yield new Rectangle(184, 208, 33, 16);
                            case stoneWall:
                                //level 1 stone wall
                                yield new Rectangle(0, 0, 16, 16);
                            default:
                                //level 1 cloud
                                yield new Rectangle(6, 353, 35, 25);
                            
                        }
        );
    }
}
