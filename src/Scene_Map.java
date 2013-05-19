import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/*
 * Scene_Map.java
 * Diese Scene ist das Herzstück des Programms, da sie den gesamten Spielablauf regelt. Sie
 * regelt die korrekte Anzeige der Map, Sprites und evtl. weiteren Grafiken, die Steuerung
 * von Spieler und Gegnern sowie das Mapscrolling.
 * 
 * Wie jede Scene implementiert sie die Methode update, welche sich (momentan) aus den
 * Methoden updateLogic und updateScreen zusammensetzt.
 * Kurz gesagt überprüft updateLogic alle Tasteneingaben und verarbeitet sie entsprechend
 * (Spielerbewegung, Menüaufruf, etc.) und zeichnet anschließend die Karte samt Sprites neu.
 * 
 * Für eine detailierte Beschreibung siehe updateLogic und updateScreen
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
		player = new Sprite("player_2", 2, 5);
		Sprite enemy1 = new Sprite("character_1", 7, 5);
		Sprite enemy2 = new Sprite("character_1", 8, 9);
		Sprite enemy3 = new Sprite("character_1", 17, 11);
		current_map = new Map("map2", this);
		sprites = new ArrayList<Sprite>();
		screen_point = new int[2];
		screen_point[0] = 0;
		screen_point[1] = 0;
		setMap(current_map);
		addSprite(player);
		addSprite(enemy1);
		addSprite(enemy2);
		addSprite(enemy3);
		setFocusOn(player);
		player.moving = false;
	}
	
	public void update() {
		Collections.sort(sprites);
		updateLogic();
		updateScreen();
	}
	
	private void updateLogic() {
		//Überprüft, ob eine Bewegungstaste gedrückt wurde und
		//bearbeitet die Koordinaten des Characters entsprechend
		//Initialisiert (wenn nötig) auch eine Bewegungsanimation
		
		//checkMenu gibt true zurück, falls tatsächlich die Scene gewechselt wurde. In
		//diesem Fall soll die update Methode natürlich so schnell wie möglich abbrechen
		if (check_menu()) return;
		
		//Falls, der Spieler gerade steuerbar ist (also nicht schon in einer
		//Bewegungsphase), dann prüfe jetzt, ob er bewegt wurde
		if (!player.moving) checkWalking();
		
		//Aktualisiere Position und Animation aller Sprites
		for (Sprite s : sprites) s.update();
		
		//Screenpoint wird aktualisiert
		updateScreenPoint();
	}
	
	private void updateScreen() {
		BufferedImage m = current_map.getLowMapImage();
		drawSprites(m);
		m = m.getSubimage(
				screen_point[0],
				screen_point[1],
				Screen.SCREEN_W,
				Screen.SCREEN_H);
		game.getScreen().getBuffer().getGraphics().drawImage(
		m,
		0,
		0,
		game.getScreen());
	}
	
	
	///////////////////////////////////////////////////////////////////////////////////////////
	//								Scene interne Methoden
	///////////////////////////////////////////////////////////////////////////////////////////
	
	private boolean check_menu() {
		if (game.getKeyHandler().getKey(KeyHandler.KEY_ESCAPE)) {
			game.getKeyHandler().clear();
			game.getKeyHandler().freeze(KeyHandler.KEY_ESCAPE, 20);
			//Menü aufrufen
			game.scene = new Scene_GameMenu(game, this);
			return true;
		}
		return false;
	}
	
	private void checkWalking() {
		//Falls eine Pfeiltaste gedrückt wurde, wird versucht den Spieler
		//zu bewegen (das ist nur möglich, wenn er versucht, auf ein begehbares
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
		}
	}

	public void addSprite(Sprite s) {
		sprites.add(s);
		Collections.sort(sprites);
	}
	
	public void setMap(Map m) {
		current_map = m;
	}
	
	public void setFocusOn(Sprite s) {
		//Der Sprites, auf den der Fokus gerichtet ist, wird als Referenzpunkt
		//beim Scrolling verwendet
		main_sprite = s;
	}
	
	private void drawSprites(BufferedImage map) {
		for (Sprite s : sprites) {
			map.getGraphics().drawImage(
					s.getImage(),
					s.getX(),
					s.getY(),
					game.getScreen());
		}
	}
	
	class SpriteComparator implements Comparator<Sprite> {

		@Override
		public int compare(Sprite arg0, Sprite arg1) {
			if (arg0.getY() < arg1.getY()) {
				return -1;
			}
			if (arg0.getY() > arg1.getY()) {
				return 1;
			}
			return 0;
		}
		
	}
	
}
