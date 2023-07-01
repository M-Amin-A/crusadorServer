package server;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Commands {

    //initial commands
    LOGIN("LOGIN (<?username>.*)"),
    GET_USER_BY_USERNAME("GET_USER_BY_USERNAME (<?username>.*)"),
    GET_USER_BY_EMAIL("GET_USER_BY_EMAIL (<?email>.*)"),
    SORT_USERS_BY_HIGH_SCORE("SORT_USERS_BY_HIGH_SCORE"),
    RANK_OF_USER("RANK_OF_USER (<?username>.*)"),
    GET_USERS("GET_USERS"),
    ADD_USER("ADD_USER (<?userJson>.*)"),
    CHANGE_USER_DATA("CHANGE_USER_DATA (<?userJson>.*)"),

    //lobby commands
    NEW_LOBBY("new lobby (?<number>\\d+)"),
    JOIN_LOBBY("join (?<name>\\S+)"),
    LEFT_LOBBY("left lobby"),
    GET_USERNAMES("get usernames (?<lobbyName>\\S+)"),
    GET_LOBBIES("get lobbies"),
    IS_ADMIN("is admin"),
    CHANGE_LOBBY_ACCESS("change lobby access"),
    GET_NUMBER("get number of players"),
    IS_LOBBY_VALID("is lobby valid"),
    LOBBY_REQUEST("lobby request"),
    START_GAME("START_GAME ( (<?playerUsername>\\S+))*\\s*"),

    //chat commands


    //in game commands
    NEXT_TURN("NEXT_TURN (<?gameId>\\S+) (<?gameData>.*)"),

    //edit map menu
    GET_MAPS("GET_MAPS"),
    GET_MAP_BY_NAME("GET_MAP_BY_NAME (<?name>.*)"),
    ADD_MAP("ADD_MAP (<?mapJson>.*)");
    private final String regex;
    private Commands(String regex){
        this.regex=regex;
    }

    public Matcher getMatcher(String input){
        Matcher matcher= Pattern.compile(regex).matcher(input);
        if(matcher.matches())
            return matcher;
        return null;
    }
}
