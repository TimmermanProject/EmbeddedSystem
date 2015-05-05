/** 
 * @author thomasverbeke
 * CLIENT CLASS
 * 
 * TODO: 	-threads!!
 * 			-test charset; could give problems due to 8bit/16bit...blah blah
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

import shared.ACK;
import shared.Frame;
import shared.RFID;
import shared.RoomData;

public class SerialComm extends Thread {
	private InputStream inputStream;
    private OutputStream outputStream;
    private SerialPort serialPort;
    private ArrayList<Frame> frameBuffer;
    
	public SerialComm (ArrayList<Frame> frameBuffer) throws NoSuchPortException, PortInUseException, UnsupportedCommOperationException, IOException {
		
		/** this part of code should be moved to a main class -- START -- **/
    	EthernetCommClient client = new EthernetCommClient("Server",99);
    	client.start();
    	
    	this.frameBuffer = frameBuffer;
    	
		/** this part of code should be moved to a main class -- END -- **/
		
		CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier("/dev/ttyS80");

		if(portIdentifier.isCurrentlyOwned()){
			System.out.println("Port is currently in use");
		} else {
			CommPort commPort = portIdentifier.open(this.getClass().getName(),4000);
			
			if( commPort instanceof SerialPort ){
			
				serialPort = (SerialPort) commPort;
				serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
			
				inputStream = serialPort.getInputStream();
				outputStream = serialPort.getOutputStream();
				
				// activate the DATA_AVAILABLE notifier
	            serialPort.notifyOnDataAvailable(true);
				
			System.out.println("Port opened, info from setupCommunication in SerialCommunicator");
			
			} else {		
				System.out.println("Error, was not a serial port");
			}
		}
    
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
    
    /** Write frames to PIC 
     * @throws IOException **/
    public void serialWrite(char frameType) throws IOException{
    	byte[] out = new byte[16];
    	out[0] = (byte) '#';
    	
    	switch (frameType){
    		case 'A':                  // frame: "#A\n"
    			out[1] = (byte) 'A';
    			out[2] = (byte) '\n';
    		//case 'B':
    	}
    		
    	outputStream.write(out, 0, out.length);
    }
    
    /** Test function to send a frame over serial connection**/
    public void sendFrameTest(){
    	System.out.println("Sending a test Frame");
    	System.out.println("Type: 'A'");
    		
    	try {
    		serialWrite('A');
			wait(1000);
		} catch (InterruptedException | IOException e) {
			e.printStackTrace();
		}
    }
    
    /** Catch serial events and assemble the incoming frames 
     * 
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
                        			case 'A': //RoomData: SIZE: 12bytes
                        				
                        				//make frame
                        				RoomData frame = new RoomData();
                        				
                        				//set BuildingID/FloorID/RoomID
                        				frame.setBuildingID((char) readBuffer[i+3]);
                        				frame.setFloorID((char) readBuffer[i+4]);
                        				frame.setRoomID((char) readBuffer[i+5]);
                        				
                        				//loop over accesscodes - 8 bytes - 
                        				ArrayList<Integer> accessCodes = new ArrayList<Integer>();
                        				for (int j=1; j<8; j++){
                        					accessCodes.add((int) readBuffer[i+5+j]); 
                        				}
                        				frame.setAccessCodes(accessCodes);
                        				
                        				//set doorStatus
                        				if (readBuffer[i+14]=='A'){
                        					frame.setDoorStatus(RoomData.doorStatusCodes.CLOSED);
                        				} else if (readBuffer[i+14]=='B'){
                        					frame.setDoorStatus(RoomData.doorStatusCodes.EMERGENCY);
                        				} else if (readBuffer[i+14]=='C'){
                        					frame.setDoorStatus(RoomData.doorStatusCodes.OPEN);
                        				}
                        			case 'B': //RFID
                        				
                        			case 'C':                 		
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
    
    /** Handle serial frame; better to take this out of this class after initial development for loose coupling of code
     *  This should be handled in separate thread
     * **/
    public void handleSerialFrame(Frame frame) {
    	if( frame instanceof RoomData ){ //RoomData Frame
    		System.out.println("FrameType: RoomData");
    		//TODO Update database
    		//TODO Send frame to Building Subsystem
		}
    	
    	else if (frame instanceof ACK){
    		
    	}
    	
    	
    }
}
