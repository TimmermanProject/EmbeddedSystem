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
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import shared.Frame;
import shared.FrameFactory;
import shared.RoomData;

public class EthernetFrameHandler extends Thread {
	Socket client;
	private static ObjectOutputStream outputBuffer;

	
	public EthernetFrameHandler (Socket client){
		this.client = client;
		try {
			outputBuffer = new ObjectOutputStream(client.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void run(){
		
		try {
			ObjectInputStream inputBuffer = new ObjectInputStream(client.getInputStream());
			System.out.println("Just connected to " + client.getRemoteSocketAddress());
			
			while (true){
				System.out.println("reading incoming data");
				Object obj = inputBuffer.readObject();
				//check incoming frame type
				FrameFactory frameFactory = new FrameFactory();
				Frame frameCast = (Frame) obj;
				
				if (frameCast != null){
					Frame frame = frameFactory.makeFrame(frameCast.getFrameType());
					System.out.println(frame.getFrameType());
					sendTestFrame();
					
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
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch(EOFException l){
	   		System.out.println("connection lost... :'(");
  		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        
	}
	
   public void sendTestFrame(){
	   RoomData frame = new RoomData();
	   frame.setFrameType(Frame.frameTypes.ROOM_DATA);
	   sendEthernetFrame(frame);
	   System.out.println("data sent... theoretically");
   }
   
   public static void sendEthernetFrame(Frame frame){
	   System.out.println("Sending frame");
	   try{
		   outputBuffer.writeObject(frame);
	   } catch(IOException e){
		   e.printStackTrace();
	   }
   }
	
	
}
