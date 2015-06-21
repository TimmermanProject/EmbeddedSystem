package core;

/** This singleton implementation does not require synchronization **/
public class DatabaseSinglet {
	private DatabaseSinglet(){}
	
	private static class SingletonHelper{
        private static final DatabaseSinglet INSTANCE = new DatabaseSinglet();
    }
	
	public static DatabaseSinglet getInstance(){
        return SingletonHelper.INSTANCE;
    }
}
