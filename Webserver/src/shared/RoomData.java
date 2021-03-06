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

public class RoomData extends Frame {
	public enum doorStatusCodes { OPEN, CLOSED, EMERGENCY }
	
	private int BuildingID;
	private int FloorID;
	private int RoomID;
	
	private ArrayList<Integer> accessCodes; 		// contains list of RFID that can enter room
	private doorStatusCodes doorStatus; // current door status, open or closed
	
	public RoomData(){
		this.setFrameType(frameTypes.ROOM_DATA);
	}
	
	/** GETTERS AND SETTERS **/
	
	/**
	 * @return the buildingID
	 */
	public int getBuildingID() {
		return BuildingID;
	}

	/**
	 * @param buildingID the buildingID to set
	 */
	public void setBuildingID(int buildingID) {
		BuildingID = buildingID;
	}

	/**
	 * @return the floorID
	 */
	public int getFloorID() {
		return FloorID;
	}

	/**
	 * @param floorID the floorID to set
	 */
	public void setFloorID(int floorID) {
		FloorID = floorID;
	}

	/**
	 * @return the roomID
	 */
	public int getRoomID() {
		return RoomID;
	}

	/**
	 * @param roomID the roomID to set
	 */
	public void setRoomID(int roomID) {
		RoomID = roomID;
	}

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
