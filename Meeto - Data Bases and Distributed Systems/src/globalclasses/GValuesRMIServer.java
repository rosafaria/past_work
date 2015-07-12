package globalclasses;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class GValuesRMIServer {
	// Database initial settings
	private String dbHost = "192.168.10.1";
	private int dbPort = 1521;
	private String dbUser = "grupo";
	private String dbPass = "rvv";
	
	//RMI Registry initial settings
	private int rmiRegistryPort = 7001;

	public GValuesRMIServer(String fileName) {
		super();
		
		// ler dados do ficheiro configuracao
	    InputStream in = null;
	    try {
		    Properties properties = new Properties();
	        in = new FileInputStream(fileName);
		    properties.load(in);
	  		dbHost = properties.getProperty("dbHost").trim();
	  		dbPort = Integer.parseInt(properties.getProperty("dbPort").trim());
	  		dbUser = properties.getProperty("dbUser").trim();
	  		dbPass = properties.getProperty("dbPass").trim();
	  		rmiRegistryPort = Integer.parseInt(properties.getProperty("rmiRegistryPort").trim());
	    }
	    catch(IOException e) {
	    	System.out.println("GValuesRMI IO:"+e.getMessage());
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

	public String getDbURL() {
		return new String("jdbc:oracle:thin:@"+dbHost+":"+dbPort+":XE");
	}

	public String getDbUser() {
		return dbUser;
	}

	public String getDbPass() {
		return dbPass;
	}

	public int getRmiRegistryPort() {
		return rmiRegistryPort;
	}

}
