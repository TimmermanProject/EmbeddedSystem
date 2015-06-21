/** 
 * @author thomasverbeke
 * 
 *	This class belongs to a shared library; shared between Raspberry Pi and Server in order for them to more easily communicate. 
 *	
 * 
 * **/

package messages;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;

import core.MYSQL_db;

public class Data extends Message {
	private static final long serialVersionUID = -1547837996222004778L;

	public enum doorStatusCodes { OPEN, CLOSED, EMERGENCY }
	
	private ArrayList<Integer> accessCodes; 		// contains list of RFID that can enter room
	private doorStatusCodes doorStatus; 			// current door status, open or closed
	
	public Data(){
		this.setFrameType(frameTypes.DATA);
	}
	
	@Override
	public void sendObject(ObjectOutputStream objectOutputStream) throws IOException{
		objectOutputStream.writeObject(this);	
	}
	
	/** GETTERS AND SETTERS **/
	
	/**
	 * @return the accessCodes
	 */
	public ArrayList getAccessCodes() {
		return accessCodes;
	}

	/**
	 * @param accessCodes the accessCodes to set
	 */
	public void setAccessCodes(ArrayList accessCodes) {
		this.accessCodes = accessCodes;
	}
	
	/**
	 * @param Add access code to set
	 */
	public void addAccessCode(int code) {
		this.accessCodes.add(code);
	}
	

	/**
	 * @return the doorStatus
	 */
	public doorStatusCodes getDoorStatus() {
		return doorStatus;
	}

	/**
	 * @param doorStatus the doorStatus to set
	 */
	public void setDoorStatus(doorStatusCodes doorStatus) {
		this.doorStatus = doorStatus;
	}

	/** message came in from building subsystem**/
	@Override
	public void execute(MYSQL_db db, ObjectOutputStream objectOutputStream,
			OutputStream serialOutputStream) throws SQLException {
		//Data msg = (Data) obj;
		//TODO update database
		System.out.println("not implemented yet");
		
	}

	
}
