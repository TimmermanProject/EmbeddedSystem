/** 
 * @author thomasverbeke
 * 
 * **/

package shared;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import database.AbstractDB;

public class RFID extends Message {
	private static final long serialVersionUID = 1L;
	private char tag; 
	public enum statusCodes { ADD, REMOVE }
	private statusCodes statusCode;
	
	public RFID (){
		this.setFrameType(messageTypes.RFID);
	}
	
	@Override
	public void sendSerial(OutputStream outputStream) {
		byte[] out = new byte[5];
    	out[0] = '#'; 							//	starting delimeter
    	out[1] = '1'; 							//	address of source
    	
    	if (statusCode == statusCodes.ADD){
    		System.out.println("Sending RFID Add Request");
    		out[2] = 'b';						//	frameType
    	} else if (statusCode == statusCodes.REMOVE){
    		System.out.println("Sending RFID Remove Request");
    		out[2] = 'c';						//	frameType
    	}
    	
		out[3] = (byte) tag; 							//	message: 1B RFID tag
		out[4] = '\n';							//	ending delimter
		
		try {
			/**
			 * @param data   Array holding data to be written
		     * @param off    Offset of data in array
		     * @param n      Amount of data to write, starting from off.
		     * @return       Amount of data actually written
		     **/
			System.out.println("FORMAT BYTE: "+Arrays.toString(out));
			
			System.out.println("FORMAT CHAR: " + new String(out, Charset.forName("ISO-8859-1")));
			System.out.println(out);
			
			outputStream.write(out, 0, out.length);
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	/** update database **/
	public void updateDatabase(Connection connection) throws SQLException{	
		System.out.println("Updating RFID for room" + this.getRoom().getRoomID());
		Statement statement = connection.createStatement();	
		
		if (this.getStatus()==RFID.statusCodes.ADD){ 
			ResultSet resultSet = statement.executeQuery("	"
					+ 	"INSERT INTO raspi.RFID "
					+ 	"(ID,RFID_Tag,RoomTableID) VALUES (NULL,"+tag+","+this.getRoom().getRoomID()+");");
		} else if (this.getStatus()==RFID.statusCodes.REMOVE) {
			ResultSet resultSet = statement.executeQuery("	"
					+ 	"DELETE FROM raspi.RFID "
					+ 	"WHERE rfid.RFID_Tag = "+tag+";");
		}	
		
	}
	
	/** message came in from building subsystem**/
	@Override
	public void execute(AbstractDB db, ObjectOutputStream objectOutputStream, OutputStream serialOutputStream) throws SQLException {
		this.sendSerial(serialOutputStream); //send over serial connection to room subsystem
		this.updateDatabase(db.getConnection());		
	}	
	
	/** Getters and Setters **/
	
	public void setTag(char tag){
		this.tag = tag;
	}
	
	public char getTag (){
		return tag;
	}
	
	public void setStatus(statusCodes statusCode){
		this.statusCode = statusCode;
	}	
	
	public statusCodes getStatus(){
		return statusCode;
	}

}
