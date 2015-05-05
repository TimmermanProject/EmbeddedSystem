/**
 * @author Thomas Verbeke
 * A socket is one end-point of a two-way communication link between two programs running on the network. 
 * CLIENT CLASS
 * 
 * TODO
 * 	-send frames based on shared library
 * 	-implement logging system? (maybe not necessary in this version)
 *  **/
package communication;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import shared.Frame;
import shared.RoomData;


public class EthernetCommClient extends Thread {
	private ObjectInputStream inputBuffer;
    private static ObjectOutputStream outputBuffer;
    private Socket server;
    
	public EthernetCommClient(String serverName, int port) throws UnknownHostException, IOException{
	     System.out.println("Connecting to " + serverName + " on port " + port);
	     server = new Socket(serverName, port); //open socket
	     
	     System.out.println("Just connected to "  + server.getRemoteSocketAddress());
	     
	     outputBuffer = new ObjectOutputStream(server.getOutputStream());
	     inputBuffer = new ObjectInputStream(server.getInputStream());
	}
	
	public void run(){
		//send 1 frame to test
		sendTestFrame();
		try {	
			while (true){
				System.out.println("incoming data");
                Object o = inputBuffer.readObject();
                handleEthernetFrame(o); //check frame
			}
		} catch (EOFException e){	
			System.out.println("Connection lost");
			try {
				server.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}			
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
	}
	
	/** getter for inputstream **/
	public ObjectInputStream getInputBuffer(){
		return inputBuffer;
	}
	
	/** getter for outputstream**/
	public ObjectOutputStream getOutputBuffer(){
		return outputBuffer;
	}
	
	/** close connections in a proper way **/
	public void closeClient() throws IOException{
		System.out.println("Closing the client...");
		
		inputBuffer.close();
		outputBuffer.close();
		server.close();
		
		System.out.println("Client closed");
	}
	
	/** check what the frame type is and handle accordingly **/
	public void handleEthernetFrame(Object o){
		if( o instanceof RoomData ){ //RoomData Frame
			System.out.println("FrameType: RoomData");
		}
	}
	
	/** wrapper to send Ethernet frames **/
	public static void sendEthernetFrame(Frame frame){ //might not work -> Object
		System.out.println("Sending frame");
		try {
			outputBuffer.writeObject(frame);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String [] args){
		int port = 8080;
		try {
			Thread t = new EthernetCommClient("169.254.26.95",port);
			t.run();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void sendTestFrame(){
		RoomData frame = new RoomData();
		frame.setFrameType(Frame.frameTypes.ROOM_DATA);
		frame.setRoomID(1);
		frame.setFloorID(2);
		frame.setBuildingID(3);
		
		sendEthernetFrame(frame);
	}
	
}

