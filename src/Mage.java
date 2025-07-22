public class Mage extends Fighter {
	
	public void Mage(){
		setName("Mage");
		setAttackDamage(40);
		setHealth(80);
		setHeal(10);
		setBehaviour(new AIProfile(AIProfile.Strategy.AGGRESSIVE, AIProfile.Priority.STRONGEST));
	}
	
	@Override
	void takeTurn() {
	
	}
}
