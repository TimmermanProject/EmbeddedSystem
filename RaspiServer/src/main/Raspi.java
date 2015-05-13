package main;

import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.UnsupportedCommOperationException;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.TooManyListenersException;

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
			System.out.println("USB communication not implemented");
		}
		
		/** COMM TO ROOM LEVEL **/
		if (COMMUNICATION_TYPE_DOWN =="Serial"){
			try {
				commFactory = new SerialFactory();
				communication_DOWN = commFactory.createComm();
				communication_DOWN.connect("/dev/ttyS80", 4000);
				
				//communication_DOWN = new SerialComm(,,db,communication_UP.getObjectOutputStream()); //portID, timeout
			} catch (IOException e) {
				System.out.println("Exception in Raspi");
				e.printStackTrace();
			}
		} else {
			
		}
		
		msgHandler = new MessageHandler(db,communication_UP.getOutputStream(),communication_DOWN.getOutputStream());
	}

	public void run(){
		try {	
			while (true){		
				ObjectInputStream objectInputStream = (ObjectInputStream) communication_UP.getInputStream();
				Object o = objectInputStream.readObject();
				msgHandler.handleMessage(o);
				communication_DOWN.addListener(db,communication_UP.getOutputStream());		
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
        } catch (TooManyListenersException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String [] args){
		Raspi t =  new Raspi();
		t.start();
	}
}
