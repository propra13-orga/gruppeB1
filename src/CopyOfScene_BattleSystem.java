import java.awt.Color;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;

import javax.imageio.ImageIO;

public class CopyOfScene_BattleSystem extends Abstract_Scene {
	
	/*
	 * Die Positionen, auf denen sich Spieler und Gegner befinden koennen sind
	 * festgelegt
	 */
	
	public static final int[][] PLAYER_POSITIONS = {
		{480,170},
		{530,260},
		{515,340}
	};
	
	public static final int[][] ENEMY_POSITIONS = {
		{50,200},
		{20, 290},
		{60, 380}
	};
	
	/*
	 * Das Kampfsystem kann verschiedene Zustaende einnehmen, jedoch immer nur einen. Dementsprechend
	 * verhaelt sich die Methode updateData()
	 */

	public static final int GET_NEXT_ACTOR = 0;
	public static final int WAIT_FOR_PLAYER = 1;
	public static final int WAIT_FOR_ENEMY = 2;
	public static final int ANIMATION = 3;
	public static final int WIN = 4;
	public static final int LOSE = 5;
	public static final int UPDATE_DATA = 6;
	public static final Random RANDOM = new Random(System.nanoTime());
	
	/*
	 * Solange HALT auf true steht friert das gesamte System ein
	 * Wenn eine Animation stattfindet, tut das Kampfsystem nichts, ausser alle
	 * Grafikdaten upzudaten. Parallel dazu fragt es in jedem Frame ab, ob EXIT_ANIMATION
	 * true ist. Wenn ja, dann wird STATUS auf NEXT_STATUS gesetzt und das System faehrt fort
	 * Wenn in updateData ein Angriff gestartet wird und eine Animation ausgefuehrt wird
	 * soll das Kampfsystem solange warten, bis die Animation vorbei ist. Ausserdem
	 * sollen die HP Werte und so erst geupdatet werden, wenn der Angriff vorbei ist
	 * EXIT_UPDATE bricht die updateData Methode dann entsprechend ab
	 */
	
	public boolean HALT = false;
	public boolean EXIT_ANIMATION = false;
	public boolean EXIT_UPDATE = false;
	
	/*
	 * Interne Daten
	 */
	
	private Object_BattleContext		ctx;
	private Abstract_Scene				previous_scene;
	private ArrayList<Integer>			action_order;
	private BufferedImage				background;
	private Object_BattleActor			current_actor;
	private int							battle_type;
	private int							next_battle_type;
	private Window_Menu					main_menu;
	private Window_Menu					menu_enemy;
	private Window_Menu					menu_item;
	private Window_Menu					menu_skill;
	private Window_Menu					menu_player;
	private Window_Menu                 menu_network;
	
	CopyOfScene_BattleSystem(Object_BattleContext ctx, Abstract_Scene previous_scene, Object_Game game) {
		super(game);
		this.ctx = ctx;
		this.previous_scene = previous_scene;
		this.battle_type = GET_NEXT_ACTOR;
		this.current_actor = null;
	}

	@Override
	public void onStart() {
		test_init();
	}

	@Override
	public void onExit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateData() {
		
		if (HALT) return;
		
		checkWinLose();
		
		switch (this.battle_type) {
		
		case GET_NEXT_ACTOR:
			getNextActor();
			break;
			
		case WAIT_FOR_PLAYER:
			getPlayerInput();
			break;
			
		case WAIT_FOR_ENEMY:
			getEnemyInput();
			break;
			
		case ANIMATION:
			if (EXIT_ANIMATION) {
				EXIT_ANIMATION = false;
				this.battle_type = this.next_battle_type;
			}
			break;
			
		case WIN:
			win();
			break;
			
		case LOSE:
			lose();
			break;
			
		case UPDATE_DATA:
			//okay, nothing to to except updating player and enemy data
			break;
		}
		
		if (this.EXIT_UPDATE) {
			this.EXIT_UPDATE = false;
			return;
		}

		for (Object_BattleActor ba : this.ctx.getAliveActors()) {
			ba.sprite.updateData();
		}
	}

	@Override
	public void updateScreen() {
		this.screen.clearRect(0, 0, Object_Screen.SCREEN_W, Object_Screen.SCREEN_H);
		this.screen.drawImage(this.background, 0, 0, null);
		for (Object_BattleActor ba : this.ctx.getAliveActors()) {
			ba.sprite.updateScreen();
		}
		if (this.battle_type == WAIT_FOR_PLAYER) {
			this.main_menu.updateScreen();
		}
		this.drawArrow();
		this.drawStats();
		this.drawActionOrder();
	}
	
	private void removeFromOrder(Object_BattleActor actor) {
		while (this.action_order.contains(actor.id)) {
			int index = this.action_order.indexOf(actor.id);
			this.action_order.remove(index);
		}
	}
	
