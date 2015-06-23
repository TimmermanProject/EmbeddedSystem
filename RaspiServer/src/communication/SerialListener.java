package communication;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.BitSet;

import shared.ACK;
import shared.Alarm;
import shared.Data;
import shared.Message;
import shared.RoomShared;
import shared.ACK.states;
import shared.ACK.types;
import main.Raspi;
import database.AbstractDB;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

public class SerialListener implements SerialPortEventListener {
	private InputStream serialInputStream;
    private ObjectOutputStream objectOutputStream;
    private AbstractDB db;
    
	public SerialListener(AbstractDB db, InputStream serialInputStream, ObjectOutputStream objectOutputStream){
		this.db = db;
		this.serialInputStream = serialInputStream;
		this.objectOutputStream = objectOutputStream;
	}
	
	public SerialListener(AbstractDB db2, InputStream in) {
		this.db = db2;
		this.serialInputStream = in;
	}

	@Override
	/** Catch serial events and assemble the incoming frames 
     * **/
    public void serialEvent(SerialPortEvent event) {
    	byte[] readBuffer = new byte[400];
    	
    	switch (event.getEventType()) { 
    		case SerialPortEvent.DATA_AVAILABLE:
    			
    			try {
    				//TODO nog eens controleren
    				if (serialInputStream.available() > 0) {
    					//NOW DECODE AND ASSEMBLE FRAME
    					int numBytes = serialInputStream.read(readBuffer);
    					//loop over data
    					for (int i=0; i<numBytes; i++) {
    						//TODO seperate thread for faster speeds?
                        	char serialChar = (char) readBuffer[i]; //cast
                        	
                        	if (serialChar == '#'){ //start delimiter
                        		System.out.println("Start of frame");
                        		
                        		int source = (int) readBuffer[i+1]; //SOURCE (3B): [ElementID (1B), RoomID (1B), FloorID (1B)]
                        		RoomShared room = new RoomShared(1,source, Raspi.FLOOR_ID);
                        		Message msg;
                        		char type = (char) readBuffer[i+2]; //third byte
                        		switch (type) {  //TYPE (1B)
                        			case 'A': // ROOMDATA (size (1B), RFIDList (size*1B), 8bits status (1B)	                      			
                        				msg = new Data();
                        				int size = (char) readBuffer[i+3];
                        				ArrayList RFIDList = new ArrayList();
                        				
                        				for (int j=0; j<size; j++){
                        					RFIDList.add( (char) readBuffer[i+3+j]);
                        				}
                        				
                        				((Data) msg).setRFIDList(RFIDList);
     
                        				BitSet statusBits = BitSet.valueOf(new byte[] {readBuffer[i+4]});
   
                        				((Data) msg).setDoorStatus(statusBits.get(0));
                        				((Data) msg).setDoorStatus2(statusBits.get(1));
                        				((Data) msg).setLightStatus1(statusBits.get(2));
                        				((Data) msg).setLightStatus2(statusBits.get(3));
                        				((Data) msg).setLightStatus3(statusBits.get(4));
                        				((Data) msg).setFireStatus(statusBits.get(5));
                        				((Data) msg).setSurveillanceStatus(statusBits.get(6));
                        				((Data) msg).setExtra(statusBits.get(7));
                        				                  		
                        				((Data) msg).updateDatabase(db.getConnection()); //update DB
                        				((Data) msg).sendObject(objectOutputStream);
                        				
                        			case 'F': //ACK (ACKType [b=addRFID,c=RemoveRFID,e=Command], tag/status (1B))
                        				char ACK_type = (char) readBuffer[i+3]; // fourth byte
                        				msg = new ACK();
                        				((ACK) msg).setState(states.SUCCESS);
                        				switch (ACK_type) {
                        					case 'b': // addRFID
                        						((ACK) msg).setType(types.RFID_ADD);
                        						((ACK) msg).setValue(readBuffer[i+4]);
                        					case 'c': // removeRFID
                        						((ACK) msg).setType(types.RFID_REMOVE);
                        						((ACK) msg).setValue(readBuffer[i+4]);
                        					case 'e': // command
                        						((ACK) msg).setType(types.COMMAND);
                        						((ACK) msg).setValue(readBuffer[i+4]);
                        				}
                        				
                        				((ACK) msg).setValue(readBuffer[i+4]);
                        				((ACK) msg).updateDatabase(db.getConnection()); //update DB   
                        				((ACK) msg).sendObject(objectOutputStream);
                        				
                        			case 'G': //NACK (ACKType [b=addRFID,c=RemoveRFID,e=Command])
                        				char NACK_type = (char) readBuffer[i+3]; // fourth byte
                        				msg = new ACK();
                        				((ACK) msg).setState(states.FAILURE);
                        				switch (NACK_type) {
                        					case 'b': // addRFID
                        						((ACK) msg).setType(types.RFID_ADD);
                        					case 'c': // removeRFID
                        						((ACK) msg).setType(types.RFID_REMOVE);
                        					case 'e': // command
                        						((ACK) msg).setType(types.COMMAND);
                        				}
                        				((ACK) msg).setValue(readBuffer[i+4]);
                        				((ACK) msg).updateDatabase(db.getConnection()); //update DB 
                        				((ACK) msg).sendObject(objectOutputStream);	
                        				
                        			case 'D':	//ALARM (8 bit status (1B)
                        				msg = new Alarm();
                        				msg.setRoom(room);
                    
                        				BitSet statusBits2 = BitSet.valueOf(new byte[] {readBuffer[i+4]});
                        				   
                        				((Alarm) msg).setDoorStatus(statusBits2.get(0));
                        				((Alarm) msg).setDoorStatus2(statusBits2.get(1));
                        				((Alarm) msg).setLightStatus1(statusBits2.get(2));
                        				((Alarm) msg).setLightStatus2(statusBits2.get(3));
                        				((Alarm) msg).setLightStatus3(statusBits2.get(4));
                        				((Alarm) msg).setFireStatus(statusBits2.get(5));
                        				((Alarm) msg).setSurveillanceStatus(statusBits2.get(6));
                        				((Alarm) msg).setExtra(statusBits2.get(7));
                        				
                        				((Alarm) msg).updateDatabase(db.getConnection()); //update DB
                        				((Alarm) msg).sendObject(objectOutputStream);	
                        		}
                        		
                        		
                        		System.out.println("Frame assembled and added to queue");
                      
                        	} else if (serialChar == '\r') { //end delimiter
                        		//end of frame -> stop
                        		System.out.println("End of frame");
                        		break;
                        	} else {
                        		System.out.println("Lost data?");
                        		i++;
                        	}
                        }
    				}
    			} catch (IOException | SQLException ex) {
                    // it's best not to throw the exception because the RXTX thread may not be prepared to handle
    			}
    	}
    }

}
