import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;


public class Scene_Level extends Scene {
	private Map current_map;
	private EntityManager eManager;
	private MovementSystem movementSystem;
	private RenderSystem renderSystem;

	public Scene_Level(Game g) {
		super(g);
		this.current_map = new Map("map1", this);
		
		this.eManager = new EntityManager(this);
		this.movementSystem = new MovementSystem(this,game.getKeyHandler());
		this.renderSystem = new RenderSystem(this,game.getScreen());
		
		Entity player = new Entity("Tollkühner Held",eManager);
		new CompMovement(player,movementSystem,2,5,0,0,false,true);
		new CompSprite(player,renderSystem,"character_2");
		new CompControls(player,movementSystem);
		new CompCamera(player,renderSystem);
		player.init();
		eManager.setPlayer(player);
		
		Entity enemy = new Entity("Gegner",eManager);
		new CompMovement(enemy,movementSystem,7,5,0,0,false,true);
		new CompSprite(enemy,renderSystem,"character_1");
		enemy.init();
	}
	
	@Override
	public void update() {
		this.eManager.update();
		this.movementSystem.update();
		this.renderSystem.update();
	}
	
	public Map getCurrentMap() {
		return this.current_map;
	}
	
	

}

/*
 * Besondere Klassen.
 */

abstract class ComponentSystem {
	protected Scene scene;
	protected String[] types;
	protected List<Component> components;
	protected Hashtable<String,List<Entity>> entitiesByType;
	
	public ComponentSystem(Scene scene, String ...types) {
		this.scene = scene;
		this.components = new LinkedList<Component>();
		this.types = types;
		this.entitiesByType = new Hashtable<String,List<Entity>>();
		for (String type : this.types){
			this.entitiesByType.put(type,new LinkedList<Entity>());
		}
	}
	
	abstract public void update();
	
	public void register(Component component) {
		this.components.add(component);
		this.entitiesByType.get(component.type).add(component.entity);
	}
	
	public void deregister(Component component) {
		this.components.remove(component);
		this.entitiesByType.get(component.type).remove(component);
	}
	
	public List<Entity> getEntitiesByType(String type) {
		return this.entitiesByType.get(type);
	}
}

class RenderSystem extends ComponentSystem {
	protected Screen screen;
	protected int[] screenPoint;
	
	public RenderSystem(Scene scene, Screen screen) {
		super(scene,"renderable","sprite","camera");
		this.screen = screen;
		this.screenPoint = new int[2];
		this.screenPoint[0] = 0;
		this.screenPoint[1] = 0;
	}
	
	@Override
	public void update() {
		screen.getBuffer().getGraphics().clearRect(0, 0, screen.SCREEN_W, screen.SCREEN_H);
		this.drawLowMap();
		this.animations();
		this.check_scrolling();
		this.drawSprites();
	}
	
	
	private void animations() {
		for (Entity entity : this.getEntitiesByType("sprite")) {
			CompSprite compSprite = (CompSprite) entity.getComponent("sprite");
			CompMovement compMovement = (CompMovement) entity.getComponent("movement");
			
			if (compMovement.moving) compSprite.movecounter += compSprite.move_distance;
			if (compSprite.movecounter >= Map.TILESIZE) {
				compSprite.movecounter = 0;
				if (compSprite.old_animation == CompSprite.ANIMATION_LEFT) {
					compSprite.old_animation = CompSprite.ANIMATION_RIGHT;
				}
				else {
					compSprite.old_animation = CompSprite.ANIMATION_LEFT;
				}
				compMovement.setMoveable();
			}
		}
	}
	
