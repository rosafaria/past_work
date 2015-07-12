package interfaces;

import globalclasses.Message;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Rosa
 */
public interface Meeto_RMI_S_I extends Remote {

    //1
    public Message login(String sender, String pass, int msgId) throws RemoteException;
    //2
    public Message registerUser (String sender, String name, String pass, int msgId) throws RemoteException;
    //37
    public Message listAllUsers(String sender, int msgId) throws RemoteException;
    
    /*Group functions*/
    
    //3
    public Message createGroup(String sender, String groupname, boolean open, int msgId) throws RemoteException;
    //5
    public Message checkPermission(String sender, String groupname, boolean open, int msgId) throws RemoteException;
    //6
    public Message listMembers(String sender, String groupname, boolean admin, int msgId) throws RemoteException;
    //7
    public Message listGroups(String sender, int msgId) throws RemoteException;
    //9
    public Message addUserToGroup(String sender, String groupname, String username,int msgId)throws RemoteException;
    //10
    public Message deleteUserFromGroup (String sender, String groupname, String username,int msgId)throws RemoteException;
    //11
    public Message makeAdmin(String sender, String groupname, String username, int msgId)throws RemoteException;
    //12
    public Message deleteAdmin(String sender, String groupname, String username, int msgId)throws RemoteException;
    //13
    public Message checkGroupRequests (String sender, int msgId)throws RemoteException;
    //14
    public Message acceptUserRequest(String sender, String groupname, String username, int msgId)throws RemoteException;
    
    /*Meeting functions*/

    //15
    public Message createMeeting(String sender, String alias, String title, String outcome, String date, String location, int msgId) throws RemoteException;
    //16
    public Message listMyMeetings(String sender, int msgId) throws RemoteException;
    //17
    public Message listMyActions(String sender, int msgId) throws RemoteException;
    //18
    public Message acceptMeeting(String sender, String meetingId, boolean decision, int msgId) throws RemoteException;
        /*decision = true - accept; false - decline*/
    //19
    public Message getMeetings(String sender, int time, int msgId) throws RemoteException;
        /*time = 1 para future, time = 0 para current, time = -1 para past*/
    //20
    public Message listMeetUsers(String sender, String meetingId, int msgId) throws RemoteException;
    //37
    public Message validateUser(String sender, String meetingId, int msgId) throws RemoteException;
        /*returns answer to whether the user is invited and has permission or not*/
    
    /*Functions within the meeting, user is validated*/

    //21
    public Message inviteUserMeeting(String sender,String usergroupname, String meetingID, int msgId) throws RemoteException;
    //22
    public Message listAgendaItems(String sender, String meetingID, int msgId) throws RemoteException;
    //23
    public Message changeAgendaItem(String sender, String meetingId, String newDescription,
                    int id, int type, int msgId) throws RemoteException;	// type=-1 for delete, 0 for modify
    //4
    public Message addAgendaItem(String sender, String meetingalias, String title, String description, int msgId) throws RemoteException;
    //24
    public Message closeMeetingAgenda(String sender,String meetingId, int msgId) throws RemoteException;
    //25
    public Message listResults(String sender, int type, String meetingID, int msgId) throws RemoteException;
    //26
    public Message addActionItem(String sender, String meetingId, String group, String description, int msgId) throws RemoteException;
    //27
    public Message completeAction (String sender, int actionid, String meetingID, int msgId) throws RemoteException;
    //28
    public Message addKeyDecison(String sender, int agendaItem, String decision, String meetingId, int msgId) throws RemoteException;
    //29
    public Message closeMeeting(String sender, String meetingId, int msgId) throws RemoteException;
    
    public Message agendaHistory(String sender, String meetingalias, int agendaId, int msgId) throws RemoteException;
    /*Chat functions*/

    //30
    public Message addChatComment(String user,String meetingId, int agendaId,String txt, int msgId) throws RemoteException;

    //31
    public Message subscribeChat(String user, String meetingId, int agendaItem, int msgId) throws RemoteException;

    
    /*TCP server connections*/

    public void subscribe(String name, Meeto_RMI_C_I client) throws RemoteException;

    public boolean registerTCPServer() throws RemoteException;
    
}