package Main;

import com.google.gson.Gson;
import model.DataBase;
import model.map.CellType;
import model.map.MapTemplate;
import server.Server;

import java.io.IOException;
import java.util.ArrayList;

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