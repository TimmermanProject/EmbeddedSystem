/** 
 * @author Thomas Verbeke
 * 
 * SERVER CLASS
 * NOTE: Currently not used; Handler is inside ethernetcommclient. But this should be extracted 
 * to decouple code.
 * Handler for incoming clients connections running on the server.  (made in a new class so it can run in a separate thread
 * TODO: 	-Handle connections problems; maybe using time-out or ping system? Alive frames?
 * 			
 * 			
 * **/

package communication;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.sql.SQLException;

import core.Database;
import core.Room;
import messages.Alarm;
import messages.Command;
import messages.Data;
import messages.DataRequest;
import messages.RFID;

public class MessageHandler extends Thread {
	private Database db;
	private ObjectOutputStream objectOutputStream;
	private OutputStream serialOutputStream;
	
	public MessageHandler(Database db,OutputStream objectOutputStream,OutputStream serialOutputStream){
		this.db = db;
		this.objectOutputStream = (ObjectOutputStream) objectOutputStream;
		this.serialOutputStream = serialOutputStream;
	}
	
	public void handleMessage(Object obj){
		//TODO check were we can implement factory
		/**
		//check incoming frame type
		MessageFactory frameFactory = new MessageFactory();
		Message msg = (Message) obj;
		
		if (msg != null){
			msg = frameFactory.makeFrame(msg.getFrameType());
		} else {
			System.out.println("Something went wrong");
		}
		**/
		if( obj instanceof Data ){
			//Data msg = (Data) obj;
			//TODO update database
		} else if (obj  instanceof DataRequest){
			DataRequest msg = (DataRequest) obj;
			try {
				Room room = db.db_getData(msg.getRoom());
				Data data = new Data();
				data.setRoom(room);
				data.sendObject(objectOutputStream); //send object to building subsystem
			} catch (SQLException e) {
				System.out.println("SQL Exception in MessageHandler");
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println("IO Exception in MessageHandler");
				e.printStackTrace();
				
			}
		}else if (obj instanceof RFID){
			RFID msg = (RFID) obj;
			try {
				msg.sendSerial(serialOutputStream); //send over serial connection to room subsystem
				if (msg.getStatus()==RFID.statusCodes.ADD){ // database call
					db.db_addTag(msg.getRoom(), msg.getTag());
				} else if (msg.getStatus()==RFID.statusCodes.REMOVE) {
					db.db_removeTag(msg.getRoom(), msg.getTag());
				}
			} catch (SQLException e) { 
				System.out.println("SQL Exception in MessageHandler");
				e.printStackTrace();
			}
		} else if (obj instanceof Command){
			Command msg = (Command) obj;
			//db.db_setCommandFlag(msg.getRoom().)
			//TODO edit database
			//send frame over serial
		} else if (obj instanceof Alarm){
			Alarm msg = (Alarm) obj;
			try {
				db.db_setAlarmFlag(msg.getRoom());
				//TODO send message to PIC to disable alarm; add to frameType; could be inside command frame!
			} catch (SQLException e) {
				System.out.println("SQL Exception in MessageHandler");
				e.printStackTrace();
			}
		}
		
		// TODO LOG FILE REQUEST
		
	}	
}