	private void drawSprites() {
		Map map = ((Scene_Level) this.scene).getCurrentMap();
		for (Entity entity : this.getEntitiesByType("sprite")) {
			CompSprite compSprite = (CompSprite) entity.getComponent("sprite");
			CompMovement compMovement = (CompMovement) entity.getComponent("movement");
			if (compSprite.movecounter > 0) {
				//ANIMATION
				if (compSprite.movecounter < Map.TILESIZE-10) {
					if (compSprite.old_animation == Sprite.ANIMATION_LEFT) compSprite.animation = Sprite.ANIMATION_RIGHT;
					else compSprite.animation = Sprite.ANIMATION_LEFT;
				}
				else {
					compSprite.animation = Sprite.ANIMATION_MIDDLE;
				}
			}
			
			int new_x = -100;
			int new_y = -100;
			
			if (map.scrolling) {
				switch (compMovement.orientation) {
				case 2: //DOWN
				case 1: //UP
					new_x = (compMovement.getOldX()-this.screenPoint[0])*Map.TILESIZE;
					new_y = (Screen.VISIBLE_TILES_Y/2)*Map.TILESIZE - Map.TILESIZE;
					break;
				case 3: //LEFT
				case 4: //RIGHT
					new_x = (Screen.VISIBLE_TILES_X/2)*Map.TILESIZE;
					new_y = (compMovement.getOldY()-this.screenPoint[1])*Map.TILESIZE - Map.TILESIZE;
					break;
				}
			}
			else {
				new_x = (compMovement.getOldX()-this.screenPoint[0])*Map.TILESIZE;
				new_y = (compMovement.getOldY()-this.screenPoint[1])*Map.TILESIZE - Map.TILESIZE;
				//Falls der Character sich nciht bewegt ist movecounter 0 und nichts
				//�ndert sich hier!
				switch (compMovement.orientation) {
				case 1: //UP
					new_y -= compSprite.movecounter;
					break;
				case 2: //DOWN
					new_y += compSprite.movecounter;
					break;
				case 3: //LEFT
					new_x -= compSprite.movecounter;
					break;
				case 4: //RIGHT
					new_x += compSprite.movecounter;
				}
			}
			this.screen.getBuffer().getGraphics().drawImage(
					compSprite.getImage(),
					new_x,
					new_y,
					this.screen);
		}
	}
	
	private void check_scrolling() {
		//Berechnet, ob die Karte gescrollt werden muss
		Entity focus = this.getEntitiesByType("camera").get(0);
		CompMovement compMovement = (CompMovement) focus.getComponent("movement");
		
		Map map = ((Scene_Level) this.scene).getCurrentMap();
		
		boolean scrolling = false;
		
		// TODO Scrollt aus irgendeinem Grund viel zu weit.
		if (compMovement.isMoving()) {
			
			switch(compMovement.orientation) {
			case 1: //UP
				if (map.getHeight()-compMovement.getY()-1 <= Screen.VISIBLE_TILES_Y/2) break;
				if (compMovement.getY() >= Screen.VISIBLE_TILES_Y/2) {
					scrolling = true;
					this.screenPoint[1]--;
				}
				break;
			case 2: //DOWN
				if (compMovement.getY() <= Screen.VISIBLE_TILES_Y/2) break;
				if (map.getHeight()-compMovement.getY() > Screen.VISIBLE_TILES_Y/2) {
					scrolling = true;
					screenPoint[1]++;
				}
				break;
			case 3: //LEFT
				if (map.getWidth()-compMovement.getX() <= Screen.VISIBLE_TILES_X/2) break;
				if (compMovement.getX() >= Screen.VISIBLE_TILES_X/2) {
					scrolling = true;
					screenPoint[0]--;
				}
				break;
			case 4: //RIGHT
				if (compMovement.getX() <= Screen.VISIBLE_TILES_X/2) break;
				if (map.getWidth()-compMovement.getX() >= Screen.VISIBLE_TILES_X/2) {
					scrolling = true;
					screenPoint[0]++;
				}
				break;
			}
		}
		map.scrolling = scrolling;
	}
	
