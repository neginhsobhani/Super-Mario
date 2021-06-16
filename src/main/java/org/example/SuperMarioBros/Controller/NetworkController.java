package org.example.SuperMarioBros.Controller;


import org.example.SuperMarioBros.Model.GameState;
import org.example.SuperMarioBros.Network.Client;
import org.example.SuperMarioBros.Network.NetworkObject;

/**
 * NetworkController handles the 2 player game .
 * It's where almost every network related thing happens.
 * It holds the class details relevant in our context.
 */
public class NetworkController {
    private static NetworkController instance;
    
    private NetworkController()
    {
        //TODO: implement if needed
    }
    
    public static NetworkController getInstance()
    {
        if(instance == null)
            instance = new NetworkController();
        return instance;
    }

    /**
     * this is the method in which the client is connected to server
     */
    public void getReady()
    {
        Client.getInstance().listen(); //to check if other client is connected
        if(Client.getInstance().isFlag()) //if another client was connected too
        {
            //sending process
            NetworkObject networkObject = new NetworkObject();
            updateNetworkObject(networkObject); //prepare the current state of client for sending
            Client.getInstance().sendMessage(networkObject); //send to another client

        }
    }
    
    public void update()
    {

    }
    
    /**
     * this method updates the network object
     * at the end of the update , the object is ready to be sent to the other client
     */
    public void updateNetworkObject(NetworkObject networkObject){
        //data of player which is going to be sent
        networkObject.setNetworkObject(GameState.getMap().player.getX(),GameState.getMap().player.getY(),GameState.win,GameState.gameOver);
    }

    
}
