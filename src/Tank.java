public class Tank extends Fighter {
	
	public Tank() {
		setName("Tank");
		setAttackDamage(10);
		setHealth(200);
		setHeal(10);
		setBehaviour(new AIProfile(AIProfile.Strategy.DEFENSIVE, AIProfile.Priority.WEAKEST));
	}
	
	@Override
	void takeTurn() {
	
	}
}
