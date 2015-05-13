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
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.TooManyListenersException;

import core.Database;


public class SerialComm extends AbstractComm  {
    public SerialPort serialPort;
    
    //Database db, ObjectOutputStream objectOutputStream
    @Override
	public void connect (String portID, int timeout) throws IOException {
		CommPortIdentifier portIdentifier;
		try {
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
   
   public void addListener(Database db, OutputStream outputStream) throws TooManyListenersException{
	   serialPort.addEventListener(new SerialListener(db, in,out,(ObjectOutputStream) outputStream));
   }
 
}
