package com.nokhrin.chatserver.service;

import java.util.List;

public interface ChatService {
    String sendMessage(String message, String userName, String roomName);
    String getMessages(String userName, String roomName,String separator);
    String joinChat(String userName,String roomName);
    String getPreviousMessages(String roomName,String separator);
    String getChatRoomNames(String separator);
    String quit(String userName,String roomName);
    boolean checkUsernameAvailability(String username);
}
