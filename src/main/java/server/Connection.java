package server;

import com.google.gson.Gson;
import model.DataBase;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.regex.Matcher;

public class Connection extends Thread{

    private final Socket socket;
    private final ServerSocket serverSocket;
    private final DataInputStream dataInputStream;
    private final DataOutputStream dataOutputStream;
    private String username=null;

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
        if((matcher=Commands.LOBBY_REQUEST.getMatcher(input))!=null){

        }
        else if((matcher=Commands.START_GAME.getMatcher(input))!=null){

        }

        return false;
    }

    private void validateConnection(String username){
        this.username=username;

        DataBase.getConnections().put(username,this);
    }

    public void writeOnSocket(String message) throws IOException {
        dataOutputStream.writeUTF(message);
    }

    public <T> Object getObjectFromJson(Class<T> objectClass, String json) {
        Gson gson = new Gson();

        return gson.fromJson(json, objectClass);
    }

    public <T> T[] getArrayOfObjectsFromJson(Class<T[]> listClass, String json) {
        Gson gson = new Gson();

        return gson.fromJson(json, listClass);
    }
}
