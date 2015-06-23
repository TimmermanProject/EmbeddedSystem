package database;

public class MYSQLFactory implements DBFactory {
	public AbstractDB createDB(){
		MYSQL database = new MYSQL();
		return database;
	}
}