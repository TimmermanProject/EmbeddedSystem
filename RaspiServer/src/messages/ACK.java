package messages;

import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.sql.SQLException;

import core.MYSQL_db;

public class ACK extends Message {
	private static final long serialVersionUID = 1L;

	public enum types {COMMAND }
	public types type;
	
	public ACK(){
		this.setFrameType(frameTypes.ACK); 
	}
	
	public void setType (types ACK_type){
		type = ACK_type;
	}
	
	public types getType(){
		return type;
	}

	/** message came in from building subsystem**/
	@Override
	public void execute(MYSQL_db db, ObjectOutputStream objectOutputStream,
			OutputStream serialOutputStream) throws SQLException {
		// TODO Auto-generated method stub
		System.out.println("currently unsupported");
	}

}
