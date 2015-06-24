package shared;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;


/** 
 * @author Thomas Verbeke
 * 
 * **/

public class Room implements Serializable {
	protected static int RoomID;
	private int ElementID;
	private int FloorID;
	
	public Room(int ElementID, int RoomID, int FloorID){
		this.ElementID = ElementID;
		this.FloorID = FloorID;
		this.RoomID = RoomID;
	}
	
	public int getElementID(){
		return ElementID;
	}
	
	public void setElementID(int ElementID){
		this.ElementID = ElementID; 
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
	
}
