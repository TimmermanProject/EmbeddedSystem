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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import shared.Frame;
import shared.RoomData;


public class EthernetCommClient extends Thread {
	private ObjectInputStream inputBuffer;
    private ObjectOutputStream outputBuffer;
    private Socket server;
    
	public EthernetCommClient(String serverName, int port) throws UnknownHostException, IOException{
	     System.out.println("Connecting to " + serverName + " on port " + port);
	     server = new Socket(serverName, port); //open socket
	     
		 inputBuffer = new ObjectInputStream(server.getInputStream());
	     outputBuffer = new ObjectOutputStream(server.getOutputStream());
	
			
	     System.out.println("Just connected to "  + server.getRemoteSocketAddress());
	
	}
	
	public void run(){
		while (true){
			try {
                Object o = inputBuffer.readObject();
                handleEthernetFrame(o); //check frame   
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
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
			//update database?
			//ACK?
			//sendEthernetFrame()
		}
	}
	
	/** wrapper to send Ethernet frames **/
	public void sendEthernetFrame(Frame frame){ //might not work -> Object
		System.out.println("Sending frame");
		try {
			outputBuffer.writeObject(frame);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}


