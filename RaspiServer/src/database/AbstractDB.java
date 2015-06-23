package database;

import java.sql.Connection;

public class AbstractDB {
	protected Connection connection;
	
	AbstractDB(){
		connection = null;
	}
	
	/** connect to database **/
	public void connect(){}
	
	/**
	 * @return the database connection
	 */
	public Connection getConnection() {
		return connection;
	}
}


