import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;


public class Scene_GameMenu extends Scene {

	Scene_Level current_map;
	boolean dirty;
	
	//Der Index des aktuell gew�hlten Befehls
	private int cursor;
	//Z�hler, um die Eingabe zu verz�gern und einfacher zu machen
	int wait_counter;
	//Die vorhandenen Befehle
	private ArrayList<String> commands;


	Scene_GameMenu(Game g, Scene_Level m) {

		super(g);
		current_map = m;
		//Zeichne das Men� auf den Bildschirm
		init_menu();
		dirty = true;
		wait_counter = 0;
	}
	
	public void update() {
		updateLogic();
		updateScreen();
	}
	
	////////////////////////////////////////////////////////////////////////////
	
	private void init_menu() {
		print("Men� starten!");
		cursor = 0;
		commands = new ArrayList<String>();
		commands.add("Spiel speichern");
		commands.add("Spiel beenden");
		updateScreen();
	}
	
	private void updateLogic() {
		if (wait_counter > 0) {
			wait_counter--;
			return;
		}
		switch (game.getKeyHandler().getLast()) {
		case KeyHandler.KEY_DOWN:
			cursor++;
			dirty = true;
			wait_counter = 10;
			break;
		case KeyHandler.KEY_UP:
			cursor--;
			dirty = true;
			wait_counter = 10;
			
			break;
		case KeyHandler.KEY_ESCAPE:
			clear_screen();
			game.getKeyHandler().clear();
			game.getKeyHandler().freeze(KeyHandler.KEY_ESCAPE, 40);
			game.scene = current_map;
			return;
		case KeyHandler.KEY_ENTER:
			switch (cursor) {
			case 0:
				//Spiel speichern
				break;
			case 1:
				//Spiel beenden
				game.scene = null;
			}
		}
		if (cursor < 0) cursor = commands.size() - 1;
		if (cursor >= commands.size()) cursor = 0;
	}
	
	private void updateScreen() {
		Graphics g = game.getScreen().getBuffer().getGraphics();
		g.clearRect(0, 0, Screen.SCREEN_W, Screen.SCREEN_H);
		int text_x = 20;//Screen.SCREEN_W/2;
		int text_y = 20;//Screen.SCREEN_H/2;
		int i = 0;
		for (String s : commands) {
			g.drawString(s, text_x, text_y+i*20);
			i++;
		}
		g.setColor(new Color(255,255,255));
		g.drawLine(text_x,
				   text_y+cursor*20,
				   text_x+100,
				   text_y+cursor*20);
		
	}
	
	private void print(String s) {
		System.out.println(s);
	}
	
	private void clear_screen() {
		print("\n\n\n\n\n\n\n\n\n\n\n\n\n");
	}
	
}
