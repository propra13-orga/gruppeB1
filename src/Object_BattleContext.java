import java.awt.image.BufferedImage;
import java.util.ArrayList;


public class Object_BattleContext {
	
	public ArrayList<Object_BattleActor> actors;
	public ArrayList<Object_BattleActor> players;
	public ArrayList<Object_BattleActor> enemies;
	public String background = "forest1";
	
	Object_BattleContext() {
		this.actors = new ArrayList<Object_BattleActor>();
		this.players = new ArrayList<Object_BattleActor>();
		this.enemies = new ArrayList<Object_BattleActor>();
	}
}
