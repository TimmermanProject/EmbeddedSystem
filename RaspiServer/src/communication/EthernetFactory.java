package communication;


public class EthernetFactory implements CommFactory {
	
	public AbstractComm createComm(){
		EthernetComm communication = new EthernetComm();
		return communication;
	}

}
