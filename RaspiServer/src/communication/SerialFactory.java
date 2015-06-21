package communication;

public class SerialFactory implements CommFactory {
	public AbstractComm createComm(){
		SerialComm communication = new SerialComm();
		return communication;
	}
}
