package communication;

import core.MYSQL_db;

public interface CommFactory {
	public AbstractComm createComm();
}
