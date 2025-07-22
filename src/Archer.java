public class Archer extends Fighter {
	
	public void Archer(){
		setName("Archer");
		setAttackDamage(15);
		setHealth(125);
		setHeal(10);
		setBehaviour(new AIProfile(AIProfile.Strategy.AGGRESSIVE, AIProfile.Priority.CLOSEST));
	}
	
	@Override
	void takeTurn() {
	
	}
}
