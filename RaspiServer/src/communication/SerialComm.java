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
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.TooManyListenersException;

import core.MYSQL_db;


public class SerialComm extends AbstractComm  {
    public SerialPort serialPort;
    static HashMap<String, CommPortIdentifier> portMap;
    static CommPortIdentifier portId;
	static CommPortIdentifier saveportId;
	
    @Override
	public void connect (String portID, int timeout) throws IOException {
		CommPortIdentifier portIdentifier;
		try {
			if (portMap == null) {
	            portMap = new HashMap<String, CommPortIdentifier>();
	            Enumeration portList = CommPortIdentifier.getPortIdentifiers();
	            System.out.println("Printing ports:");
	           
	            while (portList.hasMoreElements()) {
	                portId = (CommPortIdentifier) portList.nextElement();     
	                if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
	                    portMap.put(portId.getName(), portId);
	                    System.out.println("-portName: " + portId.getName() + "   -portID: " + portId);
	                }
	            }
			}
	            
			portIdentifier = CommPortIdentifier.getPortIdentifier(portID);
			
			if(portIdentifier.isCurrentlyOwned()){
				System.out.println("Serial port in use");
			} else {
				CommPort commPort = portIdentifier.open(this.getClass().getName(),timeout);
				
				if( commPort instanceof SerialPort ){
					serialPort = (SerialPort) commPort;
					serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
				
					in = serialPort.getInputStream();
					out = serialPort.getOutputStream();
					
		            serialPort.notifyOnDataAvailable(true); // activate the DATA_AVAILABLE notifier
		            
					    		
				} else {		
					System.out.println("Error, is not a serial port");
				}
			}	
		} catch (NoSuchPortException | PortInUseException | UnsupportedCommOperationException e) {
			System.out.println("Port is not found");
			e.printStackTrace();
		}
		
    
	}

	/** Methods for sending data to subsystem (PIC) - move to messages **/
    
    /** Write frames to PIC 
     * @throws IOException **/
    public void serialWrite(byte[] out) throws IOException{
    	this.out.write(out, 0, out.length);
    }
    
    /**
    * Close the connection on the serial port 
	 * @throws IOException 
    */
   @Override
   public void closeConnection() throws IOException{
     serialPort.getInputStream().close();
     serialPort.getOutputStream().close();
     serialPort.close(); // this call blocks while thread is attempting to read from inputstream
     
   }
   /**
   public void addListener(Database db, OutputStream outputStream) throws TooManyListenersException{
	   //serialPort.addEventListener(new SerialListener(db, in,out,(ObjectOutputStream) outputStream));
	   serialPort.addEventListener(new SerialListener(db, in,out));
   }
   **/
   
   public void addListener(MYSQL_db db) throws TooManyListenersException{
	   //serialPort.addEventListener(new SerialListener(db, in,out,(ObjectOutputStream) outputStream));
	   serialPort.addEventListener(new SerialListener(db, in,out));
   }
 
}
