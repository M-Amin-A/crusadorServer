package server;

import model.DataBase;
import model.GameData;
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
        }
    }

    public static Lobby getLobbyByName(String lobbyName) {
        for (Lobby lobby : DataBase.getActiveLobbies()) {
            if (lobby.getName().equals(lobbyName)) return lobby;
        }
        return null;
    }

    public static void destroy(String lobbyName) {
        Lobby lobby = getLobbyByName(lobbyName);
        for (User user : lobby.getUsers()) {
            user.setLobby(null);
        }
        DataBase.getActiveLobbies().remove(lobby);
        //todo خارج شدن از لابی بعد از قطع شدن اتصال
        //برای هر لابی todo chatroom
    }
}
