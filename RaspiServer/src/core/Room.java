package core;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import main.Raspi;

/** 
 * @author Thomas Verbeke
 * 
 * **/

public class Room {
	public enum doorStatusCodes { OPEN, CLOSED, EMERGENCY }
	
	private int FloorID;
	private int RoomID;
	private doorStatusCodes doorStatus;
	private boolean cmdFlag;
	private boolean alarmFlag;
	private ArrayList<Integer> tagList = new ArrayList<Integer>();
	
	public Room(int RoomID){
		this.FloorID = Raspi.FLOOR_ID;
		this.RoomID = RoomID;
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
	
	/** open door trough command frame **/
	public void openDoor(){
		//Command message = new Command();
		//message.setRoom(this);
		//message.setActionID(1);
		//message.sendSerial(serialOutputStream);
	}
	
	/** @param tag : RFID tag to be removed from subsystem door access list
	 * 	Assemble message
	 *  Send message to subsystem (PIC)
	 *  Update database
	 **/
	public void removeTag(int tag){
		tagList.remove(tag);
	}
	
	/** @param tag : RFID tag to be added to subsystem door access list
	 * 	Assemble message
	 *  Send message to subsystem (PIC)
	 *  Update database
	 **/
	public void addTag(int tag){
		tagList.add(tag);	
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

	/**
	 * @return the cmdFlag
	 */
	public boolean getCmdFlag() {
		return cmdFlag;
	}

	/**
	 * @param cmdFlag the cmdFlag to set
	 * @throws IOException 
	 */
	public void setCmdFlag(boolean cmdFlag) throws IOException {
		this.cmdFlag = cmdFlag;
	}

	/**
	 * @return the alarmFlag
	 */
	public boolean getAlarmFlag() {
		return alarmFlag;
	}

	/**
	 * @param alarmFlag the alarmFlag to set
	 * @throws IOException 
	 */
	public void setAlarmFlag(boolean alarmFlag) throws IOException {
		this.alarmFlag = alarmFlag;
	}
	
	
}
