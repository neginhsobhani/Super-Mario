package org.example.SuperMarioBros.Network;

import java.io.Serializable;

public class NetworkObject implements Serializable {
    private int marioX,marioY; //the location of other player
    private boolean win , gameOver;

    public void setmarioX(int marioX) {
        this.marioX = marioX;
    }

    public void setGetmarioY(int marioY) {
        this.marioY = marioY;
    }
    public void setWin(boolean win) {
        this.win = win;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public int getMarioX() {
        return marioX;
    }

    public int getMarioY() {
        return marioY;
    }


    public boolean isWin() {
        return win;
    }

    public boolean isGameOver() {
        return gameOver;
    }
    public void setNetworkObject(int marioX, int marioY, boolean win, boolean gameOver){
        this.marioX = marioX;
        this.marioY = marioY;
        this.win = win;
        this.gameOver = gameOver;
    }
}
