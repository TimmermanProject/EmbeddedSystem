/** 
 * @author thomasverbeke
 * 
 * **/

package shared;

public class RFID extends Frame {
	private int accessCode; 
	public enum statusCodes { ADD, REMOVE }
	private statusCodes statusCode;
	
	public RFID (){
		this.setFrameType(frameTypes.RFID);
	}
	
	/** getters and setters **/
	
	public void setAccesCode(int accessCode){
		this.accessCode = accessCode;
	}
	
	public int getAccessCode (){
		return accessCode;
	}
	
	public void setStatus(statusCodes statusCode){
		this.statusCode = statusCode;
	}
	
}
