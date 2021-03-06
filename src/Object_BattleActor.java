import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

enum BattleSide {
	PLAYER,
	ENEMY
}

public class Object_BattleActor implements Comparable<Object_BattleActor> {
	
	public static int ID_COUNTER = 0;

	public String name = "Testinit Actor";
	public BattleSide side = BattleSide.PLAYER;
	public int id;
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
	public boolean is_new = true;
	public boolean wait = false;
	public boolean dead = false;
	public Object_BattleSprite sprite;
	public List<Entity> items;
	public List<Object_Skill> skills;
	public Hashtable<String, Entity> weapons;
	public Scene_BattleSystem battle_system;

	//NOCH EINFUEGEN!
	public boolean attackable = true;
	public Entity entity;
	
	public Object_BattleActor(Scene_BattleSystem scene) {
		this.battle_system = scene;
	}
	
	public Object_BattleActor(Entity entity, Scene_BattleSystem scene) {
		Component_Battle compBattle = (Component_Battle) entity.getComponent("battle");
		Component_Inventory compInventory = (Component_Inventory) entity.getComponent("inventory");
		
		this.battle_system = scene;
		this.id = ID_COUNTER;
		ID_COUNTER++;
		this.entity = entity;
		this.extractProperties();
		this.extractItems();
		/*
		 * Die ganzen Eigenschaften sind jetzt dynamisch. compBattle enthält eine
		 * Liste mit zutreffenden Eigenschaften. Das heißt, es müssen nicht alle
		 * vorhanden sein. Daher sollten Default-Werte festgelegt sein und wenn
		 * einer ohne HP ankommt, kriegt er halt den Wert 0, etc.
		 */
		//		this.name = entity.getName();
		//		this.hp = compBattle.getHP();
		//		this.maxHp = compBattle.getMaxHP();
		//		this.mp = compBattle.getMP();
		//		this.maxMp = compBattle.getMaxMP();
		//		this.atk = compBattle.getATK();
		//		this.def = compBattle.getDEF();
		//		this.dex = compBattle.getDEX();
		//		this.speed = compBattle.getSPD();
		//		this.maxSpeed = compBattle.getMaxSPD();
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
		int s2 = this.speed;
		return Integer.compare(s1, s2);
	}
	
	public Entity getEntity() { return this.entity; }
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
	
	
	private void extractProperties() {
		Component_Battle compBattle = (Component_Battle) this.entity.getComponent("battle");
		this.maxHp = compBattle.getPropertyValue("prop_hp");
		this.hp = compBattle.getPropertyValue("prop_hp_current");
		if (compBattle.hasProperty("prop_mp")) {
			this.maxMp = compBattle.getPropertyValue("prop_mp");
			this.mp = compBattle.getPropertyValue("prop_mp_current");
		}
		else
		{
			this.mp = 0;
			this.maxMp = 0;
		}
		/*
		 * Hier, wenn nötig, weitere Eigenschaften zuweisen. Insbesondere den Sprite!
		 * Der Dateiname ist erreichbar über compBattle.getSprite();
		 */
	}
	
	
	
	private void extractItems() {
		if (!this.entity.hasComponent("inventory")) return;
		
		Component_Inventory compInventory = (Component_Inventory) this.entity.getComponent("inventory");
		for (Entity item : compInventory.getInventory()) {
			if (item != null) {
				Component_Item compItem = (Component_Item) item.getComponent("item");
				if (compItem.getProperties().contains("type_consumable")) {
					this.items.add(item);
				}
				
			}
		}
	}
}
