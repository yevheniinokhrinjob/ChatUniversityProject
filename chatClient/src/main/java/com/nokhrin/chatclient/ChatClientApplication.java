package com.nokhrin.chatclient;

import com.nokhrin.chatclient.gui.ChatUserForm;
import com.nokhrin.chatclient.service.ChatService;
import org.apache.xmlrpc.XmlRpcClient;
import org.springframework.remoting.caucho.BurlapProxyFactoryBean;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.MalformedURLException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Configuration
@SpringBootApplication
public class ChatClientApplication {

    @Bean
    public BurlapProxyFactoryBean hessianInvoker() {
        BurlapProxyFactoryBean invoker = new BurlapProxyFactoryBean();
        invoker.setServiceUrl("http://localhost:8081/chatServer");
        invoker.setServiceInterface(ChatService.class);
        return invoker;
    }
    public static void main(String[] args) throws MalformedURLException, InterruptedException {
        ConfigurableApplicationContext context = new SpringApplicationBuilder(
                ChatClientApplication.class).headless(false).run(args);

        ChatService chat = context.getBean(ChatService.class);
        String separator ="<<";
        XmlRpcClient xmlClient = new XmlRpcClient("http://localhost:8082/chatService");

        String userName;
        String roomName="Room1";

        ChatUserForm userForm = new ChatUserForm(chat);
        userForm.setClient(xmlClient);
        while (true) {
            userName = userForm.selectUsername();
            System.out.println(userName);
            if (userName == null) {
                System.exit(0);
            }
            if (userName != null && !userName.isEmpty() && chat.checkUsernameAvailability(userName)) {
                break;
            }
            continue;
        }
        userForm.init(userName);
        userForm.setAvailableChatRooms(splitStringToArray(chat.getChatRoomNames(separator),separator), userName);
        userForm.setVisible(true);

        while (true){
            if(userForm.getSelectedProtocol().equals("burlap")) {
                if (userForm.isNewRoomSelected()) {
                    chat.joinChat(userName, roomName);
                    userForm.showMessages(splitStringToArray(chat.getPreviousMessages(roomName, separator), separator));
                    userForm.setNewRoomSelected(false);
                }
                userForm.showMessages(splitStringToArray(chat.getMessages(userName, roomName, separator), separator));
                TimeUnit.MILLISECONDS.sleep(100);
                if (!userForm.getActiveRoomName().equals(roomName)) {
                    chat.quit(userName, roomName);
                    roomName = userForm.getActiveRoomName();
                }
            }else {
                if (userForm.isNewRoomSelected()) {
                    XmlRpcMethods.joinChat(xmlClient,userName, roomName);
                    userForm.showMessages(splitStringToArray(XmlRpcMethods.getPreviousMessages(xmlClient,roomName, separator), separator));
                    userForm.setNewRoomSelected(false);
                }
                userForm.showMessages(splitStringToArray(XmlRpcMethods.getMessages(xmlClient,userName, roomName, separator), separator));
                TimeUnit.MILLISECONDS.sleep(100);
                if (!userForm.getActiveRoomName().equals(roomName)) {
                    XmlRpcMethods.quit(xmlClient, userName, roomName);
                    roomName = userForm.getActiveRoomName();
                }
            }

        }
    }

    private static List<String> splitStringToArray(String s,String separator){
        if(s.equals("")){
            return new ArrayList<>();
        }
        return   Arrays.asList(s.split(separator));

    }


}
