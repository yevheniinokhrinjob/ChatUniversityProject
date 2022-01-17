package com.nokhrin.chatserver;

import com.nokhrin.chatserver.model.ChatUser;
import com.nokhrin.chatserver.model.Message;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.time.temporal.ChronoUnit.SECONDS;

public class ChatRoom {
    private String roomName;
    private List<Message> messages = new ArrayList<>();
    private List<ChatUser> chatUsers = new ArrayList<>();
    private Queue<Message> recentMessages = new LinkedList<>();
    private int queueSize=3;
    private HashMap<String, LocalDateTime> lastRequest;

    public ChatRoom (String roomName){

        lastRequest = new HashMap<>();
        Timer t = new Timer();
        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                removeNotResponseUsers();
            };
        };
        t.scheduleAtFixedRate(tt,0,5000);
        this.roomName=roomName;
    }


    public void sendMessage(String text, String userName) {
        Message newMessage=createMessage(text,userName);
        addMessageToQueue(newMessage);
        //  System.out.println();
        messages.add(newMessage);
    }
    public List<String> getPreviousMessages(){
        List<String> messagesToReceive = new ArrayList<>();
        for (Message m: recentMessages) {
            messagesToReceive.add(getMessageInString(m));
        }

        return messagesToReceive;
    }
    public List<String> getMessages(String userName) {
        List<String> messagesToReceive = new ArrayList<>();
        lastRequest.put(userName, LocalDateTime.now());
        ChatUser user = getUserByName(userName);
            // System.out.println(user.getLastMessageReceivedId() + " "+messages.size());

        for (int i = user.getLastMessageReceivedId(); i < messages.size(); i++) {
            messagesToReceive.add(getMessageInString(messages.get(i)));
        }
        user.setLastMessageReceivedId(messages.size());
        return messagesToReceive;
    }

    public void joinChat(String userName){
        ChatUser chatUser = new ChatUser();
        chatUser.setLogin(userName);
        chatUser.setLastMessageReceivedId(messages.size());
        messages.add(createMessage(userName + " joined chat","System"));
        chatUsers.add(chatUser);
    }
    private String getMessageInString(Message someMessage){
        String messageExample="";
        messageExample=someMessage.getTime()+ " " + someMessage.getSender()+": "+someMessage.getText();
        return messageExample;
    }

    private Message createMessage(String text, String sender){
        Message message = new Message();
        message.setSender(sender);
        message.setText(text);
        message.setId(messages.size());
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        String time  = dtf.format(LocalDateTime.now());
        message.setTime(time);
        return message;
    }

    private void addMessageToQueue(Message someMessage){
        if(recentMessages.size()==queueSize){
            recentMessages.poll();
        }
        recentMessages.add(someMessage);
    }

    private ChatUser getUserByName(String login){
        ChatUser user = new ChatUser();
        for (ChatUser chatUser: chatUsers) {
            if(chatUser.getLogin().equals(login)){
                user = chatUser;
            }
        }
        return user;
    }
    private void removeNotResponseUsers(){
        List<String> usersToDelete = new ArrayList<String>();
        lastRequest.forEach((k, v) -> {
            long secDifference = Math.abs(SECONDS.between(LocalDateTime.now(), v));
            if(secDifference>3){
                //
                usersToDelete.add(k);
            }
        });
        usersToDelete.forEach(user->quit(user));
    }
    public void quit(String userName){
        System.out.println("quit" + userName);
        lastRequest.remove(userName);
        chatUsers.remove(getUserByName(userName));
        messages.add(createMessage(userName + " left the chat","System"));
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
    public boolean checkUsernameAvailability(String username){
        boolean isAvailable=true;
        for (ChatUser chatUser: chatUsers) {
            if(chatUser.getLogin().equals(username)){
                isAvailable=false;
            }
        }
        return isAvailable;
    }
}
