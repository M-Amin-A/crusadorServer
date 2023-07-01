package model;

import server.Server;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;

public class Lobby {
    private int remainedSeconds = 300;
    private static int counter = 0;
    private String name = randomName();
    private ArrayList<User> users = new ArrayList<>();
    private int capacity;
    private boolean isPublic = true;

    public Lobby(User admin, int capacity) {
        users.add(admin);
        this.capacity = capacity;
        admin.setLobby(this);
        setTimer();
    }

    private void setTimer() {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (remainedSeconds > 0) remainedSeconds--;
                else Server.destroy(name);
            }
        };
        timer.schedule(task, 0,1000);
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public int getCapacity() {
        return capacity;
    }

    public synchronized void addUser(User user) {
        remainedSeconds = 300;
        users.add(user);
        user.setLobby(this);
    }

    private String randomName() {
        counter++;
        return "lobby" + counter;
    }

    //private LobbyMenu lobbyMenu;

    public synchronized ArrayList<User> getUsers() {
        return users;
    }

    public synchronized User getAdmin() {
        return users.get(0);
    }

    public String getName() {
        return name;
    }

    public synchronized void remove(User user) {
        users.remove(user);
        user.setLobby(null);
    }
}