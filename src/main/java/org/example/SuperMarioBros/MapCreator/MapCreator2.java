package org.example.SuperMarioBros.MapCreator;

import org.example.SuperMarioBros.Model.*;

import java.util.Random;

/**
 * MapCreator2 represents the map of the second level of Super Mario game.
 * It holds the class details relevant in our context.
 */
public class MapCreator2
{
    private Map map;
    
    public MapCreator2(Map map)
    {
        this.map = map;
        //set dead zone y
        map.setDeadZoneY(g(17));
        //player
        map.setPlayer(new Player(g(4), g(14)));
        //other components
        addEnemies();
        addWalls();
        addItems();
        addBackGroundObjects();
    }
    
    /**
     * grid
     *
     * @param v value
     * @return v-1 * 16
     */
    private int g(int v)
    {
        //screen is 16x16 of g(1)
        return (v - 1) * 16;
    }

    private void addBackGroundObjects()
    {
        addbg(1, 14, BackGroundObject.bush2);
        addbg(11, 14, BackGroundObject.bush2);
        addbg(10, 14, BackGroundObject.bush2);
        addbg(15, 14, BackGroundObject.bush2);
        addbg(20, 14, BackGroundObject.bush2);
        addbg(42, 14, BackGroundObject.bush2);
        addbg(48, 14, BackGroundObject.bush2);
        addbg(60, 14, BackGroundObject.bush2);
        addbg(59, 14, BackGroundObject.bush2);
        addbg(69, 14, BackGroundObject.bush2);

        addbg(87, 14, BackGroundObject.bush2);
        addbg(92, 14, BackGroundObject.bush2);
        addbg(102, 14, BackGroundObject.bush2);
        addbg(105, 14, BackGroundObject.bush2);
        addbg(115, 14, BackGroundObject.bush2);

        addbg(132, 14, BackGroundObject.bush2);
        addbg(136, 14, BackGroundObject.bush2);

        addbg(140, 14, BackGroundObject.bush2);
        addbg(152, 14, BackGroundObject.bush2);

        addbg(160, 14, BackGroundObject.bush2);


        addbg(190, 14, BackGroundObject.bush2);
        for (int i = 1; i <= 220; i++)
            if (i != 66 && i != 67 && i != 83 && i != 84 && i != 85 && i != 147 && i != 148)
                addbg(i, 16, BackGroundObject.stoneWall);
        Random rnd = new Random();
        for (int i = 0; i < 200; i += 2)
        {
            if (rnd.nextBoolean() && rnd.nextBoolean() && rnd.nextBoolean())
                addbg(i, rnd.nextInt(4) + 2, BackGroundObject.cloud2);

        }

    }
    
    private void addbg(int xg, int yg, int magic)
    {
        map.addBackGroundObject(new BackGroundObject(g(xg), g(yg), magic));
    }
    
    private void addItems()
    {
        map.addItem(new Princess(g(199), g(14)));

        //flower on the breakable walls
        map.addItem(new FlowerGrowth(g(86), g(7)));


        addCoin(22, 10);

        addCoin(37, 14);

        addCoin(40, 14);

        addCoin(41, 7);
        addCoin(42, 7);
        addCoin(43, 7);

        addCoin(66, 10);

        addCoin(74, 5);

        addCoin(79, 2);
        addCoin(80, 2);
        addCoin(81, 2);

        addCoin(88, 2);
        addCoin(89, 2);
        addCoin(90, 2);

        //above the coinwall power up
        addCoin(103, 2);

        addCoin(119, 2);
        addCoin(120, 2);

        //between solid walls
        addCoin(132, 14);
        addCoin(133, 14);

        //fake to die
        addCoin(147, 12);
        addCoin(148, 12);


        //can't get them all!
        addCoin(188, 4);
        addCoin(189, 4);
        addCoin(190, 4);
        addCoin(188, 5);
        addCoin(189, 5);
        addCoin(190, 5);
        addCoin(188, 6);
        addCoin(189, 6);
        addCoin(190, 6);
    }
    private void addCoin(int x, int y)
    {
        map.addItem(new CoinObject(g(x), g(y)));
    }
    
