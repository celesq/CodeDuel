public class AIProfile {
	
	public enum Strategy{AGGRESSIVE, DEFENSIVE, PASSIVE};
	public enum Priority{CLOSEST, WEAKEST, STRONGEST};
	
	private Strategy strategy;
	private Priority priority;
	
	public AIProfile(Strategy strategy, Priority priority){
		this.strategy = strategy;
		this.priority = priority;
	}
	
	public Strategy getStrategy() {
		return strategy;
	}
	
	public void setStrategy(Strategy strategy) {
		this.strategy = strategy;
	}
	
	public Priority getPriority() {
		return priority;
	}
	
	public void setPriority(Priority priority) {
		this.priority = priority;
	}
}
