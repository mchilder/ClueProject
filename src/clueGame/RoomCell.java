package clueGame;

public class RoomCell extends BoardCell {
	public RoomCell(int x, int y, String t) {
		super(x, y, t);
		if(t.length()==2) {
			if(t.charAt(1)=='R') {
				doorDirection = DoorDirection.RIGHT;
			} else if(t.charAt(1)=='L') {
				doorDirection = DoorDirection.LEFT;
			} else if(t.charAt(1)=='U') {
				doorDirection = DoorDirection.UP;
			} else if(t.charAt(1)=='D') {
				doorDirection = DoorDirection.DOWN;
			}
		}
	}
	public enum DoorDirection {RIGHT, LEFT, UP, DOWN};
	private DoorDirection doorDirection;
	char room;
	@Override
	public Boolean isRoom() {
		return true;
	}
	@Override
	public Boolean isDoorway() {
		if(type.length()==2)
			return (type.charAt(1)=='R' || type.charAt(1)=='L' || type.charAt(1)=='U' || type.charAt(1)=='D');
		else
			return false;
	}
	public DoorDirection getDoorDirection() {
		return doorDirection;
	}
}
