import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class GameEngine {
	
	private static final int MAX_ROUNDS = 100;
	
	private Table gameTable= new Table();
	private static Player player1, player2;
	private FighterObserver observer;
	private List<Fighter> fighters = new ArrayList<Fighter>();
	private final static GameEngine instance = new GameEngine();
	
	private GameEngine() {}
	
	public static GameEngine getInstance() {
		return instance;
	}
	
	public void initializeGame() {
		if (player1 == null && player2 == null) {
			player1 = new Player("Player1", 1);
			player2 = new Player("Player2", 2);
		}
		
		setObstacles();
		
		player1.chooseTeam();
		player2.chooseTeam();
		
		player1.placeCardsOnTable(gameTable);
		player2.placeCardsOnTable(gameTable);
	
		Table.getFightersOnTable(fighters);
		observer = new GameObserver(fighters);
	}
	
	public void start() {
		
		for (int i = 0; i < MAX_ROUNDS; i++) {
			System.out.println("Round " + (i + 1));
			Table.updateEffects();
			Table.printCurrentGameState();
			player1.waitInput(gameTable);
			player2.waitInput(gameTable);
		
		}
		System.out.println("Max rounds over. Game over\n");
		System.out.println("TIE\n");
	}
	
	public void endGame(int teamIdWon) {
		
		System.out.println("Game Over!\n");
		System.out.println("Wanna play another round? 1 - YES, 2 - NO");
		Scanner scanner = new Scanner(System.in);
		int choice = scanner.nextInt();
		while(choice != 1 && choice != 2) {
			System.out.println("Please choose one of the following options: 1 - YES, 2 - NO");
			choice = scanner.nextInt();
		}
		if (choice == 1) {
			if (teamIdWon == 1) {
				player1.setMatchesWon(player1.getMatchesWon() + 1);
			} else {
				player2.setMatchesWon(player2.getMatchesWon() + 1);
			}
			System.out.println("Current score => player1 = " + player1.getMatchesWon()
					+ ", player2 = " + player2.getMatchesWon());
			instance.initializeGame();
			instance.start();
		} else {
			System.exit(0);
		}
	}
	
	public void setObstacles() {
		Random random = new Random();
		int rowsWithObstacles = random.nextInt(Table.ROWS);
		int []visitedRows = new int[Table.ROWS];
		for (int i = 0; i < rowsWithObstacles; i++) {
			int currentRow = random.nextInt(Table.ROWS);
			while(visitedRows[currentRow] == 1) {
				currentRow = random.nextInt(Table.ROWS);
			}
			visitedRows[currentRow] = 1;
			int[] visitedColumns = new int[Table.COLUMNS];
			int obstaclesPerRow = random.nextInt(Table.MAX_OBSTACLES_PER_ROW);
			for (int j = 0; j < obstaclesPerRow; j++) {
				int currentColumn = random.nextInt(Table.COLUMNS);
				while(visitedColumns[currentColumn] == 1) {
					currentColumn = random.nextInt(Table.COLUMNS);
				}
				visitedColumns[currentColumn] = 1;
				Table.putTileOnTable(currentRow, currentColumn, new Obstacle(i, j));
			}
		}
	}
}
