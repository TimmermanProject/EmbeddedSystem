package main;

import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.TooManyListenersException;

import messages.Alarm;
import messages.Message;
import messages.RFID;
import communication.AbstractComm;
import communication.CommFactory;
import communication.EthernetFactory;
import communication.MessageHandler;
import communication.SerialComm;
import communication.SerialFactory;
import core.Database;
import core.Room;

/** 
 * @author thomasverbeke
 * TODO Maybe I could bring up the handler
 * 
 * 	List serial ports
 * 
 * 	ls /dev/tty.*
 *	ls /dev/cu.*
 **/

public class Raspi extends Thread {
	public static final String COMMUNICATION_TYPE_UP = "Ethernet";
	public static final String COMMUNICATION_TYPE_DOWN = "Serial";
	public static final int FLOOR_ID = 1;
	private AbstractComm communication_UP;
	private AbstractComm communication_DOWN;
	private CommFactory commFactory;
	private Database db;
	private MessageHandler msgHandler;
	
	
	public Raspi(){
		commFactory = null;
		
		/** DATABASE (SINGLETON?)**/
        db = new Database();
		db.connectToMYSQL();

		/** COMM TO BUILDING LEVEL **/
		if (COMMUNICATION_TYPE_UP == "Ethernet"){
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
			System.out.println("This type of communication is not implemented");
		}
		
		/** COMM TO ROOM LEVEL **/
		if (COMMUNICATION_TYPE_DOWN =="Serial"){
			try {
				commFactory = new SerialFactory();
				communication_DOWN = commFactory.createComm();
				communication_DOWN.connect("/dev/tty.usbserial-00001004", 4000);
				
				//communication_DOWN = new SerialComm(,,db,communication_UP.getObjectOutputStream()); //portID, timeout
			} catch (IOException e) {
				System.out.println("Exception in Raspi");
				e.printStackTrace();
			}
		} else {
			System.out.println("This type of communication is not implemented");
		}
		
		msgHandler = new MessageHandler(db,communication_UP.getOutputStream(),communication_DOWN.getOutputStream());
		/**
		try {
			communication_DOWN.addListener(db,communication_UP.getOutputStream());
		} catch (TooManyListenersException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	**/
	}

	public void run(){
		try {	
			//send test message for adding a RFID to list
			System.out.println("Sending test message");
			RFID msg = new RFID();
			msg.setRoom(new Room(1));
			msg.setTag('1');
			msg.setStatus(RFID.statusCodes.ADD);
			msg.sendSerial(communication_DOWN.getOutputStream());	
			
			while (true){
				if (communication_UP.status){
					ObjectInputStream objectInputStream = (ObjectInputStream) communication_UP.getInputStream();
					Object o = objectInputStream.readObject();
					msgHandler.handleMessage(o);
					
				}
			}
		} catch (EOFException e){	
			System.out.println("Connection lost");
			try {
				communication_UP.closeConnection();
			} catch (IOException e1) {
				e1.printStackTrace();
			}			
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String [] args){
		Raspi t =  new Raspi();
		t.run();
	}
}
