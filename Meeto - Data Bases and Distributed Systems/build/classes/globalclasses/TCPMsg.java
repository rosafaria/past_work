
package globalclasses;

import java.io.Serializable;
import java.util.ArrayList;

public class TCPMsg implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int id=0;
	private int opcode;
    ArrayList<Object> parameters;
    
    public TCPMsg(int opcode, ArrayList<Object> par) {
        this.opcode = opcode;
        this.parameters = par;
    }

	public int getOpcode() {
		return opcode;
	}

	public void setOpcode(int opcode) {
		this.opcode = opcode;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getUser(){
		if (parameters.size()>0)
			return (String) parameters.get(0);
		else
			return new String("");
	}

	public Object getParameter(int i){
		if ((i>=0)&&(i<parameters.size()))
			return parameters.get(i);
		else
			return null;
	}
    
}
