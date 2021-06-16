package org.example.SuperMarioBros.Model;

import org.example.SuperMarioBros.Controller.ExceptionHandler;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * AnimatedGameObject represents the game objects which have more than
 * one image and their image changes while moving.
 * It holds the class details relevant in our context.
 */
public abstract class AnimatedGameObject extends GameObject implements Drawable
{
    private ArrayList<ImageIcon> images;
    private String imageFileName;
    private int currentImageIndex;
    private int animInterval, animFrameCount;
    
    public AnimatedGameObject(int startX, int startY, String imageFileName)
    {
        super(startX, startY);
        images = new ArrayList<>();
        currentImageIndex = -1;
        animFrameCount = 0;
        animInterval = 1;
        //get the image
        try
        {
            File imageFile = new File("Files/image/" + imageFileName);
            if (!imageFile.exists())
                throw new IOException("image file " + imageFileName + " could not be loaded!");
            this.imageFileName = imageFileName;
        }
        catch (IOException e)
        {
            new ExceptionHandler(e);
            System.exit(0);
        }
    }
    
    protected ImageIcon loadImage(Rectangle crop)
    {
        
        File imageFile = new File("Files/image/" + imageFileName);
        try
        {
            if (!imageFile.exists())
                throw new IOException("image file " + imageFileName + " could not be loaded!");
            BufferedImage bufferedImage = ImageIO.read(imageFile);
            bufferedImage = bufferedImage.getSubimage(crop.x, crop.y, crop.width, crop.height);
            return new ImageIcon(bufferedImage);
        }
        catch (IOException e)
        {
            new ExceptionHandler(e);
            System.exit(0);
        }
        return null;
    }
    
    /**
     * adds an image to the animation from the same source file
     */
    protected void addImage(Rectangle crop)
    {
        ImageIcon image = loadImage(crop);
        images.add(image);
        if (images.size() == 1)
        {
            currentImageIndex = 0;
            setCollider(getWidth(), getHeight());
        }
    }
    
    protected void goNextImage()
    {
        if (animFrameCount++ < animInterval)
            return;
        animFrameCount = 0;
        if (currentImageIndex == -1)
        {
            new ExceptionHandler(new Exception("No images found in animated game object"));
            return;
        }
        currentImageIndex = (currentImageIndex + 1) % images.size();
    }
    
    /**
     * set how frequent the animation changes (i=0 : every frame) (i=1 : 1 frame in between)
     *
     * @param i animation change interval
     */
    protected void setAnimationInterval(int i)
    {
        if (i < 0)
            i = 0;
        animInterval = i;
    }
    
    protected void goFirstImage()
    {
        if (currentImageIndex == -1)
        {
            new ExceptionHandler(new Exception("No images found in animated game object"));
            return;
        }
        currentImageIndex = 0;
    }
    
    protected void resetImages()
    {
        images = new ArrayList<>();
    }
    
    @Override
    public ImageIcon getImage()
    {
        if (currentImageIndex == -1)
        {
            new ExceptionHandler(new Exception("No images found in animated game object"));
            return null;
        }
        return images.get(currentImageIndex);
    }
    
    public int getWidth()
    {
        return getImage().getIconWidth();
    }
    
    public int getHeight()
    {
        return getImage().getIconHeight();
    }
}
