package beans;

import java.io.Serializable;

/*
create table ACTIONITEM 
(
   MEETINGALIAS         VARCHAR2(10)         not null,
   ACTIONID             INTEGER              not null,
   USERLOGIN            VARCHAR2(40)         not null,
   TASK                 VARCHAR2(250)        not null,
   DONE                 NUMBER(1)            not null,
   constraint PK_ACTIONITEM primary key (MEETINGALIAS, USERLOGIN, ACTIONID)
);
*/

public class ActionItem implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String meetingalias;
    private int actionid;
    private String userlogin;
    private String task;
    private int done;

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
     * @return the actionid
     */
    public int getActionid() {
        return actionid;
    }

    /**
     * @param actionid the actionid to set
     */
    public void setActionid(int actionid) {
        this.actionid = actionid;
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
     * @return the task
     */
    public String getTask() {
        return task;
    }

    /**
     * @param task the task to set
     */
    public void setTask(String task) {
        this.task = task;
    }

    /**
     * @return the done
     */
    public int getDone() {
        return done;
    }

    /**
     * @param done the done to set
     */
    public void setDone(int done) {
        this.done = done;
    }
    
    public String toBean() {
        return meetingalias+" "+actionid+" "+task;
    }
    
    public String toString() {
        String reply = "";
        reply+=actionid+" \t";
        reply+=userlogin+" \t";
        reply+=meetingalias+" \t";
        reply+=task+" \t";
        if (done == 1) reply+= "YES";
        return reply;
    }
    
}
