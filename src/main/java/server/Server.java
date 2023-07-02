package server;

import model.DataBase;
import model.Lobby;
import model.User;

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
            Connection connection=new Connection(socket,serverSocket);
            connection.start();
        }
    }

    public static Lobby getLobbyByName(String lobbyName) {
        for (Lobby lobby : DataBase.getActiveLobbies().values()) {
            if (lobby.getName().equals(lobbyName)) return lobby;
        }
        return null;
    }

    public static void destroy(String lobbyName) {
        Lobby lobby = getLobbyByName(lobbyName);
        for (User user : lobby.getUsers()) {
            DataBase.getActiveLobbies().put(user,lobby);
        }
        DataBase.getActiveLobbies().remove(lobby);
        //برای هر لابی todo chatroom
    }
}
