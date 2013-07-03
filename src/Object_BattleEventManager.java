import java.util.ArrayList;
import java.util.Random;

/*
 * Commands:
 * 
 * 
 * 		wait(frames)
 * 
 * Fuegt f�r die naechstes 'frames' Frames keine neuen Events zu 'executed' hinzu.
 * 
 * 		waitUntilDone
 * 
 * Nimmt solange keine neuen Events entgegen, bis 'executed' keine Elemente mehr enthaelt.
 * 
 * 		playAnimation(name, delay, x, y, parallel)
 * 
 * Spielt die Animation 'name' mit entsprechendem Delay an gegebenen Koordinaten ab. Parallel gibt an, ob
 * bis zum Ende der Ausfuehrung gewartet werden soll.
 * 
 * 		changeActorAnimation(actor, animation)
 * 
 * Wird in einem Frame ausgefuehrt. �ndert die Animation eines BattleActors.
 * 
 * 		moveActor(actor, x, y, delay, parallel)
 * 
 * Bewegt den entsprechenden Actor an die Position (x, y). delay und parallel wie oben erklaert.
 * 
 * 		playSound(name)
 * 
 * Spielt einen Sound ab.
 */

public class Object_BattleEventManager extends Abstract_Update {
	
	public static final Random RANDOM = new Random(System.nanoTime());
	
	private ArrayList<Object_BattleEvent>	queue;
	private ArrayList<Object_BattleEvent>	executed;
	private Scene_BattleSystem				battlesystem;

	Object_BattleEventManager(Object_Game game, Scene_BattleSystem battlesystem) {
		super(game);
		this.queue			= new ArrayList<Object_BattleEvent>();
		this.executed		= new ArrayList<Object_BattleEvent>();
		this.battlesystem	= battlesystem;
	}

	@Override
	public void updateData() {
		
		System.out.println("updateData() beginnt");
		
		//Aufsuehrungsliste aktualisieren
		if (this.queue.size() > 0) {
			
			System.out.println("Events in Warteschlange");
			
			if (this.executed.size() == 0) {
				
				System.out.println("Fuege Events hinzu");
				
				addNextEvents();
			}
			else if ((boolean) this.executed.get(this.executed.size()-1).getAttribute("parallel")) {
				
				System.out.println("Fuege Events hinzu");
				
				//In der Warteschlange befinden sich Events und das letzte Event in
				//der Ausfuehrungsliste kann parallel ausgefuehrt werden
				addNextEvents();
			}
		}
		//Alle aktuellen Events ausfuehren
		for (Object_BattleEvent e : this.executed) {
			switch ((String) e.getAttribute("type")) {
			case "setupOrder":
				setupOrderHandler(e);
				break;
			case "getNextActor":
				getNextActorHandler(e);
				break;
			case "getPlayerInput":
				getPlayerInputHandler(e);
				break;
			case "getEnemyInput":
				getEnemyInputHandler(e);
				break;
			case "wait":
				waitHandler(e);
				break;
			case "waitUntilDone":
				waitUntilDoneHandler(e);
				break;
			case "playAnimation":
				playAnimationHandler(e);
				break;
			case "changeActorAnimation":
				changeActorAnimationHandler(e);
				break;
			case "moveActor":
				moveActorHandler(e);
				break;
			case "playSound":
				playSoundHandler(e);
				break;
			}
		}
		//Alle beendeten Events aus Ausfuehrungsliste entfernen
		ArrayList<Integer> remove = new ArrayList<Integer>();
		for (Object_BattleEvent e : this.executed) {
			if (e.isDone()) {
				remove.add(this.executed.indexOf(e));
			}
		}
		int removed = 0;
		for (int i : remove) {
			System.out.println("Entferne Objekt Nr.: "+i);
			this.executed.remove(i-removed);
			removed++;
		}
	}

	@Override
	public void updateScreen() {
		for (Object_BattleEvent e : this.executed) {
			switch ((String) e.getAttribute("type")) {
			case "getPlayerInput":
				getPlayerInputScreenHandler(e);
				break;
			}
		}
	}
	
	
	/*
	 * 
	 * 
	 * 				Verfuegbare Befehle
	 * 
	 * 
	 */
	
	
	public void setupOrder() {
		Object_BattleEvent e = new Object_BattleEvent("setupOrder");
		this.queue.add(e);
	}
	
	public void getNextActor() {
		Object_BattleEvent e = new Object_BattleEvent("getNextActor");
		this.queue.add(e);
	}
	
