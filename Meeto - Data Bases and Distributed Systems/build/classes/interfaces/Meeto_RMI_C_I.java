package interfaces;

import globalclasses.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;


public interface Meeto_RMI_C_I extends Remote {
	public boolean sendChatMessage(ArrayList<String> users, Message msg) throws RemoteException;;
	public boolean keepAlive() throws RemoteException;
}
