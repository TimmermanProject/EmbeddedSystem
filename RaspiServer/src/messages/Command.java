package messages;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import messages.RFID.statusCodes;

public class Command extends Message {
	private int actionID;
	private int newStatus;
	
	public Command(){
		this.setFrameType(frameTypes.COMMAND);
	}

	@Override
	public void sendSerial(OutputStream outputStream) {
		System.out.println("Sending Command");
		byte[] out = new byte[16];
    	out[0] = (byte) '#'; 		//	starting delimeter
    	out[1] = (byte) '1'; 		//	address of source
    	out[2] = (byte) 'e';		//	frameType
    	out[3] = (byte) actionID;	// 	message : 1B ElementID
    	out[4] = (byte) newStatus;	// 	message : 1B New Status
		out[4] = (byte) '\n';		//	ending delimter
		
		try {
			/**
			 * @param data   Array holding data to be written
		     * @param off    Offset of data in array
		     * @param n      Amount of data to write, starting from off.
		     * @return       Amount of data actually written
		     **/
			outputStream.write(out, 0, out.length);
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	@Override
	public void sendObject(ObjectOutputStream objectOutputStream) throws IOException{
		objectOutputStream.writeObject(this);	
	}
	
	/** getters and setters **/

	/**
	 * @return the actionID
	 */
	public int getActionID() {
		return actionID;
	}

	/**
	 * @param actionID the actionID to set
	 */
	public void setActionID(int actionID) {
		this.actionID = actionID;
	}	

	/**
	 * @return the newStatus
	 */
	public int getNewStatus() {
		return newStatus;
	}

	/**
	 * @param newStatus the newStatus to set
	 */
	public void setNewStatus(int newStatus) {
		this.newStatus = newStatus;
	}
}
