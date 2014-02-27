package clueGame;


public abstract class BoardCell {
	protected String type;
	private int row, col;
	public BoardCell(int x, int y, String t) {
		row = y;
		col = x;
		type = t;
	}
	public Boolean isWalkway() {
		return false;
	}
	public Boolean isRoom() {
		return false;
	}
	public Boolean isDoorway() {
		return false;
	}
	public char getInitial() {
		return type.charAt(0);
	}
}