	public void getPlayerInput(Object_BattleActor actor) {
		Object_BattleEvent e = new Object_BattleEvent("getPlayerInput");
		
		Window_Menu menu_main = new Window_Menu(this.game, "main");
		Window_Menu menu_select_enemy = new Window_Menu(this.game, "select_enemy");
		Window_Menu menu_select_player = new Window_Menu(this.game, "select_player");
		Window_Menu menu_select_item = new Window_Menu(this.game, "select_item");
		Window_Menu menu_select_skill = new Window_Menu(this.game, "select_skill");
		
		menu_main.addMenuCommand("Angriff", menu_select_enemy);
		menu_main.addMenuCommand("Skill", menu_select_skill);
		menu_main.addMenuCommand("Item", menu_select_item);
		menu_main.addReturnCommand("Verteidigen");
		
		//Kommandos fuer Auswahlmenues von Spielern und Gegnern werden erst im Kampfverlauf
		//eingefuegt. Dies ist sinnvoll, da die Liste bei dem Tod von einem Kampfteilnehmer
		//sowieso aktualisiert werden muss und bei einem Item mit wiederbelebender Wirkung
		//muss man auch schon gestorbene Mitspieler auswaehlen koennen, was sonst sinnlos ist
		
		//Zu Testzwecken wird hier das Skillmenue explizit gefuellt, spaeter ueber actor.getSkills()
		String skills[] = {"Feuerball", "Flammenmeer", "Schockfrost" };
		for (int i=0; i<skills.length; i++) {
			menu_select_skill.addMenuCommand(skills[i], menu_select_enemy);
		}
		menu_select_skill.disableCommand(2);
		menu_main.topLeft();
		Window_Menu.setMainMenu(menu_main);
		
		e.setAttribute("actor", actor);
		e.setAttribute("menu_main", menu_main);
		e.setAttribute("menu_select_enemy", menu_select_enemy);
		e.setAttribute("menu_select_player", menu_select_player);
		e.setAttribute("menu_select_item", menu_select_item);
		e.setAttribute("menu_select_skill", menu_select_skill);
		e.setAttribute("parallel", false);
		this.queue.add(e);
	}
	
	public void getEnemyInput(Object_BattleActor actor) {
		Object_BattleEvent e = new Object_BattleEvent("getEnemyInput");
		this.queue.add(e);
	}
	
	public void wait(int time) {
		Object_BattleEvent e = new Object_BattleEvent("wait");
		e.setAttribute("time", time);
		e.setAttribute("parallel", false);
		this.queue.add(e);
	}
	
	public void waitUntilDone() {
		Object_BattleEvent e = new Object_BattleEvent("waitUntilDone");
		e.setAttribute("parallel", false);
		this.queue.add(e);
	}
	
	public void playAnimation(String name, int delay, int x, int y, boolean parallel) {
		Object_BattleEvent e = new Object_BattleEvent("playAnimation");
		e.setAttribute("x", x);
		e.setAttribute("y", y);
		e.setAttribute("delay", delay);
		e.setAttribute("parallel", parallel);
		e.setAttribute("id", -1);
		this.queue.add(e);
	}
	
	public void changeActorAnimation(Object_BattleActor actor, int animation) {
		Object_BattleEvent e = new Object_BattleEvent("changeActorAnimation");
		e.setAttribute("actor", actor);
		e.setAttribute("animation", animation);
	}
	
	public void moveActor(Object_BattleActor actor, int x, int y, int delay, boolean parallel) {
		Object_BattleEvent e = new Object_BattleEvent("moveActor");
		e.setAttribute("actor", actor);
		e.setAttribute("x", x);
		e.setAttribute("y", y);
		e.setAttribute("delay", delay);
		e.setAttribute("parallel", parallel);
	}
	
	public void playSound(String name) {
		Object_BattleEvent e = new Object_BattleEvent("playSound");
		e.setAttribute("name", name);
	}
	
	
	/*
	 * 
	 * 				Private Datenhandlermethoden
	 * 
	 */
	
	
	private void setupOrderHandler(Object_BattleEvent e) {
		this.battlesystem.setActionOrder(new ArrayList<Integer>());
		while (this.battlesystem.getActionOrder().size()<20) {
			addNextActor();
		}
		e.finish();
	}
	
	private void getNextActorHandler(Object_BattleEvent e) {
		ArrayList<Integer> tmp_order = this.battlesystem.getActionOrder();
		tmp_order.add(tmp_order.get(0));
		tmp_order.remove(0);
		int next_id = tmp_order.get(0);
		for (Object_BattleActor b : this.battlesystem.getCtx().getActors()) {
			if (b.id == next_id) {
				b.is_new = true;
				this.battlesystem.setCurrentActor(b);
				break;
			}
		}
		//Neues Element in der action_order bestimmen
		addNextActor();
		if (this.battlesystem.getCtx().getPlayers().contains(this.battlesystem.getCurrentActor())) {
			this.getPlayerInput(this.battlesystem.getCurrentActor());
		}
		else {
			this.getEnemyInput(this.battlesystem.getCurrentActor());
		}
		e.finish();
	}
	
