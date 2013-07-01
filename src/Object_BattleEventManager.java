import java.util.ArrayList;

/*
 * Commands:
 * 
 * 
 * 		wait(frames)
 * 
 * Fuegt für die naechstes 'frames' Frames keine neuen Events zu 'executed' hinzu.
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
 * Wird in einem Frame ausgefuehrt. Ändert die Animation eines BattleActors.
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
		//Aufsuehrungsliste aktualisieren
		if (this.queue.size() > 0) {
			if ((boolean) this.executed.get(this.executed.size()-1).getAttribute("parallel")) {
				//In der Warteschlange befinden sich Events und das letzte Event in
				//der Ausfuehrungsliste kann parallel ausgefuehrt werden
				addNextEvents();
			}
		}
		//Alle aktuellen Events ausfuehren
		for (Object_BattleEvent e : this.executed) {
			switch ((String) e.getAttribute("type")) {
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
		for (Object_BattleEvent e : this.executed) {
			if (e.isDone()) {
				this.executed.remove(this.executed.indexOf(e));
			}
		}
	}

	@Override
	public void updateScreen() {
		// TODO Auto-generated method stub
		
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
	
	//Private handler Methoden
	
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
	
}
