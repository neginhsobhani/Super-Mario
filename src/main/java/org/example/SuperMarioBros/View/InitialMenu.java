package org.example.SuperMarioBros.View;

import org.example.SuperMarioBros.Controller.ExceptionHandler;
import org.example.SuperMarioBros.Controller.GameLoop;
import org.example.SuperMarioBros.Model.ThreadPool;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Initial Menu class represents the menu which is shown to the player(s) at first
 * It holds the class details relevant in our context
 */
public class InitialMenu extends JFrame
{
    Runnable singlePlayerStart, multiPlayerStart;
    
    public InitialMenu()
            throws IOException
    {
        super("Super Mario Bros");
        
        setLayout(null);
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e)
        {
            new ExceptionHandler(e);
        }
        //center panel
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(1, 2));
        BufferedImage myPicture = ImageIO.read(new File("Files/menuImages/" + "logo.png"));
        JLabel picLabel = new JLabel(new ImageIcon(myPicture));
        centerPanel.add(picLabel);
        centerPanel.setBackground(Color.cyan);
        setContentPane(centerPanel);
        //outer east panel
        JPanel outer = new JPanel();
        outer.setLayout(new BorderLayout());
        centerPanel.add(outer);
        outer.setOpaque(false);
        //east panel - buttonPanel
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        outer.add(panel, BorderLayout.CENTER);
        panel.setOpaque(false);
        //IP field
        JTextField ipField = new JTextField("Host IP");
        ipField.setText("127.0.0.1");
        ipField.setFont(new Font("Arial", Font.BOLD, 14));
        ipField.setPreferredSize(new Dimension(150, 50));
        ipField.setHorizontalAlignment(JTextField.CENTER);
        ipField.setMaximumSize(new Dimension(150, 50));
        ipField.setMinimumSize(new Dimension(150, 50));
        //button 1
        JButton button1 = new JButton("Single Player");
        button1.setFont(new Font("Arial", Font.BOLD, 11));
        button1.setPreferredSize(new Dimension(150, 50));
        Icon icon1 = new ImageIcon("Files/menuImages/" + "game.png");
        button1.setIcon(icon1);
        button1.addMouseListener(new MenuMouse(null));
        button1.setMaximumSize(new Dimension(150, 50));
        button1.setMinimumSize(new Dimension(150, 50));
        //button 2
        JButton button2 = new JButton("Multi Player");
        button2.setFont(new Font("Arial", Font.BOLD, 11));
        button2.setPreferredSize(new Dimension(150, 50));
        button2.setIcon(icon1);
        button2.addMouseListener(new MenuMouse(ipField));
        button2.setMaximumSize(new Dimension(150, 50));
        button2.setMinimumSize(new Dimension(150, 50));
        //label for ip address
        JLabel ipLabel = new JLabel("Host IP Address:");
        ipLabel.setFont(new Font("Arial", Font.BOLD, 14));
        ipLabel.setHorizontalAlignment(JLabel.CENTER);
        ipLabel.setPreferredSize(new Dimension(150, 50));
        ipLabel.setMaximumSize(new Dimension(150, 50));
        ipLabel.setMinimumSize(new Dimension(150, 50));
        //separator 1
        JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
        separator.setPreferredSize(new Dimension(150, 50));
        separator.setMaximumSize(new Dimension(150, 50));
        separator.setMinimumSize(new Dimension(150, 50));
        //separator 2
        JSeparator separator2 = new JSeparator(JSeparator.HORIZONTAL);
        separator2.setPreferredSize(new Dimension(150, 50));
        separator2.setMaximumSize(new Dimension(150, 50));
        separator2.setMinimumSize(new Dimension(150, 50));
        //set alignment
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        button1.setAlignmentX(Component.CENTER_ALIGNMENT);
        button2.setAlignmentX(Component.CENTER_ALIGNMENT);
        ipField.setAlignmentX(Component.CENTER_ALIGNMENT);
        ipLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        separator.setAlignmentX(Component.CENTER_ALIGNMENT);
        separator2.setAlignmentX(Component.CENTER_ALIGNMENT);
        //button panel
        panel.add(separator);
        panel.add(button1);
        panel.add(separator2);
        panel.add(button2);
        panel.add(ipLabel);
        panel.add(ipField);
        //frame related stuff
        setSize(640, 480);
        setMinimumSize(new Dimension(640, 480));
        setLocationRelativeTo(null); //put jframe center of screen
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //initiates Runnables:
        // After the player clicks 'Single Player' Start the game as single player
        singlePlayerStart = new Runnable()
        {
            @Override
            public void run()
            {
                // Create and execute the game-loop
                GameLoop game = GameLoop.getInstance();
                ThreadPool.execute(game);
                // and the game starts ...
            }
        };
        
        // After the player clicks 'Multi Player' Start the game as multi player
        multiPlayerStart = new MultiPlayerRunnable(ipField);
        
        
        setVisible(true);
    }
    
    private static class MultiPlayerRunnable implements Runnable
    {
        private JTextField ipField;
        
        public MultiPlayerRunnable(JTextField ipField)
        {
            this.ipField = ipField;
        }
        
        @Override
        public void run()
        {
            // Create and execute the game-loop
            GameLoop game = GameLoop.getInstance();
            game.setHostIP(ipField.getText());
            ThreadPool.execute(game);
            // and the game starts ...
        }
    }
    
    private class MenuMouse extends MouseAdapter
    {
        private JTextField multiplayer;
        
        public MenuMouse(JTextField multiplayer)
        {
            this.multiplayer = multiplayer;
        }
        
        @Override
        public void mouseClicked(MouseEvent e)
        {
            
            if (e.getButton() == MouseEvent.BUTTON1)
            {
                if (multiplayer == null)
                    EventQueue.invokeLater(singlePlayerStart);
                else
                {
                    EventQueue.invokeLater(multiPlayerStart);
                }
                InitialMenu.this.setVisible(false);
            }
        }
    }
}
