/** 
 * @author Thomas Verbeke
 * 
 * SERVER CLASS
 * NOTE: Currently not used; Handler is inside ethernetcommclient. But this should be extracted 
 * to decouple code.
 * Handler for incoming clients connections running on the server.  (made in a new class so it can run in a separate thread
 * TODO: 	-Handle connections problems; maybe using time-out or ping system? Alive frames?
 * 			
 * 			
 * **/

package communication;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import messages.Data;
import messages.Message;
import core.MessageFactory;

public class EthernetFrameHandler extends Thread {
	Socket client;
	
	public EthernetFrameHandler (Socket client){
		this.client = client;
	}
	
	public void run(){
		
		try {
			ObjectInputStream inputBuffer = new ObjectInputStream(client.getInputStream());
			System.out.println("Just connected to " + client.getRemoteSocketAddress());
			
			while (true){
				Object obj = inputBuffer.readObject();
				//check incoming frame type
				MessageFactory frameFactory = new MessageFactory();
				Message frameCast = (Message) obj;
				
				if (frameCast != null){
					frameFactory.makeFrame(frameCast.getFrameType());
				} else {
					System.out.println("Something went wrong");
				}
				
				//old code
				//if (obj instanceof RoomData){
					//cast incoming object to shared library frame
				//	RoomData frame = (RoomData) obj;
					//handle the frame appropriately
				//	System.out.println("Building ID: " + frame.getBuildingID() + "Floor ID: " + frame.getFloorID() + "Room ID" + frame.getRoomID() );
				//} 
	        }
		} catch (IOException | ClassNotFoundException e1) {
			System.out.println("IO Exception");
			e1.printStackTrace();
		}

        
	}
	
	
}
