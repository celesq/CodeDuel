import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameEngine {
	
	private static final int MAX_ROUNDS = 100;
	private Table gameTable;
	private Player player1, player2;
	private FighterObserver observer;
	private List<Fighter> fighters = new ArrayList<Fighter>();
	
	public GameEngine() {
		this.gameTable = new Table();
	}
	
	public void initializeGame() {
		player1 = new Player("Player1", 1);
		player2 = new Player("Player2", 2);
		
		player1.chooseTeam();
		player2.chooseTeam();
		
		player1.placeCardsOnTable(gameTable);
		player2.placeCardsOnTable(gameTable);
	
		Table.getFightersOnTable(fighters);
		observer = new GameObsever(fighters);
	}
	
	public void start() {
		
		for (int i = 0; i < MAX_ROUNDS; i++) {
			player1.waitInput(gameTable);
			player2.waitInput(gameTable);
		
		}
	}
	
	public static void endGame() {
		System.out.println("Game Over!");
	}
}
