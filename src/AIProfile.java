public class AIProfile {
	
	public final static int POISON_DAMAGE = 3;
	
	public enum Strategy{AGGRESSIVE, DEFENSIVE, PASSIVE, MAGIC};
	public enum Priority{CLOSEST, WEAKEST, STRONGEST, CLOSEST_WITH_EFFECT};
	public enum Magic{POISON, CURSE, SUPER_HEAL, NO_EFFECT}
	
	private Strategy strategy;
	private Priority priority;
	private Magic magic;
	
	public AIProfile(Strategy strategy, Priority priority){
		this.strategy = strategy;
		this.priority = priority;
	}
	
	public AIProfile(Strategy strategy, Priority priority, Magic magic){
		this.strategy = strategy;
		this.priority = priority;
		this.magic = magic;
	}
	
	public Strategy getStrategy() {
		return strategy;
	}
	
	public void setStrategy(Strategy strategy) {
		this.strategy = strategy;
	}
	
	public Priority getPriority() {
		return priority;
	}
	
	public void setPriority(Priority priority) {
		this.priority = priority;
	}
	
	public Magic getMagic() {
		return magic;
	}
	
	public void setMagic(Magic magic) {
		this.magic = magic;
	}
	
	public void moveOrMagic(Mage mage, Tile fighter) {
		int distance = Math.abs(fighter.getX() - mage.getX()) + Math.abs(fighter.getY() - mage.getY());
		if (distance < 3) {
			magicGetChoice(mage, fighter);
			System.out.println("Effect applied!");
		} else {
			System.out.println("We got to move");
			Table.moveTowards(mage, fighter.getX(), fighter.getY());
		}
	}
	
	public void magicGetChoice(Mage mage, Tile fighter) {
		switch (magic) {
			case POISON -> mage.poison(fighter);
			case CURSE -> mage.curse(fighter);
			case SUPER_HEAL -> mage.superHeal(fighter);
			default -> System.out.println("No fighter found!");
		}
	}
}
