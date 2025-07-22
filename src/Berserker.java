public class Berserker extends Fighter {
	
	public void Berserker(){
		setName("Berserker");
		setAttackDamage(25);
		setHealth(100);
		setHeal(10);
		setBehaviour(new AIProfile(AIProfile.Strategy.AGGRESSIVE, AIProfile.Priority.CLOSEST));
	}
	
	@Override
	void takeTurn() {
	
	}
}
