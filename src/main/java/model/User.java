package model;

public class User {

    public String securityQuestion;
    public String username;
    public String password;
    public String nickname;
    public String email;
    //recovery question
    public String slogan;
    public int highScore = 0;
    public int stayLoggedIn = 0;

    public User(String username, String password, String nickname, String email, String slogan, String securityQuestion) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.slogan = slogan;
        this.securityQuestion = securityQuestion;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public String getSecurityQuestion() {
        return securityQuestion;
    }

    public int getHighScore() {
        return highScore;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User) {
            return ((User) obj).username.equals(this.username);
        }
        return false;
    }

    public int getStayLoggedIn() {
        return stayLoggedIn;
    }

    public void setStayLoggedIn(int stayLoggedIn) {
        this.stayLoggedIn = stayLoggedIn;
    }

    public void setSecurityQuestion(String securityQuestion) {
        this.securityQuestion = securityQuestion;
    }
}