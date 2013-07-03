import java.awt.Color;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;

import javax.imageio.ImageIO;



public class Scene_BattleSystem extends Abstract_Scene {
	
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
	
	public static final Random RANDOM = new Random(System.nanoTime());
	
	// Interne Daten
	
	private Object_BattleContext		ctx;
	private Abstract_Scene				previous_scene;
	private ArrayList<Integer>			action_order;
	private BufferedImage				background;
	private Object_BattleActor			current_actor;
	private Object_BattleEventManager	event_manager;
	
	Scene_BattleSystem(Object_BattleContext ctx, Abstract_Scene previous_scene, Object_Game game) {
		super(game);
		this.setCtx(ctx);
		this.setPreviousScene(previous_scene);
		this.setCurrentActor(null);
		this.setEventManager(new Object_BattleEventManager(this.game, this));
		this.action_order = new ArrayList<Integer>();
	}

	@Override
	public void onStart() {
		test_init();
		this.event_manager.setupOrder();
		this.event_manager.getNextActor();
	}

	@Override
	public void onExit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateData() {
		this.event_manager.updateData();
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
		this.event_manager.updateScreen();
		drawStats();
		drawArrow();
		drawActionOrder();
	}

	public static final float random(int pMin, int pMax) {
        return pMin + RANDOM.nextFloat() * (pMax - pMin);
	}

	public Object_BattleContext getCtx() {
		return ctx;
	}

	public void setCtx(Object_BattleContext ctx) {
		this.ctx = ctx;
	}

	public ArrayList<Integer> getActionOrder() {
		return action_order;
	}

	public void setActionOrder(ArrayList<Integer> action_order) {
		this.action_order = action_order;
	}

	public BufferedImage getBackground() {
		return background;
	}

	public void setBackground(BufferedImage background) {
		this.background = background;
	}

	public Object_BattleActor getCurrentActor() {
		return current_actor;
	}

	public void setCurrentActor(Object_BattleActor current_actor) {
		this.current_actor = current_actor;
	}

	public Object_BattleEventManager getEventManager() {
		return event_manager;
	}

	public void setEventManager(Object_BattleEventManager event_manager) {
		this.event_manager = event_manager;
	}

	public Abstract_Scene getPreviousScene() {
		return previous_scene;
	}

	public void setPreviousScene(Abstract_Scene previous_scene) {
		this.previous_scene = previous_scene;
	}
	
	// Private (teilweise schlecht implementierte) Methoden
	
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
	}
	
	private void drawArrow() {
		if (this.ctx.getPlayers().contains(this.current_actor)) {
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

	private void test_init() {
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
			b.speed += rnd_speed;
			if (b.speed <= 0) b.speed = (int) (b.maxSpeed*0.1);
			if (b.speed > b.maxSpeed) b.speed = (int) (b.maxSpeed*0.95);
		}
		this.soundmanager.playMidi("fight");
	}
	
}
