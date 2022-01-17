package com.nokhrin.chatclient;

import org.apache.xmlrpc.XmlRpcClient;
import org.apache.xmlrpc.XmlRpcException;

import java.io.IOException;
import java.util.Vector;

public class XmlRpcMethods {

    public static void sendMessage(XmlRpcClient client, String message, String userName, String roomName) {
        Vector vector = new Vector();
        vector.addElement(message);
        vector.addElement(userName);
        vector.addElement(roomName);
        try {
            Object object = client.execute("chatService.sendMessage", vector);
        } catch (XmlRpcException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String getMessages(XmlRpcClient client, String userName, String roomName, String separator) {
        Vector vector = new Vector();
        vector.addElement(userName);
        vector.addElement(roomName);
        vector.addElement(separator);
        try {
            Object object = client.execute("chatService.getMessages", vector);
            return object.toString();
        } catch (XmlRpcException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
    public static void joinChat(XmlRpcClient client, String userName,String roomName) {
        Vector vector = new Vector();
        vector.addElement(userName);
        vector.addElement(roomName);
        try {
            Object object = client.execute("chatService.joinChat", vector);
        } catch (XmlRpcException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String getPreviousMessages(XmlRpcClient client, String roomName, String separator) {
        Vector vector = new Vector();
        vector.addElement(roomName);
        vector.addElement(separator);
        try {
            Object object = client.execute("chatService.getPreviousMessages", vector);
            return object.toString();
        } catch (XmlRpcException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
    public static String getChatRoomNames(XmlRpcClient client, String separator) {
        Vector vector = new Vector();
        vector.addElement(separator);

        try {
            Object object = client.execute("chatService.getChatRoomNames", vector);
            return object.toString();
        } catch (XmlRpcException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
    public static void quit(XmlRpcClient client, String userName,String roomName) {
        Vector vector = new Vector();
        vector.addElement(userName);
        vector.addElement(roomName);
        try {
            Object object = client.execute("chatService.quit", vector);
        } catch (XmlRpcException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
   
}
