import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;

import java.io.EOFException;
import java.io.IOException;

import communication.AbstractComm;
import communication.CommFactory;
import communication.EthernetFactory;
import communication.SerialComm;
import core.Database;

/** 
 * @author thomasverbeke
 * 
 **/

public class Raspi extends Thread {
	public static final String COMMUNICATION_TYPE = "Ethernet";
	private AbstractComm communication_UP;
	private SerialComm communication_DOWN;
	
	public Raspi(){
		CommFactory commFactory = null;
		
		/** COMM TO BUILDING LEVEL **/
		if (COMMUNICATION_TYPE == "Ethernet"){
			commFactory = new EthernetFactory();
			 
			communication_UP = commFactory.createComm();
			try {
				String serverAddr = "169.254.26.95";
				int port = 8080;
				communication_UP.connect(serverAddr, port); //non-blocking implementation; 
			} catch (IOException e) {
				System.out.println("IOException in initCommunication");
			}
		} else {
			System.out.println("USB communication not implemented");
		}
		
		/** COMM TO ROOM LEVEL **/
		try {
			communication_DOWN = new SerialComm("/dev/ttyS80",4000); //portID, timeout
		} catch (NoSuchPortException | PortInUseException
				| UnsupportedCommOperationException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	public void run(){
		//send 1 frame to test
		try {	
			communication_UP.sendTestFrame();	
			while (true){
				System.out.println("incoming data");
                Object o = communication_UP.inputBuffer.readObject();
                communication_UP.handleFrame(o); //check frame
			}
		} catch (EOFException e){	
			System.out.println("Connection lost");
			try {
				communication_UP.closeConnection();
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
		Raspi t =  new Raspi();
		t.start();
	}
}
