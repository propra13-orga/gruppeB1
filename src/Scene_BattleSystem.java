import java.awt.Color;
import java.awt.Font;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Scene_BattleSystem extends Abstract_Scene {
	
	public static final int GET_NEXT_ACTOR = 0;
	public static final int WAIT_FOR_PLAYER = 1;
	public static final int WAIT_FOR_ENEMY = 2;
	public static final int ANIMATION = 3;
	
	private Object_BattleContext ctx;
	private Abstract_Scene previous_scene;
	private ArrayList<Object_BattleActor> action_order;
	private int battle_type;
	private BufferedImage background;
	
	private Object_BattleActor current_actor;
	private Window_Selectable menu;
	
	Scene_BattleSystem(Object_BattleContext ctx, Abstract_Scene previous_scene, Object_Game game) {
		super(game);
		this.ctx = ctx;
		this.previous_scene = previous_scene;
		this.action_order = new ArrayList<Object_BattleActor>();
		this.battle_type = GET_NEXT_ACTOR;
		
		this.current_actor = null;
		this.menu = new Window_Selectable(0, 0, game);
		this.menu.EXIT_POSSIBLE = false;
		this.menu.EXECUTED = true;
		this.menu.addCommand("Angriff");
		this.menu.addCommand("Skills");
		this.menu.addCommand("Item");
		this.menu.addCommand("Flucht");
		
		String path = "res/background/"+this.ctx.background+".png";
		System.out.println("Lade: "+path);
		background = new BufferedImage(640, 480, BufferedImage.TYPE_INT_ARGB);
		try {
			background.getGraphics().drawImage(ImageIO.read(new File(path)),0,0,null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.sortActors();
		print(this.action_order.toString());
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onExit() {
		
	}

	@Override
	public void updateData() {
		switch (this.battle_type) {
		case GET_NEXT_ACTOR:
			print("Hole nächsten Actor");
			this.current_actor = this.action_order.get(0);
			if (this.ctx.players.contains(this.current_actor)) {
				this.battle_type = WAIT_FOR_PLAYER;
			}
			else {
				this.battle_type = WAIT_FOR_ENEMY;
			}
			break;
		case WAIT_FOR_PLAYER:
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
				this.current_actor = null;
			}
			break;
		case WAIT_FOR_ENEMY:
			if (this.battle_type == WAIT_FOR_ENEMY) {
				print("Wait for Enemy");
				this.battle_type = GET_NEXT_ACTOR;
			}
			break;
		case ANIMATION:
			break;
		}

		for (Object_BattleActor ba : this.ctx.actors) {
			ba.sprite.updateData();
		}
	}

	@Override
	public void updateScreen() {
		this.screen.clearRect(0, 0, Object_Screen.SCREEN_W, Object_Screen.SCREEN_H);
		this.screen.drawImage(this.background, 0, 0, null);
		for (Object_BattleActor ba : this.ctx.actors) {
			ba.sprite.updateScreen();
		}
		if (this.menu.EXECUTED) {
			this.menu.updateScreen();
		}
		if (this.current_actor.side == BattleSide.PLAYER) {
			Polygon p = new Polygon();
			int x = this.current_actor.sprite.x+50;
			int y = this.current_actor.sprite.y-30;
			p.addPoint(x-10, y);
			p.addPoint(x, y+20);
			p.addPoint(x+10, y);
			this.screen.setColor(new Color(255,0,0));
			this.screen.fillPolygon(p);
		}
		this.drawStats();
	}
	
	private void sortActors() {
		this.action_order = (ArrayList<Object_BattleActor>) this.ctx.actors.clone();
		for (Object_BattleActor b : this.action_order) {
			print(""+b.speed);
		}
		java.util.Collections.sort(this.action_order);
		for (Object_BattleActor b : this.action_order) {
			print(""+b.speed);
		}
	}
	
	private void drawStats() {
		int x = 250;
		int y = 20;
		for (Object_BattleActor b : this.ctx.players) {
			String infoline = b.name + "    " + b.hp + "/" + b.maxHp;
			this.screen.setColor(new Color(100,100,100));
			this.screen.fillRect(x, y, 300, 20);
			x += 20;
			y += 10;
			this.screen.setColor(new Color(255,255,255));
			this.screen.setFont(Object_Game.FONT);
			this.screen.drawString(infoline, x, y);
			y += 10;
			x -= 20;
		}
	}
	
	private void print(String s) {
		System.out.println(s);
	}

}
