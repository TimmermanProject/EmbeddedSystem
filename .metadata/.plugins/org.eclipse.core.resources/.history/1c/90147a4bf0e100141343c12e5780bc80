/** 
 * @author Thomas Verbeke
 * 
 * SERVER CLASS
 * 
 * Handler for incoming clients connections running on the server.  (made in a new class so it can run in a separate thread
 * TODO: 	-Handle connections problems; maybe using time-out or ping system? Alive frames?
 * 			-Add other frames 
 * 			-Handle frames (ACK; the rest if for Dauphin)
 * 			
 * **/

package communication;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import shared.RoomData;

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
				if (obj instanceof RoomData){
					//cast incoming object to shared library frame
					RoomData frame = (RoomData) obj;
					//handle the frame appropriately
					System.out.println("Building ID: " + frame.getBuildingID() + "Floor ID: " + frame.getFloorID() + "Room ID" + frame.getRoomID() );
				} 
	        }
		} catch (IOException | ClassNotFoundException e1) {
			System.out.println("IO Exception");
			e1.printStackTrace();
		}

        
	}
	
	
}
