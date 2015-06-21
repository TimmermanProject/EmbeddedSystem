package messages;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.sql.SQLException;

import core.MYSQL_db;

public class Alarm extends Message {
	private static final long serialVersionUID = 1L;

	public Alarm(){
		this.setFrameType(frameTypes.ALARM); 
	}
	
	@Override
	public void sendObject(ObjectOutputStream objectOutputStream) throws IOException{
		objectOutputStream.writeObject(this);	
	}

	/** message came in from building subsystem 
	 * @throws SQLException **/
	@Override
	public void execute(MYSQL_db db, ObjectOutputStream objectOutputStream,
			OutputStream serialOutputStream) throws SQLException {
		db.db_setAlarmFlag(this.getRoom());
		//TODO send message to PIC to disable alarm; add to frameType; could be inside command frame!
		
	}
}
