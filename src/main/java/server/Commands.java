package server;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Commands {

    //initial commands
    GET_USER_BY_USERNAME("GET_USER_BY_USERNAME (<?username>.*)"),
    GET_USER_BY_EMAIL("GET_USER_BY_EMAIL (<?email>.*)"),
    SORT_USERS_BY_HIGH_SCORE("SORT_USERS_BY_HIGH_SCORE"),
    RANK_OF_USER("RANK_OF_USER (<?username>.*)"),
    GET_USERS("GET_USERS"),
    ADD_USER("ADD_USER (<?userJson>.*)"),
    CHANGE_USER_DATA("CHANGE_USER_DATA (<?userJson>.*)"),

    //lobby commands
    LOBBY_REQUEST("LOBBY_REQUEST"),
    START_GAME("START_GAME ( (<?playerUsername>\\S+))*\\s*"),

    //chat commands


    //in game commands
    NEXT_TURN("NEXT_TURN (<?gameId>\\S+) (<?gameData>.*)"),

    //edit map menu
    GET_MAPS("GET_MAPS"),
    GET_MAP_BY_NAME("GET_MAP_BY_NAME (<?name>.*)"),
    ;
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
