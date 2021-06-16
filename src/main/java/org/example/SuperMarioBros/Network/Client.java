package org.example.SuperMarioBros.Network;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    private static Client instance = null;
    private Socket socket;
    private static final int PORT = 6666;
    private static  String IP = "127.0.0.1";
    private InputStream inputStream;
    private OutputStream outputStream;
    private ObjectOutputStream writer;
    private ObjectInputStream reader;
    private boolean flag = false;

    private Client() {
        try {
            socket = new Socket(IP, PORT);
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            System.out.println("vasl shodam khoshhalam");

//            interactWithServer();
//            socket.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static Client getInstance() {
        if (instance == null)
            instance = new Client();
        return instance;
    }

    public static String getIP() {
        return IP;
    }

    public static void setIP(String IP) {
        Client.IP = IP;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public void sendMessage(NetworkObject networkObject) {
        if (writer == null) {
            try {
                writer = new ObjectOutputStream(outputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
           NetworkObject temp = new NetworkObject();
            temp.setmarioX(networkObject.getMarioX());
            temp.setGetmarioY(networkObject.getMarioY());
            temp.setWin(networkObject.isWin());
            temp.setGameOver(networkObject.isGameOver());
            writer.writeObject(temp);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void listen() {
        System.out.println("get1");
        try {
            if (inputStream.read() == 1) {
                System.out.println("wow");
                flag = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Runnable r = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        if (reader == null)
                            reader = new ObjectInputStream(inputStream);

                        NetworkObject networkObject = (NetworkObject) reader.readObject();
                    } catch (IOException e) {
                        e.printStackTrace();
                        break;
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                        break;
                    }
                }
            }
        };
        new Thread(r).start();
    }
    public static void main(String[] args) {
        Client.getInstance().listen();
        System.out.println("listen client");
        if (Client.getInstance().flag) {
//            Map map = new Map();
//            map.setNumber(101);
//            Client.getInstance().sendMessage(map);
//            Map map1 = new Map();
//            map1.setNumber(202);
//            Client.getInstance().sendMessage(map1);
//            map.setNumber(303);
//            Client.getInstance().sendMessage(map);
//            Client.getInstance().sendMessage(map);
        }
    }
}
