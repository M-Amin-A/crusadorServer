package model;

public class Message {
    public String text;
    public String time;
    public String name;
    public String avatar;

    public Message(String text, String time, String name, String avatar) {
        this.text = text;
        this.time = time;
        this.name = name;
        this.avatar = avatar;
    }



    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
