package org.example.SuperMarioBros.Model;


import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Map class represents the map of each level of Super Mario Game
 * which contains all the game objects.
 * It holds the class details relevant in our context.
 */
public class Map implements Serializable
{
    //fixed number for serialization, don't change this
    private static final long serialVersionUID = 4L;
    
    public Player player;
    public ArrayList<Enemy> enemies;
    public ArrayList<Wall> walls;
    public ArrayList<GameObject> items;
    public ArrayList<BackGroundObject> backGroundObjects;
    private int deadZoneY;
    
    //network
    private ArrayList<Integer> enemyDeletedIndexes = new ArrayList<>();
    private ArrayList<Integer> wallDeletedIndexes = new ArrayList<>();
    private ArrayList<Integer> itemDeletedIndexes = new ArrayList<>();
    
    public Map()
    {
        enemies = new ArrayList<>();
        walls = new ArrayList<>();
        items = new ArrayList<>();
        backGroundObjects = new ArrayList<>();
        //no limit at first
        deadZoneY = 9999;
    }
    
    public void saveMap(int level)
            throws IOException
    {
        File file = new File("Files/map/" + level);
        file.createNewFile();
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        
        objectOutputStream.writeObject(this);
        
        objectOutputStream.close();
        fileOutputStream.close();
    }
    
    public static Map loadMap(int level)
            throws IOException, ClassNotFoundException
    {
        File file = new File("Files/map/" + level);
        if (!file.exists())
            return null;
        FileInputStream fileInputStream = new FileInputStream(file);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        
        Map res = (Map) objectInputStream.readObject();
        
        objectInputStream.close();
        fileInputStream.close();
        return res;
    }
    
    public void setDeadZoneY(int deadZoneY)
    {
        this.deadZoneY = deadZoneY;
    }
    
    public void addEnemy(Enemy enemy)
    {
        enemies.add(enemy);
    }
    
    public void addWall(Wall wall)
    {
        walls.add(wall);
    }
    
    public void addItem(GameObject item)
    {
        items.add(item);
    }
    
    public void addBackGroundObject(BackGroundObject backGroundObject)
    {
        backGroundObjects.add(backGroundObject);
    }
    
    public void removeEnemy(Enemy enemy)
    {
        enemies.remove(enemy);
    }
    
    public void removeItem(Wall wall)
    {
        walls.remove(wall);
    }
    
    public void removeItem(GameObject item)
    {
        items.remove(item);
    }
    
    public void setPlayer(Player player)
    {
        this.player = player;
    }
    
    public Player getPlayer()
    {
        return player;
    }
    
    public void update()
    {
        player.update();
        
        //Enemies
        for (int i = enemies.size() - 1; i >= 0; i--)
        {
            Enemy enemy = enemies.get(i);
            if(enemy.visible > 0)
                continue;
            enemy.update();
            checkEnemyCollision(enemy);
            if (player.collide(enemy))
            {
                player.onCollision(enemy);
            }
            if (enemy.getId() == -1)
            {
                enemyDeletedIndexes.add(i);
                enemies.remove(i);
            }
        }
        
        //Walls
        for (int i = walls.size() - 1; i >= 0; i--)
        {
            Wall wall = walls.get(i);
            if(wall.visible > 0)
                continue;
            wall.update();
            if (player.collide(wall))
            {
                player.onCollision(wall);
            }
            if (wall.getId() == -1)
            {
                wallDeletedIndexes.add(i);
                walls.remove(i);
            }
        }
        
        //Items
        for (int i = items.size() - 1; i >= 0; i--)
        {
            GameObject item = items.get(i);
            if(item.visible > 0)
                continue;
            checkItemCollision(item);
            item.update();
            if (player.collide(item))
            {
                player.onCollision(item);
            }
            if (item.getId() == -1)
            {
                itemDeletedIndexes.add(i);
                items.remove(i);
            }
        }
        
    }
    
    
    private void checkEnemyCollision(Enemy enemy)
    {
        if (enemy.visible == 0)
        {
            
            for (Wall wall : walls)
            {
                if (enemy.collide(wall))
                {
                    enemy.onCollision(wall);
                }
            }
            
            for (Enemy otherEnemy : enemies)
            {
                if (!enemy.equals(otherEnemy))
                {
                    if (enemy.collide(otherEnemy))
                    {
                        enemy.onCollision(otherEnemy);
                        otherEnemy.onCollision(enemy);
                    }
                }
            }
            
            for (GameObject item : items)
            {
                if (item instanceof FireBall || item instanceof TurtleShell)
                {
                    if (enemy.collide(item))
                    {
                        enemy.onCollision(item);
                    }
                }
            }
            
        }
    }
    
    private void checkItemCollision(GameObject item)
    {
        
        if (item instanceof MushroomGrowth)
        {
            //check wall collision
            for (Wall wall : walls)
            {
                if (item.collide(wall))
                {
                    //the order is important
                    
                    //a bumping wall collides a mushroom power up
                    wall.onCollision(item);
                    item.onCollision(wall);
                }
            }
        }
        
        else if (item instanceof CoinObject)
        {
            for (Wall wall : walls)
            {
                if (item.collide(wall))
                {
                    //a bumping wall collides a fixed coin object
                    wall.onCollision(item);
                }
            }
        }
        
        else if (item instanceof FireBall || item instanceof TurtleShell)
        {
            for (Wall wall : walls)
            {
                if (item.collide(wall))
                {
                    item.onCollision(wall);
                }
            }
        }
        
    }
    
    public int getDeadZoneY()
    {
        return deadZoneY;
    }
    
    public ArrayList<Integer> getEnemyDeletedIndexes()
    {
        return enemyDeletedIndexes;
    }
    
    public void setEnemyDeletedIndexes(ArrayList<Integer> enemyDeletedIndexes)
    {
        this.enemyDeletedIndexes = enemyDeletedIndexes;
    }
    
    public ArrayList<Integer> getWallDeletedIndexes()
    {
        return wallDeletedIndexes;
    }
    
    public void setWallDeletedIndexes(ArrayList<Integer> wallDeletedIndexes)
    {
        this.wallDeletedIndexes = wallDeletedIndexes;
    }
    
    public ArrayList<Integer> getItemDeletedIndexes()
    {
        return itemDeletedIndexes;
    }
    
    public void setItemDeletedIndexes(ArrayList<Integer> itemDeletedIndexes)
    {
        this.itemDeletedIndexes = itemDeletedIndexes;
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    public void setEnemies(ArrayList<Enemy> enemies) {
        this.enemies = enemies;
    }

    public ArrayList<Wall> getWalls() {
        return walls;
    }

    public void setWalls(ArrayList<Wall> walls) {
        this.walls = walls;
    }

    public ArrayList<GameObject> getItems() {
        return items;
    }

    public void setItems(ArrayList<GameObject> items) {
        this.items = items;
    }
}
