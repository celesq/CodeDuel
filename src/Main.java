
public class Main {
	public static void main (String[] args) {
		GameEngine instance = GameEngine.getInstance();
		instance.initializeGame();
		instance.start();
	}
}