/** 
 * @author thomasverbeke
 * 
 * **/

package shared;

import shared.Frame.frameTypes;

/** 
 * 	@author Thomas Verbeke
 * 	Factory for frame object because at run-time we don't know which object is coming trough the socket object stream
 * 
 * **/

public class FrameFactory {
	public Frame makeFrame (frameTypes frameType){
		Frame newFrame = null;
		
		if (frameType.equals(frameTypes.ROOM_DATA)){
			return new RoomData();
		} else if (equals(frameTypes.RFID)){
			return new RFID();
		} else if (equals(frameTypes.ROOM_COMMAND)){
			return new RoomCommand();
		} else { 
			return null; 
		}

	}
}
