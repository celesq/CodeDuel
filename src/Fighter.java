import javafx.scene.control.Tab;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public abstract class Fighter extends Tile{
	
	private int health, attackDamage, heal;
	private String name;
	protected AIProfile behaviour;
	private List<FighterObserver> observers = new ArrayList<>();
	
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
	
	public void getChoice(AIProfile.Strategy strategy, AIProfile.Priority priority) {
		behaviour = new AIProfile(strategy, priority);
	}
	
	@Override
	public boolean isFighter() {
		return true;
	}
	
	@Override
	public AIProfile.Strategy chooseStrategy() {
		int strategyChoice;
		AIProfile.Strategy selectedStrategy;
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("Choose Strategy for Fighter " + name);
		System.out.println("1. " + AIProfile.Strategy.AGGRESSIVE);
		System.out.println("2. " + AIProfile.Strategy.DEFENSIVE);
		System.out.println("3. " + AIProfile.Strategy.PASSIVE);
		
		strategyChoice = scanner.nextInt();
		switch (strategyChoice) {
			case 1 -> selectedStrategy = AIProfile.Strategy.AGGRESSIVE;
			case 2 -> selectedStrategy = AIProfile.Strategy.DEFENSIVE;
			case 3 -> selectedStrategy = AIProfile.Strategy.PASSIVE;
			default -> {
				System.out.println("Invalid choice. Defaulting to PASSIVE.");
				selectedStrategy = AIProfile.Strategy.PASSIVE;
			}
		}
		
		return selectedStrategy;
	}
	
	@Override
	public AIProfile.Priority choosePriority() {
		int priorityChoice;
		AIProfile.Priority selectedPriority;
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("Choose Priority for Fighter " + name);
		System.out.println("1. " + AIProfile.Priority.STRONGEST);
		System.out.println("2. " + AIProfile.Priority.CLOSEST);
		System.out.println("3. " + AIProfile.Priority.WEAKEST);
		System.out.println("4. " + AIProfile.Priority.CLOSEST_WITH_EFFECT);
		
		priorityChoice = scanner.nextInt();
		switch (priorityChoice) {
			case 1 -> selectedPriority = AIProfile.Priority.STRONGEST;
			case 2 -> selectedPriority = AIProfile.Priority.CLOSEST;
			case 3 -> selectedPriority =  AIProfile.Priority.WEAKEST;
			case 4 -> selectedPriority = AIProfile.Priority.CLOSEST_WITH_EFFECT;
			default -> {
				System.out.println("Invalid choice. Defaulting to CLOSEST.");
				selectedPriority =  AIProfile.Priority.CLOSEST;
			}
		}
		
		return selectedPriority;
	}
	
	void takeTurn(){
		System.out.println("I am taking my turn");
		makeChoice();
	}
	
	@Override
	public void effectAction() {
		switch (getEffect()) {
			case POISON -> takeDamage(this, AIProfile.POISON_DAMAGE);
			default -> {
				return;
			}
		}
	}
	
	@Override
	public void effectAction(int damageTaken, int healingTaken) {
		switch (getEffect()) {
			case CURSE -> takeDamage(this, damageTaken * 2);
			case SUPER_HEAL -> takeHeal(this, healingTaken * 2);
			default -> {
				if (damageTaken > 0) {
					takeDamage(this, damageTaken);
				} else if (healingTaken > 0) {
					takeHeal(this, healingTaken);
				}
			}
		}
	}
	
	public void attack(Tile fighter) {
		fighter.effectAction(this.getAttackDamage(), 0);
	}
	
	public void takeDamage(Tile fighter, int damageTaken) {
		int oldHp = fighter.getHealth();
		
		fighter.setHealth(fighter.getHealth() - damageTaken);
		
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
		fighter.effectAction(0, this.getHeal());
	}
	
	public void takeHeal(Tile fighter, int healingTaken) {
		fighter.setHealth(fighter.getHealth() + healingTaken);
	}
	
	public void getClosestAndAttack() {
		System.out.println("Got closest and ready to attack");
		Tile closestFighter;
		if (getPlayerId() == 1) {
			closestFighter = Table.getClosest(getX(), getY() , 2);
		} else {
			closestFighter = Table.getClosest(getX(), getY(), 1);
		}
		if (closestFighter.getX() == getX() && closestFighter.getY() == getY()) {
			System.out.println("No fighter found!");
		} else {
			moveOrTakeActionAndCheck(closestFighter, false);
		}
	}
	
	public void getClosestAndMove() {
		if (getPlayerId() == 1) {
			Table.moveToClosest(this, 2);
		} else {
			Table.moveToClosest(this, 1);
		}
	}
	
	public void getClosestAndHeal() {
		Tile closestFighter;
		if (getPlayerId() == 1) {
			closestFighter = Table.getClosest(getX(), getY(), 1);
		} else {
			closestFighter = Table.getClosest(getX(), getY(), 2);
		}
		if (closestFighter.getX() == getX() && closestFighter.getY() == getY()) {
			System.out.println("No fighter found!");
		} else {
			moveOrTakeActionAndCheck(closestFighter, true);
		}
	}
	
	public void getClosestWithEffectAndAttack() {
		Tile closestFighter;
		if (getPlayerId() == 1) {
			closestFighter = Table.getClosestWithEffect(getX(), getY(), 2);
		} else {
			closestFighter = Table.getClosestWithEffect(getX(), getY(), 1);
		}
		if (closestFighter == null) {
			System.out.println("No closest fighter with effect found, please choose again");
			chooseStrategy();
			choosePriority();
			makeChoice();
		} else if (closestFighter.getX() == getX() && closestFighter.getY() == getY()) {
			System.out.println("No fighter found!");
		} else {
			moveOrTakeActionAndCheck(closestFighter, false);
		}
	}
	
	public void getClosestWithEffectAndMove() {
		boolean found;
		if (getPlayerId() == 1) {
			found = Table.moveToClosestWithEffect(this, 2);
		} else {
			found = Table.moveToClosestWithEffect(this, 1);
		}
		if (!found) {
			System.out.println("No closest fighter with effect found, please choose again");
			chooseStrategy();
			choosePriority();
			makeChoice();
		}
	}
	
	public void getClosestWithEffectAndHeal() {
		Tile closestFighter;
		if (getPlayerId() == 1) {
			closestFighter = Table.getClosestWithEffect(getX(), getY(), 1);
		} else {
			closestFighter = Table.getClosestWithEffect(getX(), getY(), 2);
		}
		if (closestFighter == null) {
			System.out.println("No closest fighter with effect found, please choose again");
			chooseStrategy();
			choosePriority();
			makeChoice();
		} else if (closestFighter.getX() == getX() && closestFighter.getY() == getY()) {
			System.out.println("No fighter found!");
		} else {
			moveOrTakeActionAndCheck(closestFighter, true);
		}
	}
	
	public void getStrongestAndAttack() {
		Tile strongestFighter;
		if (getPlayerId() == 1) {
			strongestFighter = Table.getStrongest(getX(), getY(), 2);
		} else {
			strongestFighter = Table.getStrongest(getX(), getY(), 1);
		}
		moveOrTakeActionAndCheck(strongestFighter, false);
	}
	
	public void getStrongestAndMove() {
		Tile strongestFighter;
		if (getPlayerId() == 1) {
			strongestFighter = Table.getStrongest(getX(), getY(), 2);
		} else {
			strongestFighter = Table.getStrongest(getX(), getY(), 1);
		}
		Table.moveTowards(this, strongestFighter.getX(), strongestFighter.getY());
	}
	
	public void getStrongestAndHeal() {
		Tile strongestFighter;
		if (getPlayerId() == 1) {
			strongestFighter = Table.getStrongest(getX(), getY(), 1);
		} else {
			strongestFighter = Table.getStrongest(getX(), getY(), 2);
		}
		moveOrTakeAction(strongestFighter, true);
	}
	
	public void getWeakestAndAttack() {
		Tile weakestFighter;
		if (getPlayerId() == 1) {
			weakestFighter = Table.getWeakest(getX(), getY(), 2);
		} else {
			weakestFighter = Table.getWeakest(getX(), getY(), 1);
		}
		moveOrTakeActionAndCheck(weakestFighter, false);
	}
	
	public void getWeakestAndMove() {
		Tile weakestFighter;
		if (getPlayerId() == 1) {
			weakestFighter = Table.getWeakest(getX(), getY(), 2);
		} else {
			weakestFighter = Table.getWeakest(getX(), getY(), 1);
		}
		Table.moveTowards(this, weakestFighter.getX(), weakestFighter.getY());
	}
	
	public void getWeakestAndHeal() {
		Tile weakestFighter;
		if (getPlayerId() == 1) {
			weakestFighter = Table.getWeakest(getX(), getY(), 1);
		} else {
			weakestFighter = Table.getWeakest(getX(), getY(), 2);
		}
		moveOrTakeAction(weakestFighter, true);
	}
	
	protected void moveOrTakeAction(Tile fighter, boolean heal) {
		if ((getX() == fighter.getX() && Math.abs(getY() - fighter.getY()) < 2)
				|| (getY() == fighter.getY() && Math.abs(getX() - fighter.getX()) < 2)) {
			if(heal) {
				heal(fighter);
			} else {
				attack(fighter);
			}
		} else {
			System.out.println("Moving towards fighter");
			Table.moveTowards(this, fighter.getX(), fighter.getY());
		}
	}
	
	protected void moveOrTakeActionAndCheck(Tile fighter, boolean heal) {
		if (fighter != this) {
			moveOrTakeAction(fighter, heal);
		} else {
			System.out.println("Choose again\n");
			chooseStrategy();
			choosePriority();
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
		} else if (behaviour.getPriority() == AIProfile.Priority.CLOSEST_WITH_EFFECT) {
			if (behaviour.getStrategy() == AIProfile.Strategy.AGGRESSIVE) {
				getClosestWithEffectAndAttack();
			} else if (behaviour.getStrategy() == AIProfile.Strategy.PASSIVE) {
				getClosestWithEffectAndMove();
			} else if (behaviour.getStrategy() == AIProfile.Strategy.DEFENSIVE) {
				getClosestWithEffectAndHeal();
			}
		}
	}
}
