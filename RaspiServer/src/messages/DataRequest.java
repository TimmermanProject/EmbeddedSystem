package messages;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.sql.SQLException;

import core.MYSQL_db;
import core.Room;

public class DataRequest extends Message {
	private static final long serialVersionUID = -660126755875805552L;

	public DataRequest(){
		this.setFrameType(frameTypes.DATA_REQUEST);
	}
	
	@Override
	public void sendSerial(OutputStream outputStream) {
		byte[] out = new byte[16];
    	out[0] = (byte) '#'; 						//	starting delimeter
    	out[1] = (byte) '1'; 						//	address of source
    	out[2] = (byte) 'a';						//	frameType
		out[4] = (byte) '\n';						//	ending delimter
		
		try {
			/**
			 * @param data   Array holding data to be written
		     * @param off    Offset of data in array
		     * @param n      Amount of data to write, starting from off.
		     * @return       Amount of data actually written
		     **/
			outputStream.write(out, 0, out.length);
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

	/** message came in from building subsystem**/
	@Override
	public void execute(MYSQL_db db, ObjectOutputStream objectOutputStream,
			OutputStream serialOutputStream) throws SQLException {
		try {
			Room room = db.db_getData(this.getRoom());
			Data data = new Data();
			data.setRoom(room);
			data.sendObject(objectOutputStream); //send object to building subsystem
		} catch (IOException e) {
			System.out.println("IO Exception in MessageHandler");
			e.printStackTrace();
			
		}
	}
}