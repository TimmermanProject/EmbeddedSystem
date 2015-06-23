package shared;

import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.BitSet;

import database.AbstractDB;

public class ACK extends Message {
	private static final long serialVersionUID = 1L;
	
	public enum types {COMMAND, RFID_ADD, RFID_REMOVE }
	public enum states { SUCCESS, FAILURE};
	public types type;
	public states state;
	public byte value;
	
	public ACK(){
		this.setFrameType(messageTypes.ACK); 
	}
	
	
	/** update database **/
	public void updateDatabase(Connection connection) throws SQLException{
		Statement statement = connection.createStatement();	
		switch (type){
			case COMMAND:
				System.out.println("Updating Status bits for room" + this.getRoom().getRoomID());
				
				BitSet statusBits = BitSet.valueOf(new byte[] { value});
				   
				Boolean doorStatus = statusBits.get(0);
				Boolean doorStatus2 = statusBits.get(1);
				Boolean lightStatus1 = statusBits.get(2);
				Boolean lightStatus2  = statusBits.get(3);
				Boolean lightStatus3  = statusBits.get(4);
				Boolean FireStatus  = statusBits.get(5);
				Boolean SurveillanceStatus  = statusBits.get(6);
				Boolean Extra  = statusBits.get(7);

				ResultSet resultSet = statement.executeQuery("	UPDATE raspi.RoomTable "
						+ 	"SET doorStatus="+doorStatus +
							",doorStatus2="+doorStatus2 +
							",lightStatus1="+lightStatus1 +
							",lightStatus2="+lightStatus2 +
							",lightStatus3="+lightStatus3 +
							",FireStatus="+FireStatus +
							",SurveillanceStatus="+SurveillanceStatus +
							",Extra="+Extra +
							"WHERE roomtable.RoomID="+ this.getRoom().getRoomID() +";");
			case RFID_ADD:
				System.out.println("RFID_ADD DB Storage not implemented in Raspi" + this.getRoom().getRoomID());
				
			case RFID_REMOVE:
				System.out.println("RFID_REMOVE DB Storage not implemented in Raspi" + this.getRoom().getRoomID());
		}
				
	}
	
	/** Getters and setters **/
	public void setType (types ACK_type){
		type = ACK_type;
	}
	
	public types getType(){
		return type;
	}
	
	public void setState(states ACK_state){
		state = ACK_state;
	}
	
	public states getState(){
		return state;
	}
	
	public void setValue(byte value){
		this.value = value;
	}
	
	public byte getValue (){
		return value;
	}

	/** message came in from building subsystem**/
	@Override
	public void execute(AbstractDB db, ObjectOutputStream objectOutputStream,
			OutputStream serialOutputStream) throws SQLException {
		// TODO Auto-generated method stub
		System.out.println("unsupported");
	}

}
