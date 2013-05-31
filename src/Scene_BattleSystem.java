import java.util.ArrayList;


enum BattleType {
	ANIMATION,
	WAIT_FOR_PLAYER
}

public class Scene_BattleSystem extends Abstract_Scene {
	
	private Object_BattleContext ctx;
	private Abstract_Scene previous_scene;
	private ArrayList<Object_BattleActor> action_order;
	private BattleType battle_type;
	
	Scene_BattleSystem(Object_BattleContext ctx, Abstract_Scene previous_scene, Object_Game game) {
		super(game);
		this.ctx = ctx;
		this.previous_scene = previous_scene;
		this.action_order = new ArrayList<Object_BattleActor>();
		this.battle_type = BattleType.WAIT_FOR_PLAYER;
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onExit() {
		for (Object_BattleActor ba : this.ctx.getPlayers()) {
			ba.writeBack();
		}
	}

	@Override
	public void updateData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateScreen() {
		// TODO Auto-generated method stub
		
	}
	
	private void sortActors() {
		this.action_order.clear();
		while (this.action_order.size() < 10) {
			java.util.Collections.sort(this.ctx.getActors());
			this.action_order.add(this.ctx.getActors().get(0));
			this.ctx.getActors().get(0).reduceSpeed();
		}
	}

}
