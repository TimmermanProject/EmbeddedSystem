import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;

import communication.SerialComm;

/** 
 * @author thomasverbeke
 * Setup all the connections and appropriate threads
 * 
 * In first instance just a class to test the socket classes; we want to test if sending different kind of frames works or not
 * **/


public class SetupComm {
	public SetupComm() throws NoSuchPortException, PortInUseException, UnsupportedCommOperationException, IOException{
		//open serial port (SerialComm Class)
		//SerialComm serial = new SerialComm();
		//serial.start();
		
		
		
		//start ethernet client (EthernetCommClient)
		//create database
	}
	
}
