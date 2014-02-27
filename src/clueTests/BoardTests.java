package clueTests;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import clueGame.BadConfigFormatException;
import clueGame.Board;
import clueGame.BoardCell;
import clueGame.RoomCell;

public class BoardTests {
	public static Board b;
	@BeforeClass
	public static void Setup() {
		b = new Board("ClueLayout2.csv", "ClueLegend2.txt");
		b.loadConfigFiles();
	}
	
	@Test
	public void test1() {
		assertEquals(11, b.getRooms().size());
		assertEquals("Conservatory", b.getRooms().get('C'));
	}
	
	@Test
	public void test2() {
		assertEquals(18, b.numColumns);
		assertEquals(34,b.numRows);
		int doors = 0;
		for(BoardCell cell:b.cells) {
			if(cell.isDoorway()) {
				doors++;
			}
		}
		assertEquals(16, doors);
		assertEquals(false, b.cells.get(b.calcIndex(15,7)).isDoorway());
		assertEquals(RoomCell.DoorDirection.RIGHT, ((RoomCell)b.cells.get(b.calcIndex(4,7))).getDoorDirection());
		assertEquals('C', b.cells.get(b.calcIndex(21,14)).getInitial());
		assertEquals(129, b.calcIndex(7, 3));
	}
	
	@Test (expected = FileNotFoundException.class)
	public void test3() throws BadConfigFormatException, FileNotFoundException {
		Board b2 = new Board("badfile.csv", "badfile.odt");
		b2.loadLegend();
		b2.loadSetup();
	}

}
