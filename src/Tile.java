public abstract class Tile implements Comparable<Tile> {
	
	protected int distance;
	
	private int x, y, playerId = 0;
	
	private AIProfile.Magic effect = AIProfile.Magic.NO_EFFECT;
	
	private int effectDuration = 0;
	
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
	
	public int getDistance() {
		return distance;
	}
	
	public void setDistance(int distance) {
		this.distance = distance;
	}
	
	public String getName() {
		return null;
	}
	
	public AIProfile getBehaviour() {
		return null;
	}
	
	public void getChoice(AIProfile.Strategy strategy, AIProfile.Priority priority) {
	}
	
	public void takeDamage(Tile fighter, int damageTaken) {
	}
	
	public void effectAction() {
		return;
	}
	
	public void effectAction(int damageTaken, int healingTaken) {
		return;
	}
	
	public AIProfile.Magic getEffect() {
		return effect;
	}
	
	public void setEffect(AIProfile.Magic effect) {
		this.effect = effect;
	}
	
	public int getEffectDuration() {
		return effectDuration;
	}
	
	public void setEffectDuration(int effectDuration) {
		this.effectDuration = effectDuration;
	}
	
	public int compareTo(Tile o) {
		return Integer.compare(this.distance, o.distance);
	}
	
	abstract void takeTurn();
	
	public void makeChoice() {
	}
	
	public boolean isFighter() {
		return false;
	}
	
	public boolean isObstacle() {
		return false;
	}
	
	public boolean isMage() {
		return false;
	}
	
	public int getHealth() {
		System.out.println("Something went wrong");
		return -1;
	}
	
	public void setHealth(int health) {
		System.out.println("Something went wrong");
	}
	
	public AIProfile.Strategy chooseStrategy() {
		System.out.println("Something went wrong");
		return null;
	}
	
	public AIProfile.Priority choosePriority() {
		System.out.println("Something went wrong");
		return null;
	}
}
