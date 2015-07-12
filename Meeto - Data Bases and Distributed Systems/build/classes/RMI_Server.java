
import globalclasses.GValuesRMIServer;
import globalclasses.Message;
import interfaces.Meeto_RMI_C_I;
import interfaces.Meeto_RMI_S_I;

import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

import beans.*;


public class RMI_Server extends UnicastRemoteObject implements Meeto_RMI_S_I{
	
    private static final long serialVersionUID = 1L;
    private Connection dbconnection;
    private Meeto_RMI_C_I client;
    private GValuesRMIServer gv = new GValuesRMIServer("MeetoRMIServer.cfg");
    private Message lastChat;
    private ArrayList<String> lastChatUsers;
    
    public RMI_Server() throws RemoteException {
        super();
    }
    
    private Connection connect() {
        Connection connection;
 
        String dbURL = gv.getDbURL();
        String user = gv.getDbUser();
        String pass = gv.getDbPass();

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
	} catch (ClassNotFoundException e) {
            System.out.println("Error: Oracle JDBC Driver missing");
            return null;
	}
        
        try {
            connection = DriverManager.getConnection(dbURL, user, pass);
            connection.setAutoCommit(false);
        } catch (SQLException | NullPointerException e) {
            System.out.println("Error: Database connection failed");
            return null;
        }
        
        
        return connection;
    }
    
    public static void main(String args[]) {
        System.getProperties().put("java.security.policy", "policy.all");
        System.setSecurityManager(new RMISecurityManager());
        RMI_Server rmi;
        try {
            rmi = new RMI_Server();
            LocateRegistry.createRegistry(rmi.gv.getRmiRegistryPort()).rebind("Database", rmi);
            rmi.dbconnection = rmi.connect();
            if (rmi.dbconnection !=null){
                System.out.println("RMI Server ready.");
            }
        } catch (Exception re) {
            System.out.println("Exception in RMIServer.main: " + re);
        }
    }
    
    @Override
    public Message login(String username, String pass, int msgId) {
        ResultSet result = null;
        PreparedStatement ps = null;
        ArrayList<Object> results = new ArrayList();
        String sql = "SELECT * FROM users WHERE userlogin = ? AND password = ?";
        try {
            ps = dbconnection.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2,pass);
            result = ps.executeQuery();
            result.next();
            if (result.getRow()==0) {
                results.add("User/Password Incorrect");
                result.close();
                ps.close();
                return new Message(msgId,results, username, -1);
            }
            result.close();
            ps.close();
        } catch (SQLException ex) { 
            System.out.println("Login error: "+ex.getMessage());
            results.add("Database Error: "+ex.getMessage());
            try {
                ps.close();
                result.close();
            } catch (SQLException | NullPointerException ex1) {}
            return new Message(msgId,results, username, -1);
        }
        results.add("Sucess");
        return new Message(msgId,results, username, 1);
    }

    @Override    
    public Message registerUser(String username, String name, String pass, int msgId) {
        /*Check if username is in use*/
        ResultSet result = null;
        PreparedStatement ps = null;
        ArrayList<Object> results = new ArrayList();
        String sql = "SELECT * FROM users WHERE userlogin = ? ";
        try {
            ps = dbconnection.prepareStatement(sql);
            ps.setString(1, username);
            result = ps.executeQuery();
            result.next();
            if (result.getRow()!=0)  {
                if (result.getString(1).equals(name)&&result.getString(3).equals(pass)) {
                    results.add((Object)"User inserted in database");
                    ps.close();
                    result.close();
                    return new Message(msgId,results, username,0);
                }
                else {
                    results.add((Object)"Username in use");
                    ps.close();
                    result.close();
                    return new Message(msgId,results, username,-1);
                }
            }
            ps.close();
            result.close();
        } catch (SQLException ex) {
            System.out.println("Register error (not fatal): "+ex.getMessage());
            try {
                if (result!=null) result.close();
                if (ps!=null) ps.close();
            } catch (SQLException e){}
        }
        
        /*Insert user in table users*/
        sql = "INSERT INTO users VALUES ( ? , ? , ? , '' , '')";
        
        try {
            ps = dbconnection.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, username);
            ps.setString(3, pass);
            ps.executeUpdate();
            dbconnection.commit();
            ps.close();
        } catch (SQLException ex) {
            System.out.println("Register error: "+ex.getMessage());
            results.add("Database Error: "+ex.getMessage());
            try {
                dbconnection.rollback();
                if (ps!=null) ps.close();
            } catch (SQLException ex1) {}
            return new Message(msgId,results, username,-1);
        }
        results.add("Welcome!");
        return new Message(msgId,results, username, 1);
    }

    @Override    
    public Message createGroup(String sender, String groupname, boolean open, int msgId) {
        ArrayList<Object> results = new ArrayList();
        
        /*Check if name is available*/
        ArrayList<String> res = getUsers(groupname);
        if ((res != null) && (!res.isEmpty())) {
            results.add("Name is already in use");
            return new Message(msgId,results, sender,-1);
        }
        
        /*Insert group into table usergroup*/
        String sql = "INSERT INTO usergroup VALUES ( ? , ? , ? )";
        PreparedStatement ps = null;
        
        try {
            ps = dbconnection.prepareStatement(sql);
            ps.setString(1, groupname);
            ps.setString(2, sender);
            if (open) ps.setInt(3,0);
            else ps.setInt(3,1);
            ps.executeUpdate();
            dbconnection.commit();
            ps.close();
        } catch (SQLException ex) { 
            System.out.println("Create group error: "+ex.getMessage());
            results.add("Database error: "+ex.getMessage());
            try{
                dbconnection.rollback();
                if (ps!=null) ps.close();
            } catch (SQLException e){}
            return new Message(msgId,results, sender, -2);
        }
        results.add("Success");
        return new Message(msgId,results, sender, 1);
    }

    @Override
    public Message checkPermission(String sender, String groupname, boolean open, int msgId) {
        ArrayList<Object> results = new ArrayList();
        
        /*Check if group exists*/
        ResultSet result = null;
        PreparedStatement ps = null;
        String sql = "SELECT creator, closed FROM usergroup WHERE groupname = ? ";
        try {
            ps = dbconnection.prepareStatement(sql);
            ps.setString(1,groupname);
            result = ps.executeQuery();
            result.next();
            if (result.getRow()==0) {
                results.add("Group not found");
                result.close();
                ps.close();
                return new Message(msgId,results, sender, -2);
            }
            if ((result.getInt("closed") == 0)&& open) {
                results.add("Open group");
                result.close();
                ps.close();
                return new Message(msgId,results, sender,0);
            }
            if (result.getString("creator").equals(sender.trim())) {
                results.add("User is creator");
                result.close();
                ps.close();
                return new Message(msgId,results, sender,3);
            }
            ps.close();
            result.close();
        } catch (SQLException ex) { 
            System.out.println("Check permission error: "+ex.getMessage());
            try {
                if (ps!=null) ps.close();
                if (result!=null) result.close();
            } catch (SQLException | NullPointerException ex1) {}
            results.add("Database error: "+ex.getMessage());
            return new Message(msgId,results, sender, -3);
        }
        /*Check if user is administrator*/
        sql = "SELECT admin FROM usergoup_user WHERE groupname = ? AND userlogin = ? ";
        try {
            ps = dbconnection.prepareStatement(sql);
            ps.setString(1,groupname);
            ps.setString(2,sender);
            result = ps.executeQuery();
            result.next();
            if (result.getRow()==0)  {
                ps.close();
                result.close();
                results.add("user not found associated with group");
                return new Message(msgId,results, sender,-1);
            }
            if (result.getInt("admin") == 0) {
                ps.close();
                result.close();
                results.add("administrative privileges are not activated for this user");
                return new Message(msgId,results, sender, 1);
            }
            if (result.getInt("admin") == -1) {
                ps.close();
                result.close();
                results.add("User is not validated by administrator");
                return new Message(msgId,results, sender,-1);
            }
            ps.close();
            result.close();
        } catch (SQLException ex) {
            try {
                if (result!=null) result.close();
                if (ps!=null) ps.close();
            } catch (SQLException | NullPointerException ex1) {}
            System.out.println("Check permission error: "+ex.getMessage());
            results.add("Database error: "+ex.getMessage());
            return new Message(msgId,results, sender, -1);
        }
        results.add("User is admin");
        return new Message(msgId,results, sender,2);
    }
    
    @Override
    public Message addUserToGroup(String sender, String groupname, String username,int msgId) {
        /*check if user is there*/
        ResultSet result = null;
        ArrayList<Object> results = new ArrayList();
        PreparedStatement ps = null;
        String sql = "SELECT admin FROM usergoup_user WHERE userlogin = ? AND groupname = ? ";
        try {
            ps = dbconnection.prepareStatement(sql);
            ps.setString(1,sender);
            ps.setString(2,groupname);
            result = ps.executeQuery();
            result.next();
            if (result.getRow()!=0) { //idempotente
                result.close();
            ps.close();
                results.add("Success");
                return new Message(msgId,results, username,0);
            }
            result.close();
            ps.close();
        } catch (SQLException ex) {
            System.out.println("addUserToGroup error (not fatal): "+ex.getMessage());
            try {
                if (result!=null) result.close();
                if (ps!=null) ps.close();
            }catch(SQLException e){}
        }
        
        Message perm = checkPermission(sender, groupname, true, msgId);
        int aux = perm.getResult();
        
        if ((aux>=2) || (aux==0)) /*requester is administrator or group is open*/
            sql = "INSERT INTO usergoup_user VALUES ( ? , ? , 0)";
        else if ((aux == 1)||(sender.equals(username))) /*add user as waiting for permission*/
            sql = "INSERT INTO usergoup_user VALUES ( ? , ? , -1)";
        else {
            return perm;
        }
        try {
            ps = dbconnection.prepareStatement(sql);
            ps.setString(1, username.trim());
            ps.setString(2, groupname.trim());
            ps.executeQuery();
            dbconnection.commit();
            ps.close();
        } catch (SQLException ex) { 
            try {
                dbconnection.rollback();
                if (ps!=null) ps.close();
            } catch (SQLException ex1){}
            System.out.println("addUserToGroup error: "+ex.getMessage());
            results.add("Database Error: "+ex.getMessage());
            return new Message(msgId,results, sender, -2);
        }
        results.add("Success");
        return new Message(msgId,results, sender,1);
    }
    
    @Override
    public Message deleteUserFromGroup (String sender, String groupname, String username,int msgId) {
        /*check if user is there*/
        ArrayList<Object> results = new ArrayList();
        ResultSet result = null;
        PreparedStatement ps = null;
        String sql = "SELECT * FROM usergoup_user WHERE userlogin = ? AND groupname = ? FOR UPDATE";
        try {
            ps = dbconnection.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2,groupname);
            result = ps.executeQuery();
            result.next();
            if (result.getRow()==0) {
                results.add("user not in group");
                result.close();
            ps.close();
                return new Message(msgId,results, username, -2);
            }
            result.close();
            ps.close();
        } catch (SQLException ex) { 
            System.out.println("deleteUserFromGroup error (not fatal): "+ex.getMessage());
        }
        Message perm = checkPermission(sender, groupname, false, msgId);
        if ((perm.getResult()<2)&&(!sender.equals(username))) {
            try {
                dbconnection.rollback();
            } catch(SQLException e) {}
            return perm;
        }
        
        sql = "DELETE FROM usergoup_user WHERE groupname = ? and userlogin = ? ";
        try {
            ps = dbconnection.prepareStatement(sql);
            ps.setString(1, groupname.trim());
            ps.setString(2, username.trim());
            ps.executeUpdate();
            dbconnection.commit();
            ps.close();
        } catch (SQLException ex) { 
            System.out.println("deleteUserFromGroup error: "+ex.getMessage());
            try {
                dbconnection.rollback();
                if (ps!=null) ps.close();
            } catch(SQLException e){}
            results.add("Database error: "+ex.getMessage());
            return new Message(msgId,results, sender, -3);
        }
        results.add("Success");
        return new Message(msgId,results, sender, 1);
    }

    @Override
    public Message listMembers(String sender, String groupname, boolean admin, int msgId) {
        ArrayList<Object> results = new ArrayList();
        ResultSet result = null;
        PreparedStatement ps = null;
        String sql = "SELECT userlogin, admin FROM usergoup_user WHERE groupname = ? AND admin <>-1 "
                +"UNION SELECT creator, 2 FROM usergroup WHERE groupname = ? ";
        try {
            ps = dbconnection.prepareStatement(sql);
            ps.setString(1, groupname);
            ps.setString(2, groupname);
            result = ps.executeQuery();
            result.next();
            if (result.getRow()==0) {
                result.close();
                ps.close();
                results.add("No members found");
                return new Message(msgId,results, sender,-1);
            }
            else {
                do {
                    if (admin)
                        results.add(result.getString(1)+"\t"+result.getInt(2));
                    else results.add(result.getString(1));
                } while (result.next());
                result.close();
                ps.close();
                return new Message(msgId,results, sender, 1);
            }
        } catch (SQLException ex) {  
            System.out.println("listMembers error: "+ex.getMessage()); 
            try {
                if (result!=null) result.close();
                if (ps!=null) ps.close();
            } catch (SQLException | NullPointerException ex1) {}
            results.add("database error: "+ex.getMessage());
            return new Message(msgId,results, sender, -1);
        }
    }

    @Override
    public Message listGroups(String sender, int msgId) {
        ResultSet result = null;
        ArrayList<Object> results = new ArrayList();
        String sql = "SELECT groupname FROM usergroup";
        try {
            result = dbconnection.createStatement().executeQuery(sql);
            result.next();
            if (result.getRow()==0) {
                result.close();
                results.add("No groups found");
                return new Message(msgId,results, sender, -1);
            }
            else {
                do {
                    results.add(result.getString(1));
                } while (result.next());
                result.close();
                return new Message(msgId,results, sender, 1);
            }
        } catch (SQLException ex) {  
            System.out.println("listGroups error: " + ex.getMessage());
            try {
                result.close();
            } catch (SQLException | NullPointerException ex1) {}
            results.add("Database error: "+ex.getMessage());
            return new Message(msgId,results, sender, -1);
        }
    }
    
    @Override
    public Message makeAdmin(String sender, String groupname, String username, int msgId) {
        ArrayList<Object> results = new ArrayList();
        PreparedStatement ps = null;
        Message perm = checkPermission(sender, groupname, false,msgId);
        if (perm.getResult()<2)
            return perm;
        
        ResultSet result = null;
        String sql = "SELECT admin FROM usergoup_user WHERE userlogin = ? AND groupname = ? FOR UPDATE";
        try {
            ps = dbconnection.prepareStatement(sql);
            ps.setString(1,username);
            ps.setString(2, groupname);
            result = ps.executeQuery();
            result.next();
            if (result.getRow()==0) {
                results.add("User doesn't belong in the group.");
                try {
                    dbconnection.rollback();
                    result.close(); 
                } catch (SQLException ex1) {}
                return new Message(msgId,results, username, -1);
            }
            result.close();
            ps.close();
            sql = "UPDATE usergoup_user SET admin = 1 WHERE userlogin = ? ";
            ps = dbconnection.prepareStatement(sql);
            ps.setString(1, username);
            ps.executeUpdate();
            dbconnection.commit();
            ps.close();
        } catch (SQLException ex) {  
            try {
                dbconnection.rollback();
                if (result!=null) result.close();
                if (ps!=null) ps.close();
            } catch (SQLException | NullPointerException ex1) {}
            System.out.println("makeAdmin error: "+ex.getMessage());
            results.add("Database error: "+ex.getMessage());
            return new Message(msgId,results, username, -2);
        }
        try {
            result.close();
        } catch (SQLException | NullPointerException ex1) {}
        results.add("Success");
        return new Message(msgId,results, username, 1);
    }

    @Override
    public Message deleteAdmin(String sender, String groupname, String username, int msgId) {
        ArrayList<Object> results = new ArrayList();
        PreparedStatement ps = null;
        Message perm = checkPermission(sender, groupname, false,msgId);
        if (perm.getResult()<2)
            return perm;
        
        ResultSet result = null;
        String sql = "SELECT * FROM usergoup_user WHERE userlogin = ? AND groupname = ? FOR UPDATE";
        try {
            ps = dbconnection.prepareStatement(sql);
            ps.setString(1,username);
            ps.setString(2,groupname);
            result = ps.executeQuery();
            result.next();
            if (result.getRow()==0) {
                results.add("User doesn't belong in the group.");
                result.close();
                ps.close();
                dbconnection.rollback();
                return new Message(msgId,results, username, -1);
            }
            result.close();
            ps.close();
            sql = "UPDATE usergoup_user SET admin = 0 WHERE userlogin = ? ";
            ps = dbconnection.prepareStatement(sql);
            ps.setString(1, username);
            ps.executeUpdate();
            dbconnection.commit();
            ps.close();
        } catch (SQLException ex) {  
            System.out.println("deleteAdmin error: "+ex.getMessage());
            try {
                dbconnection.rollback();
                if (result!=null) result.close();
                if (ps!=null) ps.close();
            } catch (SQLException ex1) {}
            results.add("Database error: "+ex.getMessage());
            return new Message(msgId,results, username, -2);
        }
        results.add("Success");
        return new Message(msgId,results, username, 1);
    }

    @Override
    public Message checkGroupRequests(String sender, int msgId) {
        ArrayList<Object> results = new ArrayList();
        ResultSet result = null;
        PreparedStatement ps = null;
        String sql = "SELECT * FROM usergoup_user WHERE admin = -1 AND groupname IN (SELECT groupname FROM usergoup_user WHERE userlogin = ? AND admin = 1 UNION SELECT groupname FROM usergroup WHERE creator = ? )";
        try {
            ps = dbconnection.prepareStatement(sql);
            ps.setString(1,sender);
            ps.setString(2,sender);
            result = ps.executeQuery();
            result.next();
            if (result.getRow()==0) {
                ps.close();
                result.close();
                results.add("No request found");
                return new Message(msgId,results, sender,-1);
            }
            else { 
                results.add(new Request());
                Request novo = (Request) results.get(results.size()-1);
                do {
                    novo.setGroupMeet(result.getString("groupname"));
                    novo.setUserstatus(result.getString("userlogin"));
                    novo.setType(0);
                } while (result.next());
                ps.close();
                result.close();
                return new Message(msgId,results, sender, 1);
            }
        } catch (SQLException ex) {  
            System.out.println("checkGroupRequests error: "+ex.getMessage()); 
            try {
                if (result!= null) result.close();
                if (ps!=null) ps.close();
            } catch (SQLException ex1) {}
            results.add("Database error: "+ex.getMessage());
            return new Message(msgId,results, sender, -1);
        }
    }

    @Override
    public Message acceptUserRequest(String sender, String groupname, String username, int msgId) {
        ArrayList<Object> results = new ArrayList();
        PreparedStatement ps = null;
        Message perm = checkPermission(sender, groupname, false,msgId);
        if (perm.getResult()<2)
            return perm;
        
        ResultSet result = null;
        String sql = "SELECT * FROM usergoup_user WHERE userlogin = ? AND groupname = ? AND admin = -1 FOR UPDATE";
        try {
            ps = dbconnection.prepareStatement(sql);
            ps.setString(1,username);
            ps.setString(2,groupname);
            result = ps.executeQuery();
            result.next();
            int i = result.getRow();
            result.close();
            ps.close();
            if (i==0) {
                try {
                    dbconnection.rollback();
                } catch (SQLException | NullPointerException ex1) {}
                results.add("There is no such request.");
                return new Message(msgId,results, username, -1);
            }
            
            sql = "UPDATE usergoup_user SET admin = 0 WHERE userlogin = ? AND groupname = ? ";
            ps = dbconnection.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, groupname);
            ps.executeUpdate();
            dbconnection.commit();
            ps.close();
        } catch (SQLException ex) {  
            System.out.println("acceptUserRequest error: "+ex.getMessage());
            try {
                dbconnection.rollback();
                if (result!=null) result.close();
                if (ps!=null) ps.close();
            } catch (SQLException | NullPointerException ex1) {}
            results.add("Database error: "+ex.getMessage());
            return new Message(msgId,results, username, -2);
        }
        results.add("Success");
        return new Message(msgId,results, username, 1);
    }
    
    
    
    @Override
    public Message createMeeting(String sender, String alias, String title, String outcome, String datestr, String location, int msgId) {
        ArrayList<Object> results = new ArrayList();
        PreparedStatement ps = null;
        /*Verificar idempotencia*/
        ResultSet result = null; //TO DO BD: especificar colunas
        String sql = "SELECT * FROM meeting WHERE  meetingalias = ? ";
        try {
            ps = dbconnection.prepareStatement(sql);
            ps.setString(1,alias);
            result = ps.executeQuery();
            result.next();
            if (result.getRow()!=0)  {
                if (result.getString(2).equals(title) && result.getString(3).equals(outcome) && 
                        result.getString(5).equals(location) && compareDateTimestamp(datestr, result.getTimestamp(4))) {
                    results.add("Meeting exists in database");
                    result.close();
                    return new Message(msgId,results, sender, 0);
                }
                else {
                    results.add("Alias already used");
                    result.close();
                    return new Message(msgId,results, sender, -1);
                }
            }
        } catch (SQLException ex) { 
            System.out.println("createMeeting error (not fatal): "+ex.getMessage());
        }
        try {
            if (result!=null) result.close();
            if (ps!=null) ps.close();
        } catch (SQLException ex1) {}
        
        /*Create meeting*/
        sql = "INSERT INTO meeting VALUES ( ? , ? , ? , ? , to_date( ? , 'dd-mm-yyyy hh24:mi') , 0 , 0 ,0, ? )";
        try {
            ps = dbconnection.prepareStatement(sql);
            ps.setString(1,alias);
            ps.setString(2,title);
            ps.setString(3,outcome);
            ps.setString(4, location);
            ps.setString(5,datestr);
            ps.setString(6,sender);
            ps.executeUpdate();
            dbconnection.commit();
            ps.close();
        } catch (SQLException ex) {
            System.out.println("createMeeting error: "+ex.getMessage());
            try {
                dbconnection.rollback();
                if (ps!=null) ps.close();
            } catch (SQLException e){}
            results.add("Database error: "+ex.getMessage());
            return new Message(msgId,results, sender, -3);
        }
        results.add("Success");
        return new Message(msgId,results, sender, 1);
    }

    @Override
    public Message addAgendaItem(String sender, String meetingalias, String title, String description, int msgId) {
        ResultSet result = null;
        PreparedStatement ps = null, psa = null;
        ArrayList<Object> results = new ArrayList();
        //verificar idempotência
        String sql = "SELECT userlogin, description FROM agendaitem WHERE meetingalias = ? AND itemtitle = ? ";
        try {
            ps = dbconnection.prepareStatement(sql);
            ps.setString(1,meetingalias);
            ps.setString(2,title);
            result = ps.executeQuery();
            result.next();
            if (result.getRow()!=0) { //ja existe com aquele titulo
                if (result.getString(2).equals(description)) {
                    if (result.getString(1).equals(sender)) {
                        results.add("Success");
                        ps.close();
                        result.close();
                        return new Message(msgId,results, sender, 0); //idempotente
                    }
                    ps.close();
                    result.close();
                    results.add("Other user created the same item");
                    return new Message(msgId,results, sender, -1);
                }
                ps.close();
                result.close();
                results.add("Item exists with different description");
                return new Message(msgId,results, sender, -2);
            }
            
            //incrementar contador de reunião
            sql = "UPDATE meeting SET agendanum = agendanum+1 WHERE meetingalias = ? ";
            psa = dbconnection.prepareStatement(sql);
            psa.setString(1,meetingalias);
            psa.executeUpdate();
            psa.close();
            
            sql = "SELECT agendanum FROM meeting WHERE meetingalias = ? ";
            ps = dbconnection.prepareStatement(sql);
            ps.setString(1,meetingalias);
            result = ps.executeQuery();
            result.next();
            if (result.getRow()==0) throw new SQLException();
            int n = result.getInt(1);
            result.close();
            ps.close();
            
            sql = "INSERT INTO agendaitem VALUES( ? , ? , agenda_seq.nextval , ? ,null, ? , ? ,0)";
            ps = dbconnection.prepareStatement(sql);
            ps.setString(1,title);
            ps.setString(2, description);
            ps.setString(3, meetingalias);
            ps.setString(4,sender);
            ps.setInt(5,n);
            ps.executeUpdate();
            
            dbconnection.commit();
            psa.close();
            ps.close();
        } catch (SQLException ex) {
            try {
                result.close();
            } catch (SQLException | NullPointerException ex1) {}
            try {
                dbconnection.rollback();
                if (psa!=null) psa.close();
                if (ps!=null) ps.close();
                System.out.println("addAgendaItem error: "+ex.getMessage());
            } catch (SQLException ex1) {
                System.out.println("addAgendaItem rollback error: "+ex.getMessage());
            }
            results.add("Database error: "+ex.getMessage());
            return new Message(msgId,results, sender, -1);
        }
        results.add("Success");
        return new Message(msgId,results, sender, 1);
    }
    
    @Override
    public Message listMyMeetings(String sender, int msgId) {
        ArrayList<Object> results = new ArrayList();
        ResultSet result = null;
        PreparedStatement ps = null;
        String sql = "SELECT meetingalias FROM user_meeting WHERE status = 0 AND userlogin = ? ";
        try {
            ps = dbconnection.prepareStatement(sql);
            ps.setString(1,sender);
            result = ps.executeQuery();
            result.next();
            if (result.getRow()==0) {
                ps.close();
                result.close();
                results.add("No meetings found");
                return new Message(msgId,results, sender, -1);
            }
            else {
                do {
                    results.add(new Request());
                    Request novo = (Request)results.get(results.size()-1);
                    novo.setGroupMeet(result.getString(1));
                    novo.setType(2);
                    novo.setUserstatus("");
                } while (result.next());
                result.close();
                ps.close();
                return new Message(msgId,results, sender, 1);
            }
        } catch (SQLException ex) {
            try {
                if (ps!=null) ps.close();
                if (result!=null) result.close();
            } catch (SQLException ex1) {}
            System.out.println("listMyMeetings error: "+ex.getMessage());
            results.add("Database error: "+ex.getMessage());
            return new Message(msgId,results, sender, -1);
        }
    }

    @Override
    public Message listMyActions(String sender, int msgId) {
        ArrayList<Object> results = new ArrayList();
        ResultSet result = null;
        PreparedStatement ps = null;
        String sql = "SELECT * FROM actionitem WHERE userlogin = ? AND done = 0";
        try {
            ps = dbconnection.prepareStatement(sql);
            ps.setString(1,sender);
            result = ps.executeQuery();
            result.next();
            if (result.getRow()==0) {
                result.close();
                ps.close();
                results.add("No actions found");
                return new Message(msgId,results, sender, -1);
            }
            else {
                do {
                    results.add(new ActionItem());
                    ActionItem a = (ActionItem) results.get(results.size()-1);
                    a.setActionid(result.getInt("actionid"));
                    a.setDone(0);
                    a.setMeetingalias(result.getString("meetingalias"));
                    a.setTask(result.getString("task"));
                    a.setUserlogin(result.getString("userlogin"));
                } while (result.next());
                result.close();
                ps.close();
                return new Message(msgId,results, sender, 1);
            }
        } catch (SQLException ex) {
            System.out.println("listMyActions error: "+ex.getMessage());
            try {
                if (result!=null) result.close();
                if (ps!=null) ps.close();
            } catch (SQLException ex1) {}
            results.add("Database error: "+ ex.getMessage());
            return new Message(msgId,results, sender, -1);
        }
    }

    @Override
    public Message acceptMeeting(String sender, String  meetingalias, boolean decision, int msgId) {
        ArrayList<Object> results = new ArrayList();
        ResultSet result = null;
        meetingalias = meetingalias.trim();
        PreparedStatement ps = null;
        String sql = "SELECT * FROM user_meeting WHERE userlogin = ? AND  meetingalias = ? AND status = 0 FOR UPDATE";
        try {
            ps = dbconnection.prepareStatement(sql);
            ps.setString(1, sender);
            ps.setString(2, meetingalias);
            result = ps.executeQuery();
            result.next();
            if (result.getRow()==0) {
                result.close();
                ps.close();
                results.add("There are no pending requests to that meeting.");
                return new Message(msgId,results, sender, -1);
            }
            result.close();
            ps.close();
            
            int aux;
            if (decision) aux = 1;
            else aux = -1;
            sql = "UPDATE user_meeting SET Status = ? WHERE userlogin = ? AND  meetingalias = ? ";
            ps = dbconnection.prepareStatement(sql);
            ps.setInt(1,aux);
            ps.setString(2, sender);
            ps.setString(3, meetingalias);
            ps.executeUpdate();
            dbconnection.commit();
            ps.close();
        } catch (SQLException ex) {
            try {
                if (result!=null) result.close();
                if (ps!=null) ps.close();
            } catch (SQLException ex1) {}
            System.out.println("acceptMeeting error: "+ex.getMessage());
            results.add("Database error: "+ex.getMessage());
            return new Message(msgId,results, sender, -2);
        }
        results.add("Success");
        return new Message(msgId,results, sender, 1);
    }

    @Override
    public Message getMeetings(String sender, int time, int msgId) {
        ArrayList<Object> results = new ArrayList();
        ResultSet result = null;
        PreparedStatement ps = null;
        String sql = "";
        switch(time) {
            case -1: 
                sql = "SELECT * FROM Meeting WHERE closed = 2";
                break;
            default:
                sql = "SELECT * FROM Meeting m WHERE closed = ? AND creator = ? UNION "
                        + "SELECT m.* FROM meeting m, user_meeting mu WHERE mu.meetingalias = m.meetingalias AND mu.userlogin = ? AND m.closed = ?";
        }
        try {
            ps = dbconnection.prepareStatement (sql);
            if (time>=0) {
                ps.setInt(1,(1-time));
                ps.setString(2, sender);
                ps.setString(3, sender);
                ps.setInt(4, (1-time));
            }
            result = ps.executeQuery();
            result.next();
            if (result.getRow()==0) {
                result.close();
                ps.close();
                results.add("No meetings found.");
                return new Message(msgId,results, sender, -1);
            }
            else {
                do {
                    results.add(new Meeting());
                    Meeting m = (Meeting) results.get(results.size()-1);
                    m.setClosed(result.getInt("closed"));
                    m.setDatetime(result.getString("datetime"));
                    m.setLocation(result.getString("location"));
                    m.setMeetingalias(result.getString("meetingalias"));
                    m.setTitle(result.getString("title"));
                    m.setOutcome(result.getString("outcome"));
                    m.setCreator(result.getString("creator"));
                } while (result.next());
                result.close();
                ps.close();
                return new Message(msgId,results, sender, 1);
            }
        } catch (SQLException ex) {
            System.out.println("getMeetings error: "+ex.getMessage());
            try {
                if (ps!=null) ps.close();
                if (result!=null) result.close();
            } catch (SQLException ex1) {}
            results.add("Database error: "+ex.getMessage());
            return new Message(msgId,results, sender, -1);
        }
    }

    @Override
    public Message validateUser(String sender, String  meetingalias, int msgId) {
        ResultSet result = null;
        ArrayList<Object> results = new ArrayList();
        PreparedStatement ps = null;
        //check if it is creator
        String sql = "SELECT creator FROM meeting WHERE meetingalias = ? ";
        try {
            ps = dbconnection.prepareStatement(sql);
            ps.setString(1,meetingalias);
            result = ps.executeQuery();
            result.next();
            if (result.getRow()==0) {
                ps.close();
                result.close();
                results.add("Meeting not found");
                return new Message(msgId,results, sender, -2);
            }
            if (result.getString(1).equals(sender)) {
                ps.close();
                result.close();
                results.add("User is creator");
                return new Message(msgId,results, sender, 2);
            }
        } catch (SQLException ex) {
            System.out.println("validateUser error 1: "+ex.getMessage());
            try {
                if (ps!=null) ps.close();
                if (result!=null) result.close();
            } catch (SQLException | NullPointerException ex1) {}
            results.add("Error validating user");
            return new Message(msgId,results, sender, -2);
        }
        
        sql = "SELECT status FROM user_meeting WHERE userlogin = ? AND  meetingalias = ? ";
        try {
            ps = dbconnection.prepareStatement(sql);
            ps.setString(1,sender);
            ps.setString(2, meetingalias);
            result = ps.executeQuery();
            result.next();
            if (result.getRow()==0) {
                ps.close();
                result.close();
                results.add("User is not in meeting");
                return new Message(msgId,results, sender, -1);
            }
            else {
                ps.close();
                result.close();
                results.add("Success");
                return new Message(msgId,results, sender, 1);
            }
        } catch (SQLException ex) { 
            System.out.println("validateUser error 2: "+ex.getMessage());
            try {
                if (ps!=null) ps.close();
                if (result!=null) result.close();
            } catch (SQLException | NullPointerException ex1) {}
            results.add("Error validating user: "+ex.getMessage());
            return new Message(msgId,results, sender, -2);
        }
    }

    @Override
    public Message inviteUserMeeting(String sender, String usergroupname, String  meetingalias, int msgId) {
        ArrayList<Object> results = new ArrayList();
        ArrayList<String> users = getUsers(usergroupname);
        if ((users == null)||(users.isEmpty())) {
            results.add("No users nor usergroups with that name");
            return new Message(msgId,results, sender, -3);
        }
        users.remove(sender);
        int num = 0;
        String sql = "INSERT INTO user_meeting VALUES ( ? , ? , 0 )";
        PreparedStatement ps;
        for (String user1:users) {
            try {
                ps = dbconnection.prepareStatement(sql);
                ps.setString(2, meetingalias);
                ps.setString(1, user1);
                ps.executeUpdate();
                dbconnection.commit();
                num++;
                ps.close();
            } catch (SQLException ex) {
                System.out.println("inviteUserMeetings error (not fatal): "+ex.getMessage());
            }
        }
        results.add("Success, added "+num+" users.");
        return new Message(msgId,results, sender, 1);
    }

    private ArrayList<String> getUsers(String name) {
        ResultSet result = null;
        ArrayList<String> res = new ArrayList<String>();
        PreparedStatement ps = null;
        String sql = "SELECT userlogin FROM usergoup_user WHERE groupname = ? UNION SELECT creator FROM usergroup WHERE groupname = ? ";
        
        try {
            ps = dbconnection.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, name);
            result = ps.executeQuery();
            if (!result.next() || result.getRow()==0) {
                ps.close();
                sql = "SELECT userlogin FROM users WHERE userlogin = ?";
                ps = dbconnection.prepareStatement(sql);
                ps.setString(1,name);
                result = ps.executeQuery();
                if (!result.next() || result.getRow()==0) {
                    ps.close();
                    result.close();
                    return null;
                } 
                res.add(name);
            }
            else {
                do {
                    res.add(result.getString(1));
                } while (result.next());
            }
            ps.close();
            result.close();
            return res;
        } catch (SQLException ex) {
            System.out.println("getUsers error: "+ex.getMessage());
            try {
                if (ps!=null) ps.close();
                if (result!=null) result.close();
            } catch (SQLException ex1) {}
            return null;
        }
    }
    
    @Override
    public Message listAgendaItems(String sender, String  meetingalias, int msgId) {
        ArrayList<Object> results = new ArrayList();
        ResultSet result = null;
        PreparedStatement ps = null;
        String sql = "SELECT * FROM agendaitem WHERE meetingalias = ? AND agendaid in (SELECT max(agendaid) FROM agendaitem GROUP BY meetingalias, itemnumber) ORDER BY itemnumber";
        try {
            ps = dbconnection.prepareStatement(sql);
            ps.setString(1,meetingalias);
            result = ps.executeQuery();
            result.next();
            if (result.getRow()==0) {
                ps.close();
                result.close();
                results.add("No agenda item found.");
                return new Message(msgId,results, sender, -1);
            }
            else {
                do {
                    results.add(new AgendaItem());
                    AgendaItem a = (AgendaItem) results.get(results.size()-1);
                    a.setAgendaid(result.getInt("agendaid"));
                    a.setDescription(result.getString("description"));
                    a.setItemnumber(result.getByte("itemnumber"));
                    a.setItemtitle(result.getString("itemtitle"));
                    a.setMeetingalias(result.getString("meetingalias"));
                    a.setPrevid(result.getInt("previd"));
                    a.setUserlogin(result.getString("userlogin"));
                } while (result.next());
                ps.close();
                result.close();
                return new Message(msgId,results, sender, 1);
            }
        } catch (SQLException ex) {
            System.out.println("listAgendaItems error: "+ex.getMessage());
            try {
                if (ps!=null) ps.close();
                if (result!=null) result.close();
            } catch (SQLException | NullPointerException ex1) {}
            results.add("Error fetching agenda items: "+ex.getMessage());
            return new Message(msgId,results, sender, -1);
        }
    }

    @Override
    public Message changeAgendaItem(String sender, String  meetingalias, String description, int agendaid, int type, int msgId) {
        ArrayList<Object> results = new ArrayList();
        ResultSet result = null;
        PreparedStatement ps = null;
        String sql;        
        //check if meeting exists
        sql = "SELECT agendanum FROM meeting WHERE  meetingalias = ? FOR UPDATE";
        try {
            ps = dbconnection.prepareStatement(sql);
            ps.setString(1,meetingalias);
            result = ps.executeQuery();
            result.next();
            if (result.getRow()==0) {
                results.add("Meeting not found");
                ps.close();
                result.close();
                dbconnection.rollback();
                return new Message(msgId,results, sender, -1);
            }
            if (result.getInt(1)<agendaid) {
                ps.close();
                result.close();
                dbconnection.rollback();
                results.add("Item doesn't exist");
                return new Message(msgId,results, sender, -1);
            }
        } catch (SQLException ex) {  
            System.out.println("changeAgendaItem error: "+ex.getMessage());
            try {
                dbconnection.rollback();
                if (ps!=null) ps.close();
                if (result!=null) result.close();
            } catch (SQLException | NullPointerException ex1) {}
            results.add("Error changing agenda item: "+ex.getMessage());
            return new Message(msgId,results, sender, -2);
        }
        
        sql = "SELECT agendaid, itemtitle FROM agendaitem WHERE meetingalias = ? AND itemnumber = ? FOR UPDATE";
        int id = -1;
        String title = "";
        try {
            ps = dbconnection.prepareStatement(sql);
            ps.setString(1, meetingalias);
            ps.setInt(2, agendaid);
            result = ps.executeQuery();
            result.next();
            if (result.getRow()==0) {
                results.add("Item doesn't exist");
                dbconnection.rollback();
                ps.close();
                result.close();
                return new Message(msgId,results, sender, -1);
            }
            id = result.getInt(1);
            title = result.getString(2);
            ps.close();
            result.close();
        } catch (SQLException ex) {
            try {
                dbconnection.rollback();
                if (ps!=null) ps.close();
                if (result!=null) result.close();
            } catch (SQLException ex1) {}
            System.out.println("changeAgendaItem error: "+ex.getMessage());
            results.add("Database Error: "+ex.getMessage());
            return new Message(msgId,results, sender, -2);
        }
        if (type == -1) description = "DELETED";
        sql = "INSERT INTO agendaitem VALUES ( ? , ? ,agenda_seq.nextval, ? , ? , ? , ? ,0)";
        try {
            ps = dbconnection.prepareStatement(sql);
            ps.setString(1, title);
            ps.setString(2,description);
            ps.setString(3, meetingalias);
            ps.setInt(4,id);
            ps.setString(5,sender);
            ps.setInt(6,agendaid);
            ps.executeUpdate();
            dbconnection.commit();
            ps.close();
        } catch (SQLException ex) {  
            try {
                dbconnection.rollback();
                if (ps!=null) ps.close();
            } catch(SQLException e){}
            System.out.println("changeAgendaItem error 3: "+ex.getMessage());
            return new Message(msgId,results, sender, -2);
        }
        results.add("Success");
        return new Message(msgId,results, sender, 1);
    }

    @Override
    public Message closeMeetingAgenda(String sender, String  meetingalias, int msgId) {
        ArrayList<Object> results = new ArrayList();
        ResultSet result = null;
        PreparedStatement ps = null;
        String sql = "SELECT creator, closed, agendanum FROM meeting WHERE meetingalias = ? FOR UPDATE";
        try {
            ps = dbconnection.prepareStatement(sql);
            ps.setString(1, meetingalias);
            result = ps.executeQuery();
            result.next();
            if (result.getRow()==0) {
                dbconnection.rollback();
                ps.close();
                result.close();
                results.add("Meeting not found.");
                return new Message(msgId,results, sender, -1);
            }
            if (!result.getString(1).equals(sender)) {
                dbconnection.rollback();
                ps.close();
                result.close();
                results.add("Insufficient permission.");
                return new Message(msgId,results, sender, -1);
            }
            if (result.getInt(2)==2) {
                dbconnection.rollback();
                ps.close();
                result.close();
                results.add("Meeting has been closed.");
                return new Message(msgId,results, sender, -2);
            }
            int othernum = result.getInt(3);
            ps.close();
            result.close();
            
            sql = "UPDATE meeting SET closed = 1, agendanum = ? WHERE  meetingalias = ? ";
            ps = dbconnection.prepareStatement(sql);
            ps.setInt(1,othernum+1);
            ps.setString(2, meetingalias);
            ps.executeUpdate();
            ps.close();
            
            sql = "INSERT INTO agendaitem VALUES( ? , ? , agenda_seq.nextval , ? ,null, ? , ? ,0)";
            ps = dbconnection.prepareStatement(sql);
            ps.setString(1,"Other");
            ps.setString(2, "Any other business");
            ps.setString(3, meetingalias);
            ps.setString(4, sender);
            ps.setInt(5,othernum+1);
            ps.executeUpdate();
            dbconnection.commit();
            ps.close();
        } catch (SQLException ex) {  
            try {
                dbconnection.rollback();
                if (ps!=null) ps.close();
                if (result!=null) result.close();
            } catch (SQLException ex1) {}
            System.out.println("closeMeetingAgenda error: "+ex.getMessage());
            return new Message(msgId,results, sender, -3);
        }
        results.add(""+meetingalias+"\n1");
        try {
            this.client.sendChatMessage(getMeetUsers( meetingalias),new Message(-1, results, sender, 1));
        } catch (RemoteException ex) {}
        results = new ArrayList();
        results.add("Success");
        return new Message(msgId,results, sender, 1);
    }

    @Override
    public Message listResults(String sender, int type, String  meetingalias, int msgId) {
        ArrayList<Object> results = new ArrayList();
        PreparedStatement ps = null;
        ResultSet result = null;
        
        meetingalias = meetingalias.trim();
        String sql;
        if (type == 0) {
            sql = "SELECT * FROM actionitem WHERE meetingalias = ? ";
            try {
                ps = dbconnection.prepareStatement(sql);
                ps.setString(1, meetingalias);
                result = ps.executeQuery();
                result.next();
                if (result.getRow()==0) {
                    ps.close();
                    result.close();
                    results.add("No action item found");
                    return new Message(msgId,results, sender, -1);
                }
                else {
                    do {
                         results.add(new ActionItem());
                        ActionItem a = (ActionItem) results.get(results.size()-1);
                        a.setActionid(result.getInt("actionid"));
                        a.setDone(result.getInt("done"));
                        a.setMeetingalias(result.getString("meetingalias"));
                        a.setTask(result.getString("task"));
                        a.setUserlogin(result.getString("userlogin"));
                    } while (result.next());
                    result.close();
                    ps.close();
                    return new Message(msgId,results, sender, 1);
                }
            } catch (SQLException ex) {
                try {
                    if (ps!=null) ps.close();
                    if (result!=null) result.close();
                } catch (SQLException ex1) {}
                System.out.println("listResults error: "+ex.getMessage());
                results.add("Database error: "+ex.getMessage());
                return new Message(msgId,results, sender, -1);
            }
        }
        else {
            sql = "select a.itemtitle, k.titledescr FROM keydecision k, agendaitem a WHERE a.meetingalias = ? AND a.agendaid = k.agendaid";
            try {
                ps = dbconnection.prepareStatement(sql);
                ps.setString(1,meetingalias);
                result = ps.executeQuery();
                result.next();
                if (result.getRow()==0) {
                    ps.close();
                    result.close();
                    results.add("No key decision found");
                    return new Message(msgId,results, sender, -1);
                }
                else {
                    do {
                        results.add("About "+result.getString(1)+": "+result.getString(2));
                    } while (result.next());
                    ps.close();
                    result.close();
                    return new Message(msgId,results, sender, 1);
                }
            } catch (SQLException ex) {
                try {
                    if (ps!=null) ps.close();
                    if (result!=null) result.close();
                } catch (SQLException ex1) {}
                System.out.println("listResults error2: "+ex.getMessage());
                results.add("Database error: "+ex.getMessage());
                return new Message(msgId,results, sender, -1);
            }
        }
    }

    @Override
    public Message addActionItem(String sender, String  meetingalias, String username, String action, int msgId) {
        ArrayList<Object> results = new ArrayList();
        ResultSet result = null;
        PreparedStatement ps = null;
        String sql;
        
        //verificar idempotencia
        sql = "SELECT * FROM actionitem WHERE userlogin = ? AND task = ? AND  meetingalias = ? ";
        try {
            ps = dbconnection.prepareStatement(sql);
            ps.setString(1,username);
            ps.setString(2, action);
            ps.setString(3, meetingalias);
            result = ps.executeQuery();
            result.next();
            if (result.getRow()!=0) {
                ps.close();
                result.close();
                results.add("Action already added");
                return new Message(msgId,results, sender, 0);
            }
        } catch (SQLException ex) {  
            System.out.println("addActionItem error not fatal: "+ex.getMessage());
        }
        
        int actionnum = -1;
        sql = "SELECT actionum FROM meeting WHERE  meetingalias = ? FOR UPDATE";
        try {
            ps = dbconnection.prepareStatement(sql);
            ps.setString(1,meetingalias);
            result = ps.executeQuery();
            result.next();
            if (result.getRow()==0) {
                ps.close();
                result.close();
                results.add("Meeting not found");
                return new Message(msgId,results, sender, -1);
            }
            actionnum = result.getInt("actionum")+1;
            result.close();
            ps.close();
        } catch (SQLException ex) {  
            System.out.println("addActionItem error2: "+ex.getMessage());
            try {
                dbconnection.rollback();
                if (ps!=null) ps.close();
                if (result!=null) result.close();
            } catch (SQLException | NullPointerException ex1) {}
            results.add("Meeting not found");
            return new Message(msgId,results, sender, -2);
        }
        PreparedStatement psa = null;
        sql = "INSERT INTO actionitem VALUES ( ? , ? , ? , ? ,0)";
        try {
            ps = dbconnection.prepareStatement(sql);
            ps.setInt(2,actionnum);
            ps.setString(4, action);
            ps.setString(3, username);
            ps.setString(1, meetingalias);
            ps.executeUpdate();
            sql = "UPDATE meeting SET actionum = actionum+1 WHERE meetingalias = ? ";
            psa = dbconnection.prepareStatement(sql);
            psa.setString(1,meetingalias);
            psa.executeUpdate();
            dbconnection.commit();
            ps.close();
            psa.close();
        } catch (SQLException ex) { 
            try {
                dbconnection.rollback();
                if (ps!=null) ps.close();
                if (psa!=null) psa.close();
            } catch (SQLException ex1) { }
            System.out.println("addActionItem error 3: "+ex.getMessage());
            results.add("DB error: "+ex.getMessage());
            return new Message(msgId,results, sender, -2);
        }
        results.add("Success");
        return new Message(msgId,results, sender, 1);
        
    }

    @Override
    public Message completeAction(String sender, int actionid, String  meetingalias, int msgId) {
        ArrayList<Object> results = new ArrayList();
        String sql;
        PreparedStatement ps = null;
        sql = "UPDATE actionitem SET done = 1 WHERE userlogin = ? AND  meetingalias = ? AND actionid = ? ";
        try {
            ps = dbconnection.prepareStatement(sql);
            ps.setString(1, sender);
            ps.setString(2, meetingalias);
            ps.setInt(3, actionid);
            ps.executeQuery();
            dbconnection.commit();
            ps.close();
            results.add("Success");
            return new Message(msgId,results, sender, 1);
        } catch (SQLException ex) {
            try {
                dbconnection.rollback();
                if (ps!=null) ps.close();
            } catch (SQLException e){}
            System.out.println("completeAction error: "+ex.getMessage());
            results.add("DB error: "+ex.getMessage());
            return new Message(msgId,results, sender, -2);
        }
    }

    @Override
    public Message addKeyDecison(String sender, int agendanumber, String description, String  meetingalias, int msgId) {
        ArrayList<Object> results = new ArrayList();
        ResultSet result = null;
        String sql;
        PreparedStatement ps = null;
        
        //check if there is an agenda item with that name
        sql = "SELECT b.agid, a.decisionnum FROM agendaitem a, (SELECT max(agendaid) agid FROM agendaitem WHERE  meetingalias = ? AND itemnumber = ? GROUP BY itemnumber) b WHERE b.agid = a.agendaid FOR UPDATE";
        int decisionnum = -1;
        int agendaid = -1;
        try {
            ps = dbconnection.prepareStatement(sql);
            ps.setString(1, meetingalias);
            ps.setInt(2,agendanumber);
            result = ps.executeQuery();
            result.next();
            if (result.getRow()==0) {
                dbconnection.rollback();
                ps.close();
                result.close();
                results.add("item not found");
                return new Message(msgId,results, sender, -1);
            }
            agendaid = result.getInt(1);
            decisionnum = result.getInt(2)+1;
            result.close();
            ps.close();
        } catch (SQLException ex) {  
            System.out.println("addKeyDecision error 2: "+ex.getMessage());
            try {
                dbconnection.rollback();
                if (result!=null) result.close();
                if (ps!=null) ps.close();
            } catch (SQLException | NullPointerException ex1) {}
            results.add("Agenda Item not found");
            return new Message(msgId,results, sender, -2);
        }
        
        //idempotencia
        sql = "SELECT * FROM keydecision WHERE agendaid = ? AND titledescr = ? ";
        try {
            ps = dbconnection.prepareStatement(sql);
            ps.setInt(1,agendaid);
            ps.setString(2, description);
            result = ps.executeQuery();
            result.next();
            if (result.getRow()!=0) {//idempotencia
                dbconnection.rollback();
                ps.close();
                result.close();
                results.add("Success");
                return new Message(msgId,results, sender, 0);
            }
            ps.close();
            result.close();
            
            sql = "UPDATE agendaitem SET decisionnum = ? WHERE agendaid = ? ";
            ps = dbconnection.prepareStatement(sql);
            ps.setInt(1, decisionnum);
            ps.setInt(2, agendaid);
            ps.executeUpdate();
            ps.close();
            
            sql = "INSERT INTO keydecision VALUES ( ? , ? , ? )";
            ps = dbconnection.prepareStatement(sql);
            ps.setInt(1, agendaid);
            ps.setInt(2, decisionnum+1);
            ps.setString(3, description);
            ps.executeUpdate();
            dbconnection.commit();
            ps.close();
        } catch (SQLException ex) {  
            try {
                if (ps!=null) ps.close();
                if (result!=null) result.close();
            } catch (SQLException ex1) {}
            try {
                dbconnection.rollback();
                System.out.println("addKeyDecision error 3: "+ex.getMessage());
            } catch (SQLException ex1) {
                System.out.println("addKeyDecision rollback error: "+ex.getMessage());
            }
            results.add("KeyDecision was not changed");
            return new Message(msgId,results, sender, -2);
        }
        results.add("Success");
        return new Message(msgId,results, sender, 1);
    }

    @Override
    public Message closeMeeting(String sender, String  meetingalias, int msgId) {
        ArrayList<Object> results = new ArrayList();
        ResultSet result = null;
        PreparedStatement ps = null;
        String sql = "SELECT creator, closed, agendanum FROM meeting WHERE meetingalias = ? FOR UPDATE";
        try {
            ps = dbconnection.prepareStatement(sql);
            ps.setString(1, meetingalias);
            result = ps.executeQuery();
            result.next();
            if (result.getRow()==0) {
                dbconnection.rollback();
                ps.close();
                result.close();
                results.add("Meeting not found.");
                return new Message(msgId,results, sender, -1);
            }
            if (!result.getString(1).equals(sender)) {
                dbconnection.rollback();
                ps.close();
                result.close();
                results.add("Insufficient permission.");
                return new Message(msgId,results, sender, -1);
            }
            result.close();
            ps.close();
            
            sql = "UPDATE meeting SET closed = 2 WHERE  meetingalias = ? ";
            ps = dbconnection.prepareStatement(sql);
            ps.setString(1,  meetingalias);
            ps.executeUpdate();
            dbconnection.commit();
            ps.close();results.add(""+meetingalias+"\n2");
            try {
                this.client.sendChatMessage(getMeetUsers( meetingalias),new Message(-1, results, sender, 1));
            } catch (RemoteException ex) { }
            results.add("Success");
            return new Message(msgId,results, sender, 1);
            
        } catch (SQLException ex) {
            try {
                if (result!=null) result.close();
                if (ps!=null) ps.close();
            } catch (SQLException | NullPointerException ex1) {}
            try {
                dbconnection.rollback();
            } catch (SQLException | NullPointerException ex1) {}
            System.out.println("closeMeeting error: "+ex.getMessage());
            results.add("User/Meeting not found, error accessing database");
            return new Message(msgId,results, sender, -3);
        }
    }
    
    private boolean compareDateTimestamp (String date, Timestamp timestamp) {
        return timestamp.toString().equals(date+":00.0");
    }

    private ArrayList<String> getMeetUsers (String  meetingalias) {
        ResultSet result = null;
        ArrayList<String> res = new ArrayList();
        PreparedStatement ps = null;
        String sql = "SELECT userlogin FROM user_meeting WHERE  meetingalias = ? AND status<>-1 UNION SELECT creator FROM meeting WHERE meetingalias = ? ";
        try {
            ps = dbconnection.prepareStatement(sql);
            ps.setString(1,meetingalias);
            ps.setString(2, meetingalias);
            result = ps.executeQuery();
            result.next();
            if (result.getRow()!=0){
                do {
                    res.add(result.getString("userlogin"));
                } while (result.next());
            }
            result.close();
            ps.close();
        } catch (SQLException ex) {  
            System.out.println("getMeetUsers error: "+ex.getMessage()); 
            try {
                if (result!=null) result.close();
                if (ps!=null) ps.close();
            } catch (SQLException | NullPointerException ex1) {}
        }
        return res;
    }
    
    @Override
    public Message listMeetUsers(String sender, String  meetingalias, int msgId) throws RemoteException {
        ArrayList<Object> results = new ArrayList();
        ArrayList<String> res = getMeetUsers( meetingalias);
        if ((res == null)||res.isEmpty()) {
            results.add("No users found");
            return new Message(msgId,results, sender, -1);
        }
        for (String re : res) {
            results.add(re);
        }
        return new Message(msgId,results, sender, 1);
    }
 
    @Override
    public Message listAllUsers(String sender, int msgId) throws RemoteException {
        ArrayList<Object> results = new ArrayList();
        ResultSet result = null;
        String sql = "SELECT userlogin FROM users";
        try {
            result = dbconnection.createStatement().executeQuery(sql);
            result.next();
            if (result.getRow()==0) {
                result.close();
                results.add("No users found");
                return new Message(msgId,results, sender, -1);
            }
            else {
                do {
                    results.add(result.getString(1));
                } while (result.next());
                result.close();
                return new Message(msgId,results, sender, 1);
            }
        } catch (SQLException ex) {
            try {
                result.close();
            } catch (SQLException | NullPointerException ex1) {}
            System.out.println("listAllUserss error: "+ex.getMessage());
            results.add("Database error: "+ex.getMessage());
            return new Message(msgId,results, sender, -1);
        }
    }

    @Override
    public Message agendaHistory(String sender, String meetingalias, int itemnum, int msgId) throws RemoteException {
        ArrayList<Object> results = new ArrayList();
        ResultSet result = null;
        PreparedStatement ps = null;
        String sql = "SELECT itemnumber, itemtitle, userlogin, description FROM agendaitem WHERE meetingalias = ? AND itemnumber = ? ORDER BY agendaId";
        try {
            ps = dbconnection.prepareStatement(sql);
            ps.setString(1, meetingalias);
            ps.setInt(2,itemnum);
            result = ps.executeQuery();
            result.next();
            if (result.getRow()==0) {
                result.close();
                ps.close();
                results.add("No agenda item found.");
                return new Message(msgId,results, sender, -1);
            }
            else {
                do {
                    results.add(""+result.getInt(1)+"\t"+result.getString(2)+"\t"+result.getString(3)+"\t"+result.getString(4));
                } while (result.next());
                result.close();
                ps.close();
                return new Message(msgId,results, sender, 1);
            }
        } catch (SQLException ex) {
            System.out.println("agendaHistory error: "+ex.getMessage());
            try {
                if (result!=null) result.close();
                if (ps!=null) ps.close();
            } catch (SQLException | NullPointerException ex1) {}
            results.add("Error fetching agenda history: "+ex.getMessage());
            return new Message(msgId,results, sender, -1);
        }
    }
    
    /*****CHAT*****/
    
    @Override
    public Message addChatComment(String sender, String  meetingalias, int itemnumber, String txt, int msgId) {
        ArrayList<Object> results = new ArrayList();
        ResultSet result = null;
        String sql;
        PreparedStatement ps = null;
                
        //check if there is an agenda item with that name
        sql = "SELECT agendaid FROM agendaitem WHERE  meetingalias = ? AND itemnumber = ? ";
        int agendaid;
        try {
            ps = dbconnection.prepareStatement(sql);
            ps.setString(1, meetingalias);
            ps.setInt(2, itemnumber);
            result = ps.executeQuery();
            result.next();
            if (result.getRow()==0) {
                result.close();
                ps.close();
                results.add("Item not found");
                return new Message(msgId,results, sender, -1);
            }
            else agendaid = result.getInt(1);
            result.close();
            ps.close();
        } catch (SQLException ex) {  
            System.out.println("addChatComment error2: "+ex.getMessage());
            try {
                if (result!=null) result.close();
                if (ps!=null) ps.close();
            } catch (SQLException | NullPointerException ex1) {}
            results.add("Item not found");
            return new Message(msgId,results, sender, -2);
        }
        sql = "INSERT INTO message VALUES ( ?, ? , ? , ? )";
        try {
            ps = dbconnection.prepareStatement(sql);
            ps.setInt(1,agendaid );
            ps.setString(2, sender);
            ps.setTimestamp(3, new Timestamp(new Date().getTime()));
            ps.setString(4,  txt);
            ps.executeUpdate();
            dbconnection.commit();
            ps.close();
        } catch (SQLException ex) {  
            try {
                dbconnection.rollback();
                if (ps!=null) ps.close();
            } catch (SQLException | NullPointerException ex1) {}
            System.out.println("addChatMessage error3: "+ex.getMessage());
            results.add("Message not delivered");
            return new Message(msgId,results, sender, -2);
        }
        
        /*send message to online users*/
        String reply =  meetingalias+"\n"+itemnumber+"\n"+sender+": "+txt;
        results.add(reply);
        lastChat = new Message(-1, results, sender, 1);
        lastChatUsers = getMeetUsers( meetingalias);
        results = new ArrayList();
        try {
            this.client.sendChatMessage(lastChatUsers,lastChat);
        } catch (RemoteException ex) {
            results.add("Message sent to server");
            return new Message(msgId,results, sender, 1);
        }
        lastChat = null;
        lastChatUsers = null;
        System.out.println("Chat message delivered");
        results.add("Success");
        return new Message(msgId,results, sender, 1);
    }

    @Override
    public Message subscribeChat(String sender, String  meetingalias, int itemnumber, int msgId) {
        ArrayList<Object> results = new ArrayList();
        ResultSet result = null;
        PreparedStatement ps = null;
        String sql = "SELECT m.text, m.userlogin, m.timemsg FROM message m, agendaitem a WHERE a.meetingalias = ? AND a.itemnumber = ? AND a.agendaid = m.agendaid ORDER BY timemsg";
        try {
            ps = dbconnection.prepareStatement(sql);
            ps.setString(1, meetingalias);
            ps.setInt(2, itemnumber);
            result = ps.executeQuery();
            result.next();
            if (result.getRow()==0) {
                result.close();
                ps.close();
                return new Message(msgId,null, sender, 1);
            }
            else {
                do {
                    results.add(new ChatMessage());
                    ChatMessage c = (ChatMessage) results.get(results.size()-1);
                    c.setAgendaid(itemnumber);
                    c.setMeetingalias(meetingalias);
                    c.setText(result.getString(1));
                    c.setUserlogin(result.getString(2));
                    c.setTimemsg(result.getTimestamp(3).toString());
                } while (result.next());
                result.close();
                ps.close();
                return new Message(msgId,results, sender, 1);
            }
        } catch (SQLException ex) {  
            System.out.println("subscribeChat error: "+ex.getMessage());
            try {
                if (result!=null) result.close();
                if (ps!=null) ps.close();
            } catch (SQLException | NullPointerException ex1) {}
            results.add("Database error: "+ex.getMessage());
            return new Message(msgId,results, sender, 1);
        }
        
    }
    
    
    
    @Override
    public void subscribe(String sender, Meeto_RMI_C_I client) throws RemoteException {
        this.client = client;
        if (lastChat!=null) {
            try {
                this.client.sendChatMessage(lastChatUsers,lastChat);
            } catch (RemoteException ex) {
                return;
            }
            lastChat = null;
            lastChatUsers = null;
        }
    }

    @Override
    public boolean registerTCPServer() {
        try {
            return client.keepAlive();
        } catch (Exception e) {
            client = null;
            return false;
        }
    }
}