import java.util.ArrayList;
import java.util.Hashtable;

public class Object_BattleActor implements IBattleActor, Comparable<Object_BattleActor> {

	private String name;
	private int hp;
	private int maxHp;
	private int mp;
	private int mapMp;
	private int atk;
	private int def;
	private int speed;
	private int maxSpeed;
	private int action_cost;
	private int iq;
	private Object_BattleSpriteSet set;
	private ArrayList<Entity> items;
	private ArrayList<Entity> skills;
	private Hashtable<String, Entity> weapons;
	
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
	public Object_BattleSpriteSet getBattleSpriteSet() {
		// TODO Auto-generated method stub
		return this.set;
	}

	@Override
	public int getMaxSpeed() {
		// TODO Auto-generated method stub
		return 0;
	}

}