	private void drawLowMap() {
		Entity focus = this.getEntitiesByType("camera").get(0);
		CompMovement compMovement = (CompMovement) focus.getComponent("movement");
		CompSprite compSprite = (CompSprite) focus.getComponent("sprite");
		
		Map map = ((Scene_Level) this.scene).getCurrentMap();
		//Zeichnet die LowMap (siehe Map.drawLowMapImage)
		//Hier wird jedoch scrolling miteinbezogen, das fertige Bild
		//wird also nur noch korrekt ausgerichtet
		int map_x = -this.screenPoint[0]*Map.TILESIZE;
		int map_y = -this.screenPoint[1]*Map.TILESIZE;
		
		if (compMovement.isMoving() && map.scrolling){
			switch (compMovement.orientation) {
			case 1: //UP
				map_y -= Map.TILESIZE - compSprite.movecounter;
				break;
			case 2: //DOWN
				map_y += Map.TILESIZE - compSprite.movecounter;
				break;
			case 3: //LEFT
				map_x -= Map.TILESIZE - compSprite.movecounter;
				break;
			case 4: //RIGHT
				map_x += Map.TILESIZE - compSprite.movecounter;
				break;
			}
		}
		this.screen.getBuffer().getGraphics().drawImage(
				map.getLowMapImage(),
				map_x,
				map_y,
				this.screen);
	}
}

class MovementSystem extends ComponentSystem {
	protected KeyHandler keyHandler;
	
	public MovementSystem(Scene scene, KeyHandler keyHandler) {
		super(scene,"controls","movement");
		this.keyHandler = keyHandler;
	}
	
	@Override
	public void update() {
		// Erst die Eingaben behandeln.
		for (Entity entity : this.getEntitiesByType("controls")) {
			if (entity.hasComponent("movement")) {
				CompMovement compMovement = (CompMovement) entity.getComponent("movement");
				if (entity.isPlayer()) {
					this.handlePlayerInput(compMovement);
				}
				else {
					/*
					 * Hier muss dann die Gegnerbewegung behandelt werden. Meine
					 * Idee ist, dies über einen "Pseudo-KeyHandler" zu machen,
					 * dem dieselben Werte zuweisbar sind, wie dem echten.
					 * Am besten ginge das über ein Interface mit Funktionen wie
					 * "boolean getUp()", welches dann vom echten und vom
					 * unechten KeyHandler implementiert wird.
					 */
				}
			}
		}
		
		// Nun die Entitäten bewegen.
		for (Entity entity : this.getEntitiesByType("movement")) {
			this.moveEntity(entity);
		}
		
		// Jetzt solange illegale Kollisionen behandeln, bis alle behoben sind.
		// Schleife läuft höchstens N-mal durch, wobei N = Anzahl Entitäten.
		// Die Laufzeitkomplexität liegt aber (leider) bei höchstens N^3!
		while(true) {
			List<ECollision> illegalCollisions = getIllegalCollisions();
			if (illegalCollisions.isEmpty()) break;
			this.resolveIllegalCollisions(illegalCollisions);
		}
	}
	
	
	private void resolveIllegalCollisions(List<ECollision> illegalCollisions) {
		for (ECollision eCollision : illegalCollisions) {
			CompMovement compMovement1 = (CompMovement) eCollision.getActor().getComponent("movement");
			CompMovement compMovement2 = (CompMovement) eCollision.getUndergoer().getComponent("movement");
			
			this.resetPosition(compMovement1);
			this.resetPosition(compMovement2);
		}
	}
	
	private void resetPosition(CompMovement compMovement) {
		compMovement.setX(compMovement.getOldX());
		compMovement.setY(compMovement.getOldY());
		compMovement.setdX(0);
		compMovement.setdY(0);
		compMovement.unsetMoving();
		compMovement.setMoveable();
	}
	
