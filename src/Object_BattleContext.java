import java.util.ArrayList;
import java.util.List;

public class Object_BattleContext {
	
	private ArrayList<Object_BattleActor> actors;
	public String background = "forest1";
	
	Object_BattleContext() {
		this.actors = new ArrayList<Object_BattleActor>();
	}
	
	public Object_BattleContext(List<Object_BattleActor> actors) {
		this.actors = new ArrayList<Object_BattleActor>();
		this.actors.addAll(actors);
	}
	
	public ArrayList<Object_BattleActor> getActors() {
		return this.actors;
	}
	
	public ArrayList<Object_BattleActor> getAliveActors() {
		ArrayList<Object_BattleActor> players = new ArrayList<Object_BattleActor>();
		for (Object_BattleActor b : this.actors) {
			if (!b.dead) {
				players.add(b);
			}
		}
		return players;
	}
	
	public ArrayList<Object_BattleActor> getPlayers() {
		ArrayList<Object_BattleActor> players = new ArrayList<Object_BattleActor>();
		for (Object_BattleActor b : this.actors) {
			if (b.side == BattleSide.PLAYER) {
				players.add(b);
			}
		}
		return players;
	}
	
	public ArrayList<Object_BattleActor> getEnemies() {
		ArrayList<Object_BattleActor> players = new ArrayList<Object_BattleActor>();
		for (Object_BattleActor b : this.actors) {
			if (b.side == BattleSide.ENEMY) {
				players.add(b);
			}
		}
		return players;
	}
	
	public ArrayList<Object_BattleActor> getAlivePlayers() {
		ArrayList<Object_BattleActor> players = new ArrayList<Object_BattleActor>();
		for (Object_BattleActor b : this.actors) {
			if (b.side == BattleSide.PLAYER && (!b.dead)) {
				players.add(b);
			}
		}
		return players;
	}

	public ArrayList<Object_BattleActor> getAliveEnemies() {
		ArrayList<Object_BattleActor> players = new ArrayList<Object_BattleActor>();
		for (Object_BattleActor b : this.actors) {
			if (b.side == BattleSide.ENEMY && (!b.dead)) {
				players.add(b);
			}
		}
		return players;
	}

}