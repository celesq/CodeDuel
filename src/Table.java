import java.awt.PageAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Table {
	
	public final static int ROWS = 10;
	public final static int COLUMNS = 10;
	public final static int MAX_OBSTACLES_PER_ROW = 3;
	
	
	private static Tile[][] map;
	
	public Table() {
		map = new Tile[10][10];
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLUMNS; j++) {
				map[i][j] = new EmptyTile(i, j);
			}
		}
	}
	
	public Table(Tile[][] map) {
		this.map = map;
	}
	
	public Tile[][] getMap() {
		return map;
	}
	
	public void setMap(Tile[][] map) {
		this.map = map;
	}
	
	public static void putTileOnTable(int x, int y, Tile tile) {
		map[x][y] = tile;
		tile.setX(x);
		tile.setY(y);
	}
	
	public static void removeTileOnTable(int x, int y) {
		map[x][y] = new EmptyTile(x, y);
	}
	
	public Tile getTileOnTable(int x, int y) {
		return map[x][y];
	}
	
	public static void getFightersOnTable(List<Fighter> fighters) {
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLUMNS; j++) {
				if (map[i][j].isFighter()) {
					fighters.add((Fighter) map[i][j]);
				}
			}
		}
	}
	
	public List<Integer> spacesLeftOnRowX (int x) {
		List<Integer> spaces = new ArrayList<>();
		for (int j = 0; j < COLUMNS; j++) {
			if (!map[x][j].isFighter() && !map[x][j].isObstacle()) {
				spaces.add(j);
			}
		}
		return spaces;
	}
	
	public List<Tile> getFightersOnTableFromPlayer(int id) {
		List<Tile> fighters = new ArrayList<>();
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLUMNS; j++) {
				if (map[i][j] != null && map[i][j].isFighter()) {
					if (map[i][j].getPlayerId() == id) {
						fighters.add(map[i][j]);
					}
				}
			}
		}
		return fighters;
	}
	
	public static Tile shortestPath(Tile start, Tile goal, Tile[][] parents) {
		PriorityQueue<Tile> queue = new PriorityQueue<>();
		queue.add(start);
		
		while (!queue.isEmpty()) {
			Tile current = queue.poll();
			if (current == goal) {
				return current;
			} else {
				BFSMatrix(parents, queue, current);
			}
		}
		return null;
	}
	
	public static Tile BFS(Tile start, int desiredId, Tile[][] parents, boolean closest) {
		PriorityQueue<Tile> queue = new PriorityQueue<>();
		start.setDistance(0);
		queue.add(start);
		
		if (closest) {
			while (!queue.isEmpty()) {
				Tile current = queue.poll();
				if (current.getPlayerId() != 0) {
					return current;
				} else {
					BFSMatrix(parents, queue, current);
				}
			}
		} else {
			while (!queue.isEmpty()) {
				Tile current = queue.poll();
				if (current.getPlayerId() == desiredId) {
					return current;
				} else {
					BFSMatrix(parents, queue, current);
				}
			}
		}
		return null;
		
	}
	
	private static void BFSMatrix(Tile[][] parents, PriorityQueue<Tile> queue, Tile current) {
		int x = current.getX(), y = current.getY();
		if (y + 1 < COLUMNS && map[x][y + 1] != null && !map[x][y + 1].isObstacle()) {
			map[x][y + 1].setDistance(current.getDistance() + 1);
			queue.add(map[x][y + 1]);
			parents[x][y] = map[x][y + 1];
		}
		if (y - 1 >= 0 && map[x][y - 1] != null && !map[x][y - 1].isObstacle()) {
			map[x][y - 1].setDistance(current.getDistance() + 1);
			queue.add(map[x][y - 1]);
			parents[x][y] = map[x][y - 1];
		}
		if (x + 1 < ROWS && map[x + 1][y] != null && !map[x + 1][y].isObstacle()) {
			map[x + 1][y].setDistance(current.getDistance() + 1);
			queue.add(map[x + 1][y]);
			parents[x][y] = map[x + 1][y];
		}
		if (x - 1 >= 0 && map[x - 1][y] != null && !map[x - 1][y].isObstacle()) {
			map[x - 1][y].setDistance(current.getDistance() + 1);
			queue.add(map[x - 1][y]);
			parents[x][y] = map[x - 1][y];
		}
	}
	
	public static Tile getClosest(int x, int y, int playerId, int desiredId) {
		Tile[][] parents = new Tile[ROWS][COLUMNS];
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLUMNS; j++) {
				parents[i][j] = new EmptyTile(i, j);
			}
		}
		Tile closest = BFS(map[x][y], desiredId, parents, false);
		if (closest != null) {
			return closest;
		} else {
			System.out.println("No closest found");
			return map[x][y];
		}
	}
	
	public static Tile getStrongest(int x, int y) {
		Tile strongest = null;
		int maxStrongest = -1;
		
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLUMNS; j++) {
				if (map[i][j] != null) {
					if (map[i][j].isFighter()) {
						if (map[i][j].getHealth() > maxStrongest && (i != x || j != y)) {
							maxStrongest = map[i][j].getHealth();
							strongest = map[i][j];
						}
					}
				}
			}
		}
		
		return strongest;
	}
	
	public static Tile getWeakest(int x, int y) {
		Tile weakest = null;
		int maxWeakest = Integer.MAX_VALUE;
		
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLUMNS; j++) {
				if (map[i][j] != null) {
					if (map[i][j].isFighter()) {
						if (map[i][j].getHealth() < maxWeakest && (i != x || j != y)) {
							maxWeakest = map[i][j].getHealth();
							weakest = map[i][j];
						}
					}
				}
			}
		}
		return weakest;
	}
	
	public static void moveToClosest(Tile fighter) {
		Tile[][] parents = new Tile[ROWS][COLUMNS];
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLUMNS; j++) {
				parents[i][j] = new EmptyTile(i, j);
			}
		}
		
		Tile closest = BFS(fighter, -1, parents, true);
		if (closest == null) {
			System.out.println("No closest fighter found\n");
		} else {
			backTrackPathAndMove(fighter, parents, closest);
		}
	}
	
	public static void moveTowards(Tile fighter, int x, int y) {
		Tile[][] parents = new Tile[ROWS][COLUMNS];
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLUMNS; j++) {
				parents[i][j] = new EmptyTile(i, j);
			}
		}
		Tile destination = shortestPath(fighter, map[x][y], parents);
		if (destination != null) {
			backTrackPathAndMove(fighter, parents, destination);
			
			
		} else {
			System.out.println("No destination fighter found\n");
		}
	}
	
	private static void backTrackPathAndMove(Tile fighter, Tile[][] parents, Tile destination) {
		Tile current = parents[destination.getX()][destination.getY()], last = destination;
		while (current != fighter) {
			last = current;
			current = parents[current.getX()][current.getY()];
		}
		
		removeTileOnTable(fighter.getX(), fighter.getY());
		fighter.setX(last.getX());
		fighter.setY(last.getY());
		putTileOnTable(fighter.getX(), fighter.getY(), fighter);
	}
	
	public static boolean checkIfTeamDead(int id) {
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLUMNS; j++) {
				if (map[i][j].getPlayerId() == id && map[i][j].getHealth() > 0)
					return false;
			}
		}
		return true;
	}
	
	public static void printCurrentGameState() {
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLUMNS; j++) {
				if (!map[i][j].isFighter() && !map[i][j].isObstacle()) {
					System.out.println("On row" + i + " and column " + j + " we got empty tile");
				} else if (map[i][j].isFighter()) {
					System.out.println("On row" + i + " and column " + j + " we got " +
							map[i][j].getName() + " with health " + map[i][j].getHealth());
				} else if (map[i][j].isObstacle()) {
					System.out.println("On row" + i + " and column " + j + " we got an obstacle");
				}
			}
		}
	}
}
