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
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.sql.SQLException;

import core.MYSQL_db;
import messages.Message;

public class MessageHandler extends Thread {
	private MYSQL_db db;
	private ObjectOutputStream objectOutputStream;
	private OutputStream serialOutputStream;
	
	public MessageHandler(MYSQL_db db,OutputStream objectOutputStream,OutputStream serialOutputStream){
		this.db = db;
		this.objectOutputStream = (ObjectOutputStream) objectOutputStream;
		this.serialOutputStream = serialOutputStream;
	}
	
	public void handleMessage(Object obj) throws SQLException{
		//command - execute pattern
		Message msg = (Message) obj;
		msg.execute(db,objectOutputStream, serialOutputStream);	
	}	
}