    private void addWalls()
    {
        for (int i = 1; i <= 220; i++)
            if (i != 66 && i != 67 && i != 83 && i != 84 && i != 85 && i != 147 && i != 148)
                map.addWall(new PlatformWall(g(i), g(15)));

        map.addWall(new BreakableWall(g(20), g(11)));
        map.addWall(new CoinWall(g(21), g(11), CoinWall.POWERUP));
        map.addWall(new BreakableWall(g(22), g(11)));
        map.addWall(new CoinWall(g(23), g(11)));
        map.addWall(new BreakableWall(g(24), g(11)));
        map.addWall(new CoinWall(g(22), g(7)));
        map.addWall(new PipeWall(g(28), g(13), 1));
        map.addWall(new PipeWall(g(38), g(12), 2));
        map.addWall(new PipeWall(g(46), g(11), 3));
//        added later for dest pipe wall
//        map.addWall(new PipeWall(g(56), g(11), 3));
        PipeWall destPipe;


        map.addWall(new BreakableWall(g(74), g(11)));
        map.addWall(new CoinWall(g(75), g(11), CoinWall.POWERUP));
        map.addWall(new BreakableWall(g(76), g(11)));

        //breakable wall with hidden item
        map.addWall(new BreakableWall(g(78), g(7), 3));
        for (int i = 77; i < 77 + 6; i++)
            if (i != 78)
                map.addWall(new BreakableWall(g(i), g(7)));

        //breakable wall with hidden item
        map.addWall(new BreakableWall(g(86), g(7), 3));
        map.addWall(new BreakableWall(g(87), g(7)));
        map.addWall(new CoinWall(g(88), g(7), 3));
        //breakable wall with hidden item
        map.addWall(new BreakableWall(g(88), g(11), 5));

        map.addWall(new BreakableWall(g(94), g(11)));
        map.addWall(new BreakableWall(g(95), g(11)));
        map.addWall(new CoinWall(g(100), g(11)));
        map.addWall(new CoinWall(g(103), g(11)));
        map.addWall(new CoinWall(g(103), g(7), CoinWall.POWERUP));
        map.addWall(new CoinWall(g(106), g(11)));
        map.addWall(new BreakableWall(g(112), g(11)));
        map.addWall(new BreakableWall(g(115), g(7)));
        map.addWall(new BreakableWall(g(116), g(7)));
        map.addWall(new BreakableWall(g(117), g(7)));
        //breakable wall with item
        map.addWall(new BreakableWall(g(122), g(7), 3));
        map.addWall(new CoinWall(g(123), g(7)));
        map.addWall(new BreakableWall(g(123), g(11)));
        map.addWall(new BreakableWall(g(124), g(11)));
        map.addWall(new CoinWall(g(124), g(7)));
        map.addWall(new BreakableWall(g(125), g(7)));

        map.addWall(new SolidWall(g(131), g(11)));
        map.addWall(new SolidWall(g(131), g(12)));
        map.addWall(new SolidWall(g(130), g(12)));
        map.addWall(new SolidWall(g(131), g(13)));
        map.addWall(new SolidWall(g(130), g(13)));
        map.addWall(new SolidWall(g(129), g(13)));

        map.addWall(new SolidWall(g(128), g(14)));
        map.addWall(new SolidWall(g(129), g(14)));
        map.addWall(new SolidWall(g(130), g(14)));
        map.addWall(new SolidWall(g(131), g(14)));


        //between solid walls


        map.addWall(new SolidWall(g(134), g(14)));
        map.addWall(new SolidWall(g(135), g(14)));
        map.addWall(new SolidWall(g(136), g(14)));
        map.addWall(new SolidWall(g(137), g(14)));

        map.addWall(new SolidWall(g(134), g(13)));
        map.addWall(new SolidWall(g(135), g(13)));
        map.addWall(new SolidWall(g(136), g(13)));

        map.addWall(new SolidWall(g(134), g(12)));
        map.addWall(new SolidWall(g(135), g(12)));

        map.addWall(new SolidWall(g(134), g(11)));


        map.addWall(new SolidWall(g(142), g(14)));
        map.addWall(new SolidWall(g(143), g(14)));
        map.addWall(new SolidWall(g(144), g(14)));
        map.addWall(new SolidWall(g(145), g(14)));
        map.addWall(new SolidWall(g(146), g(14)));


        map.addWall(new SolidWall(g(143), g(13)));
        map.addWall(new SolidWall(g(144), g(13)));
        map.addWall(new SolidWall(g(145), g(13)));
        map.addWall(new SolidWall(g(146), g(13)));

        map.addWall(new SolidWall(g(144), g(12)));
        map.addWall(new SolidWall(g(145), g(12)));
        map.addWall(new SolidWall(g(146), g(12)));

        map.addWall(new SolidWall(g(145), g(11)));
        map.addWall(new SolidWall(g(146), g(11)));

        //hole between walls here

        map.addWall(new SolidWall(g(149), g(11)));

        map.addWall(new SolidWall(g(149), g(12)));
        map.addWall(new SolidWall(g(150), g(12)));

        map.addWall(new SolidWall(g(149), g(13)));
        map.addWall(new SolidWall(g(150), g(13)));
        map.addWall(new SolidWall(g(151), g(13)));

        map.addWall(new SolidWall(g(149), g(14)));
        map.addWall(new SolidWall(g(150), g(14)));
        map.addWall(new SolidWall(g(151), g(14)));
        map.addWall(new SolidWall(g(152), g(14)));

        map.addWall(destPipe = new PipeWall(g(157), g(13), 1));

        //breakable wall with hidden item
        map.addWall(new BreakableWall(g(162), g(11), 3));
        map.addWall(new BreakableWall(g(163), g(11)));
        map.addWall(new CoinWall(g(164), g(11)));
        map.addWall(new BreakableWall(g(165), g(11)));

        //destination pipe
        map.addWall(new PipeWall(g(175), g(13), 1));

        for (int i = 177; i < 177 + 9; i++)
            map.addWall(new SolidWall(g(i), g(14)));
        for (int i = 178; i < 177 + 9; i++)
            map.addWall(new SolidWall(g(i), g(13)));
        for (int i = 179; i < 177 + 9; i++)
            map.addWall(new SolidWall(g(i), g(12)));
        for (int i = 180; i < 177 + 9; i++)
            map.addWall(new SolidWall(g(i), g(11)));
        for (int i = 181; i < 177 + 9; i++)
            map.addWall(new SolidWall(g(i), g(10)));
        for (int i = 182; i < 177 + 9; i++)
            map.addWall(new SolidWall(g(i), g(9)));
        for (int i = 183; i < 177 + 9; i++)
            map.addWall(new SolidWall(g(i), g(8)));
        for (int i = 184; i < 177 + 9; i++)
            map.addWall(new SolidWall(g(i), g(7)));


        map.addWall(new SolidWall(g(195), g(14)));


        //source pipe wall
        map.addWall(new PipeWall(g(56), g(11), 3, destPipe));
    }
    
