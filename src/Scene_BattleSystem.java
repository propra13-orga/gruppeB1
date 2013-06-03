import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;


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
	private BufferedImage background;
	
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
		
		String path = "res/background/"+this.ctx.getBackground()+".png";
		System.out.println("Lade: "+path);
		background = new BufferedImage(640, 480, BufferedImage.TYPE_INT_ARGB);
		try {
			background.getGraphics().drawImage(ImageIO.read(new File(path)),0,0,null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
		/*
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
		*/
		for (Object_BattleActor ba : this.ctx.getActors()) {
			ba.getBattleSprite().updateData();
		}
	}

	@Override
	public void updateScreen() {
		this.screen.clearRect(0, 0, Object_Screen.SCREEN_W, Object_Screen.SCREEN_H);
		this.screen.drawImage(this.background, 0, 0, null);
		for (Object_BattleActor ba : this.ctx.getActors()) {
			ba.getBattleSprite().updateScreen();
		}
	}
	
	private void sortActors() {
		java.util.Collections.sort(this.ctx.getActors());
		this.action_order = this.ctx.getActors();
	}

}
