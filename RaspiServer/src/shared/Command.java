package shared;

public class Command extends Message {
	private int BuildingID;
	private int FloorID;
	private int RoomID;
	private int ActionID;
	
	public Command(){
		this.setFrameType(frameTypes.ROOM_COMMAND);
	}

	/** getters and setters **/
	
	/**
	 * @return the buildingID
	 */
	public int getBuildingID() {
		return BuildingID;
	}

	/**
	 * @param buildingID the buildingID to set
	 */
	public void setBuildingID(int buildingID) {
		BuildingID = buildingID;
	}

	/**
	 * @return the floorID
	 */
	public int getFloorID() {
		return FloorID;
	}

	/**
	 * @param floorID the floorID to set
	 */
	public void setFloorID(int floorID) {
		FloorID = floorID;
	}

	/**
	 * @return the roomID
	 */
	public int getRoomID() {
		return RoomID;
	}

	/**
	 * @param roomID the roomID to set
	 */
	public void setRoomID(int roomID) {
		RoomID = roomID;
	}

	/**
	 * @return the actionID
	 */
	public int getActionID() {
		return ActionID;
	}

	/**
	 * @param actionID the actionID to set
	 */
	public void setActionID(int actionID) {
		ActionID = actionID;
	}	
}
