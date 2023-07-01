package model;

import model.map.MapTemplate;
import server.Connection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class DataBase {
    private static final String usersDataBaseFilePath = "src/main/resources/usersDataBase.json";
    private static final String mapsDataBaseFilePath = "src/main/resources/mapsDataBase.json";
    private static final ArrayList<Connection> connections=new ArrayList<>();
    private static ArrayList<Lobby> activeLobbies=new ArrayList<>();
    private static ArrayList<GameData> activeGames=new ArrayList<>();
    private static final ArrayList<MapTemplate> publicMapTemplates=new ArrayList<>();
    private static ArrayList<User> users=new ArrayList<>();

    public static synchronized void initializeDataBase() {
        User[] loadingUsers=SaveAndLoad.loadArrayData(usersDataBaseFilePath,User[].class);
        if (loadingUsers != null) {
            users.addAll(Arrays.asList(loadingUsers));
        }
        MapTemplate[] loadingMapTemplate=SaveAndLoad.loadArrayData(mapsDataBaseFilePath,MapTemplate[].class);
        if (loadingMapTemplate != null) {
            publicMapTemplates.addAll(Arrays.asList(loadingMapTemplate));
        }
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
        SaveAndLoad.saveData(users, usersDataBaseFilePath);
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

    public static synchronized ArrayList<GameData> getActiveGames() {
        return activeGames;
    }

    public static synchronized void setActiveGames(ArrayList<GameData> activeGames) {
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

    public static synchronized ArrayList<Connection> getConnections() {
        return connections;
    }

    public static MapTemplate getMapByName(String name) {
        for (MapTemplate mapTemplate : publicMapTemplates) {
            if (mapTemplate.getName().equals(name)) return mapTemplate;
        }
        return null;
    }

    public static ArrayList<MapTemplate> getMaps(){
        ArrayList<MapTemplate> mapTemplate=new ArrayList<>();
        for(MapTemplate template: publicMapTemplates)
            mapTemplate.add(template);

        return mapTemplate;
    }

    public static String getMapsDataBaseFilePath() {
        return mapsDataBaseFilePath;
    }

    public static void addMapTemplate(MapTemplate mapTemplate) {
        publicMapTemplates.add(mapTemplate);
        SaveAndLoad.saveData(publicMapTemplates, usersDataBaseFilePath);
    }

    public static Connection getConnectionByUserName(String username) {
        for (Connection connection : connections) {
            if (connection.getClientUsername().equals(username)) return connection;
        }
        return null;
    }

    public static GameData getGameDataById(String id) {
        for (GameData gameData : getActiveGames()) {
            if (gameData.getGameID().equals(id)) return gameData;
        }
        return null;
    }
}
