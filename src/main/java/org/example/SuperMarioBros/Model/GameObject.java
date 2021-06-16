package org.example.SuperMarioBros.Model;

import java.awt.*;
import java.io.Serializable;

/**
 * GameObject represents all objects such as player,enemy
 * and walls in super mario game.
 * It holds the class details in our context.
 */
public abstract class GameObject implements Serializable
{
    
    private int locX, locY;
    private int id;
    private Rectangle collider;
    //gravity stuff
    private int lastMoveX;
    private float velY, gravityMagnitude;
    private boolean gravity;
    private boolean jump;
    //-1 : left of screen, 0 = in the screen, 1 = right of the screen
    public int visible;
    
    public GameObject(int startX, int startY)
    {
        //set the game object location
        locX = startX;
        locY = startY;
        id = 0;
        gravity = jump = false;
        gravityMagnitude = 0.5f;
        lastMoveX = 0;
        visible = 1;
        //set the collider bounds later (when image loads)
    }
    
    public int getLastMoveX()
    {
        return lastMoveX;
    }
    
    public void setLastMoveX(int lastMoveX)
    {
        this.lastMoveX = lastMoveX;
    }
    
    public int getId()
    {
        return id;
    }
    
    protected void setCollider(int width, int height)
    {
        if (width == 0 || height == 0)
        {
            collider = null;
            return;
        }
        
        collider = new Rectangle(locX, locY, width, height);
    }
    
    /**
     * checks if two game objects are colliding
     *
     * @param other the second game object
     * @return true if colliding
     */
    public boolean collide(GameObject other)
    {
        //make sure the game objects have a collider
        if (this.collider == null || other.collider == null)
            return false;
        //if the colliders collide
        if (collider.intersects(other.collider))
            return true;
        return false;
    }
    
    public Rectangle getCollider()
    {
        return collider;
        //immutable implementation
        //return new Rectangle(collider.x, collider.y, collider.width, collider.height);
    }
    
    public void enableGravity()
    {
        gravity = true;
    }
    
    /**
     * changes the gravity magnitude, be cautious, any uncontrolled change
     * in the default gravity may cause vibration later!
     *
     * @param amount new gravity magnitude value
     */
    public void setGravityMagnitude(float amount)
    {
        gravityMagnitude = amount;
    }
    
    public void disableGravity()
    {
        gravity = false;
        velY = 0;
    }
    
    /**
     * jumps if the game object is on the ground
     *
     * @param power jump power
     * @return true if jumped successfully (on the ground)
     */
    public boolean jump(float power)
    {
        jump = true;
        //somehow equals to when game object is on the ground
        if (Math.abs(velY) < gravityMagnitude * 4)
        {
            velY = -power;
            return true;
        }
        return false;
    }
    
    /**
     * jumps even if the game object is not on the ground
     *
     * @param power jump power
     */
    public void forceJump(float power)
    {
        jump = true;
        velY = -power;
    }
    
    public boolean isJumping()
    {
        return jump;
    }
    
    /**
     * moves the game object
     *
     * @param x how many pixels to move on x-axis (can be negative)
     * @param y how many pixels to move on y-axis (can be negative)
     */
    public void move(int x, int y)
    {
        locX += x;
        locY += y;
        if (collider != null)
            collider.translate(x, y);
        //for wall collision horizontally
        lastMoveX = x;
    }
    
    public void setPosition(int x, int y)
    {
        locX = x;
        locY = y;
        collider = new Rectangle(locX, locY, getWidth(), getHeight());
    }
    
    public int getX()
    {
        return locX;
    }
    
    public int getY()
    {
        return locY;
    }
    
    /**
     * update is called once per frame, almost <<< ALL >>>  children which
     * override this function should call super.update()
     */
    public void update()
    {
        checkDeadZone();
        if (gravity)
        {
            
            //effect of velocity on position ( y = vt )
            int deltaY = (int) velY;
            locY += deltaY;
            if (collider != null)
                collider.translate(0, deltaY);
            
            //effect of gravity acceleration on velocity ( v = gt )
            velY += gravityMagnitude;
        }
    }
    
    /**
     * onCollision is called when two colliders intersect, almost <<< ALL >>>  children which
     * override this function should call super.onCollision(other)
     */
    public void onCollision(GameObject other)
    {
        if (gravity)
        {
            if (other instanceof Wall)
            {
                double f;
                if (velY <= 0)
                    f = Math.floor(velY);
                else
                    //prevent vibrating on the ground (IT'S A MAGIC EQUATION! don't know how it works!)
                    if (velY == gravityMagnitude * 4)
                    {
                        //go on the surface (custom movement instead of using f variable)
                        f = 0;
                        int newY = other.getY() - getHeight();
                        locY = newY;
                        if (collider != null)
                            collider.y = newY;
                    }
                    else
                    {
                        f = velY;
                    }
                
                int deltaY = (int) (-f);
                locY += deltaY;
                locX -= lastMoveX;
                if (collider != null)
                    collider.translate(-lastMoveX, deltaY);
                if (isAbove(other) || isUnder(other))
                {
                    velY = velY / 10;
                    if (Math.abs(velY) < gravityMagnitude)
                        velY = 0;
                }
                else
                {
                    locY -= deltaY;
                    if (collider != null)
                        collider.translate(0, -deltaY);
                }
                
                if (jump && isAbove(other))
                {
                    jump = false;
                }
            }
        }
    }
    
    protected void goLastMove()
    {
        int deltaY = (int) -velY;
        locY += deltaY;
        locX -= lastMoveX;
        if (collider != null)
            collider.translate(-lastMoveX, deltaY);
    }
    
    //game object is under the other object
    public boolean isUnder(GameObject other)
    {
        if (other.getY() + (0.95f * other.getHeight()) < getY())
            return true;
        return false;
    }
    
    //game object is above the other object
    public boolean isAbove(GameObject other)
    {
        if (getY() + (0.95f * getHeight()) < other.getY())
            return true;
        return false;
    }
    
    private void checkDeadZone()
    {
        if (locY > GameState.getMap().getDeadZoneY())
        {
            visible = -1;
            destroy();
        }
    }
    
    public abstract int getWidth();
    
    public abstract int getHeight();
    
    public void destroy()
    {
        id = -1;
        collider = null;
    }
    
    public void setVisible(int visible)
    {
        if (visible > 0)
            this.visible = 1;
        else if (visible < 0)
            this.visible = -1;
        else
            this.visible = 0;
    }
    
    /*
    public int getVisible()
    {
        return visible;
    }
     */
    public void applyMarioSize()
    {

    }
}
