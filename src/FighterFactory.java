public class FighterFactory {
	
	public static Fighter fighterCreate(int type, int id) {
		Fighter fighter = null;
		switch (type) {
			case 1: fighter = new Berserker(id); break;
			case 2: fighter = new Archer(id); break;
			case 3: fighter = new Mage(id); break;
			case 4: fighter = new Healer(id); break;
			case 5: fighter = new Tank(id); break;
			default: return null;
		}
		return fighter;
	}
}
