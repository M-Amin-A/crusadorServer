package server;

import com.google.gson.Gson;
import model.*;
import model.map.MapTemplate;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.regex.Matcher;

public class Connection extends Thread{

    private final Socket socket;
    private final ServerSocket serverSocket;
    private final DataInputStream dataInputStream;
    private final DataOutputStream dataOutputStream;
    private String clientUsername =null;

    public Connection(Socket socket, ServerSocket serverSocket) throws IOException {
        System.out.println("new connection on port: "+socket.getPort());
        this.socket = socket;
        this.serverSocket = serverSocket;
        dataInputStream=new DataInputStream(socket.getInputStream());
        dataOutputStream=new DataOutputStream(socket.getOutputStream());
    }

    public String getClientUsername() {
        return clientUsername;
    }

    @Override
    public void run(){
        while (true){
            try {
                String input=dataInputStream.readUTF();
                boolean terminate=inputHandler(input);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Boolean inputHandler(String input) throws IOException {
        Matcher matcher;

        System.out.println("received from port "+socket.getPort()+" : "+input);
        //initial commands
        if((matcher=Commands.LOGIN.getMatcher(input))!=null){
            validateConnection(matcher.group("username"));
        }
        else if((matcher=Commands.GET_USER_BY_USERNAME.getMatcher(input))!=null){
            getUserByUsername(matcher);
        }
        else if((matcher=Commands.GET_USER_BY_EMAIL.getMatcher(input))!=null){
            getUserByEmail(matcher);
        }
        else if((matcher=Commands.SORT_USERS_BY_HIGH_SCORE.getMatcher(input))!=null){
            sortUsersByHighScore();
        }
        else if((matcher=Commands.RANK_OF_USER.getMatcher(input))!=null){
            rankOfUser(matcher);
        }
        else if((matcher=Commands.GET_USERS.getMatcher(input))!=null){
            getUsers();
        }
        else if((matcher=Commands.ADD_USER.getMatcher(input))!=null){
            addUser(matcher);
        }
        else if((matcher=Commands.CHANGE_USER_DATA.getMatcher(input))!=null){
            changeUserData(matcher);
        }


        //lobby commands
        else if ((matcher = Commands.NEW_LOBBY.getMatcher(input)) != null) {
            newLobby(matcher);
        } else if ((matcher = Commands.JOIN_LOBBY.getMatcher(input)) != null) {
            joinLobby(matcher);
        } else if (Commands.LEFT_LOBBY.getMatcher(input) != null) {
            leftLobby();
        } else if ((matcher = Commands.GET_USERNAMES.getMatcher(input)) != null) {
            sendUsernames(matcher);
        } else if (Commands.GET_LOBBIES.getMatcher(input) != null) {
            sendLobbyNames();
        } else if (Commands.IS_ADMIN.getMatcher(input) != null) {
            isAdmin();
        } else if (Commands.CHANGE_LOBBY_ACCESS.getMatcher(input) != null) {
            changeLobbyAccess();
        } else if (Commands.GET_NUMBER.getMatcher(input) != null) {
            sendNumberOfPlayers();
        } else if (Commands.IS_LOBBY_VALID.getMatcher(input) != null) {
            isLobbyValid();
        }
        //todo where is start game??


        //in game commands
        else if((matcher=Commands.NEXT_TURN.getMatcher(input))!=null){
            nextTurn(matcher);
        }
        //map editor
        else if((matcher=Commands.GET_MAPS.getMatcher(input))!=null){
            getMaps();
        }
        else if((matcher=Commands.ADD_MAP.getMatcher(input))!=null){
            addMaps(matcher);
        }
        else if((matcher=Commands.GET_MAP_BY_NAME.getMatcher(input))!=null){
            getMapByName(matcher);
        }

        SaveAndLoad.saveData(DataBase.getUsers(), DataBase.getUsersDataBaseFilePath());
        SaveAndLoad.saveData(DataBase.getMaps(), DataBase.getMapsDataBaseFilePath());

        return false;
    }

    private void addMaps(Matcher matcher) {
        String mapJson=matcher.group("mapJson");
        MapTemplate mapTemplate;
        try {
            mapTemplate = (MapTemplate) getObjectFromJson(MapTemplate.class, mapJson);
        }catch (Exception e){
            return;
        }
        DataBase.addMapTemplate(mapTemplate);
    }

    private void isLobbyValid() throws IOException {
        User user = DataBase.getUserByUsername(clientUsername);
        boolean isValid = (user.getLobby() != null);
        dataOutputStream.writeBoolean(isValid);
    }
    private void sendNumberOfPlayers() throws IOException {
        User user = DataBase.getUserByUsername(clientUsername);
        dataOutputStream.writeInt(user.getLobby().getUsers().size());
    }

    private void changeLobbyAccess() {
        User user = DataBase.getUserByUsername(clientUsername);
        Lobby lobby = user.getLobby();
        lobby.setPublic(!lobby.isPublic());
    }

    private void isAdmin() throws IOException {
        User user = DataBase.getUserByUsername(clientUsername);
        boolean isAdmin = user.getLobby().getAdmin().equals(user);
        dataOutputStream.writeBoolean(isAdmin);
    }

    private void sendLobbyNames() throws IOException {
        dataOutputStream.writeInt(getPublicSize(DataBase.getActiveLobbies()));
        for (Lobby lobby : DataBase.getActiveLobbies()) {
            if (!lobby.isPublic()) continue;
            dataOutputStream.writeUTF(lobby.getName());
            dataOutputStream.writeInt(lobby.getCapacity());
            String users = "";
            for (User user : lobby.getUsers()) {
                users += user.getNickname() + ",";
            }
            dataOutputStream.writeUTF(users);
        }
    }

    private int getPublicSize(ArrayList<Lobby> lobbies) {
        int counter = 0;
        for (Lobby lobby : lobbies) {
            if (lobby.isPublic()) counter++;
        }
        return counter;
    }

    private void sendUsernames(Matcher matcher) throws IOException {
        String lobbyName = matcher.group("lobbyName");
        Lobby lobby = Server.getLobbyByName(lobbyName);
        dataOutputStream.writeInt(lobby.getUsers().size());
        for (User user1 : lobby.getUsers()) {
            dataOutputStream.writeUTF(user1.getNickname());
        }
    }

    private void leftLobby() {
        User user = DataBase.getUserByUsername(clientUsername);
        Lobby lobby = user.getLobby();
        lobby.remove(user);
        if (lobby.getUsers().size() == 0) DataBase.getActiveLobbies().remove(lobby);
    }

    private void joinLobby(Matcher matcher) throws IOException {
        User user = DataBase.getUserByUsername(clientUsername);
        String lobbyName = matcher.group("name");
        Lobby lobby = Server.getLobbyByName(lobbyName);
        if (lobby != null) {
            lobby.addUser(user);
            dataOutputStream.writeUTF(lobby.getName());
        }
        else dataOutputStream.writeUTF("null");
        if (lobby.getCapacity() <= lobby.getUsers().size()) {
            ArrayList<String> usernames=new ArrayList<>();
            for(User user1:lobby.getUsers())
                usernames.add(user1.username);
            startGame(usernames);
        }
    }

    private void newLobby(Matcher matcher) throws IOException {
        User user = DataBase.getUserByUsername(clientUsername);
        int capacity = Integer.parseInt(matcher.group("number"));
        Lobby lobby = new Lobby(user, capacity);
        DataBase.getActiveLobbies().add(lobby);
        dataOutputStream.writeUTF(lobby.getName());
    }

    private void startGame(ArrayList<String> usernames){
        GameData gameData=GameData.GenerateGameData(usernames);
        writeOnSocket(""+gameData.getGameID());
    }

    private void getMaps(){
        writeOnSocket(new Gson().toJson(DataBase.getMaps()));
    }
    private void getMapByName(Matcher matcher){
        String name=matcher.group("name").trim();
        MapTemplate mapTemplate= DataBase.getMapByName(name);

        writeOnSocket(new Gson().toJson(mapTemplate));
    }
    private void nextTurn(Matcher matcher){
        String gameId;
        String clientGameData;
        try {
            gameId = matcher.group("gameId").trim();
            clientGameData=matcher.group("gameData").trim();
        }catch (Exception e){
            e.printStackTrace();
            return;
        }
        GameData serverGameData=DataBase.getGameDataById(gameId);
        serverGameData.sendToPlayers(clientGameData);
    }

    private void getUserByUsername(Matcher matcher){
        String username=matcher.group("username").trim();
        User user= DataBase.getUserByUsername(username);

        writeOnSocket(new Gson().toJson(user));
    }
    private void getUserByEmail(Matcher matcher){
        String username=matcher.group("email").trim();
        User user= DataBase.getUserByEmail(username);

        writeOnSocket(new Gson().toJson(user));
    }

    private void sortUsersByHighScore(){
        ArrayList<User> users=DataBase.sortUserByHighScore();
        writeOnSocket(new Gson().toJson(users));
    }
    private void rankOfUser(Matcher matcher){
        String username=matcher.group("username").trim();

        ArrayList<User> users=DataBase.sortUserByHighScore();
        int rank=DataBase.rankOfUser(users,username);
        writeOnSocket(""+rank);
    }

    private void getUsers(){
        writeOnSocket(new Gson().toJson(DataBase.getUsers()));
    }
    private void addUser(Matcher matcher){
        String userJson=matcher.group("userJson");
        User user;
        try {
            user = (User) getObjectFromJson(User.class, userJson);
        }catch (Exception e){
            return;
        }
        DataBase.addUser(user);
    }

    private void changeUserData(Matcher matcher){
        String userJson=matcher.group("userJson");
        User newUser=(User) getObjectFromJson(User.class,userJson);

        User oldUser=DataBase.getUserByUsername(newUser.getUsername());
        DataBase.getUsers().remove(oldUser);
        DataBase.getUsers().add(newUser);
    }

    private void validateConnection(String clientUsername){
        this.clientUsername =clientUsername;

        DataBase.getConnections().add(this);
    }

    public void writeOnSocket(String message) {
        try {
            System.out.println("sent to port "+socket.getPort()+" : "+message);
            dataOutputStream.writeUTF(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T getObjectFromJson(Class<T> objectClass, String json) {
        Gson gson = new Gson();

        T object;
        try {
            object = (T)gson.fromJson(json, objectClass);
        } catch (Exception e){
            return null;
        }
        return object;
    }

    public <T> T[] getArrayOfObjectsFromJson(Class<T[]> listClass, String json) {
        Gson gson = new Gson();

        T[] object;
        try {
            object = (T[])gson.fromJson(json, listClass);
        } catch (Exception e){
            return null;
        }
        return object;
    }
}
