/**
 * @author Thomas Verbeke
 * A socket is one end-point of a two-way communication link between two programs running on the network. 
 * Client class (typically running on RasPi) 
 * TODO
 * 	-send frames based on shared library
 *  **/
package communication;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class EthernetCommClient extends Thread {
	public static void main(String serverName, int port)
	   {
	      try {
	         System.out.println("Connecting to " + serverName + " on port " + port);
	         Socket server = new Socket(serverName, port); //open socket
	         
	         ObjectInputStream inputBuffer = new ObjectInputStream(server.getInputStream());
	         ObjectOutputStream outputBuffer = new ObjectOutputStream(server.getOutputStream());

				
	         System.out.println("Just connected to "  + server.getRemoteSocketAddress());
	         
	        
	         //server.close();
	      } catch(IOException e) {
	         e.printStackTrace();
	      }
	   }		
}


