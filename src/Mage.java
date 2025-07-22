public class Mage extends Fighter {
	
	public Mage(int id){
		setName("Mage");
		setPlayerId(id);
		setAttackDamage(40);
		setHealth(80);
		setHeal(10);
		setBehaviour(new AIProfile(AIProfile.Strategy.AGGRESSIVE, AIProfile.Priority.STRONGEST));
	}
}
