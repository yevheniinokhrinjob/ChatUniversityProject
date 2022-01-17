package com.nokhrin.chatclient.gui;


import com.nokhrin.chatclient.XmlRpcMethods;
import com.nokhrin.chatclient.service.ChatService;
import org.apache.xmlrpc.XmlRpcClient;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

@Component
public class ChatUserForm extends JFrame {

    private ChatService chatService;
    private XmlRpcClient client;
    private JTextArea textArea = new JTextArea();
    private String activeRoomName="Room1";
    private JButton sendButton;
    private JPanel panel;
    private JTextField messageField;
    private boolean newRoomSelected=true;
    private String selectedProtocol="burlap";
    public ChatUserForm(ChatService chatService){
        super( "Chat");
        this.chatService = chatService;
        this.setBounds(50,100,1000,800);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void init(String username){
        JButton protocolButton = new JButton("Change to xml");
        protocolButton.setBounds(790,500,170,60);
        protocolButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(selectedProtocol.equals("burlap")){
                    selectedProtocol="xml";
                    protocolButton.setText("Change to burlap");
                }else if(selectedProtocol.equals("xml")){
                    selectedProtocol="burlap";
                    protocolButton.setText("Change to xml");
                }
            }
        });
        textArea=createTextArea(20,20,750,650,16);
        sendButton = new JButton("Send");
        sendButton.setBounds(880,700,80,16);
        sendButton.addActionListener(new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(selectedProtocol.equals("burlap")) {
                            chatService.sendMessage(messageField.getText(), username, activeRoomName);
                        }
                        else if(selectedProtocol.equals("xml")){
                            XmlRpcMethods.sendMessage(client,messageField.getText(), username, activeRoomName);

                        }
                        messageField.setText("");
                    }
                }
        );
        messageField = createTextField(20,700,800,20);
        JScrollPane scroll = new JScrollPane ();
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);


        scroll.setBounds(20,20,750,650);
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.getViewport().add(textArea);
        panel = new JPanel();
        panel.setLayout(null);
        panel.add(protocolButton);
        panel.add(sendButton);
        panel.add(scroll);
        panel.add(messageField);
        //panel.add(textArea);
        add(panel);
    }


    private JTextArea createTextArea(int x,int y,int wight,int height, int size) {
    JTextArea jTextArea = new JTextArea();
        jTextArea.setBounds(x,y,wight,height);
        jTextArea.setFont(new Font("Serif", Font.PLAIN, size));
        jTextArea.setLineWrap(true);
        jTextArea.setEditable(false);
        jTextArea.setWrapStyleWord(true);
        return jTextArea;
    }

    public void showMessages(List<String> messages){
          messages.forEach(message->{
             textArea.append(message + "\n");
          });

    }
    private JTextField createTextField(int x, int y, int wight, int height){
        JTextField someTextField = new JTextField();
        someTextField.setBounds(x,y,wight,height);
        return someTextField;
    }

    public String selectUsername(){
        String username = (String) JOptionPane.showInputDialog(this,"Set username","",
                JOptionPane.PLAIN_MESSAGE,null,null,"");
        return username;
    }

    public void setAvailableChatRooms(List<String> roomNames, String userName){
        int removalValue=0;
        for (String name: roomNames) {
            JButton button = new JButton(name);
            button.setBounds(790,20+removalValue,170,40);
            button.addActionListener(new ActionListener()
                                         {
                                             @Override
                                             public void actionPerformed(ActionEvent e) {
                                                 textArea.selectAll();
                                                 textArea.replaceSelection("");
                                              //   chatService.quit(userName,activeRoomName);
                                                // chatService.joinChat(userName,activeRoomName);
                                                 newRoomSelected=true;
                                                 activeRoomName=name;
                                             }
                                         }
            );
            panel.add(button);
            removalValue=removalValue+45;
        }

    }

    public String getActiveRoomName() {
        return activeRoomName;
    }

    public boolean isNewRoomSelected() {
        return newRoomSelected;
    }

    public void setNewRoomSelected(boolean newRoomSelected) {
        this.newRoomSelected = newRoomSelected;
    }

    public String getSelectedProtocol() {
        return selectedProtocol;
    }

    public void setClient(XmlRpcClient client) {
        this.client = client;
    }
}

