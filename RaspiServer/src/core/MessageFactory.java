/** 
 * @author thomasverbeke
 * 
 * **/

package core;

import messages.ACK;
import messages.Alarm;
import messages.Data;
import messages.Message;
import messages.Message.messageTypes;

/** 
 * 	@author Thomas Verbeke
 * 	Factory for frame object because at run-time we don't know which object is coming trough the socket object stream
 * 
 * **/

public class MessageFactory {
	public Message makeFrame (messageTypes frameType){
		if (frameType.equals(messageTypes.DATA)){
			return new Data();
		} else if (equals(messageTypes.ALARM)){
			return new Alarm();
		} else if (equals(messageTypes.ACK)){
			return new ACK();
		} else { 
			return null; 
		}

	}
}
