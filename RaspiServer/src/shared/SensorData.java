/** 
 * @author thomasverbeke
 * 
 *	This class belongs to a shared library; shared between Raspberry Pi and Server in order for them to more easily communicate. 
 *	
 * 
 * **/

package shared;

import java.io.Serializable;
import java.util.ArrayList;

public class SensorData extends Message {
	public enum doorStatusCodes { OPEN, CLOSED, EMERGENCY }
	
	private ArrayList<Integer> accessCodes; 		// contains list of RFID that can enter room
	private doorStatusCodes doorStatus; // current door status, open or closed
	
	public SensorData(){
		this.setFrameType(frameTypes.ROOM_DATA);
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

	
}