	private void sortActors() {
		this.action_order = new ArrayList<Integer>();
		while (this.action_order.size()<10) {
			addNextActor();
		}
	}
	
	private void getPlayerInput() {
		if (this.main_menu.isExecuted()) {
			this.main_menu.updateData();
		}
		else {
			this.main_menu.setupMenuPath();
			switch (this.main_menu.getCurrentChoice()) {
			
			case "null":
				//Normalerweise w�rde jetzt hier die Cursorposition
				//abgefragt, aber der einzige return command in main ist
				//Verteidugung, von daher ist klar, was gew�hlt wurde
				defend(this.current_actor);
				this.battle_type = WAIT_FOR_PLAYER;
				break;
				
			case "enemy":
				//Gegner wurde gew�hlt
				this.main_menu.nextMenu();
				Object_BattleActor chosen_enemy = this.ctx.getAliveEnemies().get(this.main_menu.getCurrentCursor());
				this.attack(this.current_actor, chosen_enemy);
				
				this.current_actor.sprite.attack(chosen_enemy.sprite);
				this.battle_type = ANIMATION;
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
	}
	
	private void defend(Object_BattleActor actor) {
		print(actor.name + " verteidigt sich!");
	}
	
	private void attack(Object_BattleActor actor, Object_BattleActor target) {
		//Berechne Schaden
		int damage = (2*actor.atk-target.def)/4;
		damage += random(-damage/20, damage/20);
		if (damage < 0) damage = (int) random(1, 5);
		target.hp -= damage;
		//Pruefe ob target tot ist
		if (target.hp <= 0) {
			actorDied(target);
		}
		//Speedabzug beim actor
		this.current_actor.speed -= 50;
		if (this.current_actor.speed <= 0) {
			this.current_actor.speed = this.current_actor.maxSpeed;
			this.current_actor.wait = true;
		}
		actor.sprite.attack(target.sprite);
		this.battle_type = ANIMATION;
		this.next_battle_type = UPDATE_DATA;
	}
	
	private void actorDied(Object_BattleActor actor) {
		actor.dead = true;
		actor.hp = 0;
		removeFromOrder(actor);
		if (actor.side == BattleSide.ENEMY) {
			//GEGNER GESTORBEN
			this.menu_enemy.clear();
			for (Object_BattleActor enemy : this.ctx.getAliveEnemies()) {
				if (enemy.attackable) {
					this.menu_enemy.addReturnCommand(enemy.name, false);
				}
			}
		}
		else {
			//SPIELER GESTORBEN
			this.menu_player.clear();
			for (Object_BattleActor player : this.ctx.getAlivePlayers()) {
				this.menu_player.addReturnCommand(player.name, false);
			}
		}
	}
	
	private void getEnemyInput() {
		int i = (int) random(0, this.ctx.getAlivePlayers().size()-1);
		Object_BattleActor target = this.ctx.getAlivePlayers().get(i);
		attack(this.current_actor, target);
		this.battle_type = GET_NEXT_ACTOR;
	}
	
	private void drawArrow() {
		if (this.current_actor.side == BattleSide.PLAYER) {
			Polygon p = new Polygon();
			int x = this.current_actor.sprite.getX()+50;
			int y = this.current_actor.sprite.getY()-20;
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
		for (Object_BattleActor b : this.ctx.getPlayers()) {
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
		for (Object_BattleActor enemy : this.ctx.getAliveEnemies()) {
			x = enemy.sprite.getX();
			y = enemy.sprite.getY();
			x += 75;
			y += 15;
			String info = enemy.hp + "/" + enemy.maxHp;
			this.screen.drawString(info, x, y);
		}
	}
	
	private void win() {
		print("GEWONNEN");
		this.game.quit();
	}
	
	private void lose() {
		print("VERLOREN");
		this.game.switchScene(this.previous_scene);
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
			for (Object_BattleActor b : this.ctx.getActors()) {
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
	
	private void getNextActor() {
		//Naechsten Actor bestimmten und aus action_order entfernen
		this.action_order.add(this.action_order.get(0));
		this.action_order.remove(0);
		int next_id = this.action_order.get(0);
		for (Object_BattleActor b : this.ctx.getActors()) {
			if (b.id == next_id) {
				this.current_actor = b;
				break;
			}
		}
		//Neues Element in der actino_order bestimmen
		addNextActor();
		if (this.ctx.getPlayers().contains(this.current_actor)) {
			this.main_menu.reset();
			this.battle_type = WAIT_FOR_PLAYER;
		}
		else {
			this.battle_type = WAIT_FOR_ENEMY;
		}
	}
	
	public void setContext(Object_BattleContext ctx) {
		this.ctx = ctx;
	}
	
	private void addNextActor() {
		ArrayList<Object_BattleActor> actors = this.ctx.getAliveActors();
		java.util.Collections.sort(actors);
		for (int i=1; i<actors.size(); i++) {
			actors.get(i).wait = false;
		}
		Object_BattleActor actor = actors.get(0);
		if (actor.wait) {
			actor.wait = false;
			actor = actors.get(1);
		}
		this.action_order.add(actor.id);
		actor.speed *= 0.7;
		if (actor.speed <= 0) {
			actor.speed = (int) (actor.maxSpeed*0.8) + (int) random(-actor.maxSpeed/10, actor.maxSpeed/10);
			actor.wait = true;
		}
	}
	
	private void checkWinLose() {
		if (this.ctx.getAliveEnemies().size() == 0) {
			this.battle_type = WIN;
		}
		if (this.ctx.getAlivePlayers().size() == 0) {
			this.battle_type = LOSE;
		}
	}
	
	private void print(String s) {
		System.out.println(s);
	}

	public static final float random(int pMin, int pMax) {
        return pMin + RANDOM.nextFloat() * (pMax - pMin);
    }
	
	private void test_init() {
		/*
		 * Initialisiere Menues
		 */
		this.main_menu = new Window_Menu(game, "main", 0, 0);
		this.menu_enemy = new Window_Menu(game, "enemy", 0, 0);
		this.menu_item = new Window_Menu(game, "item", 0, 0);
		this.menu_skill = new Window_Menu(game, "skills", 0, 0);
		this.menu_player = new Window_Menu(game, "player", 0, 0);
		this.menu_network = new Window_Menu(game, "network", 0, 0);
		Window_Menu.setMainMenu(this.main_menu);
		
		for (Object_BattleActor enemy : this.ctx.getEnemies()) {
			if (enemy.attackable) {
				this.menu_enemy.addReturnCommand(enemy.name);
			}
		}
		for (Object_BattleActor player : this.ctx.getPlayers()) {
			this.menu_player.addReturnCommand(player.name);
		}
		this.main_menu.addMenuCommand("Angriff", this.menu_enemy);
		this.main_menu.addMenuCommand("Skills", this.menu_skill);
		this.main_menu.addMenuCommand("Item", this.menu_item);
		this.main_menu.addMenuCommand("Network", this.menu_network);
		this.main_menu.addReturnCommand("Verteidigung");
		
		//Hier zu Testzwecken das Item/SKill-men� mit Eintr�gen f�llen
		this.menu_item.addMenuCommand("Kleiner Heiltrank", this.menu_player);
		this.menu_item.addMenuCommand("Gro�er Heiltrank", this.menu_player);
		this.menu_skill.addMenuCommand("Doppelschlag", this.menu_enemy);
		this.menu_skill.addMenuCommand("Feuerball", this.menu_enemy);
		this.menu_skill.addMenuCommand("Kleine Heilung", this.menu_enemy);
		this.menu_network.addMenuCommand("Multiplayer", this.menu_network);
		// !!! !!! !!! WICHTIG !!! !!! !!!
		//sp�ter wenn pageable menu umgesetzt ist k�nnen skills nach physischen, magischen und
		//heilenden skills sortiert werden
		
		//Skill und Itemmen� wird erst mit Eintr�gen gef�llt, wenn es aufgerufen wird,
		//weil dann der zugeh�rige Character feststeht (Player1 hat andere Skills als Player2)
		String path = "res/background/"+this.ctx.background+".png";
		background = new BufferedImage(640, 480, BufferedImage.TYPE_INT_ARGB);
		try {
			background.getGraphics().drawImage(ImageIO.read(new File(path)),0,0,null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (int k=0; k<this.ctx.getActors().size(); k++) {
			this.ctx.getActors().get(k).id = k;
		}
		
		int rnd_speed;
		for (Object_BattleActor b : this.ctx.getActors()) {
			rnd_speed = (int) CopyOfScene_BattleSystem.random(-(int) (b.maxSpeed*0.15), (int) (b.maxSpeed*0.15));
			print("random: "+rnd_speed);
			b.speed += rnd_speed;
			if (b.speed <= 0) b.speed = (int) (b.maxSpeed*0.1);
			if (b.speed > b.maxSpeed) b.speed = (int) (b.maxSpeed*0.95);
		}
		this.sortActors();
		int next_id = this.action_order.get(0);
		for (Object_BattleActor b : this.ctx.getActors()) {
			if (b.id == next_id) {
				this.current_actor = b;
				break;
			}
		}
		if (this.ctx.getPlayers().contains(this.current_actor)) {
			this.main_menu.reset();
			this.battle_type = WAIT_FOR_PLAYER;
		}
		else {
			this.battle_type = WAIT_FOR_ENEMY;
		}
		this.soundmanager.playMidi("fight");
	}

	@Override
	public void updateDataOnSwitching() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateScreenOnSwitching() {
		// TODO Auto-generated method stub
		
	}

}
