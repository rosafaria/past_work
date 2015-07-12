
package beans;

import java.io.Serializable;

public class Request implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String userstatus;
    private String groupMeet;
    private int type; //0 - user/group, 1 - user/meeting, 2 - meeting/status
    
    public String getUserstatus() {
        return userstatus;
    }

    public void setUserstatus(String userstatus) {
        this.userstatus = userstatus;
    }

    public String getGroupMeet() {
        return groupMeet;
    }

    public void setGroupMeet(String groupMeet) {
        this.groupMeet = groupMeet;
    }
    
    @Override
    public String toString() {
        return groupMeet + "\t"+userstatus;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


}
