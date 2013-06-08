import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;

enum BattleSide {
	PLAYER,
	ENEMY
}

public class Object_BattleActor implements Comparable<Object_BattleActor> {

	public String name = "Testinit Actor";
	public BattleSide side = BattleSide.PLAYER;
	public int hp = 100;
	public int maxHp = 100;
	public int mp = 50;
	public int maxMp = 50;
	public int atk = 67;
	public int def = 54;
	public int speed = 200;
	public int maxSpeed = 200;
	public int action_cost = 80;
	public int dex = 70;
	public boolean wait = false;
	public Object_BattleSprite sprite;
	public ArrayList<Entity> items;
	public ArrayList<Entity> skills;
	public Hashtable<String, Entity> weapons;
	
	private Entity entity;
	
	
	public Object_BattleActor() {
		
	}
	
	public Object_BattleActor(Entity entity) {
		Component_Battle compBattle = (Component_Battle) entity.getComponent("battle");
		Component_Inventory compInventory = (Component_Inventory) entity.getComponent("inventory");
		
		this.entity = entity;
		this.name = entity.getName();
		this.hp = compBattle.getHP();
		this.maxHp = compBattle.getMaxHP();
		this.mp = compBattle.getMP();
		this.maxMp = compBattle.getMaxMP();
		this.atk = compBattle.getATK();
		this.def = compBattle.getDEF();
		this.dex = compBattle.getDEX();
		this.speed = compBattle.getSPD();
		this.maxSpeed = compBattle.getMaxSPD();
		//this.items = Arrays.asList(compInventory.getInventory());
		// Im Inventar sind momentan nur IDs gespeichert, über welche man im
		// EntityManager Entitäten abrufen kann. Einfach aus Performanzgründen.
		// Leider ist das nicht wirklich mit dem BattleSystem kompatibel. Da
		// muss ich mir noch was überlegen. Wahrscheinlich wird es doch ein Item-
		// Objekt geben, was dann auch von den Läden benutzt wird. Auch wenn
		// mir das etwas widerstrebt, weil es den Komponentenansatz kaputtmacht.
		// Naja.
	}

	@Override
	public int compareTo(Object_BattleActor arg0) {
		int s1 = arg0.speed;
		return Integer.compare(s1, this.speed);
	}
	
	public String getName() { return this.name; }
	public int getHP() { return this.hp; }
	public int getMaxHP() { return this.maxHp; }
	public int getMP() { return this.mp; }
	public int getMaxMP() { return this.maxMp; }
	public int getATK() { return this.atk; }
	public int getDEF() { return this.def; }
	public int getSpeed() { return this.speed; }
	public int getMaxSpeed() { return this.maxSpeed; }
	public int getActionCost() { return this.maxSpeed / 3; }
	public int getDEX() { return this.dex; }
}
