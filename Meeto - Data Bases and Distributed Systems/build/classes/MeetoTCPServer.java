
import globalclasses.GValuesTCPServer;
import globalclasses.Message;
import globalclasses.TCPMsg;
import interfaces.Meeto_RMI_C_I;
import interfaces.Meeto_RMI_S_I;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;


public class MeetoTCPServer extends UnicastRemoteObject implements Meeto_RMI_C_I{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static MeetoTCPServer tcpServer= null; 
	public CopyOnWriteArrayList <ConnectThread> clientList = new CopyOnWriteArrayList<ConnectThread>();
	public Meeto_RMI_S_I rmiServer = null;
	private GValuesTCPServer gv = new GValuesTCPServer("MeetoTCPServer.cfg");
	private boolean acceptClients = false;
	private boolean primaryServer = false;
	

	public MeetoTCPServer() throws RemoteException {
		super();
		tcpServer = this;
	}

	public boolean sendChatMessage(ArrayList<String> users, Message msg) throws RemoteException{
		String currentUser="";
		ConnectThread client = null;
		boolean status = true;
		// enviar mensagem para todos os clientes online
		for(int i=0; i<users.size();i++){
			currentUser = users.get(i);
		    for(int j=0;j<clientList.size();j++) {
		    	client =clientList.get(j);
		    	if (client.getUser().compareTo(currentUser)==0){
		    		// user online - enviar mensagem char		    		
					 try {
						client.out.reset();
						client.out.writeObject(msg);
					} catch (IOException e) {
						status = false;		//devolver falso pois falhou o envio a um cliente que esta online
						System.out.println("Sending chat message to user:"+currentUser+" failed ...");
//						e.printStackTrace();
					}	
		    	}
	    	}
			
		}	
		System.out.println("Delivering chat message to connected users");
		return status;
	}
	
	public boolean keepAlive() throws RemoteException{
		return isPrimaryServer();
	}

	public boolean isPrimaryServer() {
		return primaryServer;
	}

	public void setPrimaryServer(boolean primaryServer) {
		this.primaryServer = primaryServer;
	}

	public GValuesTCPServer getGV(){
		return gv;
	}
	
	public boolean isAcceptClients() {
		return acceptClients;
	}

	synchronized public void setAcceptClients(boolean aClients) {
		acceptClients = aClients;
	}

	private boolean renewRMIConnection(){
		int num = gv.getCurrentRMIRetry();
		System.out.println("Begin renewRMIConnection "+num);
		while (num<=gv.getnRMIRetries()){
			// verify RMI connectivity
			try {
				String nameURL = "rmi://"+gv.getServerRMIHost()+":"+gv.getServerRMIPort()+"/"+gv.getServerRMIName();
				System.out.println("RMI name:"+nameURL);
				rmiServer = (Meeto_RMI_S_I) Naming.lookup(nameURL);
				// RMIRegistry connectivity OK
				System.out.println("renewRMIConnection OK .... Registry");
				return true;
			} catch (MalformedURLException | RemoteException
					| NotBoundException e1) {
				System.out.println("renewRMI: " + e1.getMessage());
//				e1.printStackTrace();
				try {
					Thread.sleep(gv.getSleepTime());
				} catch (InterruptedException e) {
					System.out.println("renewRMI sleep: " + e1.getMessage());
//					e.printStackTrace();
				}
			}
			
		}
		System.out.println("Unable to connect to RMI Server please try again later");
		return false;
	}
	
	private void infoClientsRMIup(){
		// informa todos as thread online que RMI reiniciou solicitando reenvio eventuais pendentes
		ConnectThread client = null;
                ArrayList<Object> result = new ArrayList();
                result.add((Object)"RMI server up again resend pedent messages");
		Message msg = new Message(-5,result,"TCPServer",1);
	    for(int j=0;j<clientList.size();j++) {
	    	client =clientList.get(j);
				 try {
					client.out.reset();
					client.out.writeObject(msg);
				} catch (IOException e) {
					System.out.println("Sending RMI 'up again' to thread:"+client.getThreadNumber()+" failed ...");
//					e.printStackTrace();
				}	
    	}
		
	}

