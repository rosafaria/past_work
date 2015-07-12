package beans;

import java.io.Serializable;

public class ChatMessage implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String userlogin;
    private String text;
    private String timemsg;
    private String meetingalias;
    private int agendaid;

    /**
     * @return the userlogin
     */
    public String getUserlogin() {
        return userlogin;
    }

    /**
     * @param userlogin the userlogin to set
     */
    public void setUserlogin(String userlogin) {
        this.userlogin = userlogin;
    }

    /**
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * @param text the text to set
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * @return the timemsg
     */
    public String getTimemsg() {
        return timemsg;
    }

    /**
     * @param timemsg the timemsg to set
     */
    public void setTimemsg(String timemsg) {
        this.timemsg = timemsg;
    }

    /**
     * @return the meetingalias
     */
    public String getMeetingalias() {
        return meetingalias;
    }

    /**
     * @param meetingalias the meetingalias to set
     */
    public void setMeetingalias(String meetingalias) {
        this.meetingalias = meetingalias;
    }

    /**
     * @return the agendaid
     */
    public int getAgendaid() {
        return agendaid;
    }

    /**
     * @param agendaid the agendaid to set
     */
    public void setAgendaid(int agendaid) {
        this.agendaid = agendaid;
    }
    
    public String toString() {
        return userlogin + " ("+timemsg+"): "+text;
    }
}
