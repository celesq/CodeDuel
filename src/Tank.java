public class Tank extends Fighter {
	
	public Tank(int id) {
		setName("Tank");
		setPlayerId(id);
		setAttackDamage(10);
		setHealth(225);
		setHeal(10);
		setBehaviour(new AIProfile(AIProfile.Strategy.DEFENSIVE, AIProfile.Priority.WEAKEST));
	}
}
