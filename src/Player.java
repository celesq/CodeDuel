import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Player {
	
	private final int id;
	private String name;
	private int matchesWon;
	private List<Fighter> team;
	private final List<Fighter> fightersTemplate = List.of(new Berserker(), new Archer(),
			new Mage(), new Tank(), new Healer());
	
	public Player(String name, int id) {
		this.name = name;
		this.matchesWon = 0;
		this.team = new ArrayList<>();
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getMatchesWon() {
		return matchesWon;
	}
	
	public void setMatchesWon(int matchesWon) {
		this.matchesWon = matchesWon;
	}
	
	public List<Fighter> getTeam() {
		return team;
	}
	
	public void setTeam(List<Fighter> team) {
		this.team = team;
	}
	
	public void waitInput(Table gameTable) {
		List<Tile> fightersOnTable = gameTable.getFightersOnTableFromPlayer(id);
		for (Tile f : fightersOnTable) {
			AIProfile.Strategy strategy = chooseStrategy(f);
			AIProfile.Priority priority = choosePriority(f);
			f.getChoice(strategy, priority);
			f.takeTurn();
		}
	}
	
	public void chooseTeam() {
		
		for (Fighter f : fightersTemplate) {
			f.setPlayerId(id);
		}
		
		for (int i = 0; i < 5; i++) {
			Scanner scanner = new Scanner(System.in);
			System.out.println("Please choose a fighter");
			int choice = scanner.nextInt();
			while (choice < 1 || choice > 5) {
				System.out.println("Please choose again, a number between 1 and 5");
				choice = scanner.nextInt();
			}
			team.add(fightersTemplate.get(choice - 1));
		}
	}
	
	public void placeCardsOnTable(Table gameTable){
		Scanner scanner = new Scanner(System.in);
		List<Integer> spacesLeft = new ArrayList<>();
		int choiceRow= 1000, choiceCol = 1000;
		for (Fighter figher : team) {
			System.out.println("Choose where to put Fighter " + figher.getName());
			try {
				if (id == 1) {
					while (5 > choiceRow || choiceRow > 9) {
						System.out.println("You can choose a row between 5-9");
						choiceRow = scanner.nextInt();
					}
					spacesLeft = gameTable.spacesLeftOnRowX(choiceRow);
					
				} else {
					while (1 > choiceRow || choiceRow > 4) {
						System.out.println("You can choose a row between 1-4");
						choiceRow = scanner.nextInt();
					}
					spacesLeft = gameTable.spacesLeftOnRowX(choiceRow);
					
				}
				
				while(!spacesLeft.contains(choiceCol)){
					System.out.println("You can choose a collumn of" + spacesLeft);
					choiceCol = scanner.nextInt();
				}
				
				Table.putTileOnTable(choiceRow, choiceCol, figher);
				team.remove(figher);
				
			} catch (Exception e) {
				System.out.println("Please choose again, a number");
			}
		}
	}
	
	public static AIProfile.Strategy chooseStrategy(Tile fighter) {
		
		int strategyChoice;
		AIProfile.Strategy selectedStrategy;
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("Choose Strategy for Fighter " + fighter.getName());
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
	
	public static AIProfile.Priority choosePriority(Tile fighter) {
		
		int priorityChoice;
		AIProfile.Priority selectedPriority;
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("Choose Priority for Fighter " + fighter.getName());
		System.out.println("1. " + AIProfile.Priority.STRONGEST);
		System.out.println("2. " + AIProfile.Priority.CLOSEST);
		System.out.println("3. " + AIProfile.Priority.WEAKEST);
		
		priorityChoice = scanner.nextInt();
		switch (priorityChoice) {
			case 1 -> selectedPriority = AIProfile.Priority.STRONGEST;
			case 2 -> selectedPriority = AIProfile.Priority.CLOSEST;
			case 3 -> selectedPriority =  AIProfile.Priority.WEAKEST;
			default -> {
				System.out.println("Invalid choice. Defaulting to CLOSEST.");
				selectedPriority =  AIProfile.Priority.CLOSEST;
			}
		}
		
		return selectedPriority;
	}
}
