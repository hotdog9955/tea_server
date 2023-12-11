package org.example;

import java.net.Socket;

// i hate java, import everything
import java.util.*;
import java.io.*;

public class ClientHandler implements Runnable{
    public static int userCount = 0;
    public static ArrayList<ClientHandler> clientHandles = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufRead;
    private BufferedWriter bufWrite;
    private int userNum;

    public ClientHandler(Socket socket){
        try{
            this.socket = socket;
            this.bufWrite = new BufferedWriter(new OutputStreamWriter((socket.getOutputStream())));
            this.bufRead = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.userNum = userCount;
            System.out.println("Connected!");
            clientHandles.add(this);
            broadcast("User #" + this.userNum + " has joined the chat.");
            bufWrite.write(userNum + "\n");
            bufWrite.flush();
            userCount++;
        } catch(IOException e){
            closeAll(socket, bufRead, bufWrite);
        }
    }

    @Override
    public void run() {
        String message;
        while (socket.isConnected()) {
            try {

                message = bufRead.readLine();
                if (message == null){
                    throw new IOException("whoops");
                }
                broadcast("User #" + userNum + " " + message + "\n");
            }catch(IOException e){
                closeAll(socket, bufRead, bufWrite);
                break;
            }
        }
    }

    public void broadcast(String msg){

        for(ClientHandler clientHandler : clientHandles){
            try{
                System.out.println("1");
                clientHandler.bufWrite.write(msg);
                System.out.println("2");
                clientHandler.bufWrite.flush();
                System.out.println("3");
                System.out.println(msg);
            }catch(IOException e){
                System.out.println("oouyccc");
                closeAll(socket, bufRead, bufWrite);
            }
        }
    }

    public void removeClient(){
        clientHandles.remove(this);
        broadcast("User #" + userNum + " has left the chat.");
        System.out.println("Disconnected!");
    }

    public void closeAll(Socket socket, BufferedReader bufRead, BufferedWriter bufWrite){
        removeClient();
        try{
            if (bufRead != null){
                bufRead.close();
            }
            if (bufWrite != null){
                bufWrite.close();
            }
            if (socket != null){
                socket.close();
            }
        }catch(IOException e ){
            e.printStackTrace();
        }
    }
}


