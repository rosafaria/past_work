package beans;

import java.io.Serializable;

/*
create table MEETING 
(
   MEETINGALIAS         VARCHAR2(10)         not null,
   TITLE                VARCHAR2(30)         not null,
   OUTCOME              VARCHAR2(250)        not null,
   LOCATION             VARCHAR2(75)         not null,
   DATETIME             TIMESTAMP            not null,
   CLOSED               NUMBER(1)            not null,
   CREATOR              VARCHAR2(10)         not null,
   ITEMNUM              INTEGER,
   constraint PK_MEETING primary key (MEETINGALIAS)
);
*/

public class Meeting implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String meetingalias;
    private String title;
    private String outcome;
    private String location;
    private String datetime;
    private int closed;
    private String creator;

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
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the outcome
     */
    public String getOutcome() {
        return outcome;
    }

    /**
     * @param outcome the outcome to set
     */
    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return the datetime
     */
    public String getDatetime() {
        return datetime;
    }

    /**
     * @param datetime the datetime to set
     */
    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    /**
     * @return the closed
     */
    public int getClosed() {
        return closed;
    }

    /**
     * @param closed the closed to set
     */
    public void setClosed(int closed) {
        this.closed = closed;
    }

    /**
     * @return the creator
     */
    public String getCreator() {
        return creator;
    }

    /**
     * @param creator the creator to set
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }
    
    public String toString() {
        String reply = "";
        reply+=meetingalias+"\t";
        reply+=title+"\t";
        reply+=outcome+"\t";
        reply+=location+"\t";
        reply+=datetime+"\t";
        reply+=creator+"\t";
        if (closed == 1) reply+= "Closed";
        else {
            if (closed == 2) reply+= "Ended";
        }
        return reply;
    }
    
}
