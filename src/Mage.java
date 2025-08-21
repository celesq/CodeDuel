import java.util.Scanner;

public class Mage extends Fighter {
	
	public Mage(int id){
		setName("Mage");
		setPlayerId(id);
		setAttackDamage(20);
		setHealth(80);
		setHeal(10);
		setBehaviour(new AIProfile(AIProfile.Strategy.AGGRESSIVE, AIProfile.Priority.STRONGEST));
	}
	
	@Override
	public boolean isMage() {
		return true;
	}
	
	private AIProfile.Magic chooseMagic() {
		AIProfile.Magic magic = null;
		Scanner scan = new Scanner(System.in);
		int choice;
		System.out.println("Choose a magic to use:");
		System.out.println("1. " + AIProfile.Magic.POISON);
		System.out.println("2. " + AIProfile.Magic.CURSE);
		System.out.println("3. " + AIProfile.Magic.SUPER_HEAL);
		choice = scan.nextInt();
		while (choice < 1 || choice > 3) {
			System.out.println("Invalid choice, choose between 1 and 3");
			System.out.println("1. " + AIProfile.Magic.POISON);
			System.out.println("2. " + AIProfile.Magic.CURSE);
			System.out.println("3. " + AIProfile.Magic.SUPER_HEAL);
			choice = scan.nextInt();
		}
		switch (choice) {
			case 1 -> magic = AIProfile.Magic.POISON;
			case 2 -> magic = AIProfile.Magic.CURSE;
			case 3 -> magic = AIProfile.Magic.SUPER_HEAL;
			default -> System.out.println("Invalid choice, choose between 1 and 3");
		}
		
		return magic;
	}
	
