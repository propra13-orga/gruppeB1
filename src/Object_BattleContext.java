import java.awt.image.BufferedImage;
import java.util.ArrayList;


public class Object_BattleContext implements IBattleContext {
	
	private ArrayList<Object_BattleActor> actors;
	private ArrayList<Object_BattleActor> players;
	private ArrayList<Object_BattleActor> enemies;
	private String background = "bridge";
	
	Object_BattleContext(Object_BattleActor b, Object_BattleActor c) {
		this.actors = new ArrayList<Object_BattleActor>();
		this.players = new ArrayList<Object_BattleActor>();
		this.enemies = new ArrayList<Object_BattleActor>();
		this.actors.add(b);
		this.actors.add(c);
	}

	@Override
	public ArrayList<Object_BattleActor> getActors() {
		// TODO Auto-generated method stub
		return this.actors;
	}

	@Override
	public ArrayList<Object_BattleActor> getPlayers() {
		// TODO Auto-generated method stub
		return this.players;
	}

	@Override
	public ArrayList<Object_BattleActor> getEnemies() {
		// TODO Auto-generated method stub
		return this.enemies;
	}

	@Override
	public String getBackground() {
		// TODO Auto-generated method stub
		return this.background;
	}
}
