package database;


import java.sql.DriverManager;

public class MYSQL extends AbstractDB {
	
	//TODO Add parameters user, password, database
	public void connect(){
	    try {
	        // The newInstance() call is a work around for some
	        // broken Java implementations
	        Class.forName("com.mysql.jdbc.Driver").newInstance();
	        
	        connection = DriverManager.getConnection("jdbc:mysql://localhost/raspi?"
	                    + "user=root");   
	        
	        System.out.println("Connected to MYSQL database 'Raspi@root' ");
	    } catch (Exception e) {
	        // handle the error
	    	System.out.println("Error while connecting to mysql");
	    	e.printStackTrace();
	    }
	}
}

/**

private DatabaseFactory(){}

private static class SingletonHelper{
    private static final DatabaseClient INSTANCE = new DatabaseClient();
}

public static DatabaseClient getInstance(){
    return SingletonHelper.INSTANCE;
}

**/