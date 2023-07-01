package model;

import server.Connection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class GameData {
    private ArrayList<String> playerUsernames;
    private String gameID;
    private GameData(){}
    public static GameData GenerateGameData(ArrayList<String> playerUsernames){
        GameData gameData=new GameData();
        gameData.setPlayerUsernames(playerUsernames);

        String id="";
        Random random=new Random();
        do {
            id=""+random.nextInt()%1000000;
        }while(DataBase.getActiveGames().containsKey(id));

        gameData.setGameID(id);

        DataBase.getActiveGames().put(id,gameData);
        return gameData;
    }

    public ArrayList<String> getPlayerUsernames() {
        return playerUsernames;
    }

    public void setPlayerUsernames(ArrayList<String> playerUsernames) {
        this.playerUsernames = playerUsernames;
    }

    public String getGameID() {
        return gameID;
    }

    public void setGameID(String gameID) {
        this.gameID = gameID;
    }

    public void sendToPlayers(String message) throws IOException {
        for(String username:playerUsernames){
            Connection connection=DataBase.getConnections().get(username);
            connection.writeOnSocket(message);
        }
    }
}
