import java.util.ArrayList;


public class Scene_Map extends Scene {

	int[] screen_point; //Das Tile, welches sich relativ zum Player
						//in der oberen linken Ecke befindet
	
	private Sprite player;
	private Map current_map;
	private boolean skip_moving;
	private ArrayList<Sprite> sprites;
	private Map map;
	private Sprite main_sprite;
	//private boolean menu_access;
	
	Scene_Map(Game g) {
		super(g);
		//Die meisten initialisierungen müssen noch ausgelagert werden
		player = new Sprite("character_2", 2, 5);
		current_map = new Map("map1", this);
		sprites = new ArrayList<Sprite>();
		screen_point = new int[2];
		screen_point[0] = 0;
		screen_point[1] = 0;
		setMap(current_map);
		addSprite(player);
		setFocusOn(player);
		//menu_access = true;
	}
	
	public void update() {
		updateLogic();
		updateScreen();
	}
	
	private void updateLogic() {
		//Überprüft, ob eine Bewegungstaste gedrückt wurde und
		//bearbeitet die Koordinaten des Characters entsprechend
		//Initialisiert (wenn nötig) auch eine Bewegungsanimation
		
		//Prüfe, ob Menü aufgerufen werden kann, wenn ja rufe Menü
		//Gegebenenfalls auf
		if (check_menu()) return;
		//if (!menu_access && !game.getKeyHandler().get_escape()) {
		//	System.out.println("Menu wieder freigegeben");
		//	menu_access = true;
		//}
		
		//Falls, der Spieler gerade steuerbar ist (also nicht schon in einer
		//Bewegungsphase), dann prüfe jetzt, ob er gestuert wurde
		if (!skip_moving) {
		
			//Alte Koordinaten des Characters speichern um zu
			//prüfen, ob eine Bewegung animiert werden muss
			player.save_position();
			check_walking();
			current_map.scrolling = false;
			//Hat der Character sich bewegt
			if (player.get_old_x() != player.pos_x || player.get_old_y() != player.pos_y) {
				//Spieler hat sich bewegt!
				player.moving = true;
				skip_moving = true;
				//Scrolling?
				check_scrolling();
			}
		}
		
		//Animiere den Spieler
		player_animation();
	}
	
	private void updateScreen() {
		//Zuerst alles löschen
		game.getScreen().getBuffer().getGraphics().clearRect(
				0,
				0,
				Screen.SCREEN_W,
				Screen.SCREEN_H);
		//Den teil der Map anzeigen, der "unter" den Sprites liegt (Boden, etc)
		drawLowMap();
		//Sprites zeichnen
		drawSprites();
	}
	
	
	///////////////////////////////////////////////////////////////////////////////////////////
	//								Scene interne Methoden
	///////////////////////////////////////////////////////////////////////////////////////////
	
	private boolean check_menu() {
		//if (!menu_access) return false;
		//menu_access = false;
		if (game.getKeyHandler().get_escape()) {
			game.getKeyHandler().clear();
			game.getKeyHandler().freeze(KeyHandler.KEY_ESCAPE, 20);
			game.scene = new Scene_GameMenu(game, this);
			//game.scene = new Scene_StartMenu(game);
			return true;
		}
		return false;
	}
	
	private void check_walking() {
		//Koordinaten entsprechend der Eingabe (falls möglich)
		//anpassen
		switch (game.getKeyHandler().get_last()) {
		case 1: //UP
			player.direction = KeyHandler.KEY_UP;
			if (player.pos_y == 0) break;
			if (!current_map.isPassable(player.pos_x, player.pos_y-1)) break;
			player.pos_y -= 1;
			break;
			
		case 2: //DOWN
			player.direction = KeyHandler.KEY_DOWN;
			if (player.pos_y == current_map.getHeight()-1) break;
			if (!current_map.isPassable(player.pos_x, player.pos_y+1)) break;
			player.pos_y += 1;
			break;
		case 3: //LEFT
			player.direction = KeyHandler.KEY_LEFT;
			if (player.pos_x == 0) break;
			if (!current_map.isPassable(player.pos_x-1, player.pos_y)) break;
			player.pos_x -= 1;
			break;
		case 4: //RIGHT
			player.direction = KeyHandler.KEY_RIGHT;
			if (player.pos_x == current_map.getWidth()-1) break;
			if (!current_map.isPassable(player.pos_x+1, player.pos_y)) break;
			player.pos_x += 1;
		}
	}
	
	private void check_scrolling() {
		boolean scrolling = false;
		switch(player.direction) {
		case 1: //UP
			if (current_map.getHeight()-player.pos_y-1 <= Screen.VISIBLE_TILES_Y/2) break;
			if (player.pos_y >= Screen.VISIBLE_TILES_Y/2) {
				scrolling = true;
				screen_point[1]--;
			}
			break;
		case 2: //DOWN
			if (player.pos_y <= Screen.VISIBLE_TILES_Y/2) break;
			if (current_map.getHeight()-player.pos_y > Screen.VISIBLE_TILES_Y/2) {
				scrolling = true;
				screen_point[1]++;
			}
			break;
		case 3: //LEFT
			if (current_map.getWidth()-player.pos_x <= Screen.VISIBLE_TILES_X/2) break;
			if (player.pos_x >= Screen.VISIBLE_TILES_X/2) {
				scrolling = true;
				screen_point[0]--;
			}
			break;
		case 4: //RIGHT
			if (player.pos_x <= Screen.VISIBLE_TILES_X/2) break;
			if (current_map.getWidth()-player.pos_x >= Screen.VISIBLE_TILES_X/2) {
				scrolling = true;
				screen_point[0]++;
			}
			break;
		}
		current_map.scrolling = scrolling;
	}
	
