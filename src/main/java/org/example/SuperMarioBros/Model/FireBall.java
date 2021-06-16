package org.example.SuperMarioBros.Model;

import org.example.SuperMarioBros.Controller.AudioPlayer;

import java.awt.*;

/**
 * FireBall represents the arrow which Mario shoots to kill enemies
 * It holds the class details relevant in our context.
 */
public class FireBall extends AnimatedGameObject
{
    private int speed;
    private int jumpSpeed;
    private int life;
    private int explosionAnim;
    
    public FireBall(int startX, int startY, boolean isRight)
    {
        super(startX, startY, "objects.png");
        addImage(new Rectangle(96, 144, 8, 8));
        addImage(new Rectangle(104, 144, 8, 8));
        addImage(new Rectangle(96, 152, 8, 8));
        addImage(new Rectangle(104, 152, 8, 8));
        
        setAnimationInterval(3);
        
        speed = isRight ? 6 : -6;
        jumpSpeed = 6;
        life = 4;
        visible = 0;
        explosionAnim = -1;
        enableGravity();
        setGravityMagnitude(1);
    }
    
    @Override
    public void update()
    {
        if(explosionAnim > 12)
        {
            super.destroy();
            return;
        }
        super.update();
        goNextImage();
        if(explosionAnim > -1)
            explosionAnim++;
        else
        {
            move(speed, 0);
    
            if (visible != 0)
                destroy();
        }
    }
    
    @Override
    public void onCollision(GameObject other)
    {
        if(explosionAnim > -1)
            return;
        super.onCollision(other);
        if (other instanceof Wall)
        {
            if (isAbove(other))
            {
                forceJump(jumpSpeed);
                life--;
                if (life <= 0)
                {
                    AudioPlayer.play(AudioPlayer.Firework);
                    destroy();
                }
            }
            else
            {
                //side collision or collision from under!
                AudioPlayer.play(AudioPlayer.Firework);
                destroy();
            }
        }
    }
    
    //TODO: destroy animation (boom)
    @Override
    public void destroy()
    {
//        super.destroy();
        GameState.getMap().getPlayer().decreaseActiveFireBalls();
        if(visible == 0)
            initExplosion();
        else
            super.destroy();
//        if (visible == 0)
    }
    
    
    private void initExplosion()
    {
        resetImages();
        addImage(new Rectangle(112,144,16,16));
        addImage(new Rectangle(112,161,16,16));
        addImage(new Rectangle(112,176,16,16));
        setAnimationInterval(4);
        explosionAnim = 0;
        setCollider(0,0);
        disableGravity();
    }
    
}
