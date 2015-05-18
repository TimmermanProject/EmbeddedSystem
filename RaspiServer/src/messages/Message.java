/** 
 * @author thomasverbeke
 * 
 * **/

package messages;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.sql.SQLException;

import core.Database;
import core.Room;

/** 
 * @author Thomas Verbeke
 * Abstract wrapper class for the frames
 * Factory Pattern
 * 
 * **/

public abstract class Message implements Serializable {
	private static final long serialVersionUID = 1L;

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

	public void sendSerial(OutputStream outputStream) {
		System.out.println("Not supported for this message");
	}
	
	public void sendObject(ObjectOutputStream objectOutputStream) throws IOException{
		System.out.println("Not supported for this message");
	}
	
	/** message came in from building subsystem**/
	public abstract void execute(Database db, ObjectOutputStream objectOutputStream, OutputStream serialOutputStream) throws SQLException;
}
