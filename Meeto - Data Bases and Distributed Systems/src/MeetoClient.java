
import globalclasses.GValuesClient;
import globalclasses.Message;
import globalclasses.TCPMsg;


import graphicInterface.UIClient;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;


public class MeetoClient {

    public static MeetoClient client = null;
    private boolean run = true;
    private ArrayList<TCPMsg> cmdList= new ArrayList<TCPMsg>();
    private Socket socketTCPServer = null;

    public MeetoClient() {
            super();
            client = this;
    }

    public boolean isRun() {
        return run;
    }

    public void setRun(boolean run) {
        this.run = run;
    }

    public void closeSocketTCPServer() throws IOException{
        if(socketTCPServer!=null)
            socketTCPServer.close();
    }

    public boolean delCmdList(int id) {
        TCPMsg msg = null;

        if(id<0)	// mensagem nao e resposta a UICLient - por exemplo e um chat
            return true;

        // remove comando da fila de espera
        for(int i=0;i<cmdList.size();i++){
            msg = cmdList.get(i);
            if (msg.getId()==id){
                cmdList.remove(i);
                return true;
            }
        }
        return false;
    }

    public void addCmdList(TCPMsg msg) {
            // adiciona mais um comando a fila de espera
            cmdList.add(msg);
    }

    public TCPMsg getCmdList(int index){
            if ((index>=0)&&(index<cmdList.size()))
                    return cmdList.get(index);
            else
                    return null;
    }

    public static void main(String[] args) {

        MeetoClient Main = new MeetoClient();		

        // Pipes para ligacao com UIClient
        PipedInputStream pinUI_TCP = null;
        PipedOutputStream poutUI_TCP = null;

        PipedInputStream pinTCP_UI = null;
        PipedOutputStream poutTCP_UI = null;

        ObjectInputStream inUI = null;
        ObjectOutputStream outUI = null;


    // criacao de pipes para interligacao com UIClient
        pinUI_TCP = new PipedInputStream();
        try {
            poutUI_TCP = new PipedOutputStream(pinUI_TCP);
            poutUI_TCP.flush();
        } catch (IOException e1) {
            System.out.println("Creating UI_TCP pipe:"+e1.getMessage());		    
//		e1.printStackTrace();
        }
    	
    	pinTCP_UI = new PipedInputStream();
    	try {
            poutTCP_UI = new PipedOutputStream(pinTCP_UI);
            poutTCP_UI.flush();
            outUI = new ObjectOutputStream(poutTCP_UI);
            outUI.flush();
        } catch (IOException e1) {
            System.out.println("Creating TCP_UI pipe:"+e1.getMessage());		    
//			e1.printStackTrace();
        }

        String serverHost = "";
        int serverPort = 0;
        UserIOControle userIOThread = null;
        GValuesClient gv = new GValuesClient("MeetoClient.cfg");
	    
        // Arrancar UIClient
        // pipes para UI - poutUI_TCP e pinTCP_UI
        // pipes para mim pinUI_TCP e poutTCP_UI
        UIClient MyUI = new UIClient(pinTCP_UI,poutUI_TCP);

        // Criacao da Thread para atender aos pedidos do utilizador
        try {
                inUI = new ObjectInputStream(pinUI_TCP);
        } catch (IOException e1) {
            System.out.println("Creting UI_TCP stream:"+e1.getMessage());		    
//			e1.printStackTrace();
        }
        userIOThread = new UserIOControle(inUI);
        userIOThread.start();

        System.out.println("Main initial Retry:" + gv.getCurrentRetry()+"/"+gv.getCurrentServerRetry()+" ...");		    
        while (Main.run && (gv.getCurrentRetry()<gv.getnRetries())){
            serverHost = gv.getServerHost();
            serverPort = gv.getServerPort();		
            System.out.println("Server = " + serverHost+" Port = "+serverPort);		    
            try {
                Main.socketTCPServer = new Socket(serverHost, serverPort);
                System.out.println("SOCKET=" + Main.socketTCPServer);

                ObjectInputStream in = new ObjectInputStream(Main.socketTCPServer.getInputStream());
                ObjectOutputStream out = new ObjectOutputStream(Main.socketTCPServer.getOutputStream());
                out.flush();

                // update output Stream
                userIOThread.setOut(out);
                gv.resetGlobalValues();

                // Reenviar comandos pendentes da arraylist se existirem
                for(int i=0;i<Main.cmdList.size();i++){
                    TCPMsg msg = Main.getCmdList(i);
                    // Enviar comando para o servidor TCP				
                    out.reset();
                    out.writeObject(msg);
                    System.out.println("ReSent to TCP message ID:"+msg.getId());
                }

                userIOThread.setConnected(true);	// notify UserIOControl

                // Control received messages
                Message msg;
                while(Main.run){
                    // ler respostas do servidorTCP
                    msg = (Message) in.readObject();
                    String msgTxt = "ID ="+msg.getId()+ " txt="+msg.getMsg();

                    if(msg.getId()==-5){
                        // mensagem do TCPServer a solicitar envio de comandos pendentes

                        // Reenviar comandos pendentes da arraylist se existirem
                        for(int i=0;i<Main.cmdList.size();i++){
                            TCPMsg msgT = Main.getCmdList(i);
                            // Enviar comando para o servidor TCP				
                            out.reset();
                            out.writeObject(msgT);
                            System.out.println("ReSent to TCP message ID:"+msg.getId());
                        }

                    } else if(msg.getId()==-1){
                        // mensagem de chat a enviar a UIClient
                        MyUI.sendChatMsg(msg.toString());
                    }
                    else {
                        // envio das respostas para a UIClient
                        outUI.reset();
                        outUI.writeObject(msg);
                    }

                    // confirmar ack e eliminar da arralist de comandos pendentes
                    Main.delCmdList(msg.getId());

                    // escreve resposta recebida
                    System.out.println("Received: " + msgTxt);
                }

            } catch (UnknownHostException e) {
                System.out.println("Main Sock client:" + e.getMessage());
//				e.printStackTrace();
            } catch (EOFException e) {
                System.out.println("Main EOF:" + e.getMessage());
//				e.printStackTrace();
            } catch (IOException e) {
                System.out.println("Main IO:" + e.getMessage());
//				e.printStackTrace();
            } catch (Exception e) {
                System.out.println("Main Exception:" + e.getMessage());
//				e.printStackTrace();
            } finally {
                try {
                    Main.closeSocketTCPServer();
                } catch (IOException e) {
                    System.out.println("Main close:" + e.getMessage());
                }
            }
			
			// Tratamento restabelecimento conneccao ao servidor TCP
            userIOThread.setConnected(false);
            if (Main.run){
                try {
                    Thread.sleep(gv.getSleepTime());
                } catch (InterruptedException e) {
                    System.out.println("Main Sleep:" + e.getMessage());
                }
                System.out.println("Main Retry:" + gv.getCurrentRetry()+"/"+gv.getCurrentServerRetry()+" ... Server "+gv.getCurrentServer());		    		    	
            }
        }
        if (Main.run){
            userIOThread.interrupt(); // interromper a Thread IO para esta terminar
            System.out.println("Server TCP not available please retry later ...");		    	    	    	
            MyUI.killMe(); // matar a interface grafica	    
        } else{
            System.out.println("MeetoClient ... Thanks for using this program");
            MyUI.killMe();
        }
    }
}