	private void player_animation() {
		//Der movecounter von jedem Sprite ist so groß, wie
		//ein Tile. Während einer Bewegung wird er hochgezählt, um
		//Den Character Pixelweise zu bewegen und zu animieren
		//Wenn der movecounter 32 (die Tilegröße) erreicht hat
		//dann ist die Animation vorbei und alles wird zurückgesetzt.
		if (player.moving) player.movecounter += player.move_distance; 
		if (player.movecounter >= Map.TILESIZE) {
			player.movecounter = 0;
			//Koordinaten werden jetzt angepasst
			player.save_position();
			//Schrittanimation für die nächste Bewegung wird gewechselt
			if (player.old_animation == Sprite.ANIMATION_LEFT) {
				player.old_animation = Sprite.ANIMATION_RIGHT;
			}
			else {
				player.old_animation = Sprite.ANIMATION_LEFT;
			}
			player.moving = false;
			skip_moving = false;
		}
	}

	public void addSprite(Sprite s) {
		sprites.add(s);
		//Lade jede Animation einmal in den Bildpuffer. Dadurch wird das Flackern
		//beim ersten Anzeigen der Animationen verhindert.
		//Grund: unbekannt
		for (int x=0; x<12; x++) {
			//Evtl muss hier wirklich ins board gezeichnet werden
			game.getScreen().getBuffer().getGraphics().drawImage(
					s.getImage(),
					0,
					0,
					game.getScreen());
		}
	}
	
	public void setMap(Map m) {
		map = m;
	}
	
	public void setFocusOn(Sprite s) {
		main_sprite = s;
	}
	
	private void drawLowMap() {
		int map_x = -screen_point[0]*Map.TILESIZE;
		int map_y = -screen_point[1]*Map.TILESIZE;
		if (main_sprite.moving && map.scrolling){
			switch (player.direction) {
			case 1: //UP
				map_y -= Map.TILESIZE - main_sprite.movecounter;
				break;
			case 2: //DOWN
				map_y += Map.TILESIZE - main_sprite.movecounter;
				break;
			case 3: //LEFT
				map_x -= Map.TILESIZE - main_sprite.movecounter;
				break;
			case 4: //RIGHT
				map_x += Map.TILESIZE - main_sprite.movecounter;
				break;
			}
		}
		game.getScreen().getBuffer().getGraphics().drawImage(
				map.getLowMapImage(),
				map_x,
				map_y,
				game.getScreen());
	}
	
	private void drawSprites() {
		for (Sprite s : sprites) {
			if (s.movecounter > 0) {
				//ANIMATION
				if (s.movecounter < Map.TILESIZE-10) {
					if (s.old_animation == Sprite.ANIMATION_LEFT) s.animation = Sprite.ANIMATION_RIGHT;
					else s.animation = Sprite.ANIMATION_LEFT;
				}
				else {
					s.animation = Sprite.ANIMATION_MIDDLE;
				}
			}
			//Berechne tatsächliche Pixelkoordinaten in Abhängigkeit von
			//Animation und Scrolling
			int new_x=-100;
			int new_y=-100;
			//Falls scrolling aktiviert ist, verändert der Character zwar seine Mapkoordinaten
			//aber wird weiterhin zentriert im Bild angezeigt
			if (map.scrolling) {
				switch (s.direction) {
				case 2: //DOWN
				case 1: //UP
					new_x = (s.get_old_x()-screen_point[0])*Map.TILESIZE;
					new_y = (Screen.VISIBLE_TILES_Y/2)*Map.TILESIZE - Map.TILESIZE;
					break;
				case 3: //LEFT
				case 4: //RIGHT
					new_x = (Screen.VISIBLE_TILES_X/2)*Map.TILESIZE;
					new_y = (s.get_old_y()-screen_point[1])*Map.TILESIZE - Map.TILESIZE;
					break;
				}
			}
			else {
				new_x = (s.get_old_x()-screen_point[0])*Map.TILESIZE;
				new_y = (s.get_old_y()-screen_point[1])*Map.TILESIZE - Map.TILESIZE;
				//Falls der Character sich nciht bewegt ist movecounter 0 und nichts
				//ändert sich hier!
				switch (s.direction) {
				case 1: //UP
					new_y -= s.movecounter;
					break;
				case 2: //DOWN
					new_y += s.movecounter;
					break;
				case 3: //LEFT
					new_x -= s.movecounter;
					break;
				case 4: //RIGHT
					new_x += s.movecounter;
				}
			}
			game.getScreen().getBuffer().getGraphics().drawImage(
					s.getImage(),
					new_x,
					new_y,
					game.getScreen());
		}
	}
}
