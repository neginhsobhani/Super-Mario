package org.example.SuperMarioBros.Controller;

import org.example.SuperMarioBros.Model.ThreadPool;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

/**
 * This class is the audio player for this project.
 * It holds the class details relevant in our context.
 */
public class AudioPlayer
{
    //audio file names
    public static final String
            Background = "background.wav",
            //TODO: level 2 background music
            Background2 = "background.wav",
            Coin = "coin.wav",
            Die = "die.wav",
            Firework = "firework.wav",
            GameOver = "gameover.wav",
            Jump = "jump.wav",
            Kill = "kill.wav",
            LifeGain = "life_gain.wav",
            Pause = "pause.wav",
            Pipe = "pipe.wav",
            PowerUpAppear = "powerup_appear.wav",
            PowerUpGet = "powerup_get.wav",
            Shoot = "shoot.wav",
            ShootKill = "shoot_kill.wav",
            TimeWarning = "time_warning.wav",
            WallBreak = "wall_break.wav",
            WallBump = "wall_bump.wav",
            WinLevel = "win_level.wav",
            WinWorld = "win_world.wav";
    
    private static boolean wait = false;
    
    //capturing running audios
    private static HashMap<String, PlayerUtility> runningAudios = new HashMap<>();
    
    public static void play(String audioName)
    {
        play(audioName, false, false);
    }
    
    public static synchronized void playWait(String audioName)
    {
        wait = true;
        play(audioName, false, true);
        while (wait)
        {
            //busy working!
            try
            {
                Thread.sleep(50);
            }
            catch (InterruptedException e)
            {
                new ExceptionHandler(e);
            }
        }
    }
    
    public static void playWithLoop(String audioName)
    {
        play(audioName, true, false);
    }
    
    private static Runnable play(String audioName, boolean loop, boolean wait)
    {
        Runnable res = new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    File audioFile = new File("Files/audio/" + audioName);
                    AudioInputStream stream = AudioSystem.getAudioInputStream(audioFile.getAbsoluteFile());
                    Clip clip = AudioSystem.getClip();
                    clip.open(stream);
//                    if (runningAudios.containsKey(audioName))
//                    {
//                        runningAudios.get(audioName).stop();
//                        removeRunningAudio(audioName);
//                    }
                    //volume control
                    FloatControl gainControl =
                            (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                    if (audioName.equals(Background))
                        gainControl.setValue(-10.0f); // Reduce volume by 10 decibels.
                    else
                        gainControl.setValue(-5.0f);
            
                    PlayerUtility audioPlayerUtility;
                    ThreadPool.execute(audioPlayerUtility = new PlayerUtility(clip, audioName, loop, wait));
                    runningAudios.put(audioName, audioPlayerUtility);
                }
                catch (UnsupportedAudioFileException | IOException | LineUnavailableException e)
                {
                    new ExceptionHandler(e);
                }
            }
        };
        ThreadPool.execute(res);
        return res;
    }
    
    public static void stop(String audioName)
    {
        if(runningAudios.get(audioName) == null)
            return;
        runningAudios.get(audioName).stop();
    }
    
    private static void removeRunningAudio(String audioName)
    {
        runningAudios.remove(audioName);
    }
    
    private static class PlayerUtility implements Runnable
    {
        private Clip clip;
        private String audioName;
        private boolean loop;
        private boolean wait;
        
        private PlayerUtility(Clip clip, String audioName, boolean loop, boolean wait)
        {
            this.clip = clip;
            this.audioName = audioName;
            this.loop = loop;
            this.wait = wait;
        }
        
        @Override
        public void run()
        {
            clip.start();
            try
            {
                audioWait(clip);
            }
            catch (InterruptedException e)
            {
                new ExceptionHandler(e);
            }
        }
        
        public void stop()
        {
            loop = false;
            clip.stop();
            clip.close();
            removeRunningAudio(audioName);
            if(wait)
                AudioPlayer.wait = false;
        }
        
        private void audioWait(Clip clip)
                throws InterruptedException
        {
            final CountDownLatch syncLatch = new CountDownLatch(1);
            clip.addLineListener(
                    new LineListener()
                    {
                        @Override
                        public void update(LineEvent event)
                        {
                            if (event.getType() == LineEvent.Type.STOP)
                            {
                                syncLatch.countDown();
                                if (loop)
                                    playWithLoop(audioName);
                                else
                                    stop();
                            }
                        }
                    }
            );
            
            //syncLatch.await();
        }
    }
}