	public static void main(String[] args) {
        
		MeetoTCPServer Main = null;
		try {
			Main = new MeetoTCPServer();
		} catch (RemoteException e2) {
			System.out.println("Unable to run TCP Server ... Please try later.");      
//			e2.printStackTrace();
			System.exit(-1);
		}
		
		int nextThread=0;
		ServerSocket listenSocket = null;


		while (Main.renewRMIConnection()){
			try {
				// RMIRegistry connectivity OK
				
				if (!Main.rmiServer.registerTCPServer()){
					// Primary Server
					Main.setPrimaryServer(true);
					// fazer subscrive ...
					Main.rmiServer.subscribe("TCP", (Meeto_RMI_C_I) Main);
					
					Main.gv.resetCurrentRMIRetry();
					System.out.println("RMIConnection OK .... Primary Server");
					
					// Inicializar Thread Pong
					Pong myPong = new Pong();
					myPong.start();
					
					try{
			            int serverPort = Main.gv.getServerTCPPort();	
			            
						listenSocket = new ServerSocket(serverPort);
						Main.setAcceptClients(true);
			            System.out.println("A Escuta no Porto = "+listenSocket);
			            
			            // Informa eventuais clientes ligados de que reiniciou conneccao RMI e solicita pendentes
			            Main.infoClientsRMIup();
			            
			            while(Main.isAcceptClients()) {
			                Socket clientSocket = listenSocket.accept(); // BLOQUEANTE
			                System.out.println("CLIENT_SOCKET (created at accept())="+clientSocket+" Thread "+nextThread);
			                Main.clientList.add(new ConnectThread(clientSocket, nextThread++));
			                System.out.println("Existem "+Main.clientList.size()+" Threads de clientes");
			            }
			        }catch(IOException e){
			        	System.out.println("Meeto main Listen:" + e.getMessage());
			        	break;
					} finally {
					    if (listenSocket != null)
							try {
								listenSocket.close();
							} catch (IOException e) {
							    System.out.println("Meeto main close:" + e.getMessage());
							} catch (Exception e) {
							    System.out.println("Meeto main:" + e.getMessage());
							}
					}
					
					// Terminar a Thread Pong
					myPong.execute = false;
					myPong = null;
				} else {
					// Secundary Server
					Main.setPrimaryServer(true);
					Main.gv.resetCurrentRMIRetry();
					System.out.println("RMIConnection OK .... Secundary Server ");
					
					// Inicializar Ping
					Ping myPing = new Ping(Main.gv);
					while(myPing.serverAlive());
					
				}

				
				
			} catch (RemoteException e1) {
				System.out.println("RMI in main: " + e1.getMessage());
//				e1.printStackTrace();
				try {
					Thread.sleep(Main.gv.getSleepTime());
				} catch (InterruptedException e) {
					System.out.println("Main Sleep .... "+e.getMessage());
//					e.printStackTrace();
				}
			}
			
		}
		System.out.println("Unable to run TCP Server ... Try later.");      
	}

}

//Thread para tratar de cada canal de comunicacao com um cliente
class ConnectThread extends Thread {
	boolean closeThread = false;
	
	private ObjectInputStream in;
	public ObjectOutputStream out;
	private Socket clientSocket;
	private int threadNumber = -1;
	private String user = "";
 
	public ConnectThread (Socket aClientSocket, int aThreadNumber) {
		super();
		 threadNumber = aThreadNumber;
	     try{
	         clientSocket = aClientSocket;
	         out = new ObjectOutputStream(clientSocket.getOutputStream());
	         out.flush();
	         in = new ObjectInputStream(clientSocket.getInputStream());
	         	    	 
	         this.start();
	     }catch(IOException e){
	     	System.out.println("ConnetThread A("+threadNumber+"):" + e.getMessage());
	     }catch(Exception e){
	      	System.out.println("ConnetThread B("+threadNumber+"):" + e.getMessage());    	 
	     }
	}
	
	public String getUser(){
		return this.user;
	}
	
