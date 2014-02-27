package clueTests;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import clueGame.Board;
import clueGame.BoardCell;

public class BoardAdjTests {

	private static Board board;
	@BeforeClass
	public static void setUp() {
		board = new Board("ClueLayout2.csv", "ClueLegend2.txt");
		board.loadConfigFiles();
		board.calcAdjacencies();
	}
	
	@Test
	public void onlyWalkways() {
		Assert.assertEquals(4, board.getAdjList(board.calcIndex(31,10)).size());
	}
	@Test
	public void locationsAtEdge() {
		Assert.assertEquals(3, board.getAdjList(board.calcIndex(25,0)).size());
		Assert.assertEquals(2, board.getAdjList(board.calcIndex(0,8)).size());
		Assert.assertEquals(2, board.getAdjList(board.calcIndex(12,17)).size());
		Assert.assertEquals(3, board.getAdjList(board.calcIndex(33,11)).size());
	}
	@Test
	public void besideRoomNotDoor() {
		Assert.assertEquals(3, board.getAdjList(board.calcIndex(27,4)).size());
		Assert.assertEquals(2, board.getAdjList(board.calcIndex(10,4)).size());
	}
	@Test
	public void adjacentToDoor() {
		Assert.assertEquals(3, board.getAdjList(board.calcIndex(5,8)).size());
		Assert.assertEquals(4, board.getAdjList(board.calcIndex(27,7)).size());
	}
	@Test
	public void Doorway() {
		Assert.assertEquals(1, board.getAdjList(board.calcIndex(5,7)).size());
		Assert.assertEquals(1, board.getAdjList(board.calcIndex(16,9)).size());
	}
	
	@Test
	public void targetAlongWalkway() {
		// Take one step, essentially just the adj list
		board.startTargets(12,9, 2);
		Set<BoardCell> targets= board.getTargets();
		// Ensure doesn't exit through the wall
		Assert.assertEquals(4, targets.size());
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(12,7))));
		
		// Take two steps
		board.startTargets(9,16, 2);
		targets= board.getTargets();
		// Ensure doesn't exit through the wall
		Assert.assertEquals(4, targets.size());
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(11,16))));
		
		// Take four steps
		board.startTargets(23,13, 4);
		targets= board.getTargets();
		// Ensure doesn't exit through the wall
		Assert.assertEquals(4, targets.size());
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(22,16))));
		
		// Take four steps
		board.startTargets(27, 0, 4);
		targets= board.getTargets();
		// Ensure doesn't exit through the wall
		Assert.assertEquals(7, targets.size());
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(26,3))));
	}
	
	@Test
	public void targetEnterRoom() {
		// Take one step, essentially just the adj list
		board.startTargets(31,12, 1);
		Set<BoardCell> targets= board.getTargets();
		// Ensure doesn't exit through the wall
		Assert.assertEquals(4, targets.size());
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(31, 13))));
		
		// Take two steps
		board.startTargets(27,6, 2);
		targets= board.getTargets();
		// Ensure doesn't exit through the wall
		Assert.assertEquals(6, targets.size());
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(28,7))));
	}
	
	@Test
	public void targetLeaveRoom() {
		// Take one step, essentially just the adj list
		board.startTargets(31,13, 1);
		Set<BoardCell> targets= board.getTargets();
		// Ensure doesn't exit through the wall
		Assert.assertEquals(1, targets.size());
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(31, 12))));
		
		// Take two steps
		board.startTargets(23,16, 2);
		targets= board.getTargets();
		System.out.println(targets.toString());
		// Ensure doesn't exit through the wall
		Assert.assertEquals(2, targets.size());
		Assert.assertTrue(targets.contains(board.getCellAt(board.calcIndex(22, 17))));
	}
}
