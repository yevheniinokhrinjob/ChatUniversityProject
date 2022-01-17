package com.nokhrin.chatserver.model;

public class ChatUser {
    private String login;
    private int lastMessageReceivedId=0;


    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public int getLastMessageReceivedId() {
        return lastMessageReceivedId;
    }

    public void setLastMessageReceivedId(int lastMessageReceivedId) {
        this.lastMessageReceivedId = lastMessageReceivedId;
    }
}
