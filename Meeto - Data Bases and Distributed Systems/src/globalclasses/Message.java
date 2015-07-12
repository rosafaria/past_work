package globalclasses;

import java.io.Serializable;
import java.util.ArrayList;

public class Message implements Serializable{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private int id;
    private ArrayList<Object> msg;
    private String user;            // client
    private int result;
        
    public Message(int id, ArrayList<Object> msg, String username, int result) {
        this.id = id;
        this.msg = msg;
        this.user = username;
        this.result = result;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    public void setMsg(ArrayList<Object> msg) {
            this.msg = msg;
    }

    /**
     * @return the msg
     */
    public ArrayList<Object> getMsg() {
        return msg;
    }

    public int getResult() {
        return result;
    }

    /**
     * @return the user
     */
    public String getUser() {
        return user;
    }
    
    @Override
    public String toString() {
        if ((msg == null)||(msg.isEmpty())) return "";
        String reply = "";
        for (Object msg1 : msg) {
            reply += msg1.toString() + "\n";
        }
        return reply;
    }
}
