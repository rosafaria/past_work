
package beans;

import java.io.Serializable;

public class AgendaItem implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String meetingalias;
    private String itemtitle;
    private int itemnumber;
    private String description;
    private String userlogin;
    private int agendaid;
    private int previd;

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
     * @return the itemtitle
     */
    public String getItemtitle() {
        return itemtitle;
    }

    /**
     * @param itemtitle the itemtitle to set
     */
    public void setItemtitle(String itemtitle) {
        this.itemtitle = itemtitle;
    }

    /**
     * @return the itemnumber
     */
    public int getItemnumber() {
        return itemnumber;
    }

    /**
     * @param itemnumber the itemnumber to set
     */
    public void setItemnumber(int itemnumber) {
        this.itemnumber = itemnumber;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

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

    /**
     * @return the previd
     */
    public int getPrevid() {
        return previd;
    }

    /**
     * @param previd the previd to set
     */
    public void setPrevid(int previd) {
        this.previd = previd;
    }
    
    public String toHistory() {
        return itemnumber+"\t"+itemtitle+"\t"+userlogin+"\t"+description;
    }
    
    public String toString() {
        return itemnumber+"\t"+itemtitle+"\t"+description;
    }
}
