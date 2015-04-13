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

import shared.RoomData;

public class SerialComm extends Thread {
	private InputStream inputStream;
    private OutputStream outputStream;
    private SerialPort serialPort;
     
    
	public SerialComm () throws NoSuchPortException, PortInUseException, UnsupportedCommOperationException, IOException {
		
		/** this part of code should be moved to a main class -- START -- **/
    	EthernetCommClient client = new EthernetCommClient("Server",99);
    	client.start();
    	
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
    
    public void serialEvent(SerialPortEvent event) {
    	byte[] readBuffer = new byte[400];
    	
    	switch (event.getEventType()) { 
    		case SerialPortEvent.DATA_AVAILABLE:
    			
    			try {
    				
    				if (inputStream.available() > 0) {
    					//NOW DECODE AND ASSEMBLE FRAME
    					int numBytes = inputStream.read(readBuffer);
    
    					for (int i=0; i<numBytes; i++) {
    						//seperate thread for faster speeds?
                        	char serialChar = (char) readBuffer[i];
                        	
                        	if (serialChar == '#'){ //start delimiter
                        		
                        		//construct new frame
                        		char source = (char) readBuffer[i+1];
                        		char destination = (char) readBuffer[i+2];
                        		char type = (char) readBuffer[i+3];
                        		
                        		switch (type) {
                        			case 'A': //RoomData (let's say roomData has a size of 3 bytes)
                        				RoomData frame = new RoomData();
                        				frame.setBuildingID(0);
                        				frame.setFloorID(0);
                        				frame.setRoomID(0);
                        				
                        			case 'B': 
                        			case 'C':                 		
                        		}
                        		
                        		//add frame to queue
                        		
                      
                        	} else if (serialChar == '\r') { //end delimiter
                        		//end of frame -> stop
                        		break;
                        	} else {
                        		i++;
                        	}
                        }
    				}
    			} catch (IOException ex) {
                    // it's best not to throw the exception because the RXTX thread may not be prepared to handle
    			}
    	}
    }
 
}
