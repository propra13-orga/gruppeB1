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
	
	/*
	 * Interne Daten
	 */
	
	private Object_BattleContext		ctx;
	private Abstract_Scene				previous_scene;
	private ArrayList<Integer>			action_order;
	private BufferedImage				background;
	private Object_BattleActor			current_actor;
	private Window_Menu					main_menu;
	private Window_Menu					menu_enemy;
	private Window_Menu					menu_item;
	private Window_Menu					menu_skill;
	private Window_Menu					menu_player;
	private Object_BattleEventManager	event_manager;
	
	Scene_BattleSystem(Object_BattleContext ctx, Abstract_Scene previous_scene, Object_Game game) {
		super(game);
		this.ctx				= ctx;
		this.previous_scene		= previous_scene;
		this.current_actor		= null;
		this.event_manager		= new Object_BattleEventManager(this.game, this);
	}

	@Override
	public void onStart() {
		
	}

	@Override
	public void onExit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateData() {
		
	}

	@Override
	public void updateScreen() {
		
	}
	
	private void print(String s) {
		System.out.println(s);
	}

	public static final float random(int pMin, int pMax) {
        return pMin + RANDOM.nextFloat() * (pMax - pMin);
	}

}