	public int getThreadNumber(){
		return this.threadNumber;
	}
 
	public void closeThread() {
		this.closeThread = true;
		if (in!=null)
			try {
				in.close();
			} catch (IOException e) {
			   	System.out.println("ConnetThread close in T("+threadNumber+") IO:" + e.getMessage());
//				e.printStackTrace();
			}
		if (out!=null)
			try {
				out.close();
			} catch (IOException e) {
			   	System.out.println("ConnetThread close out T("+threadNumber+") IO:" + e.getMessage());
//				e.printStackTrace();
			}
		if (clientSocket!=null)
			try {
				clientSocket.close();
			} catch (IOException e) {
			   	System.out.println("ConnetThread close socket T("+threadNumber+") IO:" + e.getMessage());
	//				e.printStackTrace();
			}
		MeetoTCPServer.tcpServer.clientList.remove(this);	// Remove Thread da clientList
		this.interrupt();
	}

	public void run(){
	     Message resposta = new Message(0,null,"",0);
	
		 System.out.println("ConnetThread ["+threadNumber+"] iniciou run ...");
		 try{
			 while(!closeThread){
				 //an echo server
				 TCPMsg msgTCP = (TCPMsg) in.readObject();
				 int opCode = msgTCP.getOpcode();
				 System.out.println("T["+threadNumber + "] Recebeu Opcode: "+opCode);
				 
				 // actualizar utilizador desta thread
				 if (opCode!=1)
					 user = msgTCP.getUser();
				 
				 // Aqui chamada do RMI e tramento de instrucoes
				 switch (opCode){
				 	case 1:
				 		resposta = MeetoTCPServer.tcpServer.rmiServer.login(
				 				(String) msgTCP.getParameter(0), (String) msgTCP.getParameter(1), msgTCP.getId());
				 		if (resposta.getResult()==1)
				 			user = msgTCP.getUser();
					 	break;
				 	case 2:
				 		resposta = MeetoTCPServer.tcpServer.rmiServer.registerUser(
				 				(String) msgTCP.getParameter(0), (String) msgTCP.getParameter(1),
				 				(String) msgTCP.getParameter(2), msgTCP.getId());
					 	break;
				 	case 3:
				 		resposta = MeetoTCPServer.tcpServer.rmiServer.createGroup(
				 				(String) msgTCP.getParameter(0), (String) msgTCP.getParameter(1),
				 				(boolean) msgTCP.getParameter(2), msgTCP.getId());
					 	break;
				 	case 5:
				 		resposta = MeetoTCPServer.tcpServer.rmiServer.checkPermission(
				 				(String) msgTCP.getParameter(0), (String) msgTCP.getParameter(1),
				 				(boolean) msgTCP.getParameter(2), (int) msgTCP.getId());
					 	break;
				 	case 6:
				 		resposta = MeetoTCPServer.tcpServer.rmiServer.listMembers(
				 				(String) msgTCP.getParameter(0), (String) msgTCP.getParameter(1), 
                                                                (boolean) msgTCP.getParameter(2), (int) msgTCP.getId());
					 	break;
				 	case 7:
				 		resposta = MeetoTCPServer.tcpServer.rmiServer.listGroups(
				 				(String) msgTCP.getParameter(0), msgTCP.getId());
					 	break;
				 	case 9:
				 		resposta = MeetoTCPServer.tcpServer.rmiServer.addUserToGroup(
				 				(String) msgTCP.getParameter(0), (String) msgTCP.getParameter(1),
				 				(String) msgTCP.getParameter(2), msgTCP.getId());
					 	break;
				 	case 10:
				 		resposta = MeetoTCPServer.tcpServer.rmiServer.deleteUserFromGroup(
				 				(String) msgTCP.getParameter(0), (String) msgTCP.getParameter(1),
				 				(String) msgTCP.getParameter(2), msgTCP.getId());
					 	break;
				 	case 11:
				 		resposta = MeetoTCPServer.tcpServer.rmiServer.makeAdmin(
				 				(String) msgTCP.getParameter(0), (String) msgTCP.getParameter(1),
				 				(String) msgTCP.getParameter(2), msgTCP.getId());
					 	break;
				 	case 12:
				 		resposta = MeetoTCPServer.tcpServer.rmiServer.deleteAdmin(
				 				(String) msgTCP.getParameter(0), (String) msgTCP.getParameter(1),
				 				(String) msgTCP.getParameter(2), msgTCP.getId());
					 	break;
				 	case 13:
				 		resposta = MeetoTCPServer.tcpServer.rmiServer.checkGroupRequests(
				 				(String) msgTCP.getParameter(0), msgTCP.getId());
					 	break;
				 	case 14:
				 		resposta = MeetoTCPServer.tcpServer.rmiServer.acceptUserRequest(
				 				(String) msgTCP.getParameter(0), (String) msgTCP.getParameter(1),
				 				(String) msgTCP.getParameter(2), msgTCP.getId());
					 	break;
				 	case 15:
				 		resposta = MeetoTCPServer.tcpServer.rmiServer.createMeeting(
				 				(String) msgTCP.getParameter(0), (String) msgTCP.getParameter(1),
				 				(String) msgTCP.getParameter(2), (String) msgTCP.getParameter(3),
				 				(String) msgTCP.getParameter(4), (String) msgTCP.getParameter(5), msgTCP.getId());
					 	break;
				 	case 16:
				 		resposta = MeetoTCPServer.tcpServer.rmiServer.listMyMeetings(
				 				(String) msgTCP.getParameter(0), msgTCP.getId());
					 	break;
				 	case 36:
				 		resposta = MeetoTCPServer.tcpServer.rmiServer.listMeetUsers(
				 				(String) msgTCP.getParameter(0), (String) msgTCP.getParameter(1), msgTCP.getId());
					 	break;
				 	case 17:
				 		resposta = MeetoTCPServer.tcpServer.rmiServer.listMyActions(
				 				(String) msgTCP.getParameter(0), msgTCP.getId());
					 	break;
				 	case 18:
				 		resposta = MeetoTCPServer.tcpServer.rmiServer.acceptMeeting(
				 				(String) msgTCP.getParameter(0), (String) msgTCP.getParameter(1),
				 				(boolean) msgTCP.getParameter(2), msgTCP.getId());
					 	break;
				 	case 19:
				 		resposta = MeetoTCPServer.tcpServer.rmiServer.getMeetings(
				 				(String) msgTCP.getParameter(0), (int) msgTCP.getParameter(1), msgTCP.getId());
					 	break;
				 	case 20:
				 		resposta = MeetoTCPServer.tcpServer.rmiServer.validateUser(
				 				(String) msgTCP.getParameter(0), (String) msgTCP.getParameter(1), msgTCP.getId());
					 	break;
				 	case 21:
				 		resposta = MeetoTCPServer.tcpServer.rmiServer.inviteUserMeeting(
				 				(String) msgTCP.getParameter(0), (String) msgTCP.getParameter(1),
				 				(String) msgTCP.getParameter(2), msgTCP.getId());
					 	break;
				 	case 22:
				 		resposta = MeetoTCPServer.tcpServer.rmiServer.listAgendaItems(
				 				(String) msgTCP.getParameter(0), (String) msgTCP.getParameter(1), msgTCP.getId());
					 	break;
                                        case 8:
                                                resposta = MeetoTCPServer.tcpServer.rmiServer.agendaHistory(
                                                        (String) msgTCP.getParameter(0), (String) msgTCP.getParameter(1), 
                                                        (int) msgTCP.getParameter(2), msgTCP.getId());
                                                break;
                                        case 4:
				 		resposta = MeetoTCPServer.tcpServer.rmiServer.addAgendaItem(
				 				(String) msgTCP.getParameter(0), (String) msgTCP.getParameter(1),
				 				(String) msgTCP.getParameter(2), (String) msgTCP.getParameter(3),
				 				msgTCP.getId());
					 	break;
				 	case 23:
				 		resposta = MeetoTCPServer.tcpServer.rmiServer.changeAgendaItem(
				 				(String) msgTCP.getParameter(0), (String) msgTCP.getParameter(1),
				 				(String) msgTCP.getParameter(2), (int) msgTCP.getParameter(3),
				 				(int) msgTCP.getParameter(4), msgTCP.getId());
					 	break;
				 	case 24:
				 		resposta = MeetoTCPServer.tcpServer.rmiServer.closeMeetingAgenda(
				 				(String) msgTCP.getParameter(0), (String) msgTCP.getParameter(1), msgTCP.getId());
					 	break;
				 	case 25:
				 		resposta = MeetoTCPServer.tcpServer.rmiServer.listResults(
				 				(String) msgTCP.getParameter(0), (int) msgTCP.getParameter(1),
				 				(String) msgTCP.getParameter(2), msgTCP.getId());
					 	break;
				 	case 26:
				 		resposta = MeetoTCPServer.tcpServer.rmiServer.addActionItem(
				 				(String) msgTCP.getParameter(0), (String) msgTCP.getParameter(1),
				 				(String) msgTCP.getParameter(2), (String) msgTCP.getParameter(3), msgTCP.getId());
					 	break;
				 	case 27:
				 		resposta = MeetoTCPServer.tcpServer.rmiServer.completeAction(
				 				(String) msgTCP.getParameter(0), (int) msgTCP.getParameter(1),
				 				(String) msgTCP.getParameter(2), msgTCP.getId());
					 	break;
				 	case 28:
				 		resposta = MeetoTCPServer.tcpServer.rmiServer.addKeyDecison(
				 				(String) msgTCP.getParameter(0), (int) msgTCP.getParameter(1),
				 				(String) msgTCP.getParameter(2), (String) msgTCP.getParameter(3), msgTCP.getId());
					 	break;
				 	case 29:
				 		resposta = MeetoTCPServer.tcpServer.rmiServer.closeMeeting(
				 				(String) msgTCP.getParameter(0), (String) msgTCP.getParameter(1), msgTCP.getId());
					 	break;
				 	case 30:
				 		resposta = MeetoTCPServer.tcpServer.rmiServer.addChatComment(
				 				(String) msgTCP.getParameter(0), (String) msgTCP.getParameter(1),
				 				(int) msgTCP.getParameter(2), (String) msgTCP.getParameter(3), msgTCP.getId());
					 	break;
				 	case 31:
				 		resposta = MeetoTCPServer.tcpServer.rmiServer.subscribeChat(
				 				(String) msgTCP.getParameter(0), (String) msgTCP.getParameter(1),
				 				(int) msgTCP.getParameter(2), msgTCP.getId());
					 	break;
				 	case 37:
				 		resposta = MeetoTCPServer.tcpServer.rmiServer.listAllUsers(
				 				(String) msgTCP.getParameter(0), msgTCP.getId());
					 	break;
				 	case 50:
                                            String txt = "The connected useres are:\n";
			 		    for(int i=0;i<MeetoTCPServer.tcpServer.clientList.size();i++) {
			 		    	if (MeetoTCPServer.tcpServer.clientList.get(i).getUser().length()!=0)
			 		    		txt = txt + "User "+i+" - "+MeetoTCPServer.tcpServer.clientList.get(i).getUser() + '\n';
			 		    	else
				 		    	txt = txt + "User "+i+" - Not yet loged on \n";

                                            }
                                            ArrayList<Object> res = new ArrayList();
                                            res.add((Object)txt);
                                            resposta = new Message(msgTCP.getId(),res,user,1);
                                            System.out.println("Informing Client of connected users");
                                            break;
 	
			 		default:
                                            res = new ArrayList();
                                                res.add((Object)"Wrong opCode RMI not contacted");
			 			resposta = new Message(msgTCP.getId(),res,(String) msgTCP.getParameter(0),-4);
			 			System.out.println("Wrong opCode RMI not contacted");
		 				break;
				 }

				 String data = resposta.toString();
				 System.out.println("T["+threadNumber + "] Respondeu ao cliente : " + data);

				 // envio de resposta ao MeetoClient
				 out.reset();
				 out.writeObject(resposta);													 
			 }
	     }catch(EOFException e){
			// ObjectInputStream foi fechada pelo outro lado
			System.out.println("ConnetThread run A("+threadNumber+") EOF:" + e);  
//			e.printStackTrace();
		 }catch(RemoteException e){
			// Erro no acesso RMI
			System.out.println("ConnetThread run B("+threadNumber+") IO:" + e);
//		  	e.printStackTrace();
		  	MeetoTCPServer.tcpServer.setAcceptClients(false);
		  	// deveria tambem fazer interrupt do accept do main !!!
		  	// restantes threads dos clientes so vao descobrir quando tentarem aceder ao RMI
		 }catch(IOException e){
		  	System.out.println("ConnetThread run C("+threadNumber+") IO:" + e);
		  	e.printStackTrace();
		 }catch(Exception e){
		   	System.out.println("ConnetThread run D("+threadNumber+") IO:" + e);
		   	//e.printStackTrace();
		 }
		 this.closeThread();
	     System.out.println("Tread "+threadNumber+" terminou.");
	}
}

