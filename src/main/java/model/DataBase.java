package model;

import model.map.MapTemplate;
import server.Connection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class DataBase {
    private static final String usersDataBaseFilePath = "./src/main/dataFiles/UsersDataBase.json";
    private static final HashMap<String, Connection> connections=new HashMap<>();
    private static HashMap<String, Lobby> activeLobbies;
    private static HashMap<String, GameData> activeGames;
    private static final HashMap<String, MapTemplate> publicMapTemplates=new HashMap<>();
    private static ArrayList<User> users;

    public static synchronized void initializeDataBase() {
        //todo get from json file
    }

    //todo save data of data base        SaveAndLoad.saveData(AppData.getUsers(), AppData.getUsersDataBaseFilePath());

    public static synchronized User getUserByUsername(String username) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(username)) {
                return users.get(i);
            }
        }
        return null;
    }

    public static synchronized User getUserByEmail(String email) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getEmail().equalsIgnoreCase(email)) {
                return users.get(i);
            }
        }
        return null;
    }

    public static synchronized void addUser(User user) {
        users.add(user);
    }
    public static ArrayList<User> sortUserByHighScore() {
        ArrayList<User> sortedUsers = new ArrayList<>();
        for (int i = 0; i < users.size(); i++) {
            sortedUsers.add(users.get(i));
        }
        for (int i = 0; i < users.size(); i++) {
            for (int j = i + 1; j < users.size(); j++) {
                if (sortedUsers.get(i).getHighScore() < sortedUsers.get(j).getHighScore()) {
                    Collections.swap(sortedUsers, i, j);
                }
            }
        }
        return sortedUsers;
    }

    public static synchronized int rankOfUser(ArrayList<User> sortedUsers, String username) {
        for (int i = 0; i < sortedUsers.size(); i++) {
            if (sortedUsers.get(i).getUsername().equals(username)) {
                return i + 1;
            }
        }
        return 0;
    }

    public static synchronized HashMap<String, Lobby> getActiveLobbies() {
        return activeLobbies;
    }

    public static synchronized void setActiveLobbies(HashMap<String, Lobby> activeLobbies) {
        DataBase.activeLobbies = activeLobbies;
    }

    public static synchronized HashMap<String, GameData> getActiveGames() {
        return activeGames;
    }

    public static synchronized void setActiveGames(HashMap<String, GameData> activeGames) {
        DataBase.activeGames = activeGames;
    }

    public static synchronized ArrayList<User> getUsers() {
        return users;
    }

    public static synchronized void setUsers(ArrayList<User> users) {
        DataBase.users = users;
    }

    public static synchronized String getUsersDataBaseFilePath() {
        return usersDataBaseFilePath;
    }

    public static synchronized HashMap<String, Connection> getConnections() {
        return connections;
    }

    public static MapTemplate getMapByName(String name) {
        return publicMapTemplates.get(name);
    }

    public static ArrayList<MapTemplate> getMaps(){
        ArrayList<MapTemplate> mapTemplate=new ArrayList<>();
        for(MapTemplate template: publicMapTemplates.values())
            mapTemplate.add(template);

        return mapTemplate;
    }
}
