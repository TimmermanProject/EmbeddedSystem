/** 
 * @author thomasverbeke
 * 
 * **/

package shared;

import java.io.OutputStream;
import java.io.Serializable;

/** 
 * @author Thomas Verbeke
 * Abstract wrapper class for the frames
 * Factory Pattern
 * 
 * **/

public abstract class Message implements Serializable {
	public enum frameTypes { DATA, DATA_REQUEST, ACK, RFID, COMMAND, ALARM }
	private frameTypes frameType;
	private Room room;
	
	//getter
	public frameTypes getFrameType(){
		return frameType;
	}
	
	//setter
	public void setFrameType (frameTypes frameType){
		this.frameType = frameType;
	}
	
	//setter
	public void setRoom(Room room){
		this.room = room;
	}
	
	//getter
	public Room getRoom(){
		return room;
	}

	void send(OutputStream outputStream) {
		System.out.println("Not supported for this message");
	}
}
