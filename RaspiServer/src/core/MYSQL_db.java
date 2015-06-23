package core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import shared.RoomShared;

public class MYSQL_db {
	private Connection connect = null;
	
	public void connectToMYSQL(){
	    try {
	        // The newInstance() call is a work around for some
	        // broken Java implementations
	        Class.forName("com.mysql.jdbc.Driver").newInstance();
	        
	        connect = DriverManager.getConnection("jdbc:mysql://localhost/raspi?"
	                    + "user=root");   
	        
	        System.out.println("Connected to MYSQL database 'Raspi@root' ");
	    } catch (Exception e) {
	        // handle the error
	    	System.out.println("Error while connecting to mysql");
	    	e.printStackTrace();
	    }
	}

	/**
	 * @return the connect
	 */
	public Connection getConnection() {
		return connect;
	}

	/**
	 * @param connect the connect to set
	 */
	public void setConnection(Connection connect) {
		this.connect = connect;
	}
	
	
	/** OLD CODE

	public RoomShared db_setCommandFlag(RoomShared room) throws SQLException{
		System.out.println("Set alarm");
		statement = connect.createStatement();
		resultSet = statement.executeQuery(""
				+ 	"UPDATE raspi.Rooms "
			//	+ 	"SET cmdFlag ="+room.getCmdFlag() +" "
				+ 	"WHERE rooms.RoomID="+room.getRoomID() +";");
		
		return room;
	}
	
	
	**/
}
