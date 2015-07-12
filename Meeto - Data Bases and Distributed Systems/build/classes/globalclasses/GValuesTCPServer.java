package globalclasses;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class GValuesTCPServer {
	private int serverTCPPort;
	private int serverUDPPort;
	private String serverRMIHost;
	private int serverRMIPort;
	private String serverRMIName;
	private String primaryServerUDPHost;
	private int primaryServerUDPPort;
	private int currentRMIRetry = 0;
	private int nRMIRetries = 5;
	private int currentTCPRetry = 0;
	private int nTCPRetries = 3;
	private int currentUDPRetry = 0;
	private int nUDPRetries = 3;
	private long sleepTime = 3000;
	
	public GValuesTCPServer(String fileName) {
		super();
		serverRMIHost = "localhost";
		serverRMIPort = 7001;
		serverRMIName = "Database";	
		primaryServerUDPHost = "localhost";
		primaryServerUDPPort = 6500;
		serverTCPPort = 6000;
		serverUDPPort = 6000;
		
		// ler dados do ficheiro configuracao
	    InputStream in = null;
	    try {
		    Properties properties = new Properties();
	        in = new FileInputStream(fileName);
		    properties.load(in);
	  		serverTCPPort = Integer.parseInt(properties.getProperty("serverTCPPort").trim());
	  		serverUDPPort = Integer.parseInt(properties.getProperty("serverUDPPort").trim());
			serverRMIHost = properties.getProperty("serverRMIHost");
			serverRMIPort = Integer.parseInt(properties.getProperty("serverRMIPort").trim());
			serverRMIName = properties.getProperty("serverRMIName");
			primaryServerUDPHost = properties.getProperty("primaryServerUDPHost");
			primaryServerUDPPort = Integer.parseInt(properties.getProperty("primaryServerUDPPort").trim());
			nRMIRetries = Integer.parseInt(properties.getProperty("nRMIRetries"));
			nTCPRetries = Integer.parseInt(properties.getProperty("nTCPRetries").trim());
			nUDPRetries = Integer.parseInt(properties.getProperty("nUDPRetries").trim());
			sleepTime = Integer.parseInt(properties.getProperty("sleepTime").trim());
	    }
	    catch(IOException e) {
	    	System.out.println("GValuesTCP IO:"+e.getMessage());
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
	public int getServerTCPPort(){
		return serverTCPPort;
	}
	public int getServerUDPPort(){
		return serverUDPPort;
	}
	public String getServerRMIHost() {
		
		return serverRMIHost;
	}
	public int getServerRMIPort() {
		return serverRMIPort;
	}
	public String getServerRMIName() {
		return serverRMIName;
	}
	public String getPrimaryServerUDPHost() {
		return primaryServerUDPHost;
	}

	public int getPrimaryServerUDPPort() {
		return primaryServerUDPPort;
	}

	public int getCurrentRMIRetry() {
		currentRMIRetry++;
		return currentRMIRetry;
	}
	
	public void resetCurrentRMIRetry() {
		this.currentRMIRetry = 0;
	}
	public int getnRMIRetries() {
		return nRMIRetries;
	}
	public int getCurrentTCPRetry() {
		currentTCPRetry++;
		return currentTCPRetry;
	}
	public void resetCurrentTCPRetry() {
		this.currentTCPRetry = 0;
	}
	public int getnTCPRetries() {
		return nTCPRetries;
	}

	public int getCurrentUDPRetry() {
		currentUDPRetry++;
		return currentUDPRetry;
	}
	public void resetCurrentUDPRetry() {
		this.currentUDPRetry = 0;
	}
	public int getnUDPRetries() {
		return nUDPRetries;
	}

	public long getSleepTime() {
		return sleepTime;
	}

}
