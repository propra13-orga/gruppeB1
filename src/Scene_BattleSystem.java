import java.util.ArrayList;


enum BattleType {
	NONE,
	ANIMATION,
	WAIT_FOR_PLAYER,
	WAIT_FOR_ENEMY
}

public class Scene_BattleSystem extends Abstract_Scene {
	
	private Object_BattleContext ctx;
	private Abstract_Scene previous_scene;
	private ArrayList<Object_BattleActor> action_order;
	private BattleType battle_type;
	
	private Object_BattleActor current_actor;
	private SubScene_WindowSelectable menu;
	
	Scene_BattleSystem(Object_BattleContext ctx, Abstract_Scene previous_scene, Object_Game game) {
		super(game);
		this.ctx = ctx;
		this.previous_scene = previous_scene;
		this.action_order = new ArrayList<Object_BattleActor>();
		this.battle_type = BattleType.NONE;
		
		this.current_actor = null;
		this.menu = new SubScene_WindowSelectable(0, 0, game);
		this.menu.EXIT_POSSIBLE = false;
		this.menu.addCommand("Angriff");
		this.menu.addCommand("Skills");
		this.menu.addCommand("Item");
		this.menu.addCommand("Flucht");
		
		this.sortActors();
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
		if (this.current_actor == null) {
		 	this.current_actor = this.action_order.get(0);
			if (this.ctx.getPlayers().contains(this.current_actor)) {
				this.battle_type = BattleType.WAIT_FOR_PLAYER;
			}
			else {
				this.battle_type = BattleType.WAIT_FOR_ENEMY;
			}
		}
		if (this.battle_type == BattleType.WAIT_FOR_PLAYER) {
			if (this.menu.EXECUTED) {
				this.menu.updateData();
			}
			else {
				switch (this.menu.cursor) {
				case 0: //Angriff
					break;
				case 1: //Skills
					break;
				case 2: //Item
					break;
				case 3: //Flucht
					break;
				}
			}
			this.current_actor = null;
		}
		else if (this.battle_type == BattleType.WAIT_FOR_ENEMY) {
			this.current_actor = null;
		}
	}

	@Override
	public void updateScreen() {
		// TODO Auto-generated method stub
		
	}
	
	private void sortActors() {
		java.util.Collections.sort(this.ctx.getActors());
		this.action_order = this.ctx.getActors();
	}

}
