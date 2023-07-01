package model;

import model.map.MapTemplate;
import server.Connection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class DataBase {
    private static final String usersDataBaseFilePath = DataBase.class.getResource("/usersDataBase.json").toString();
    private static final String mapsDataBaseFilePath = DataBase.class.getResource("/mapsDataBase.json").toString();
    private static final HashMap<String, Connection> connections=new HashMap<>();
    private static ArrayList<Lobby> activeLobbies=new ArrayList<>();
    private static HashMap<String, GameData> activeGames=new HashMap<>();
    private static final HashMap<String, MapTemplate> publicMapTemplates=new HashMap<>();
    private static ArrayList<User> users=new ArrayList<>();

    public static synchronized void initializeDataBase() {

    }
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
        SaveAndLoad.saveData(user, usersDataBaseFilePath);
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

    public static synchronized ArrayList<Lobby> getActiveLobbies() {
        return activeLobbies;
    }

    public static synchronized void setActiveLobbies(ArrayList<Lobby> activeLobbies) {
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

    public static String getMapsDataBaseFilePath() {
        return mapsDataBaseFilePath;
    }
}
