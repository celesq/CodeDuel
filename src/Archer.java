public class Archer extends Fighter {
	
	public Archer(int id){
		setName("Archer");
		setPlayerId(id);
		setAttackDamage(15);
		setHealth(125);
		setHeal(10);
		setBehaviour(new AIProfile(AIProfile.Strategy.AGGRESSIVE, AIProfile.Priority.CLOSEST));
	}
	
	@Override
	public void moveOrTakeAction(Tile fighter, boolean heal) {
		int distance = Math.abs(fighter.getX() - this.getX()) + Math.abs(fighter.getY() - this.getY());
		if (distance <= 4) {
			if(heal) {
				heal(fighter);
			} else {
				attack(fighter);
			}
		} else {
			Table.moveTowards(this, fighter.getX(), fighter.getY());
		}
	}
}
