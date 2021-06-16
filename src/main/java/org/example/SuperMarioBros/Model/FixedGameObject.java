package org.example.SuperMarioBros.Model;

import org.example.SuperMarioBros.Controller.ExceptionHandler;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * FixedGameObject class represents the game objects which
 * have one image.
 * It holds the class details relevant in our context.
 */
public abstract class FixedGameObject extends GameObject implements Drawable
{
    //fixed image icon
    private ImageIcon image;
    
    public FixedGameObject(int startX, int startY, String imageFileName, Rectangle crop)
    {
        super(startX, startY);
        //get the image
        try
        {
            image = loadImage(imageFileName, crop);
        }
        catch (IOException e)
        {
            new ExceptionHandler(e);
            System.exit(0);
        }
        //set the collider bounds based on image width and height
        setCollider(image.getIconWidth(), image.getIconHeight());
    }
    
    public FixedGameObject(int startX, int startY, String imageFileName)
    {
        super(startX, startY);
        //get the image
        try
        {
            image = new ImageIcon("Files/image/" + imageFileName);
            if (image.getImage() == null)
                throw new IOException("image file " + imageFileName + " could not be loaded!");
        }
        catch (IOException e)
        {
            new ExceptionHandler(e);
            System.exit(0);
        }
        //set the collider bounds based on image width and height
        setCollider(image.getIconWidth(), image.getIconHeight());
    }
    
    protected ImageIcon loadImage(String imageFileName, Rectangle crop)
            throws IOException
    {
        ImageIcon image = new ImageIcon("Files/image/" + imageFileName);
        if (image.getImage() == null)
            throw new IOException("image file " + imageFileName + " could not be loaded!");
        BufferedImage bufferedImage = ImageIO.read(new File("Files/image/" + imageFileName));
        bufferedImage = bufferedImage.getSubimage(crop.x, crop.y, crop.width, crop.height);
        return new ImageIcon(bufferedImage);
    }
    
    public void setImage(ImageIcon image)
    {
        this.image = image;
    }
    
    public ImageIcon getImage()
    {
        return image;
    }
    
    public int getWidth()
    {
        return image.getIconWidth();
    }
    
    public int getHeight()
    {
        return image.getIconHeight();
    }
    
    
}