    private void addEnemies()
    {
        map.addEnemy(new TurtleEnemy(g(22), g(14),true));
        map.addEnemy(new MushroomEnemy(g(30), g(14)));


        map.addEnemy(new MushroomEnemy(g(42), g(14)));
        map.addEnemy(new MushroomEnemy(g(44), g(14)));
        //2 enemies on breakable walls
        map.addEnemy(new MushroomEnemy(g(75), g(10)));
        map.addEnemy(new HedgeHogEnemy(g(78), g(6)));

        map.addEnemy(new MushroomEnemy(g(96), g(14)));
        map.addEnemy(new TurtleEnemy(g(98), g(14),false));
    
        map.addEnemy(new TurtleEnemy(g(108), g(14),true));
        map.addEnemy(new TurtleEnemy(g(112), g(14),false));
        map.addEnemy(new HedgeHogEnemy(g(110), g(14)));

        map.addEnemy(new HedgeHogEnemy(g(118), g(14)));
        map.addEnemy(new TurtleEnemy(g(120), g(14),true));


        map.addEnemy(new MushroomEnemy(g(122), g(14)));
        map.addEnemy(new MushroomEnemy(g(124), g(14)));
        map.addEnemy(new MushroomEnemy(g(127), g(14)));

        map.addEnemy(new MushroomEnemy(g(169), g(14)));
        map.addEnemy(new MushroomEnemy(g(171), g(14)));
    }
}

