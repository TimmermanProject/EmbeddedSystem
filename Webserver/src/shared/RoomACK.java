/** 
 * @author thomasverbeke
 * QUESTION: Do we want 1 ACK frame or individual ACK frames? do we ACK all frames?
 * **/

package shared;

public class RoomACK extends Frame {
	
	
	public RoomACK(){
		this.setFrameType(frameTypes.ROOM_ACK);
	}
}
