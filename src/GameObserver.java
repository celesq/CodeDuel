import java.util.List;

public class GameObserver implements FighterObserver {
	
	private List<Fighter> allFighters;
	
	public GameObserver(List<Fighter> allFighters) {
		this.allFighters = allFighters;
		for (Fighter f : allFighters) {
			f.addObserver(this);
		}
	}
	
	@Override
	public void fighterDamaged(Fighter fighter, int oldHp, int newHp) {
		System.out.println("Fighter" + fighter.getName() + " got damaged! Old HP: " + oldHp +
				" New HP: " + newHp);
	}
	
	@Override
	public void fighterDeath(Fighter fighter) {
		System.out.println("Fighter" + fighter.getName() + "got killed!");
	}
	
	@Override
	public void allDeadFromTeam(int id) {
		System.out.println("All dead from team" + id);
		if (id == 1) {
			System.out.println("Team 2 Won");
			GameEngine.getInstance().endGame(2);
		} else {
			System.out.println("Team 1 Won");
			GameEngine.getInstance().endGame(1);
		}
	}
	
	@Override
	public void fighterHealed(Fighter fighter, int oldHp, int newHp) {
		System.out.println("Fighter" + fighter.getName() + " got healed! Old HP: " + oldHp + " ,new HP: " + newHp);
	}
}
