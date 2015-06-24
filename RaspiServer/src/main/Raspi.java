package main;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.SQLException;

import shared.Alarm;
import shared.Command;
import shared.Room;
import communication.AbstractComm;
import communication.CommFactory;
import communication.EthernetFactory;
import communication.MessageHandler;
import communication.SerialFactory;
import database.AbstractDB;
import database.DBFactory;
import database.MYSQLFactory;


/** 
 * @author thomasverbeke
 * TODO Maybe I could bring up the handler
 * 
 * 	List serial ports
 * 
 * 	ls /dev/tty.*
 *	ls /dev/cu.*
 *	
 *	start tomcat:  	cd /Library/Tomcat/bin
 *					./startup.sh
 *
 *	MYSQL: 
 *  mysql.server start
 *  had to do  sudo chown -R _mysql /usr/local/var/mysql
 **/

public class Raspi extends Thread {
	public static final String COMMUNICATION_TYPE_UP = "Ethernet";
	public static final String COMMUNICATION_TYPE_DOWN = "Serial";
	public static final String DATABASE_TYPE = "Mysl"; //better to have in the code
	public static final int FLOOR_ID = 1;
	private AbstractComm communication_UP;
	private AbstractComm communication_DOWN;
	private CommFactory commFactory;
	private DBFactory dbFactory;
	private MessageHandler msgHandler;
	private AbstractDB db;
	
	
	public Raspi(){
		commFactory = null;
		dbFactory = null;
		
		/** DATABASE **/
		dbFactory = new MYSQLFactory();
		db = dbFactory.createDB();
		db.connect();

		/** COMM TO BUILDING LEVEL **/
		if (COMMUNICATION_TYPE_UP == "Ethernet"){
			commFactory = new EthernetFactory();
			 
			communication_UP = commFactory.createComm();
			try {
				String serverAddr = "169.254.202.43";
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
			
			
			//RFID msg = new RFID();
			//msg.setRoom(new Room(1));
			//msg.setTag('1');
			//msg.setStatus(RFID.statusCodes.ADD);
			//msg.sendSerial(communication_DOWN.getOutputStream());	
			
			
			ObjectInputStream objectInputStream = (ObjectInputStream) communication_UP.getInputStream();
			Object o;
				
			/**
			//TEST 1: OPEN DOOR
			System.out.println("Open Door");
			Command msg = new Command();
			msg.setLightStatus1(true);
			msg.setDoorStatus(true);
			//msg.sendSerial(communication_DOWN.getOutputStream());
			msg.sendSerial(System.out);
			**/
			//TEST 2: ALARM
			Alarm alrm = new Alarm();
			alrm.setRoom(new Room(1,1,1));
			try {
				alrm.sendObject((ObjectOutputStream) communication_UP.getOutputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
		
			while (true){
				
				try {
					o = objectInputStream.readObject();
					msgHandler.handleMessage(o);
				} catch (ClassNotFoundException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}		
				 
			}
		
		/**} catch (EOFException e){	
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
		}**/
	}
	
	public static void main(String [] args){
		Raspi t =  new Raspi();
		t.run();
	}
}
