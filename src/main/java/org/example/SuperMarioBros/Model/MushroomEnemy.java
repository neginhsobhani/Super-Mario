package org.example.SuperMarioBros.Model;

import org.example.SuperMarioBros.Controller.AudioPlayer;

import javax.swing.*;
import java.awt.*;

/**
 * MushroomEnemy represents one kind of enemy .
 * It holds the class details relevant in our context.
 */
public class MushroomEnemy extends Enemy
{
    private ImageIcon squished;
    private int deathAnim;
    private int deathAnimTotal;
    
    public MushroomEnemy(int startX, int startY)
    {
        super(startX, startY);
        addImage(new Rectangle(0, 16, 16, 16));
        addImage(new Rectangle(16, 16, 16, 16));
        deathAnim = -1;
        deathAnimTotal = 20;
        enableGravity();
    }
    
    @Override
    public void update()
    {
        if(deathAnim > -1)
        {
            if(deathAnim < deathAnimTotal)
                deathAnim++;
            else
                super.destroy();
        }
        else
            super.update();
    }
    
    @Override
    public void destroy()
    {
        //super.destroy();
        resetImages();
        addImage(new Rectangle(32,16,16,16));
        deathAnim = 0;
        disableGravity();
        setCollider(0,0);
        addScore(100);
        AudioPlayer.play(AudioPlayer.Kill);
    }
}