class UserIOControle extends Thread{

    private volatile boolean connected = false;	
    private ObjectOutputStream out = null;
    private ObjectInputStream inUI = null;
    private int msgId = 1;

    public UserIOControle(ObjectInputStream inUI) {
            super();
            this.inUI = inUI;
    }

    public synchronized boolean isConnected() throws InterruptedException {
        while(!this.connected){
            wait();
        }
        return connected;
    }

    public synchronized void setConnected(boolean connected) {
        this.connected = connected;
        notify();
    }

    public UserIOControle() {
        super();
        this.out = null;
    }

    public ObjectOutputStream getOut() {
        return out;
    }

    public void setOut(ObjectOutputStream out) {
        this.out = out;
    }

    public void run(){
        try {
            while (MeetoClient.client.isRun()) {
                // Obter commando da interface grafica
                TCPMsg msg = null;
                try {
                    msg = (TCPMsg) inUI.readObject();
                } catch (ClassNotFoundException | IOException e1) {
                    System.out.println("Error reading from UI pipe:" + e1.getMessage());
                }

                if (msg!=null){
                    System.out.println("UserIOControle Opcode:" + msg.getOpcode());					
                    if (msg.getOpcode()==51){
                        // terminar MeetoClient por indicacao interface grafica
                        MeetoClient.client.setRun(false);
                    } else {
                        // Acrescenta ID a mensagem
                        msg.setId(msgId++);

                        // wait for available socket
                        isConnected();

                        // acrescentar comando pendente a uma arraylist
                        MeetoClient.client.addCmdList(msg);

                        // Enviar comando para o servidor TCP				
                        try {
                            out.reset();
                            out.writeObject(msg);
                        } catch (IOException e) {
                            System.out.println("Write Object IO:"+e.getMessage());
                        }						
                    }
                }				
            }
        } catch (InterruptedException e1) {
            System.out.println("UserIOControle Interrupted:" + e1.getMessage());
//			e1.printStackTrace();
        }
        try {
                MeetoClient.client.closeSocketTCPServer();
        } catch (IOException e) {
                e.printStackTrace();
        }
        System.out.println("Thread UserIOControle terminated");
    }
	
}

