import java.awt.image.BufferedImage;
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
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getHP() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMaxHP() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMP() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMaxMP() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getATK() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getDEF() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getIQ() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ArrayList<Entity> getItems() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Entity> getSkills() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Hashtable<String, Entity> getEquipment() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BufferedImage getBattleSprite() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void writeBack() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getActionCost() {
		// TODO Auto-generated method stub
		return 0;
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
		return 0;
	}

	@Override
	public int getMaxSpeed() {
		// TODO Auto-generated method stub
		return 0;
	}

}
