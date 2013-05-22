import java.awt.image.BufferedImage;
import java.util.ArrayList;

/*
 * Scene_Map.java
 * Diese Scene ist das Herzstï¿½ck des Programms, da sie den gesamten Spielablauf regelt. Sie
 * regelt die korrekte Anzeige der Map, Sprites und evtl. weiteren Grafiken, die Steuerung
 * von Spieler und Gegnern sowie das Mapscrolling.
 * 
 * Wie jede Scene implementiert sie die Methode update, welche sich (momentan) aus den
 * Methoden updateLogic und updateScreen zusammensetzt.
 * Kurz gesagt ï¿½berprï¿½ft updateLogic alle Tasteneingaben und verarbeitet sie entsprechend
 * (Spielerbewegung, Menï¿½aufruf, etc.) und zeichnet anschlieï¿½end die Karte samt Sprites neu.
 * 
 * Fï¿½r eine detailierte Beschreibung siehe updateLogic und updateScreen
 */

public class Scene_Map extends Scene {

	int[] screen_point; //Das Tile, welches sich relativ zum Player
						//in der oberen linken Ecke befindet. Wichtig
						//beim Scrolling!
	
	private Sprite player;
	private Map current_map;
	private ArrayList<Sprite> sprites;
	private Sprite main_sprite;
	
	Scene_Map(Game g) {
		super(g);
		//Die meisten Initialisierungen werden noch ausgelagert
		player = new Sprite("player_2", 3, 3);
		//Sprite enemy1 = new Sprite("player_1", 6, 5);
		//Sprite enemy2 = new Sprite("character_1", 6, 9);
		//Sprite enemy3 = new Sprite("character_1", 17, 11);
		current_map = new Map("map2");
		sprites = new ArrayList<Sprite>();
		screen_point = new int[2];
		screen_point[0] = 0;
		screen_point[1] = 0;
		setMap(current_map);
		addSprite(player);
		//addSprite(enemy1);
		//addSprite(enemy2);
		//addSprite(enemy3);
		setFocusOn(player);
		player.moving = false;
	}
	
	public void update() {
		updateLogic();
		updateScreen();
	}
	
	private void updateLogic() {
		//ï¿½berprï¿½ft, ob eine Bewegungstaste gedrï¿½ckt wurde und
		//bearbeitet die Koordinaten des Characters entsprechend
		//Initialisiert (wenn nï¿½tig) auch eine Bewegungsanimation
		
		//checkMenu gibt true zurï¿½ck, falls tatsï¿½chlich die Scene gewechselt wurde. In
		//diesem Fall soll die update Methode natï¿½rlich so schnell wie mï¿½glich abbrechen
		if (check_menu()) return;
		//Falls, der Spieler gerade steuerbar ist (also nicht schon in einer
<<<<<<< HEAD
		//Bewegungsphase), dann prï¿½fe jetzt, ob er bewegt wurde
		if (!skip_moving) {
		
			//Alte Koordinaten des characters speichern um zu
			//prï¿½fen, ob eine Bewegung animiert werden muss
			player.save_position();
			check_walking();
			current_map.scrolling = false;
			//Hat der Character sich bewegt?
			if (player.get_old_x() != player.pos_x || player.get_old_y() != player.pos_y) {
				//Spieler hat sich bewegt!
				player.moving = true;
				skip_moving = true;
				//Muss gescrollt werden?
				check_scrolling();
			}
		}
		
		//Animiere den Spieler (falls er sich nicht bewegt hat, wird er einfach
		//nur stehend angezeigt)
		player_animation();
	}
	
	private void updateScreen() {
		//Zuerst alles lï¿½schen
		game.getScreen().getBuffer().getGraphics().clearRect(
				0,
				0,
				Screen.SCREEN_W,
				Screen.SCREEN_H);
		//Den teil der Map anzeigen, der "unter" den Sprites liegt (Boden, etc)
		drawLowMap();
		//Sprites zeichnen
		drawSprites();
		//Spï¿½ter noch:
		//drawHighMap()
=======
		//Bewegungsphase), dann prüfe jetzt, ob er bewegt wurde
		if (!player.moving) checkWalking();
		//Aktualisiere Position und Animation aller Sprites
		for (Sprite s : sprites) s.update();
		//Screenpoint wird aktualisiert
		updateScreenPoint();
	}
	
	private void updateScreen() {
		//Die gesamte Map wird als Bild berechnet
		BufferedImage map = new BufferedImage(
				current_map.getWidth()*Map.TILESIZE,
				current_map.getHeight()*Map.TILESIZE,
				BufferedImage.TYPE_INT_ARGB);
		current_map.drawTiles(map, TileSet.BELOW_SPRITE);
		current_map.drawTiles(map, TileSet.SAME_LEVEL_AS_SPRITE);
		//Sprites werden drauf gezeichnet
		drawSprites(map);
		current_map.drawTiles(map, TileSet.ABOVE_SPRITE);
		//Das sichtbare Feld um den Spieler wird ausgeschnitten...
		map = map.getSubimage(
				screen_point[0],
				screen_point[1],
				Screen.SCREEN_W,
				Screen.SCREEN_H);
		//...und angezeigt
		game.getScreen().getBuffer().getGraphics().drawImage(map,0,0,null);
>>>>>>> 85c8cee54e8b7f323cca4d4494bfbc1927ca60ca
	}
	
	
	///////////////////////////////////////////////////////////////////////////////////////////
	//								Scene-interne Methoden
	///////////////////////////////////////////////////////////////////////////////////////////
	
