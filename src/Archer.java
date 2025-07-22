public class Archer extends Fighter {
	
	public Archer(int id){
		setName("Archer");
		setPlayerId(id);
		setAttackDamage(15);
		setHealth(125);
		setHeal(10);
		setBehaviour(new AIProfile(AIProfile.Strategy.AGGRESSIVE, AIProfile.Priority.CLOSEST));
	}
}
