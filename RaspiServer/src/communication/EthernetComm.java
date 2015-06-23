package communication;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;


/** 
 * @author thomasverbeke
 * Concrete Ethernet Factory
 * TODO Add frame types in handler
 * 		Connect in new thread
 * 
 * **/
public class EthernetComm extends AbstractComm {
	private Socket server;
	
	@Override
	public void connect(final String serverName, final int port) throws UnknownHostException, IOException {
		System.out.println("Attempting to connect to " + serverName + " on port " + port);
		
		/** blocks in case server cannot be found; calling method in new thread**/
		Thread openSocket = new Thread(new Runnable() {
		     public void run() {
		    	 try {
					server = new Socket(serverName, port);
					out = new ObjectOutputStream(server.getOutputStream());
					in = new ObjectInputStream(server.getInputStream());
					status=true;
					System.out.println("Just connected to "  + server.getRemoteSocketAddress());
				} catch (IOException e) {
					System.out.println("No server found");
					status=false;
				} 
		     }
		});  
		openSocket.start();	
	}

	@Override
	public void closeConnection() throws IOException {
		System.out.println("Closing the client...");
		in.close();
		out.close();
		server.close();
		System.out.println("Client closed");
	}
}
