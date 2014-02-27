package clueGame;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

import clueGame.RoomCell.DoorDirection;

public class Board {
	public static ArrayList<BoardCell> cells;
	public static Map<Character, String> rooms;
	public int numRows, numColumns;
	public static String setupFile;
	public static String legendFile;
	public static Set<BoardCell> targets;
	public static int dirs[][] = {{0,1},{0,-1},{-1,0},{1,0}};
	public static ArrayList<LinkedList<Integer>> adjList;
	
	public Board(String setup, String legend) {
		cells = new ArrayList<BoardCell>();
		rooms = new TreeMap<Character, String>();
		legendFile=legend;
		setupFile=setup;
	}
	
	public Board() {
		cells = new ArrayList<BoardCell>();
		rooms = new TreeMap<Character, String>();
		setupFile ="ClueLayout.csv";
		legendFile="ClueLegend.txt";
	}
	
	public void loadConfigFiles() {
		try {
			loadLegend();
			loadSetup();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void loadLegend() throws BadConfigFormatException, FileNotFoundException {
		FileReader read;
		read = new FileReader(legendFile);
		Scanner in = new Scanner(read);
		in = new Scanner(read);
		while(in.hasNextLine()) {
			String line = in.nextLine();
			if(!line.contains(", ")) {
				in.close();
				throw new BadConfigFormatException();
			}
			String setup[] = line.split(", ");
			if(setup[0].length()!=1 || setup.length > 2) {
				in.close();
				throw new BadConfigFormatException();
			}
			rooms.put(setup[0].charAt(0), setup[1]);
		}
		in.close();
	}
	
	public void loadSetup() throws BadConfigFormatException, FileNotFoundException {
		FileReader read;
		String line;
		String spots[];
		int col = 0,row = 0;
		read = new FileReader(setupFile);
		Scanner in = new Scanner(read);
		while(in.hasNextLine()) {
			line = in.nextLine();
			spots = line.split(",");
			for(String spot:spots) {
				if(!rooms.containsKey(spot.charAt(0))) {
					in.close();
					throw new BadConfigFormatException();
				}
				if(spot.contains("W")) {
					WalkwayCell insert;
					insert = new WalkwayCell(col, row, spot);
					col++;
					cells.add(insert);
				} else {
					RoomCell insert;
					insert = new RoomCell(col, row, spot);
					col++;
					cells.add(insert);
				}
			}
			if(row==0) {
				numColumns = col;
			}
			if(numColumns != col) {
				in.close();
				throw new BadConfigFormatException();
			}
			row++;
			col=0;
		}
		numRows=row;
		in.close();
	}
	
	public Map<Character, String> getRooms() {
		return rooms;
	}
	
	public RoomCell getRoomCellAt(int x, int y) {
		if(cells.get(calcIndex(x,y)).isRoom())
			return (RoomCell) cells.get(calcIndex(x,y));
		else
			return null;
	}
	
	public int calcIndex(int row, int col) {
		return row*numColumns+col;
	}
	
	public String getxy(int index) {
		int col = index%numColumns;
		int row = (index-col)/numColumns;
		return "Row: " + row + ", Col: " + col;
	}
	
	public int getNumColumns() {
		return numColumns;
	}
	
	public int getNumRows() {
		return numRows;
	}
	
	public BoardCell getCellAt(int calcIndex) {
		return cells.get(calcIndex);
	}
	
	public void calcAdjacencies() {
		adjList = new ArrayList<LinkedList<Integer>>();;
		for(int y = 0; y < numRows; y++) {
			for(int x = 0; x < numColumns; x++) {
				LinkedList<Integer> adj = new LinkedList<Integer>();
				BoardCell cell = getCellAt(calcIndex(y,x));
				for(int[] dir:dirs) {
					if(x+dir[0] >= 0 && x+dir[0] < numColumns && y+dir[1] >= 0 && y+dir[1] < numRows) {
						if(cell.isDoorway()) {
							BoardCell check = getCellAt(calcIndex(y + dir[1], x + dir[0]));
							RoomCell checkdoor = getRoomCellAt(y, x);
							if(checkdoor.getDoorDirection() == DoorDirection.RIGHT && dir[0]==1 && check.isWalkway()) {
								adj.add(calcIndex(y + dir[1], x + dir[0]));
							} else if(checkdoor.getDoorDirection() == DoorDirection.LEFT && dir[0]==-1 && check.isWalkway()) {
								adj.add(calcIndex(y + dir[1], x + dir[0]));
							} else if(checkdoor.getDoorDirection() == DoorDirection.UP && dir[1]==-1 && check.isWalkway()) {
								adj.add(calcIndex(y + dir[1], x + dir[0]));
							} else if(checkdoor.getDoorDirection() == DoorDirection.DOWN && dir[1]==1 && check.isWalkway()) {
								adj.add(calcIndex(y + dir[1], x + dir[0]));
							}
						} else if(cell.isWalkway()) {
							BoardCell check = getCellAt(calcIndex(y + dir[1], x + dir[0]));
							if(check.isWalkway())
								adj.add(calcIndex(y + dir[1], x + dir[0]));
							else if(check.isDoorway()) {
								RoomCell checkdoor = getRoomCellAt(y + dir[1], x + dir[0]);
								if(checkdoor.getDoorDirection() == DoorDirection.RIGHT && dir[0]==-1) {
									adj.add(calcIndex(y + dir[1], x + dir[0]));
								} else if(checkdoor.getDoorDirection() == DoorDirection.LEFT && dir[0]==1) {
									adj.add(calcIndex(y + dir[1], x + dir[0]));
								} else if(checkdoor.getDoorDirection() == DoorDirection.UP && dir[1]==1) {
									adj.add(calcIndex(y + dir[1], x + dir[0]));
								} else if(checkdoor.getDoorDirection() == DoorDirection.DOWN && dir[1]==-1) {
									adj.add(calcIndex(y + dir[1], x + dir[0]));
								}
							}
						}
					}
				}
				adjList.add(adj);
			}
		}
	}
	
	public LinkedList<Integer> calcAdjacenciesForPos(int from) {
		return adjList.get(from);
	}
	
	public void startTargets(int row, int col, int steps) {
		targets = new HashSet<BoardCell>();
		ArrayList<ArrayList<Integer>> adjList = new ArrayList<ArrayList<Integer>>();
		ArrayList<ArrayList<Integer>> adjList2 = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> start = new ArrayList<Integer>();
		start.add(calcIndex(row,col));
		adjList.add(start);
		for(int i = 0; i < steps; i++) {
			adjList2 = new ArrayList<ArrayList<Integer>>(adjList);
			for(int j = 0; j < adjList2.size(); j++) {
				ArrayList<Integer> path = adjList2.get(j);
				if(i==0 || !getCellAt(path.get(path.size()-1)).isDoorway()) {
					LinkedList<Integer> next = getAdjList(path.get(path.size()-1));
					ArrayList<Integer> temp;
					for(Integer x:next) {
						temp = new ArrayList<Integer>(path);
						if(temp.contains(x) == false) {
							temp.add(x);
							adjList.add(temp);
						}
					}
					adjList.remove(path);
				}
			}
		}
		for(int j = 0; j < adjList.size(); j++) {
			if(adjList.get(j).size() == steps+1 || getCellAt(adjList.get(j).get(adjList.get(j).size()-1)).isDoorway()) {
				BoardCell add = getCellAt(adjList.get(j).get(adjList.get(j).size()-1));
				targets.add(add);
			}
		}
	}
	
	public Set<BoardCell> getTargets() {
		return targets;
	}
	
	public LinkedList<Integer> getAdjList(int from) {
		return adjList.get(from);
	}
}


