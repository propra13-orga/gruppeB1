import java.util.ArrayList;
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
	public int mapMp = 50;
	public int atk = 67;
	public int def = 54;
	public int speed = 200;
	public int maxSpeed = 200;
	public int action_cost = 80;
	public int iq = 70;
	public Object_BattleSprite sprite;
	public ArrayList<Entity> items;
	public ArrayList<Entity> skills;
	public Hashtable<String, Entity> weapons;

	@Override
	public int compareTo(Object_BattleActor arg0) {
		int s1 = arg0.speed;
		return Integer.compare(s1, this.speed);
	}
}
