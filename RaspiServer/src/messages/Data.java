/** 
 * @author thomasverbeke
 * 
 *	This class belongs to a shared library; shared between Raspberry Pi and Server in order for them to more easily communicate. 
 *	
 * 
 * **/

package messages;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import shared.RoomShared;
import core.AbstractDB;

public class Data extends Message {
	private static final long serialVersionUID = -1547837996222004778L;
	private int size;
	private ArrayList<Integer> RFIDList; 		// contains list of RFID that can enter room
	
	private boolean doorStatus;
	private boolean doorStatus2;
	private boolean lightStatus1;
	private boolean lightStatus2;
	private boolean lightStatus3;
	private boolean FireStatus;
	private boolean SurveillanceStatus;
	private boolean Extra;
	
//ROOMDATA (size (1B), RFIDList (size*1B), 8bits status (1B)	  
	
	
	public Data(){
		this.setFrameType(messageTypes.DATA);
	}
	
	@Override
	public void sendObject(ObjectOutputStream objectOutputStream) throws IOException{
		objectOutputStream.writeObject(this);	
	}
	
	/** update database **/
	public void updateDatabase(Connection connection) throws SQLException{
		System.out.println("Updating data for room" + this.getRoom().getRoomID());
		Statement statement = connection.createStatement();	
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
	}

	/** message came in from building subsystem**/
	@Override
	public void execute(AbstractDB db, ObjectOutputStream objectOutputStream,
			OutputStream serialOutputStream) throws SQLException {
		System.out.println("not implemented yet");
		
	}
	
	/** GETTERS AND SETTERS **/

	/**
	 * @return the size
	 */
	public int getSize() {
		return size;
	}

	/**
	 * @param size the size to set
	 */
	public void setSize(int size) {
		this.size = size;
	}

	/**
	 * @return the rFIDList
	 */
	public ArrayList<Integer> getRFIDList() {
		return RFIDList;
	}

	/**
	 * @param rFIDList the rFIDList to set
	 */
	public void setRFIDList(ArrayList<Integer> rFIDList) {
		RFIDList = rFIDList;
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
