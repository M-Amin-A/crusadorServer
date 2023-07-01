package server;

import model.GameData;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public Server(){
        System.out.println("server is running...");
    }

    public void run() throws IOException {
        ServerSocket serverSocket=new ServerSocket(8080);

        while (true){
            Socket socket=serverSocket.accept();
        }
    }
}
