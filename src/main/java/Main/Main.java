package Main;

import model.DataBase;
import server.Server;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Server server=new Server();
        DataBase.initializeDataBase();
        try {
            server.run();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}