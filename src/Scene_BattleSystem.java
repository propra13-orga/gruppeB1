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
	private ArrayList<Integer> action_order;
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
		
		Window_Menu.setMainMenu(this.main_menu);
		
		for (Object_BattleActor enemy : this.ctx.enemies) {
			if (enemy.attackable) {
				this.menu_enemy.addReturnCommand(enemy.name);
			}
		}
		
		for (Object_BattleActor player : this.ctx.players) {
			this.menu_player.addReturnCommand(player.name);
		}
		
		this.main_menu.addMenuCommand("Angriff", this.menu_enemy);
		this.main_menu.addMenuCommand("Skills", this.menu_skill);
		this.main_menu.addMenuCommand("Item", this.menu_item);
		this.main_menu.addReturnCommand("Verteidigung");
		
		//Hier zu Testzwecken das Item/SKill-men� mit Eintr�gen f�llen
		this.menu_item.addMenuCommand("Kleiner Heiltrank", this.menu_player);
		this.menu_item.addMenuCommand("Gro�er Heiltrank", this.menu_player);
		
		this.menu_skill.addMenuCommand("Doppelschlag", this.menu_enemy);
		this.menu_skill.addMenuCommand("Feuerball", this.menu_enemy);
		this.menu_skill.addMenuCommand("Kleine Heilung", this.menu_enemy);
		// !!! !!! !!! WICHTIG !!! !!! !!!
		//sp�ter wenn pageable menu umgesetzt ist k�nnen skills nach physischen, magischen und
		//heilenden skills sortiert werden
		
		//Skill und Itemmen� wird erst mit Eintr�gen gef�llt, wenn es aufgerufen wird,
		//weil dann der zugeh�rige Character feststeht (Player1 hat andere Skills als Player2)
		
		String path = "res/background/"+this.ctx.background+".png";
		System.out.println("Lade: "+path);
		background = new BufferedImage(640, 480, BufferedImage.TYPE_INT_ARGB);
		try {
			background.getGraphics().drawImage(ImageIO.read(new File(path)),0,0,null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (int k=0; k<this.ctx.actors.size(); k++) {
			this.ctx.actors.get(k).id = k;
		}

		this.sortActors();
		int next_id = this.action_order.get(0);
		for (Object_BattleActor b : this.ctx.actors) {
			if (b.id == next_id) {
				this.current_actor = b;
				break;
			}
		}
		if (this.ctx.players.contains(this.current_actor)) {
			this.main_menu.reset();
			this.battle_type = WAIT_FOR_PLAYER;
		}
		else {
			this.battle_type = WAIT_FOR_ENEMY;
		}
		this.soundmanager.playMidi("battle");
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
			this.action_order.add(this.action_order.get(0));
			this.action_order.remove(0);
			int next_id = this.action_order.get(0);
			for (Object_BattleActor b : this.ctx.actors) {
				if (b.id == next_id) {
					this.current_actor = b;
					break;
				}
			}
			if (this.ctx.players.contains(this.current_actor)) {
				this.main_menu.reset();
				this.battle_type = WAIT_FOR_PLAYER;
			}
			else {
				this.battle_type = WAIT_FOR_ENEMY;
			}
			break;
			
			
			
			
			
		case WAIT_FOR_PLAYER:
			if (this.main_menu.final_decision == false) {
				this.main_menu.updateData();
			}
			else {
				//Menu beendet, pruefe welches Menu zuletzt bedient wurde und
				//pruefe dann dessen Eintr�ge
				print(this.main_menu.final_name);
				switch (this.main_menu.final_name) {
				
				case "main":
					//Normalerweise w�rde jetzt hier die Cursorposition
					//abgefragt, aber der einzige return command in main ist
					//Verteidugung, von daher ist klar, was gew�hlt wurde
					print("VERTEIDUGUNG!");
					break;
					
				case "enemy":
					//Gegner wurde gew�hlt
					switch (this.main_menu.final_cursor) {
					case 0: //Gegner 1
						this.print("Greife Gegner 1 an!");
						break;
					case 1:
						print("Greife Gegner 2 an!");
						break;
					case 2:
						print("Greife Gegner 3 an!");
					}
					Object_BattleActor chosen_enemy = this.ctx.enemies.get(this.main_menu.final_cursor);
					double damage = this.current_actor.action_cost*1.2 - (chosen_enemy.def*0.5);
					chosen_enemy.hp -= (int) damage;
					this.current_actor.speed -= 50;
					if (this.current_actor.speed <= 0) {
						this.current_actor.speed = this.current_actor.maxSpeed;
						this.current_actor.wait = true;
					}
					break;
					
				case "player":
					//Hier dann dynamisch das gew�hlte Item w�hlen
					print("Benutze Item!");
					break;
					
				case "skill":
					//Hier dynamisch den gew�hlten Skill w�hlen
					print("Benutze Skill!");
					break;
				}
				
				this.battle_type = GET_NEXT_ACTOR;
				
			}
			break;
			
			
			
			
			
		case WAIT_FOR_ENEMY:
			print("GEGNER MACHT WAS");
			print("DER N�CHSTE BITTE");
			this.battle_type = GET_NEXT_ACTOR;
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
		if (this.battle_type == WAIT_FOR_PLAYER) {
			this.main_menu.updateScreen();
		}
		this.drawArrow();
		this.drawStats();
		this.drawActionOrder();
	}
	
	private void sortActors() {
		Object_BattleActor actor;
		int i;
		this.action_order = new ArrayList<Integer>();
		for (Object_BattleActor b : this.ctx.actors) {
			print("ID: "+b.id);
		}
		while (this.action_order.size()<20) {
			print("Neuer Durchlauf:");
			java.util.Collections.sort(this.ctx.actors);
			print("this.ctx.actors: "+this.ctx.actors.toString());
			for (i=1; i<this.ctx.actors.size(); i++) {
				this.ctx.actors.get(i).wait = false;
			}
			actor = this.ctx.actors.get(0);
			if (actor.wait) {
				actor.wait = false;
				actor = this.ctx.actors.get(1);
			}
			print("Aktuelle Actor ID: "+actor.id);
			this.action_order.add(actor.id);
			actor.speed /= 2;
			if (actor.speed <= actor.maxSpeed*0.2) {
				actor.speed = (int) (actor.maxSpeed*0.9);
				actor.wait = true;
			}
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
		for (Object_BattleActor enemy : this.ctx.enemies) {
			x = enemy.sprite.x;
			y = enemy.sprite.y;
			x += 75;
			y += 15;
			String info = enemy.hp + "/" + enemy.maxHp;
			this.screen.drawString(info, x, y);
		}
	}
	
	private void drawActionOrder() {
		Object_BattleActor actor = null;
		this.screen.setColor(new Color(0, 77, 148));
		this.screen.fillRect(0, 440, 640, 40);
		int x = 10;
		int y = 470;
		boolean first = true;
		this.screen.setColor(new Color(255,255,255));
		for (int id : this.action_order) {
			for (Object_BattleActor b : this.ctx.actors) {
				if (b.id == id) {
					actor = b;
					break;
				}
			}
			this.screen.drawString(actor.name, x, y);
			if (first) {
				first = false;
				this.screen.setColor(new Color(150,150,150));
			}
			x += this.screen.getFontMetrics().stringWidth(actor.name) + 20;
		}
	}
	
	private void print(String s) {
		System.out.println(s);
	}

}
