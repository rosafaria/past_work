package graphicInterface;

import globalclasses.Message;
import globalclasses.TCPMsg;
import interfaces.UIWindow;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import javax.swing.JFrame;


public class UIClient {
    private ArrayList<UIWindow> windows;
    private String username;
    private String chatMeet;
    private int chatItem;
    private Chat chat;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    
    public UIClient(PipedInputStream pin, PipedOutputStream pout){
        try {
            oos = new ObjectOutputStream(pout);
            ois = new ObjectInputStream(pin);
        } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
        }
        UIWindow login = new Login(this);
        this.windows = new ArrayList<UIWindow>();
        this.windows.add(login);
        login.setVisible(true);
    }
    
    
    public Message sendTCP(int operation, ArrayList<Object> parameters) {
	if ((operation!=1)&&(operation!=2)&&(parameters!=null))
            parameters.add(0,username);
        try {
            TCPMsg msg = new TCPMsg(operation, parameters);
            oos.writeObject(msg);
            if (operation !=51) {
                return (Message) ois.readObject();
            }
        } catch (IOException | ClassNotFoundException e) {
                
        }
        return new Message(0, null, "", -4);
    }
    
    public void addWindow(UIWindow newWindow) {
        windows.add(newWindow);
    }
    
    public UIWindow removeWindow() {
        windows.remove(windows.size()-1);
        return windows.get(windows.size()-1);
    }
    
    public UIWindow lastWindow() {
        return windows.get(windows.size()-1);
    }
    
    public void setUser (String username) {
        this.username = username;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setChat(Chat janela, String meetingID, int itemID) {
        this.chat = janela;
        this.chatMeet = meetingID;
        this.chatItem = itemID;
        System.out.println("Chat set");
    }
    
    public void sendChatMsg (String msg) {
        String[] msgParts = msg.split("\n");
        if ((msgParts.length==2)&&(msgParts[0].trim().equals(chatMeet))) {
            System.out.println("olaaaaaaa");
            int a;
            try{
                a = Integer.parseInt(msgParts[1]);
            } catch (NumberFormatException e) {return;}
            for (int i = 0; i<windows.size();i++) {
                windows.get(i).setClosed(a);
            }
            return;
        }
        if (msgParts[0].equals(chatMeet) && msgParts[1].equals(""+chatItem)) {
            chat.printMsg(msgParts[2]);
        }
    }
    
    public boolean killMe() {
        windows.get(windows.size()-1).setVisible(false);
        for (int i = windows.size()-1; i>=0;i--) {
            windows.get(i).dispose();
        }
        System.exit(0);
        return true;
    }
}

