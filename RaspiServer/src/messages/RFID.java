/** 
 * @author thomasverbeke
 * 
 * **/

package messages;

import java.io.IOException;
import java.io.OutputStream;

public class RFID extends Message {
	private int tag; 
	public enum statusCodes { ADD, REMOVE }
	private statusCodes statusCode;
	
	public RFID (){
		this.setFrameType(frameTypes.RFID);
	}
	
	@Override
	public void send(OutputStream outputStream) {
		byte[] out = new byte[16];
    	out[0] = (byte) '#'; 							//	starting delimeter
    	out[1] = (byte) '1'; 							//	address of source
    	
    	if (statusCode == statusCodes.ADD){
    		out[2] = (byte) 'b';						//	frameType
    	} else if (statusCode == statusCodes.REMOVE){
    		out[2] = (byte) 'c';						//	frameType
    	}
    	
		out[3] = (byte) tag; 							//	message: 1B RFID tag
		out[4] = (byte) '\n';							//	ending delimter
		
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
	
	/** getters and setters **/
	
	public void setTag(int tag){
		this.tag = tag;
	}
	
	public int getTag (){
		return tag;
	}
	
	public void setStatus(statusCodes statusCode){
		this.statusCode = statusCode;
	}	
}
