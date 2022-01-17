package com.nokhrin.chatserver;

import com.nokhrin.chatserver.service.ChatService;
import com.nokhrin.chatserver.service.ChatServiceImpl;
import org.apache.xmlrpc.WebServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.remoting.caucho.BurlapServiceExporter;
import org.springframework.remoting.support.RemoteExporter;

@SpringBootApplication
public class ChatServerApplication {
    public static ChatServiceImpl chatService = new ChatServiceImpl();
    public static void main(String[] args) {
        SpringApplication.run(ChatServerApplication.class, args);
        initialiseXmlRpc();
    }

    @Bean(name = "/chatServer")
    public RemoteExporter hessianServiceExporter() {

        BurlapServiceExporter exporter = new BurlapServiceExporter();
        exporter.setService(chatService);
        exporter.setServiceInterface(ChatService.class);
        return exporter;
    }

    private static void initialiseXmlRpc(){
        WebServer server = new org.apache.xmlrpc.WebServer(8082);
        server.addHandler("chatService", chatService);
        server.start();
    }
}