	private void getPlayerInputHandler(Object_BattleEvent e) {
		System.out.println("playerInput Handler");
		Window_Menu main = (Window_Menu) e.getAttribute("menu_main");
		if (main.isExecuted()) {
			main.updateData();
		}
		else {
			main.setupMenuPath();
			switch (main.getCurrentCursor()) {
			case 0:								//Angriff
				System.out.println("Angriff!");
				break;
			}
			e.finish();
		}
	}
	
	private void getEnemyInputHandler(Object_BattleEvent e) {
		this.getNextActor();
		e.finish();
	}
	
	private void waitHandler(Object_BattleEvent e) {
		e.setAttribute("time", ((int)e.getAttribute("time"))-1);
		if ((int)e.getAttribute("time") <= 0) {
			e.finish();
		}
	}
	
	private void waitUntilDoneHandler(Object_BattleEvent e) {
		if (this.executed.size() == 0) {
			e.finish();
		}
	}
	
	private void playAnimationHandler(Object_BattleEvent e) {
		long id = (long) e.getAttribute("id");
		if (id == -1) {
			this.animationmanager.playAnimation(
					(String)	e.getAttribute("name"),
					(int)		e.getAttribute("delay"),
					(int)		e.getAttribute("x"),
					(int)		e.getAttribute("y"));
		}
		else if (!this.animationmanager.isAnimationExecuted(id)) {
			e.finish();
		}
	}
	
	private void changeActorAnimationHandler(Object_BattleEvent e) {
		((Object_BattleActor) e.getAttribute("actor")).sprite.animation_type = (int) e.getAttribute("animation");
		e.finish();
	}
	
	private void moveActorHandler(Object_BattleEvent e) {
		//ACHTUNG: DELAY WIRD NOCH NICHT BEACHTET
		int src_x = ((Object_BattleActor) e.getAttribute("actor")).sprite.x;
		int src_y = ((Object_BattleActor) e.getAttribute("actor")).sprite.y;
		int dest_x = (int) e.getAttribute("x");
		int dest_y = (int) e.getAttribute("y");
		if (src_x == dest_x && src_y == dest_y) {
			e.finish();
			return;
		}
		if (src_x < dest_x) src_x++;
		if (src_x > dest_x) src_x--;
		if (src_y < dest_y) src_y++;
		if (src_y > dest_y) src_y--;
	}
	
	private void playSoundHandler(Object_BattleEvent e) {
		this.soundmanager.playSound((String)e.getAttribute("name"));
		e.finish();
	}
	
	/*
	 * 
	 * 			Private Screenhandlermethoden
	 * 			ACHTUNG, Screenhandler gibt es nur fuer bestimmte Events, deswegen
	 * 			wird im Executionloop der Eventtyp abgefrafr und die entsprechende
	 * 			Methode nur ausgerufen, wenn es sie tatsaechlich gibt (das muss man
	 * 			momentan als Programmierer wissen...)
	 * 
	 */
	
	private void getPlayerInputScreenHandler(Object_BattleEvent e) {
		Window_Menu menu = (Window_Menu) e.getAttribute("menu_main");
		menu.updateScreen();
	}
	
	//Andere private Methoden
	
	private void addNextEvents() {
		Object_BattleEvent current;
		while (this.queue.size() > 0) {
			current = this.queue.get(0);
			this.queue.remove(0);
			this.executed.add(current);
			if (!((boolean) current.getAttribute("parallel"))) {
				break;
			}
		}
	}
	
	private void addNextActor() {
		ArrayList<Object_BattleActor> actors = this.battlesystem.getCtx().getAliveActors();
		java.util.Collections.sort(actors);
		for (int i=1; i<actors.size(); i++) {
			actors.get(i).wait = false;
		}
		Object_BattleActor actor = actors.get(0);
		if (actor.wait) {
			actor.wait = false;
			actor = actors.get(1);
		}
		this.battlesystem.getActionOrder().add(actor.id);
		actor.speed *= 0.7;
		if (actor.speed <= 0) {
			actor.speed = (int) (actor.maxSpeed*0.8) + (int) random(-actor.maxSpeed/10, actor.maxSpeed/10);
			actor.wait = true;
		}
	}
	
	// Statische Methoden
	
	public static final int random(int pMin, int pMax) {
        return (int) (pMin + RANDOM.nextFloat() * (pMax - pMin));
	}
	
}
