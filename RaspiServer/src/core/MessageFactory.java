/** 
 * @author thomasverbeke
 * 
 * **/

package core;

import messages.ACK;
import messages.Alarm;
import messages.Data;
import messages.Message;
import messages.Message.frameTypes;

/** 
 * 	@author Thomas Verbeke
 * 	Factory for frame object because at run-time we don't know which object is coming trough the socket object stream
 * 
 * **/

public class MessageFactory {
	public Message makeFrame (frameTypes frameType){
		if (frameType.equals(frameTypes.DATA)){
			return new Data();
		} else if (equals(frameTypes.ALARM)){
			return new Alarm();
		} else if (equals(frameTypes.ACK)){
			return new ACK();
		} else { 
			return null; 
		}

	}
}
