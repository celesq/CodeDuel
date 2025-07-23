public class Berserker extends Fighter {
	
	public Berserker(int id){
		setName("Berserker");
		setPlayerId(id);
		setAttackDamage(25); //25 trb
		setHealth(100);
		setHeal(10);
		setBehaviour(new AIProfile(AIProfile.Strategy.AGGRESSIVE, AIProfile.Priority.CLOSEST));
	}
}
