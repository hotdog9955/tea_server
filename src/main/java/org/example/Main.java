package org.example;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
import java.io.IOException;
import java.net.*;
import java.util.*;
public class Main {
    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(23788);

        while (!ss.isClosed()){
            Socket socket = ss.accept();
            ClientHandler clientHandler = new ClientHandler(socket);

            Thread thread = new Thread(clientHandler);
            thread.start();
        }
    }
}