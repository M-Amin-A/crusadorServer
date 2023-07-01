package server;

import com.google.gson.Gson;
import javafx.css.Match;
import model.DataBase;
import model.GameData;
import model.User;
import model.map.MapTemplate;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;

public class Connection extends Thread{

    private final Socket socket;
    private final ServerSocket serverSocket;
    private final DataInputStream dataInputStream;
    private final DataOutputStream dataOutputStream;
    private String clientUsername =null;

    public Connection(Socket socket, ServerSocket serverSocket) throws IOException {
        this.socket = socket;
        this.serverSocket = serverSocket;
        dataInputStream=new DataInputStream(socket.getInputStream());
        dataOutputStream=new DataOutputStream(socket.getOutputStream());
    }

    @Override
    public void run(){
        while (true){
            try {
                if(dataInputStream.available()!=0){
                    String input=dataInputStream.readUTF();
                    System.out.println(input);  //todo may be deleted later

                    boolean terminate=inputHandler(input);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Boolean inputHandler(String input){
        Matcher matcher;

        System.out.println("received from port "+socket.getPort()+" : "+input);
        //initial commands
        if((matcher=Commands.GET_USER_BY_USERNAME.getMatcher(input))!=null){
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
        else if((matcher=Commands.LOBBY_REQUEST.getMatcher(input))!=null){

        }
        else if((matcher=Commands.START_GAME.getMatcher(input))!=null){
            startGame(matcher);
        }



        //in game commands
        else if((matcher=Commands.NEXT_TURN.getMatcher(input))!=null){
            nextTurn(matcher);
        }
        //map editor
        else if((matcher=Commands.GET_MAPS.getMatcher(input))!=null){
            getMaps();
        }
        else if((matcher=Commands.GET_MAP_BY_NAME.getMatcher(input))!=null){
            getMapByName(matcher);
        }

        return false;
    }

    private void startGame(Matcher matcher){
        String[] usernames=new String[matcher.groupCount()];
        for(int i=1;i<=matcher.groupCount();i++){
            try {
                usernames[i] = matcher.group(i);
            }catch (Exception e){
                e.printStackTrace();
                return;
            }
        }

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
        GameData serverGameData=DataBase.getActiveGames().get(gameId);
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

        DataBase.getConnections().put(clientUsername,this);
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
