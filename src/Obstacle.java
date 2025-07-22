public class Obstacle extends Tile{
	
	public Obstacle(int x, int y){
		setX(x);
		setY(y);
		setPlayerId(0);
	}
	
	@Override
	void takeTurn() {}
	
	public boolean isObstacle() {
		return true;
	}
	
}
