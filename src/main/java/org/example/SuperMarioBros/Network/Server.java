package org.example.SuperMarioBros.Network;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
public class Server  {

    private ServerSocket serverSocket;
    private static final int PORT = 6666;
    private InputStream[] inputStreams = new InputStream[2];
    private OutputStream[] outputStreams = new OutputStream[2];
    private ClientHandler[] clientHandlers = new ClientHandler[2];

    public Server() {
        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        System.out.println("|||:::");
        Socket clientSocket = null;
        try {
            for(int i=0;i<2;i++) {
                clientSocket = serverSocket.accept();
                System.out.println("client "+i+" connected");
                inputStreams[i] = clientSocket.getInputStream();
                outputStreams[i] = clientSocket.getOutputStream();

                clientHandlers[i] = new ClientHandler(i);
            }
            new Thread(clientHandlers[1]).start();
            new Thread(clientHandlers[0]).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ClientHandler implements Runnable
    {
        private int index;
        public ClientHandler(int index) {
            this.index = index;
        }

        @Override
        public void run() {
            try {
                System.out.println("there we go");
                outputStreams[index].write(1);
                interactWithClient();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        private void interactWithClient()
        {
            try {
                ObjectOutputStream writer = new ObjectOutputStream(outputStreams[(index+1)%2]);
                ObjectInputStream reader = new ObjectInputStream(inputStreams[index]);

                while (true){
                    NetworkObject networkObject = (NetworkObject) reader.readObject();

                    writer.writeObject(networkObject);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) {
        new Server().start();
    }
}
