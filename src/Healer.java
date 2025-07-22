public class Healer extends Fighter {
	
	public Healer(int id){
		setName("Healer");
		setPlayerId(id);
		setAttackDamage(5);
		setHealth(150);
		setHeal(25);
		setBehaviour(new AIProfile(AIProfile.Strategy.PASSIVE, AIProfile.Priority.WEAKEST));
	}
}