	private List<ECollision> getIllegalCollisions() {
		List<ECollision> illegalCollisions = new LinkedList<ECollision>();
		for (int i = 0; i < this.getEntitiesByType("movement").size(); i++) {
			CompMovement compMovement1 = (CompMovement) this.getEntitiesByType("movement").get(i).getComponent("movement");
			if (compMovement1.collidable) {
				for (int j = 0; j < i; j++) {
					if (i != j) {
						CompMovement compMovement2 = (CompMovement) this.getEntitiesByType("movement").get(j).getComponent("movement");
						if (compMovement1.x == compMovement2.x 
								&& compMovement1.y == compMovement2.y) {
							if (compMovement1.collidable 
									&& this.isIllegalCollision(compMovement1, compMovement2)) {
								illegalCollisions.add(new ECollision(compMovement1.entity, compMovement2.entity));
							}							
						}
					}
				}
			}
		}
		return illegalCollisions;
	}
	
	private class ECollision extends Event {
		public ECollision(Entity actor, Entity undergoer) {
			super(actor,undergoer);
		}
	}
	
	private boolean isIllegalCollision(CompMovement compMovement1, CompMovement compMovement2) {
		if (!compMovement1.walkable && !compMovement2.walkable) return true;
		return false;
	}
	
	private boolean walkable(int x, int y) {
		return ((Scene_Level) this.scene).getCurrentMap().isPassable(x, y);
	}
	
	private void moveEntity(Entity entity) {
		CompMovement compMovement = (CompMovement) entity.getComponent("movement");
		if (compMovement.isMoveable()) {
			int dx = compMovement.getdX();
			int dy = compMovement.getdY();
			compMovement.addToX(dx);
			compMovement.addToY(dy);
			if (dx != 0 || dy != 0) {
				compMovement.setMoving();
				compMovement.unsetMoveable();
			}
			else compMovement.unsetMoving();
		}
	}
	
	private void handlePlayerInput(CompMovement compMovement) {
		if (compMovement.isMoveable()) {
			int dx = 0;
			int dy = 0;
			switch(this.keyHandler.getLast()) {
			case 1: // UP
				compMovement.setOrientation(1);
				dx = 0;
				dy = -1;
				break;
			case 2: // DOWN
				compMovement.setOrientation(2);
				dx = 0;
				dy = 1;
				break;
			case 3: // LEFT
				compMovement.setOrientation(3);
				dx = -1;
				dy = 0;
				break;
			case 4: // RIGHT
				compMovement.setOrientation(4);
				dx = 1;
				dy = 0;
				break;
			default:
				dx = 0;
				dy = 0;
			}
			int newX = compMovement.getX()+dx;
			int newY = compMovement.getY()+dy;
			if (this.walkable(newX, newY)) {
				compMovement.setdX(dx);
				compMovement.setdY(dy);
			}
			else {
				compMovement.setdX(0);
				compMovement.setdY(0);
			}
		}
			
	}
}



abstract class Component {
	protected ComponentSystem system;
	protected String type;
	protected Entity entity;
	
	public Component(String type, Entity entity, ComponentSystem system) {
		this.type = type;
		this.system = system;
		this.entity = entity;
		entity.addComponent(this);
	}
	
	public void init() {
		this.system.register(this);
	}
	
	public void deinit() {
		this.system.deregister(this);
	}
}

class CompMovement extends Component {
	public int x, y, dx, dy;
	public int orientation;
	public boolean moving, walkable, collidable, moveable;
	
	public CompMovement(Entity entity, ComponentSystem system,
			int x, int y, int dx, int dy,
			boolean walkable, boolean collidable) {
		super("movement",entity,system);
		this.x = x;
		this.y = y;
		this.dx = dx;
		this.dy = dy;
		this.orientation = 1;
		this.walkable = walkable;
		this.collidable = collidable;
		this.moveable = true;
	}
	
	public CompMovement(Entity entity, ComponentSystem system,
			int x, int y) {
		this(entity,system,x,y,0,0,true,false);
	}
	
	// Getters
	
