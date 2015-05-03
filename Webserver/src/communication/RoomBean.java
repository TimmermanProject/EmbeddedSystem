package communication;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RoomBean {
	public boolean doorStatus;
	public RoomBean(boolean doorStatus){
		this.doorStatus = doorStatus;
	}
	
	RoomBean(){}
		
}
