/** 
 * @author thomasverbeke
 * 
 * **/

package shared;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.sql.SQLException;

import database.AbstractDB;

/** 
 * @author Thomas Verbeke
 * Abstract wrapper class for the frames
 * Factory Pattern
 * 
 * **/

public abstract class Message implements Serializable {
	private static final long serialVersionUID = 1L;

	public enum messageTypes { DATA, DATA_REQUEST, ACK, RFID, COMMAND, ALARM }
	private messageTypes messageType;
	private Room room;
	
	//getter
	public messageTypes getFrameType(){
		return messageType;
	}
	
	//setter
	public void setFrameType (messageTypes frameType){
		this.messageType = frameType;
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
	public abstract void execute(AbstractDB db, ObjectOutputStream objectOutputStream, OutputStream serialOutputStream) throws SQLException;
}