	private boolean check_menu() {
		if (game.getKeyHandler().getKey(KeyHandler.KEY_ESCAPE)) {
			game.getKeyHandler().clear();
			game.getKeyHandler().freeze(KeyHandler.KEY_ESCAPE, 20);
			//Menï¿½ aufrufen
			game.scene = new Scene_GameMenu(game, this);
			return true;
		}
		return false;
	}
	
<<<<<<< HEAD
	private void check_walking() {
		//Falls eine Pfeiltaste gedrï¿½ckt wurde, wird versucht den Spieler
		//zu bewegen (das ist nur mï¿½glich, wenn er versucht, auf ein begehbares
=======
	private void checkWalking() {
		//Falls eine Pfeiltaste gedrückt wurde, wird versucht den Spieler
		//zu bewegen (das ist nur möglich, wenn er versucht, auf ein begehbares
>>>>>>> 85c8cee54e8b7f323cca4d4494bfbc1927ca60ca
		//Feld zu gelangen
		switch (game.getKeyHandler().getLast()) {
		case 1: //UP
			player.direction = KeyHandler.KEY_UP;
			if (player.getTileY() == 0) return;
			if (!current_map.isPassable(player.getTileX(), player.getTileY()-1)) return;
			break;
		case 2: //DOWN
			player.direction = KeyHandler.KEY_DOWN;
			if (player.getTileY() == current_map.getHeight()-1) return;
			if (!current_map.isPassable(player.getTileX(), player.getTileY()+1)) return;
			break;
		case 3: //LEFT
			player.direction = KeyHandler.KEY_LEFT;
			if (player.getTileX() == 0) return;
			if (!current_map.isPassable(player.getTileX()-1, player.getTileY())) return;
			break;
		case 4: //RIGHT
			player.direction = KeyHandler.KEY_RIGHT;
			if (player.getTileX() == current_map.getWidth()-1) return;
			if (!current_map.isPassable(player.getTileX()+1, player.getTileY())) return;
			break;
		case 0:
			return;
		}
		//Alle Fehler wurden ausgeschlossen, Spieler darf sich bewegen
		player.makeStep();
	}
	
<<<<<<< HEAD
	private void player_animation() {
		//Der movecounter von jedem Sprite ist so groï¿½, wie
		//ein Tile (momentan 32). Wï¿½hrend einer Bewegung wird er hochgezï¿½hlt
		//, um den character Pixelweise zu bewegen und zu animieren
		//Wenn der movecounter 32 (die Tilegrï¿½ï¿½e) erreicht hat
		//dann ist die Animation vorbei und alles wird zurï¿½ckgesetzt.
		if (player.moving) player.movecounter += player.move_distance; 
		if (player.movecounter >= Map.TILESIZE) {
			//Der Spieler hat seine neue Position erreicht, die Animation ist vorbei
			player.movecounter = 0;
			//Koordinaten werden jetzt angepasst
			player.save_position();
			//Schrittanimation fï¿½r die nï¿½chste Bewegung wird gewechselt
			if (player.old_animation == Sprite.ANIMATION_LEFT) {
				player.old_animation = Sprite.ANIMATION_RIGHT;
			}
			else {
				player.old_animation = Sprite.ANIMATION_LEFT;
			}
			player.moving = false;
			//Spieler kann wieder per tastatur gesteuert werden
			skip_moving = false;
=======
	private void updateScreenPoint() {
		screen_point[0] = main_sprite.getX() - (Screen.SCREEN_W / 2);
		screen_point[1] = main_sprite.getY() - (Screen.SCREEN_H / 2) + 16;
		if (screen_point[0] < 0) screen_point[0] = 0;
		if (screen_point[0] > current_map.getWidth()*Map.TILESIZE-Screen.SCREEN_W) {
			screen_point[0] = current_map.getWidth()*Map.TILESIZE-Screen.SCREEN_W;
		}
		if (screen_point[1] < 0) screen_point[1] = 0;
		if (screen_point[1] > current_map.getHeight()*Map.TILESIZE-Screen.SCREEN_H) {
			screen_point[1] = current_map.getHeight()*Map.TILESIZE-Screen.SCREEN_H;
>>>>>>> 85c8cee54e8b7f323cca4d4494bfbc1927ca60ca
		}
	}

	public void addSprite(Sprite s) {
		sprites.add(s);
	}
	
	public void setMap(Map m) {
		current_map = m;
	}
	
	public void setFocusOn(Sprite s) {
		//Der Sprites, auf den der Fokus gerichtet ist, wird als Referenzpunkt
		//beim Scrolling verwendet
		main_sprite = s;
	}
	
	private void drawSprites(BufferedImage screen) {
		for (Sprite s : sprites) {
			screen.getGraphics().drawImage(s.getLowerHalf(),s.getX(),s.getY()+32,null);
		}
<<<<<<< HEAD
		game.getScreen().getBuffer().getGraphics().drawImage(
				map.getLowMapImage(),
				map_x,
				map_y,
				game.getScreen());
	}
	
	private void drawSprites() {
		//Geht die Liste der Sprites durch und zeichnet sie
		// !!! WICHTIG !!!
		//Momentan werden Sprites noch nicht danach sortiert, wer "ï¿½ber" wem steht
		//Das wird noch umgesetzt
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
			//Berechne tatsï¿½chliche Pixelkoordinaten in Abhï¿½ngigkeit von
			//Animation und Scrolling
			int new_x=-100;
			int new_y=-100;
			//Falls scrolling aktiviert ist, verï¿½ndert der Character zwar seine Mapkoordinaten
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
				//ï¿½ndert sich hier!
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
=======
		for (Sprite s : sprites) {
			screen.getGraphics().drawImage(s.getUpperHalf(),s.getX(),s.getY(),null);
>>>>>>> 85c8cee54e8b7f323cca4d4494bfbc1927ca60ca
		}
	}
}
