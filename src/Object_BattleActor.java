import java.util.ArrayList;
import java.util.Hashtable;

public class Object_BattleActor implements IBattleActor, Comparable<Object_BattleActor> {

	private String name = "Testinit Actor";
	private int hp = 100;
	private int maxHp = 100;
	private int mp = 50;
	private int mapMp = 50;
	private int atk = 67;
	private int def = 54;
	private int speed = 200;
	private int maxSpeed = 200;
	private int action_cost = 80;
	private int iq = 70;
	private Object_BattleSprite sprite;
	private ArrayList<Entity> items;
	private ArrayList<Entity> skills;
	private Hashtable<String, Entity> weapons;
	
	Object_BattleActor(Object_Game game, int x, int y, int delay) {
		this.sprite = new Object_BattleSprite("battlechar-1", x, y, delay, game);
	}
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return this.name;
	}

	@Override
	public int getHP() {
		// TODO Auto-generated method stub
		return this.hp;
	}

	@Override
	public int getMaxHP() {
		// TODO Auto-generated method stub
		return this.maxHp;
	}

	@Override
	public int getMP() {
		// TODO Auto-generated method stub
		return this.mp;
	}

	@Override
	public int getMaxMP() {
		// TODO Auto-generated method stub
		return this.mapMp;
	}

	@Override
	public int getATK() {
		// TODO Auto-generated method stub
		return this.atk;
	}

	@Override
	public int getDEF() {
		// TODO Auto-generated method stub
		return this.def;
	}

	@Override
	public int getIQ() {
		// TODO Auto-generated method stub
		return this.iq;
	}

	@Override
	public ArrayList<Entity> getItems() {
		// TODO Auto-generated method stub
		return this.items;
	}

	@Override
	public ArrayList<Entity> getSkills() {
		// TODO Auto-generated method stub
		return this.skills;
	}

	@Override
	public Hashtable<String, Entity> getEquipment() {
		// TODO Auto-generated method stub
		return this.weapons;
	}

	@Override
	public void writeBack() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getActionCost() {
		// TODO Auto-generated method stub
		return this.action_cost;
	}
	
	@Override
	public void reduceSpeed() {
		this.speed -= this.getActionCost();
		if (this.speed < 0) {
			this.speed = 0;
		}
	}
	
	@Override
	public void resetSpeed() {
		this.speed = this.maxSpeed;
	}

	@Override
	public int compareTo(Object_BattleActor arg0) {
		int s1 = arg0.getSpeed();
		return Integer.compare(this.getSpeed(), s1);
	}

	@Override
	public int getSpeed() {
		// TODO Auto-generated method stub
		return this.speed;
	}

	@Override
	public Object_BattleSprite getBattleSprite() {
		// TODO Auto-generated method stub
		return this.sprite;
	}

	@Override
	public int getMaxSpeed() {
		// TODO Auto-generated method stub
		return 0;
	}

}