	@Override
	public AIProfile.Strategy chooseStrategy() {
		int strategyChoice;
		AIProfile.Strategy selectedStrategy;
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("Choose Strategy for Fighter " + getName());
		System.out.println("1. " + AIProfile.Strategy.AGGRESSIVE);
		System.out.println("2. " + AIProfile.Strategy.DEFENSIVE);
		System.out.println("3. " + AIProfile.Strategy.PASSIVE);
		System.out.println("4. " + AIProfile.Strategy.MAGIC);
		
		strategyChoice = scanner.nextInt();
		switch (strategyChoice) {
			case 1 -> selectedStrategy = AIProfile.Strategy.AGGRESSIVE;
			case 2 -> selectedStrategy = AIProfile.Strategy.DEFENSIVE;
			case 3 -> selectedStrategy = AIProfile.Strategy.PASSIVE;
			case 4 -> selectedStrategy = AIProfile.Strategy.MAGIC;
			default -> {
				System.out.println("Invalid choice. Defaulting to PASSIVE.");
				selectedStrategy = AIProfile.Strategy.PASSIVE;
			}
		}
		
		behaviour.setStrategy(selectedStrategy);
		return selectedStrategy;
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
			} else if (behaviour.getStrategy() == AIProfile.Strategy.MAGIC) {
				AIProfile.Magic magic = chooseMagic();
				behaviour.setMagic(magic);
				magicClosest();
			}
		} else if (behaviour.getPriority() == AIProfile.Priority.STRONGEST) {
			
			if (behaviour.getStrategy() == AIProfile.Strategy.AGGRESSIVE) {
				getStrongestAndAttack();
			} else if (behaviour.getStrategy() == AIProfile.Strategy.PASSIVE) {
				getStrongestAndMove();
			} else if (behaviour.getStrategy() == AIProfile.Strategy.DEFENSIVE) {
				getStrongestAndHeal();
			} else if (behaviour.getStrategy() == AIProfile.Strategy.MAGIC) {
				AIProfile.Magic magic = chooseMagic();
				behaviour.setMagic(magic);
				magicStrongest();
			}
		} else if (behaviour.getPriority() == AIProfile.Priority.WEAKEST) {
			
			if (behaviour.getStrategy() == AIProfile.Strategy.AGGRESSIVE) {
				getWeakestAndAttack();
			} else if (behaviour.getStrategy() == AIProfile.Strategy.PASSIVE) {
				getWeakestAndMove();
			} else if (behaviour.getStrategy() == AIProfile.Strategy.DEFENSIVE) {
				getWeakestAndHeal();
			} else if (behaviour.getStrategy() == AIProfile.Strategy.MAGIC) {
				AIProfile.Magic magic = chooseMagic();
				behaviour.setMagic(magic);
				magicWeakest();
			}
		} else if (behaviour.getPriority() == AIProfile.Priority.CLOSEST_WITH_EFFECT) {
			if (behaviour.getStrategy() == AIProfile.Strategy.AGGRESSIVE) {
				getClosestWithEffectAndAttack();
			} else if (behaviour.getStrategy() == AIProfile.Strategy.PASSIVE) {
				getClosestWithEffectAndMove();
			} else if (behaviour.getStrategy() == AIProfile.Strategy.DEFENSIVE) {
				getClosestWithEffectAndHeal();
			} else if (behaviour.getStrategy() == AIProfile.Strategy.MAGIC) {
				AIProfile.Magic magic = chooseMagic();
				behaviour.setMagic(magic);
				magicClosestWithEffect();
			}
		}
	}
	
	private void magicClosestWithEffect() {
		Tile closestFighter;
		if (behaviour.getMagic() == AIProfile.Magic.SUPER_HEAL) {
			if (getPlayerId() == 1) {
				closestFighter = Table.getClosestWithEffect(getX(), getY(), 1);
			} else {
				closestFighter = Table.getClosestWithEffect(getX(), getY(), 2);
			}
		} else if (getPlayerId() == 1) {
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
			behaviour.moveOrMagic(this, closestFighter);
		}
	}
	
	private void magicClosest() {
		Tile closestFighter;
		if (behaviour.getMagic() == AIProfile.Magic.SUPER_HEAL) {
			if (getPlayerId() == 1) {
				closestFighter = Table.getWeakest(getX(), getY(), 1);
			} else {
				closestFighter = Table.getWeakest(getX(), getY(), 2);
			}
		} else if (getPlayerId() == 1) {
			closestFighter = Table.getClosest(getX(), getY() , 2);
		} else {
			closestFighter = Table.getClosest(getX(), getY(), 1);
		}
		if (closestFighter.getX() == getX() && closestFighter.getY() == getY()) {
			System.out.println("No fighter found!");
		} else {
			behaviour.moveOrMagic(this, closestFighter);
		}
	}
	
	private void magicStrongest() {
		Tile strongestFighter;
		if (behaviour.getMagic() == AIProfile.Magic.SUPER_HEAL) {
			if (getPlayerId() == 1) {
				strongestFighter = Table.getWeakest(getX(), getY(), 1);
			} else {
				strongestFighter = Table.getWeakest(getX(), getY(), 2);
			}
		} else if (getPlayerId() == 1) {
			strongestFighter = Table.getStrongest(getX(), getY(), 2);
		} else {
			strongestFighter = Table.getStrongest(getX(), getY(), 1);
		}
		behaviour.moveOrMagic(this, strongestFighter);
	}
	
	private void magicWeakest() {
		Tile weakestFighter;
		if (behaviour.getMagic() == AIProfile.Magic.SUPER_HEAL) {
			if (getPlayerId() == 1) {
				weakestFighter = Table.getWeakest(getX(), getY(), 1);
			} else {
				weakestFighter = Table.getWeakest(getX(), getY(), 2);
			}
		} else if (getPlayerId() == 1) {
			weakestFighter = Table.getWeakest(getX(), getY(), 2);
		} else {
			weakestFighter = Table.getWeakest(getX(), getY(), 1);
		}
		behaviour.moveOrMagic(this, weakestFighter);
	}
	
	public void poison(Tile fighter) {
		fighter.setEffect(AIProfile.Magic.POISON);
		fighter.setEffectDuration(3);
		fighter.takeDamage(fighter, AIProfile.POISON_DAMAGE);
		System.out.println("Fighter " + fighter.getName() + " is poisoned for 3 rounds!");
	}
	
	public void curse(Tile fighter) {
		fighter.setEffect(AIProfile.Magic.CURSE);
		fighter.setEffectDuration(3);
		System.out.println("Fighter " + fighter.getName() + " is cursed for 3 rounds!");
	}
	
	public void superHeal(Tile fighter) {
		fighter.setEffect(AIProfile.Magic.SUPER_HEAL);
		fighter.setEffectDuration(3);
		System.out.println("Fighter " + fighter.getName() + " is superHealed for 3 rounds!");
	}
}
