package clueGame;

public class WalkwayCell extends BoardCell {
	public WalkwayCell(int x, int y, String t) {
		super(x, y, t);
		
	}

	@Override
	public Boolean isWalkway() {
		return true;
	}
}
