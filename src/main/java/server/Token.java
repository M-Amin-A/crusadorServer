package server;

public class Token {
    public static boolean isTokenValid(String token) {
        return token.matches("\\w\\w\\w\\d@token");
    }

    public static String newToken() {
        char[] chars = new char[3];
        chars[0] = newCharRandom();
        chars[1] = newCharRandom();
        chars[2] = newCharRandom();
        return String.valueOf(chars) + (int)(Math.random()*10) + "@token";
    }

    private static char newCharRandom() {
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        double random = Math.random() * 26;
        return alphabet.charAt((int) random);
    }
}