	public int getX() { return this.x; }
	public int getY() { return this.y; }
	public int getdX() { return this.dx; }
	public int getdY() { return this.dy; }
	public int getOldX() { return this.x-this.dx; }
	public int getOldY() { return this.y-this.dy; }
	public int getOrientation() { return this.orientation; }
	
	public boolean isMoving() { return this.moving; }
	public boolean isWalkable() { return this.walkable; }
	public boolean isCollidable() { return this.collidable; }
	public boolean isMoveable() { return this.moveable; }
	
	// Setters
	
	public void setX(int x) { this.x = x; }
	public void setY(int y) { this.y = y; }
	public void setdX(int dx) { this.dx = dx; }
	public void setdY(int dy) { this.dy = dy; }
	public void addToX(int dx) { this.x += dx; }
	public void addToY(int dy) { this.y += dy; }
	public void setOrientation(int d) { this.orientation = d; }
	
	public void setMoving() { this.moving = true; }
	public void setMoveable() { this.moveable = true; }
	
	public void unsetMoving() { this.moving = false; }
	public void unsetMoveable() { this.moveable = false; }
}

class CompCamera extends Component {
	public CompCamera(Entity entity, ComponentSystem system) {
		super("camera",entity,system);
	}
}

class CompControls extends Component {
	public CompControls(Entity entity, ComponentSystem system) {
		super("controls",entity,system);
	}
}

class CompRenderable extends Component {
	public Sprite sprite;
	public CompRenderable(Entity entity, ComponentSystem system,
			String spritePath) {
		super("renderable",entity,system);
		sprite = new Sprite(spritePath);
	}
	
	public int getMovecounter() {
		return this.sprite.movecounter;
	}
	
	public boolean isVisible() {
		return this.sprite.visible;
	}
	
	public void setVisible() {
		this.sprite.visible = true;
	}
	
	public void setInvisible() {
		this.sprite.visible = false;
	}
	
	public void setMovecounter(int i) {
		this.sprite.movecounter = i;
	}
	
	public void addToMovecounter(int i) {
		sprite.movecounter += i;
	}
}

class EntityManager {
	protected Scene scene;
	protected List<Entity> entities;
	protected Entity player;
	
	public EntityManager(Scene scene) {
		this.scene = scene;
		this.entities = new LinkedList<Entity>();
	}
	
	public void update() {
		
	}
	
	public void register(Entity entity) {
		if (this.entities.contains(entity)) this.entities.add(entity);
	}
	
	public void deregister(Entity entity) {
		try {
			entity.deinit();
			this.entities.remove(entity);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public void setPlayer(Entity entity) {
		this.player = entity;
	}
	
	public boolean isPlayer(Entity entity) {
		if (this.player.equals(entity)) return true;
		return false;
	}
}
class Entity {
	protected String name;
	protected EntityManager manager;
	private Hashtable<String,Component> components;;
	protected boolean active = false;
	
	public Entity(String name, EntityManager manager) {
		this.name = name;
		this.manager = manager;
		this.components = new Hashtable<String,Component>();
	}
	
	public void init() {
		for (Component component : this.components.values()) {
			component.init();
		}
		this.manager.register(this);
		this.active = true;
	}
	
	public void deinit() {
		for (Component component : this.components.values()) {
			component.deinit();
		}
		this.active = false;
	}
	
	public void addComponent(Component component) {
		this.components.put(component.type, component);
	}
	
	public boolean hasComponent(String type) {
		if (this.components.containsKey(type)) return true;
		return false;
	}
	
	public Component getComponent(String type) {
		return this.components.get(type);
	}
	
	public boolean isPlayer() {
		return this.manager.isPlayer(this);
	}
}

abstract class Event {
	protected Entity actor;
	protected Entity undergoer;
	
	public Event(Entity actor, Entity undergoer) {
		this.actor = actor;
		this.undergoer = undergoer;
	}
	
	public Entity getActor() {
		return this.actor;
	}
	
	public Entity getUndergoer() {
		return this.undergoer;
	}
}