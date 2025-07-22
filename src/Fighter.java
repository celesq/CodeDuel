import javafx.scene.control.Tab;

import java.util.ArrayList;
import java.util.List;

public abstract class Fighter extends Tile{
	
	private int health, attackDamage, heal;
	private String name;
	private AIProfile behaviour;
	private List<FighterObserver> observers = new ArrayList<>();
	
	public void getChoice(AIProfile.Strategy strategy, AIProfile.Priority priority) {
		behaviour = new AIProfile(strategy, priority);
	}
	
	@Override
	public int getHealth() {
		return health;
	}
	
	public void setHealth(int health) {
		this.health = health;
	}
	
	public int getAttackDamage() {
		return attackDamage;
	}
	
	public void setAttackDamage(int attackDamage) {
		this.attackDamage = attackDamage;
	}
	
	public int getHeal() {
		return heal;
	}
	
	public void setHeal(int heal) {
		this.heal = heal;
	}
	
	public AIProfile getBehaviour() {
		return behaviour;
	}
	
	public void setBehaviour(AIProfile behaviour) {
		this.behaviour = behaviour;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void addObserver(FighterObserver observer) {
		observers.add(observer);
	}
	
	public void removeObserver(FighterObserver observer) {
		observers.remove(observer);
	}
	
	@Override
	public boolean isFighter() {
		return true;
	}
	
	public void attack(Tile fighter) {
		int oldHp = fighter.getHealth();
		
		fighter.setHealth(fighter.getHealth() - this.getAttackDamage());
		
		if (fighter.getHealth() <= 0) {
			fighter.setHealth(0);
			Table.removeTileOnTable(fighter.getX(), fighter.getY());
		}
		
		for (FighterObserver observer : observers) {
			observer.fighterDamaged((Fighter) fighter, oldHp, fighter.getHealth());
			if (fighter.getHealth() == 0 && oldHp > 0) {
				observer.fighterDeath((Fighter) fighter);
				if (Table.checkIfTeamDead(fighter.getPlayerId())) {
					observer.allDeadFromTeam(fighter.getPlayerId());
				}
			}
		}
	}
	
	public void heal(Tile fighter) {
		fighter.setHealth(fighter.getHealth() + this.getHeal());
	}
	
	public void getClosestAndAttack() {
		System.out.println("Got closest and ready to attack");
		Tile closestFighter;
		if (getPlayerId() == 1) {
			closestFighter = Table.getClosest(getX(), getY(), 1 , 2);
		} else {
			closestFighter = Table.getClosest(getX(), getY(), 2 , 1);
		}
		if (closestFighter.getX() == getX() && closestFighter.getY() == getY()) {
			System.out.println("No fighter found!");
		} else {
			if ((getX() == closestFighter.getX() && Math.abs(getY() - closestFighter.getY()) < 2)
			|| (getY() == closestFighter.getY() && Math.abs(getX() - closestFighter.getX()) < 2)) {
				attack(closestFighter);
				System.out.println("Fighter attacked!");
			} else {
				System.out.println("We got to move");
				Table.moveTowards(this, closestFighter.getX(), closestFighter.getY());
			}
		}
	}
	
	void takeTurn(){
		System.out.println("I am taking my turn");
		makeChoice();
	}
	
	public void getClosestAndMove() {
		Table.moveToClosest(this);
	}
	
	public void getClosestAndHeal() {
		Tile closestFighter;
		if (getPlayerId() == 1) {
			closestFighter = Table.getClosest(getX(), getY(), 2 , 1);
		} else {
			closestFighter = Table.getClosest(getX(), getY(), 1 , 2);
		}
		if (closestFighter.getX() == getX() && closestFighter.getY() == getY()) {
			System.out.println("No fighter found!");
		} else {
			moveOrTakeAction(closestFighter, true);
		}
	}
	
	public void getStrongestAndAttack() {
		Tile strongestFighter = Table.getStrongest(getX(), getY());
		moveOrTakeActionAndCheck(strongestFighter, false);
	}
	
	public void getStrongestAndMove() {
		Tile strongestFighter = Table.getStrongest(getX(), getY());
		Table.moveTowards(this, strongestFighter.getX(), strongestFighter.getY());
	}
	
	public void getStrongestAndHeal() {
		Tile strongestFighter = Table.getStrongest(getX(), getY());
		moveOrTakeAction(strongestFighter, true);
	}
	
	public void getWeakestAndAttack() {
		Tile weakestFighter = Table.getWeakest(getX(), getY());
		moveOrTakeActionAndCheck(weakestFighter, false);
	}
	
	public void getWeakestAndMove() {
		Tile weakest = Table.getWeakest(getX(), getY());
		Table.moveTowards(this, weakest.getX(), weakest.getY());
	}
	
	public void getWeakestAndHeal() {
		Tile weakest = Table.getWeakest(getX(), getY());
		moveOrTakeAction(weakest, true);
	}
	
	private void moveOrTakeAction(Tile fighter, boolean heal) {
		if ((getX() == fighter.getX() && Math.abs(getY() - fighter.getY()) < 2)
				|| (getY() == fighter.getY() && Math.abs(getX() - fighter.getX()) < 2)) {
			if(heal) {
				heal(fighter);
			} else {
				attack(fighter);
			}
		} else {
			Table.moveTowards(this, fighter.getX(), fighter.getY());
		}
	}
	
	private void moveOrTakeActionAndCheck(Tile fighter, boolean heal) {
		if (fighter != this) {
			moveOrTakeAction(fighter, heal);
		} else {
			System.out.println("Choose again\n");
			Player.chooseStrategy(this);
			Player.choosePriority(this);
			makeChoice();
		}
	}
	
	@Override
	public void makeChoice() {
		if (behaviour.getPriority() == AIProfile.Priority.CLOSEST) {
			
			if (behaviour.getStrategy() == AIProfile.Strategy.AGGRESSIVE) {
				getClosestAndAttack();
				
			} else if (behaviour.getStrategy() == AIProfile.Strategy.PASSIVE) {
				getClosestAndMove();
				
			} else if (behaviour.getStrategy() == AIProfile.Strategy.DEFENSIVE) {
				getClosestAndHeal();
			}
		} else if (behaviour.getPriority() == AIProfile.Priority.STRONGEST) {
			
			if (behaviour.getStrategy() == AIProfile.Strategy.AGGRESSIVE) {
				getStrongestAndAttack();
			} else if (behaviour.getStrategy() == AIProfile.Strategy.PASSIVE) {
				getStrongestAndMove();
			} else if (behaviour.getStrategy() == AIProfile.Strategy.DEFENSIVE) {
				getStrongestAndHeal();
			}
		} else if (behaviour.getPriority() == AIProfile.Priority.WEAKEST) {
			
			if (behaviour.getStrategy() == AIProfile.Strategy.AGGRESSIVE) {
				getWeakestAndAttack();
			} else if (behaviour.getStrategy() == AIProfile.Strategy.PASSIVE) {
				getWeakestAndMove();
			} else if (behaviour.getStrategy() == AIProfile.Strategy.DEFENSIVE) {
				getWeakestAndHeal();
			}
		}
	}
}
