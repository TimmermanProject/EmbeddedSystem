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
	
	private boolean doorStatus;
	private boolean doorStatus2;
	private boolean lightStatus1;
	private boolean lightStatus2;
	private boolean lightStatus3;
	private boolean FireStatus;
	private boolean SurveillanceStatus;
	private boolean Extra;
	
	public ACK(){
		this.setFrameType(messageTypes.ACK); 
	}
	
	
	/** update database **/
	public void updateDatabase(Connection connection) throws SQLException{
		Statement statement = connection.createStatement();	
		switch (type){
			case COMMAND:
				System.out.println("Updating Status bits for room" + this.getRoom().getRoomID());

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


	/**
	 * @return the doorStatus
	 */
	public boolean isDoorStatus() {
		return doorStatus;
	}


	/**
	 * @param doorStatus the doorStatus to set
	 */
	public void setDoorStatus(boolean doorStatus) {
		this.doorStatus = doorStatus;
	}


	/**
	 * @return the doorStatus2
	 */
	public boolean isDoorStatus2() {
		return doorStatus2;
	}


	/**
	 * @param doorStatus2 the doorStatus2 to set
	 */
	public void setDoorStatus2(boolean doorStatus2) {
		this.doorStatus2 = doorStatus2;
	}


	/**
	 * @return the lightStatus1
	 */
	public boolean isLightStatus1() {
		return lightStatus1;
	}


	/**
	 * @param lightStatus1 the lightStatus1 to set
	 */
	public void setLightStatus1(boolean lightStatus1) {
		this.lightStatus1 = lightStatus1;
	}


	/**
	 * @return the lightStatus2
	 */
	public boolean isLightStatus2() {
		return lightStatus2;
	}


	/**
	 * @param lightStatus2 the lightStatus2 to set
	 */
	public void setLightStatus2(boolean lightStatus2) {
		this.lightStatus2 = lightStatus2;
	}


	/**
	 * @return the lightStatus3
	 */
	public boolean isLightStatus3() {
		return lightStatus3;
	}


	/**
	 * @param lightStatus3 the lightStatus3 to set
	 */
	public void setLightStatus3(boolean lightStatus3) {
		this.lightStatus3 = lightStatus3;
	}


	/**
	 * @return the fireStatus
	 */
	public boolean isFireStatus() {
		return FireStatus;
	}


	/**
	 * @param fireStatus the fireStatus to set
	 */
	public void setFireStatus(boolean fireStatus) {
		FireStatus = fireStatus;
	}


	/**
	 * @return the surveillanceStatus
	 */
	public boolean isSurveillanceStatus() {
		return SurveillanceStatus;
	}


	/**
	 * @param surveillanceStatus the surveillanceStatus to set
	 */
	public void setSurveillanceStatus(boolean surveillanceStatus) {
		SurveillanceStatus = surveillanceStatus;
	}


	/**
	 * @return the extra
	 */
	public boolean isExtra() {
		return Extra;
	}


	/**
	 * @param extra the extra to set
	 */
	public void setExtra(boolean extra) {
		Extra = extra;
	}

}
