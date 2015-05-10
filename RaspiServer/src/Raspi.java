import java.io.EOFException;
import java.io.IOException;

import communication.AbstractComm;
import communication.CommFactory;
import communication.EthernetFactory;

import core.Database;

/** 
 * @author thomasverbeke
 * 
 **/

public class Raspi extends Thread {
	public static final String COMMUNICATION_TYPE = "Ethernet";

	@SuppressWarnings("unused")
	public Raspi(){
		CommFactory commFactory = null;
		Database db = new Database();
		db.connectToMYSQL();
		
		/** COMM TO BUILDING LEVEL **/
		if (COMMUNICATION_TYPE == "Ethernet"){
			commFactory = new EthernetFactory();
			initUpCommunication(commFactory);
		} else {
			System.out.println("USB communication not implemented");
		}
		
		/** COMM TO ROOM LEVEL (SERIAL) **/
	}
	
	/** Initialise communciation with Building Subsystem**/
	public void initUpCommunication(CommFactory communicationFactory){
		AbstractComm communication = communicationFactory.createComm();
		try {
			String serverAddr = "169.254.26.95";
			int port = 8080;
			communication.connect(serverAddr, port);
		} catch (IOException e) {
			System.out.println("IOException in initCommunication");
		}
	}
	
	public void run(AbstractComm communication){
		//send 1 frame to test
		try {	
			communication.sendTestFrame();	
			while (true){
				System.out.println("incoming data");
                Object o = communication.inputBuffer.readObject();
                communication.handleFrame(o); //check frame
			}
		} catch (EOFException e){	
			System.out.println("Connection lost");
			try {
				communication.closeConnection();
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
	
	public static void main(String [] args){
		Thread t = new Raspi();
		t.run();	
	}
}
