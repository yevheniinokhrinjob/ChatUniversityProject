package com.nokhrin.chatserver.service;

import com.nokhrin.chatserver.ChatRoom;
import com.nokhrin.chatserver.model.ChatUser;
import com.nokhrin.chatserver.model.Message;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ChatServiceImpl implements ChatService {
    private List<ChatRoom> rooms = new ArrayList<>();
    public ChatServiceImpl(){
        rooms.add(new ChatRoom("Room1"));
        rooms.add(new ChatRoom("Room2"));
        rooms.add(new ChatRoom("Room3"));
    }
    @Override
    public String sendMessage(String message, String userName, String roomName) {
        getChatRoomByName(roomName).sendMessage(message,userName);
        return "";
    }

    @Override
    public String getMessages(String userName, String roomName,String separator) {
        String messages="";
        for (String mes: getChatRoomByName(roomName).getMessages(userName)) {
            messages+=mes+separator;
        }
        System.out.println(messages + " x");
        return  messages;
    }


    public String joinChat(String userName, String roomName) {
        getChatRoomByName(roomName).joinChat(userName);
        return "";
    }

    @Override
    public String getPreviousMessages(String roomName,String separator) {
        String prevMes="";
        for (String mes:getChatRoomByName(roomName).getPreviousMessages()) {
            prevMes+=mes+separator;

        }
        System.out.println(prevMes);
        return prevMes;
    }

    private ChatRoom getChatRoomByName(String name){
        for (ChatRoom chatRoom: rooms) {
            if(chatRoom.getRoomName().equals(name)){
                return chatRoom;
            }
        }
        return null;
    }

    public String getChatRoomNames(String separator){
        String names="";
        for (ChatRoom room: rooms) {
            names+=room.getRoomName()+separator;
        }
        return names;
    }

    @Override
    public String quit(String userName, String roomName) {
        getChatRoomByName(roomName).quit(userName);
        return "";
    }
    public boolean checkUsernameAvailability(String username){
        boolean isAvailable = true;
        for (ChatRoom room: rooms) {
            if(!room.checkUsernameAvailability(username)){
                isAvailable=false;
            }
        }
        return isAvailable;
    }
}
