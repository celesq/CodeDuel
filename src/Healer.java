public class Healer extends Fighter {
	
	public Healer(int id){
		setName("Healer");
		setPlayerId(id);
		setAttackDamage(5);
		setHealth(150);
		setHeal(30);
		setBehaviour(new AIProfile(AIProfile.Strategy.PASSIVE, AIProfile.Priority.WEAKEST));
	}
	
	@Override
	public void moveOrTakeAction(Tile fighter, boolean heal) {
		int distance = Math.abs(getX() - fighter.getX()) + Math.abs(getY() - fighter.getY());
		
		if (distance <= 3) {
			if (heal) {
				System.out.println("Healer healing");
				heal(fighter);
				return;
			}
		}
		
		if ((getX() == fighter.getX() && Math.abs(getY() - fighter.getY()) < 2)
				|| (getY() == fighter.getY() && Math.abs(getX() - fighter.getX()) < 2)) {
			System.out.println("Healer attacking");
			attack(fighter);
		} else {
			System.out.println("Moving towards fighter");
			Table.moveTowards(this, fighter.getX(), fighter.getY());
		}
	}
}
