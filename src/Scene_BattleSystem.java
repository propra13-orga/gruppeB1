import java.awt.Color;
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
	
	private Window_Menu main_menu;
	private Window_Menu menu_enemy;
	private Window_Menu menu_item;
	private Window_Menu menu_skill;
	private Window_Menu menu_player;
	
	Scene_BattleSystem(Object_BattleContext ctx, Abstract_Scene previous_scene, Object_Game game) {
		super(game);
		this.ctx = ctx;
		this.previous_scene = previous_scene;
		this.action_order = new ArrayList<Object_BattleActor>();
		this.battle_type = GET_NEXT_ACTOR;
		
		this.current_actor = null;
		
		/*
		 * Initialisiere Menues
		 */
		
		this.main_menu = new Window_Menu("main", 0, 0, game);
		this.menu_enemy = new Window_Menu("enemy", 0, 0, game);
		this.menu_item = new Window_Menu("item", 0, 0, game);
		this.menu_skill = new Window_Menu("skills", 0, 0, game);
		this.menu_player = new Window_Menu("player", 0, 0, game);
		
		for (Object_BattleActor enemy : this.ctx.enemies) {
			this.menu_enemy.addReturnCommand(enemy.name);
		}
		
		for (Object_BattleActor player : this.ctx.players) {
			this.menu_player.addReturnCommand(player.name);
		}
		
		//Hier zu Testzwecken das Itemmenü mit Einträgen füllen
		this.menu_item.addReturnCommand("Kleiner Heiltrank");
		this.menu_item.addReturnCommand("Großer Heiltrank");
		
		//Skill und Itemmenü wird erst mit Einträgen gefüllt, wenn es aufgerufen wird,
		//weil dann der zugehörige Character feststeht (Player1 hat andere Skills als Player2)
		
		this.main_menu.EXIT_POSSIBLE = false;
		this.main_menu.addMenuCommand("Angriff", this.menu_enemy);
		this.main_menu.addMenuCommand("Skills", this.menu_skill);
		this.main_menu.addMenuCommand("Item", this.menu_item);
		this.main_menu.addReturnCommand("Verteidigung");
		
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
		//this.soundmanager.abspielen("battle");
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onExit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateData() {
		
		switch (this.battle_type) {
		
		case GET_NEXT_ACTOR:
			this.current_actor = this.action_order.get(0);
			if (this.ctx.players.contains(this.current_actor)) {
				this.battle_type = WAIT_FOR_PLAYER;
			}
			else {
				this.battle_type = WAIT_FOR_ENEMY;
			}
			break;
			
		case WAIT_FOR_PLAYER:
			if (this.main_menu.isExecuted()) {
				this.main_menu.updateData();
			}
			else {
				
				//Menu beendet, pruefe welches Menu zuletzt bedient wurde und
				//pruefe dann dessen Einträge
				
				switch (this.main_menu.final_name) {
				case "enemy":
					//Gegner wurde gewählt
					switch (this.main_menu.final_cursor) {
					case 0: //Gegner 1
						this.print("GEGNER 1");
						break;
					case 1:
						print("Gegner 2!");
						break;
					case 2:
						print("Gegner 3!");
					}
					
				case "item":
					print("item");
				}
				
			}
			
		case WAIT_FOR_ENEMY:
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
		if (this.main_menu.isExecuted()) {
			this.main_menu.updateScreen();
		}
		this.drawArrow();
		this.drawStats();
		this.drawActionOrder();
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
	
	private void drawArrow() {
		if (this.current_actor.side == BattleSide.PLAYER) {
			Polygon p = new Polygon();
			int x = this.current_actor.sprite.x+50;
			int y = this.current_actor.sprite.y-20;
			p.addPoint(x-10, y);
			p.addPoint(x, y+20);
			p.addPoint(x+10, y);
			this.screen.setColor(new Color(255,0,0));
			this.screen.fillPolygon(p);
		}
	}
	
	private void drawStats() {
		int x = 280;
		int y = 5;
		int alpha;
		for (Object_BattleActor b : this.ctx.players) {
			if (this.current_actor == b) {
				alpha = 255;
			}
			else {
				alpha = 220;
			}
			String infoline = b.name + "    " + b.hp + "/" + b.maxHp;
			this.screen.setColor(new Color(0,77,148));
			this.screen.fillRect(x+10, y, 330, 40);
			y += 30;
			this.screen.setColor(new Color(0,21,72));
			this.screen.fillRect(x, y, 350, 10);
			x += 15;
			this.screen.setColor(new Color(255, 255, 255, alpha));
			this.screen.setFont(Object_Game.FONT);
			y -= 8;
			this.screen.drawString(infoline, x, y);
			y+= 8;
			y += 20;
			x -= 15;
		}
	}
	
	private void drawActionOrder() {
		this.screen.setColor(new Color(0, 77, 148));
		this.screen.fillRect(0, 440, 640, 40);
		int x = 10;
		int y = 470;
		boolean first = true;
		this.screen.setColor(new Color(255,255,255));
		for (Object_BattleActor b : this.action_order) {
			this.screen.drawString(b.name, x, y);
			if (first) {
				first = false;
				this.screen.setColor(new Color(150,150,150));
			}
			x += this.screen.getFontMetrics().stringWidth(b.name) + 20;
		}
	}
	
	private void print(String s) {
		System.out.println(s);
	}

}
