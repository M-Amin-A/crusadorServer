package model;

import server.Connection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class DataBase {
    private static final String usersDataBaseFilePath = "./src/main/dataFiles/UsersDataBase.json";
    private static final HashMap<String, Connection> connections=new HashMap<>();
    private static HashMap<String, Lobby> activeLobbies;
    private static HashMap<String, GameData> activeGames;
    private static ArrayList<User> users;

    public static void initializeDataBase() {
        //todo get from json file
    }

    //        SaveAndLoad.saveData(AppData.getUsers(), AppData.getUsersDataBaseFilePath());

    public static User getUserByUsername(String username) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(username)) {
                return users.get(i);
            }
        }
        return null;
    }

    public static User getUserByEmail(String email) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getEmail().equalsIgnoreCase(email)) {
                return users.get(i);
            }
        }
        return null;
    }

    public static void addUser(User user) {
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

    public static int rankOfUser(ArrayList<User> sortedUsers, String username) {
        for (int i = 0; i < sortedUsers.size(); i++) {
            if (sortedUsers.get(i).getUsername().equals(username)) {
                return i + 1;
            }
        }
        return 0;
    }

    public static HashMap<String, Lobby> getActiveLobbies() {
        return activeLobbies;
    }

    public static void setActiveLobbies(HashMap<String, Lobby> activeLobbies) {
        DataBase.activeLobbies = activeLobbies;
    }

    public static HashMap<String, GameData> getActiveGames() {
        return activeGames;
    }

    public static void setActiveGames(HashMap<String, GameData> activeGames) {
        DataBase.activeGames = activeGames;
    }

    public static ArrayList<User> getUsers() {
        return users;
    }

    public static void setUsers(ArrayList<User> users) {
        DataBase.users = users;
    }

    public static String getUsersDataBaseFilePath() {
        return usersDataBaseFilePath;
    }

    public static HashMap<String, Connection> getConnections() {
        return connections;
    }
}