//Class para enviar Ping e esperar pelo Pong 
class Ping {
	GValuesTCPServer gv = null;
	
	public Ping(GValuesTCPServer gv){
		this.gv = gv;		
	}

	public boolean serverAlive(){
		DatagramSocket aSocket = null;
		try {
			aSocket = new DatagramSocket();    

			String texto = "Keep Alive";
			long timeOut = gv.getSleepTime();
			aSocket.setSoTimeout((int) timeOut);
			InetAddress aHost = InetAddress.getByName(gv.getPrimaryServerUDPHost());
			int serverPort = gv.getPrimaryServerUDPPort();
			System.out.println("Ping iniciado ... Servidor Primario UDP:"+aHost+":"+serverPort);

			while(gv.getCurrentUDPRetry()<=gv.getnUDPRetries()){
				
				byte [] m = texto.getBytes();
				
				DatagramPacket request = new DatagramPacket(m,m.length,aHost,serverPort);
				aSocket.send(request);			                        
				byte[] buffer = new byte[1000];
				DatagramPacket reply = new DatagramPacket(buffer, buffer.length);	
				try {
					aSocket.receive(reply);
					gv.resetCurrentUDPRetry();
					System.out.println("server Alive Recebeu: " + new String(reply.getData(), 0, reply.getLength()));
				} catch (SocketTimeoutException e){
					System.out.println("serverAlive: "+e.getMessage());
				} finally {
					try {
						Thread.sleep(gv.getSleepTime());
					} catch (InterruptedException e) {
						System.out.println("serverAlive TimeOut: "+e.getMessage());
//						e.printStackTrace();
					}
				}
			} // while
		}catch (SocketException e){
			System.out.println("Socket: " + e.getMessage());
		}catch (
				IOException e){System.out.println("IO: " + e.getMessage());
		}finally {
			if(aSocket != null)
				aSocket.close();
		}
		gv.resetCurrentUDPRetry();
		return false;
	}
}

//Thread que recebendo Ping envia um Pong 
class Pong extends Thread {
	public boolean execute = true;

	public void run(){		
		DatagramSocket aSocket = null;
		String s;
		try{
			int udpPort = MeetoTCPServer.tcpServer.getGV().getServerUDPPort();
			aSocket = new DatagramSocket(udpPort);
			System.out.println("Servidor Pong iniciado ... Socket Datagram a escutar no porto "+udpPort);
			while(execute){
				byte[] buffer = new byte[1000]; 			
				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
				aSocket.receive(request);
				s=new String(request.getData(), 0, request.getLength());	
				System.out.println("Pong - Servidor UDP Recebeu: " + s);	
								
				DatagramPacket reply = new DatagramPacket(buffer, 
						buffer.length, request.getAddress(), request.getPort());
				aSocket.send(reply);
			}
		}catch (SocketException e){
			System.out.println("Socket: " + e.getMessage());
		}catch (IOException e) {
			System.out.println("IO: " + e.getMessage());
		}finally {
			if(aSocket != null)
				aSocket.close();
		}		
	}

}
