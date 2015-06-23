package core;

/** This singleton implementation does not require synchronization **/
public interface DBFactory {	
	public AbstractDB createDB();
	
}


