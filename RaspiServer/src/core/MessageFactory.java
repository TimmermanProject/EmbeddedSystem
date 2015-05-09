/** 
 * @author thomasverbeke
 * 
 * **/

package core;

import shared.Command;
import shared.Message;
import shared.RFID;
import shared.SensorData;
import shared.Message.frameTypes;

/** 
 * 	@author Thomas Verbeke
 * 	Factory for frame object because at run-time we don't know which object is coming trough the socket object stream
 * 
 * **/

public class MessageFactory {
	public Message makeFrame (frameTypes frameType){
		Message newFrame = null;
		
		if (frameType.equals(frameTypes.ROOM_DATA)){
			return new SensorData();
		} else if (equals(frameTypes.RFID)){
			return new RFID();
		} else if (equals(frameTypes.ROOM_COMMAND)){
			return new Command();
		} else { 
			return null; 
		}

	}
}
