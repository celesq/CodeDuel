public interface FighterObserver {
	void fighterDamaged(Fighter fighter, int oldHp, int newHp);
	void fighterDeath(Fighter fighter);
	void allDeadFromTeam(int id);
	void fighterHealed(Fighter fighter, int oldHp, int newHp);
}
