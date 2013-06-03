import java.util.ArrayList;

public interface IBattleContext {

	public ArrayList<Object_BattleActor> getActors();
	public ArrayList<Object_BattleActor> getPlayers();
	public ArrayList<Object_BattleActor> getEnemies();
	public String getBackground();
	
}
