import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;


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
	
	public Object_BattleContext(List<Object_BattleActor> players, List<Object_BattleActor> enemies) {
		this.players = (ArrayList) players;
		this.enemies = (ArrayList) enemies;
		this.actors = (ArrayList) players;
		this.actors.addAll(enemies);
	}

}