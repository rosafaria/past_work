package globalclasses;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class GValuesClient {
	private int numServers = 2;
	private String[] serverHost = new String[numServers];
	private int[] serverPort = new int[numServers];
	private int currentServer = 0;
	private int currentServerRetry = 0;
	private int nServerRetries = 3;
	private int currentRetry = 0;
	private int nRetries = 3;
	private int currentSendRetry = 0;
	private int nSendRetries = 3;
	private long sleepTime = 3000;
	
	public GValuesClient(String fileName) {
		super();
		
		// Obter dados do ficheiro configuracao
		serverHost[0]="localhost";
		serverPort[0]=6000;
		serverHost[1]="127.0.0.1";
		serverPort[1]=6500;
		// ler dados do ficheiro configuracao
	    InputStream in = null;
	    try {
		    Properties properties = new Properties();
	        in = new FileInputStream(fileName);
		    properties.load(in);
	  		numServers = Integer.parseInt(properties.getProperty("numServers").trim());
	  		if(numServers>=2){
	  			serverHost = new String[numServers];
	  			serverPort = new int[numServers];
		  		for(int i=0;i<numServers;i++){
		  			String stxt = "serverHost" + (i+1);
		  			serverHost[i]=properties.getProperty(stxt);
		  			stxt = "serverPort" + (i+1);
		  			serverPort[i]=Integer.parseInt(properties.getProperty(stxt).trim());
		  		}
				currentServer = Integer.parseInt(properties.getProperty("currentServer").trim())-1;	
				if ((currentServer>=numServers)||(currentServer<0))
					currentServer = 0;
	  		}
			nServerRetries = Integer.parseInt(properties.getProperty("nServerRetries").trim());
			nRetries = Integer.parseInt(properties.getProperty("nRetries").trim());
			nSendRetries = Integer.parseInt(properties.getProperty("nSendRetries").trim());
			sleepTime = Integer.parseInt(properties.getProperty("sleepTime").trim());
	    }
	    catch(IOException e) {
	    	System.out.println("GValuesClient IO:"+e.getMessage());
//			e.printStackTrace();
	    }
	    finally {
	      if(in != null) {
	        try {
	          in.close();
	        }
	        catch(IOException e) {
		    	System.out.println("GValues IO close:"+e.getMessage());	        	
	        }
	      }
	    }
	}
	
	public void resetGlobalValues(){ // used when connection suceeds to reset the counters
		currentServerRetry=0;
		currentRetry = 0;
	}

	public String getServerHost() {
		if (currentServerRetry>=nServerRetries){
			currentServer = (currentServer +1) % numServers;
			currentServerRetry = 0;
			currentRetry +=1;
		}
		currentServerRetry +=1;
		return serverHost[currentServer];
	}

	public int getServerPort() {
		return serverPort[currentServer];
	}

	public long getSleepTime() {
		return sleepTime;
	}

	public int getnRetries() {
		return nRetries;
	}

	public int getCurrentRetry() {
		return currentRetry;
	}

	public int getCurrentServerRetry() {
		return currentServerRetry;
	}

	public int getCurrentServer() {
		return currentServer;
	}

	public int getCurrentSendRetry() {
		return currentSendRetry;
	}

	public void resetCurrentSendRetry() {
		this.currentSendRetry = 0;
	}

	public int getnSendRetries() {
		return nSendRetries;
	}
	
}
