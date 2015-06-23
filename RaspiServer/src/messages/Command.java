package messages;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.BitSet;

import core.AbstractDB;

public class Command extends Message {
	private static final long serialVersionUID = 1L;
	private boolean doorStatus;
	private boolean doorStatus2;
	private boolean lightStatus1;
	private boolean lightStatus2;
	private boolean lightStatus3;
	private boolean FireStatus;
	private boolean SurveillanceStatus;
	private boolean Extra;
	
	public Command(){
		this.setFrameType(messageTypes.COMMAND);
	}

	@Override
	public void sendSerial(OutputStream outputStream) {
		System.out.println("Sending Command");
		byte[] out = new byte[6];
    	out[0] = (byte) '#'; 		//	starting delimeter
    	out[1] = (byte) '1'; 		//	address of dest
    	out[2] = (byte) '2';		//  address of source (2 = raspb pi)
    	out[3] = (byte) 'e';		//	frameType
    
    	BitSet statusBits = new BitSet();
    	statusBits.set(0,doorStatus);
    	statusBits.set(1,doorStatus2);
    	statusBits.set(2,lightStatus1);
    	statusBits.set(3,lightStatus2);
    	statusBits.set(4,lightStatus3);
    	statusBits.set(5,FireStatus);
    	statusBits.set(6,SurveillanceStatus);
    	statusBits.set(7,Extra);
    	
    	out[4] = (byte) statusBits.toByteArray()[0];	// 	message : 1B ElementID EDIT THIS
		out[5] = (byte) '\n';		//	ending delimter
		
		try {
			/**
			 * @param data   Array holding data to be written
		     * @param off    Offset of data in array
		     * @param n      Amount of data to write, starting from off.
		     * @return       Amount of data actually written
		     **/
			System.out.println(out.length);
			outputStream.write(out, 0, out.length);
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	@Override
	public void sendObject(ObjectOutputStream objectOutputStream) throws IOException{
		objectOutputStream.writeObject(this);	
	}
	
	
	/** message came in from building subsystem 
	 * @throws SQLException **/
	@Override
	public void execute(AbstractDB db, ObjectOutputStream objectOutputStream,
			OutputStream serialOutputStream) throws SQLException {

		//foward command to RCU (room sub system)
		sendSerial(serialOutputStream);
	} 
	
	/** Getters and setters **/

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
