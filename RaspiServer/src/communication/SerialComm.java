/** 
 * @author thomasverbeke
 * CLIENT CLASS
 * 
 * TODO: 	-threads!!
 * 			-test charset; could give problems due to 8bit/16bit...blah blah
 * 			-keep alive protocol? timer + send request if no data for certain amount of time; create new frame or use existing?
 * **/

package communication;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import test.EthernetCommClient;
import core.Database;
import core.Room;
import messages.ACK;
import messages.Alarm;
import messages.Data;
import messages.Message;
import messages.RFID;

public class SerialComm extends Thread {
	private InputStream inputStream;
    private OutputStream outputStream;
    private SerialPort serialPort;
    private Database db;
    private SerialMessageHandler msgHandler;
    
	public SerialComm (String portID, int timeout) throws NoSuchPortException, PortInUseException, UnsupportedCommOperationException, IOException {
		CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portID);

		if(portIdentifier.isCurrentlyOwned()){
			System.out.println("Serial port in use");
		} else {
			CommPort commPort = portIdentifier.open(this.getClass().getName(),timeout);
			
			if( commPort instanceof SerialPort ){
				serialPort = (SerialPort) commPort;
				serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
			
				inputStream = serialPort.getInputStream();
				outputStream = serialPort.getOutputStream();
				
	            serialPort.notifyOnDataAvailable(true); // activate the DATA_AVAILABLE notifier
				
	            System.out.println("SerialComm Open");

	            db = new Database(); // connect to database
	    		db.connectToMYSQL();
	    		
	    		msgHandler = new SerialMessageHandler(db); // create messageHandler
	    		
	    		System.out.println("Connected to database");
	    		
			} else {		
				System.out.println("Error, is not a serial port");
			}
		}	
    
	}
	
	 /** Catch serial events and assemble the incoming frames 
     *  TODO Callback routine
     * **/
    public void serialEvent(SerialPortEvent event) {
    	byte[] readBuffer = new byte[400];
    	
    	switch (event.getEventType()) { 
    		case SerialPortEvent.DATA_AVAILABLE:
    			
    			try {
    				
    				if (inputStream.available() > 0) {
    					//NOW DECODE AND ASSEMBLE FRAME
    					int numBytes = inputStream.read(readBuffer);
    					//loop over data
    					for (int i=0; i<numBytes; i++) {
    						//TODO seperate thread for faster speeds?
                        	char serialChar = (char) readBuffer[i]; //cast
                        	
                        	if (serialChar == '#'){ //start delimiter
                        		System.out.println("Start of frame");
                        		char source = (char) readBuffer[i+1]; //second byte
                        		char type = (char) readBuffer[i+2]; // third byte
                        		
                        		switch (type) {
                        			//type
                        			case 'A': //RoomData: SIZE: 12bytes; as result of request or RFID set/delete
                        				
                        				//make frame
                        				Data roomData = new Data();
                        				
                        				//set BuildingID/FloorID/RoomID
                        				roomData.setRoom(new Room((char) readBuffer[i+3],(char) readBuffer[i+4]));
         
                        				//loop over accesscodes - 8 bytes - 
                        				ArrayList<Integer> accessCodes = new ArrayList<Integer>();
                        				for (int j=1; j<8; j++){
                        					accessCodes.add((int) readBuffer[i+5+j]); 
                        				}
                        				roomData.setAccessCodes(accessCodes);
                        				
                        				//set doorStatus
                        				if (readBuffer[i+14]=='A'){
                        					roomData.setDoorStatus(Data.doorStatusCodes.CLOSED);
                        				} else if (readBuffer[i+14]=='B'){
                        					roomData.setDoorStatus(Data.doorStatusCodes.EMERGENCY);
                        				} else if (readBuffer[i+14]=='C'){
                        					roomData.setDoorStatus(Data.doorStatusCodes.OPEN);
                        				}
                        				
                        				msgHandler.handleMessage(roomData);
                        			case 'F': //ACK (for command)
                        				ACK ack = new ACK();
                        				ack.setType(ACK.types.COMMAND);
                        				msgHandler.handleMessage(ack);
                        				
                        			case 'D':	//Alarm 
                        				Alarm alarm = new Alarm();
                        				msgHandler.handleMessage(alarm);
                        				
                        		}
                        		
                        		//TODO add frame to queue for handling
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
    			} catch (IOException ex) {
                    // it's best not to throw the exception because the RXTX thread may not be prepared to handle
    			}
    	}
    }
    

	/** Methods for sending data to subsystem (PIC) **/
    
    /** Write frames to PIC 
     * @throws IOException **/
    public void serialWrite(byte[] out) throws IOException{
    	outputStream.write(out, 0, out.length);
    }
    
   
    public void sendRoomDataRequest() throws IOException{
    	System.out.println("Sending Room Data Request");

    	byte[] out = new byte[16];
    	out[0] = (byte) '#';
    	
		out[1] = (byte) 'a';
		out[2] = (byte) '\n';
		
		serialWrite(out);
    }
    
    public void sendRFIDRequest(boolean status, byte RFID_tag) throws IOException{
    	byte[] out = new byte[16];
    	out[0] = (byte) '#';
    	if (status==true){
    		// RFID Add
    		System.out.println("Sending RFID Add Request");
    		out[1] = (byte) 'b';
    		out[2] = RFID_tag;
    		out[3] = (byte) '\n';
    	} else {
    		// RFID_Remove
    		System.out.println("Sending RFID Remove Request");
    		out[1] = (byte) 'c';
    		out[2] = RFID_tag;
    		out[3] = (byte) '\n';
        }
    	
    	serialWrite(out);
    }
    
    public void sendCommand(byte command) throws IOException{
		System.out.println("Sending Command");

    	byte[] out = new byte[16];
    	out[0] = (byte) '#';
    	out[1] = (byte) 'e';
    	out[2] = command;
    	out[3] = (byte) '\n';
    	
    	serialWrite(out);
    }
 			
    
    /** Test function to send a frame over serial connection**/
    public void sendFrameTest(){
    	System.out.println("Sending a test Frame");
    	System.out.println("Type: 'A'");
    		/**
    	try {
    		serialWrite('A');
			wait(1000);
		} catch (InterruptedException | IOException e) {
			e.printStackTrace();
		}
		**/
    }
    
    /** getters and setters  **/
    
    /**
     * Function returns the input stream setupCommunication should be called first
     * @return 
     */
    public InputStream getInputStream(){
        return inputStream;
    }
    
    /**
     * Getter for the outputstream in case data is to be written to the serial port
     * @return
     */
    public OutputStream getOutputStream(){
        return outputStream;
    }
    
	
    /**
    * Close the connection on the serial port 
	 * @throws IOException 
    */
   public void closeConnection() throws IOException{
     serialPort.getInputStream().close();
     serialPort.getOutputStream().close();
     serialPort.close(); // this call blocks while thread is attempting to read from inputstream
     
   }
}
