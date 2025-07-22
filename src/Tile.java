public abstract class Tile {
	
	private int x,y, playerId = 0;
	
	
	public int getX() {
		return x;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public int getPlayerId() {
		return playerId;
	}
	
	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}
	
	public String getName() {
		return null;
	}
	
	public AIProfile getBehaviour() {
		return null;
	}
	
	public void getChoice(AIProfile.Strategy strategy, AIProfile.Priority priority) {
		return;
	}
	
	abstract void takeTurn();
	
	public void makeChoice() {
		return;
	}
	
	public boolean isFighter() {
		return false;
	}
	
	public int getHealth() {
		System.out.println("Something went wrong");
		return -1;
	}
	
	public void setHealth(int health) {
		System.out.println("Something went wrong");
		return;
	}
}
