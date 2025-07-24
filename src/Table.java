import javax.crypto.DecapsulateException;
import java.awt.PageAttributes;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Stack;

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
	
	private static List<Tile> makeForbiddenList(Tile myFighter, Tile destination) {
		List<Tile> forbidden = new ArrayList<>();
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLUMNS; j++) {
				if (map[i][j] != myFighter && map[i][j] != destination) {
					if (map[i][j].isFighter() || map[i][j].isObstacle()) {
						forbidden.add(map[i][j]);
					}
				}
			}
		}
		return forbidden;
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
	
	public static void updateEffects() {
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLUMNS; j++) {
				if (map[i][j].isFighter()) {
					if (map[i][j].getEffectDuration() > 0) {
						map[i][j].setEffectDuration(map[i][j].getEffectDuration() - 1);
						map[i][j].effectAction();
					} else if (map[i][j].getEffect() != AIProfile.Magic.NO_EFFECT) {
							map[i][j].setEffect(AIProfile.Magic.NO_EFFECT);
						}
				}
			}
		}
	}
	
	private static void setDistances() {
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLUMNS; j++) {
				map[i][j].setDistance(Integer.MAX_VALUE);
			}
		}
	}
	
	public static Tile shortestPath(Tile start, Tile goal, Tile[][] parents, List<Tile> forbidden) {
		setDistances();
		start.setDistance(0);
		PriorityQueue<Tile> queue = new PriorityQueue<>();
		queue.add(start);
		
		while (!queue.isEmpty()) {
			Tile current = queue.poll();
			if (current.getX() == goal.getX() && current.getY() == goal.getY()) {
				return current;
			} else {
				BFSMatrix(parents, queue, current, forbidden);
			}
		}
		return null;
	}
	
	public static Tile BFS(Tile start, int desiredId, Tile[][] parents, boolean withEffect) {
		PriorityQueue<Tile> queue = new PriorityQueue<>();
		setDistances();
		start.setDistance(0);
		queue.add(start);
		List<Tile> forbidden = new ArrayList<>();
		
		while (!queue.isEmpty()) {
			Tile current = queue.poll();
			if (current.getPlayerId() == desiredId) {
				if (!withEffect) {
					return current;
				} else {
					if (current.getEffectDuration() > 0) {
						return current;
					} else {
						BFSMatrix(parents, queue, current, forbidden);
					}
				}
			} else {
				BFSMatrix(parents, queue, current, forbidden);
			}
		}
		
		return null;
		
	}
	
	private static void BFSMatrix(Tile[][] parents, PriorityQueue<Tile> queue, Tile current, List<Tile> forbidden) {
		int x = current.getX(), y = current.getY();
		
		if (y + 1 < COLUMNS && !map[x][y + 1].isObstacle() && parents[x][y + 1] == null && !forbidden.contains(map[x][y + 1])) {
			map[x][y + 1].setDistance(current.getDistance() + 1);
			queue.add(map[x][y + 1]);
			parents[x][y + 1] = map[x][y];
		}
		if (y - 1 >= 0 && !map[x][y - 1].isObstacle() && parents[x][y - 1] == null && !forbidden.contains(map[x][y - 1])) {
			map[x][y - 1].setDistance(current.getDistance() + 1);
			queue.add(map[x][y - 1]);
			parents[x][y - 1] = map[x][y];
		}
		if (x + 1 < ROWS && !map[x + 1][y].isObstacle() && parents[x + 1][y] == null && !forbidden.contains(map[x + 1][y])) {
			map[x + 1][y].setDistance(current.getDistance() + 1);
			queue.add(map[x + 1][y]);
			parents[x + 1][y] = map[x][y];
		}
		if (x - 1 >= 0 && !map[x - 1][y].isObstacle() && parents[x - 1][y] == null && !forbidden.contains(map[x - 1][y])) {
			map[x - 1][y].setDistance(current.getDistance() + 1);
			queue.add(map[x - 1][y]);
			parents[x - 1][y] = map[x][y];
		}
	}
	
	public static Tile getClosest(int x, int y, int desiredId) {
		Tile[][] parents = new Tile[ROWS][COLUMNS];
		Tile closest = BFS(map[x][y], desiredId, parents, false);
		if (closest != null) {
			return closest;
		} else {
			System.out.println("No closest found");
			return map[x][y];
		}
	}
	
	public static Tile getStrongest(int x, int y, int desiredId) {
		Tile strongest = null;
		int maxStrongest = -1;
		
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLUMNS; j++) {
				if (map[i][j] != null) {
					if (map[i][j].isFighter()) {
						if (map[i][j].getHealth() > maxStrongest && (i != x || j != y) && map[i][j].getPlayerId() == desiredId) {
							maxStrongest = map[i][j].getHealth();
							strongest = map[i][j];
						}
					}
				}
			}
		}
		
		return strongest;
	}
	
	public static Tile getWeakest(int x, int y, int desiredId) {
		Tile weakest = null;
		int maxWeakest = Integer.MAX_VALUE;
		
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLUMNS; j++) {
				if (map[i][j] != null) {
					if (map[i][j].isFighter()) {
						if (map[i][j].getHealth() < maxWeakest && (i != x || j != y) && map[i][j].getPlayerId() == desiredId) {
							maxWeakest = map[i][j].getHealth();
							weakest = map[i][j];
						}
					}
				}
			}
		}
		return weakest;
	}
	public static void moveToClosest(Tile fighter, int desiredId) {
		Tile[][] parents = new Tile[ROWS][COLUMNS];
		Tile closest = BFS(fighter, desiredId, parents, false);
		if (closest == null) {
			System.out.println("No closest fighter found\n");
		} else {
			backTrackPathAndMove(fighter, parents, closest);
		}
	}
	
	public static boolean moveToClosestWithEffect(Tile fighter, int desiredId) {
		Tile[][] parents = new Tile[ROWS][COLUMNS];
		Tile closest = BFS(fighter, desiredId, parents, true);
		if (closest == null) {
			System.out.println("No closest fighter found\n");
			return false;
		} else {
			backTrackPathAndMove(fighter, parents, closest);
			return true;
		}
	}
	
	public static Tile getClosestWithEffect(int x, int y, int desiredId) {
		Tile[][] parents = new Tile[ROWS][COLUMNS];
		return BFS(map[x][y], desiredId, parents, true);
	}
	
	public static void moveTowards(Tile myFighter, int x, int y) {
		Tile[][] parents = new Tile[ROWS][COLUMNS];
		List<Tile> forbidden = makeForbiddenList(myFighter, map[x][y]);
		Tile destination = shortestPath(myFighter, map[x][y], parents, forbidden);
		if (destination != null) {
			backTrackPathAndMove(myFighter, parents, destination);
		} else {
			System.out.println("No destination fighter found\n");
		}
	}
	
	private static void backTrackPathAndMove(Tile myFighter, Tile[][] parents, Tile destination) {
		Tile current = destination , last = destination;
		while (current != myFighter && current != null) {
			last = current;
			current = parents[current.getX()][current.getY()];
		}
		
		removeTileOnTable(myFighter.getX(), myFighter.getY());
		putTileOnTable(last.getX(), last.getY(), myFighter);
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
