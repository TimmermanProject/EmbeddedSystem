package shared;

import java.io.Serializable;

/** 
 * @author Thomas Verbeke
 * Abstract wrapper class for the frames
 * Factory Pattern
 * 
 * **/

public abstract class Frame implements Serializable {
	public enum frameTypes { ROOM_DATA, ROOM_ACK, RFID_ADD, RFID_REMOVE, ROOM_ACTION }
	public frameTypes frameType;
	
	//getter
	public frameTypes getFrameType(){
		return frameType;
	}
	
	//setter
	public void setFrameType (frameTypes frameType){
		this.frameType = frameType;
	}
}
