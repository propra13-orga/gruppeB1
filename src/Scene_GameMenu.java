import java.util.ArrayList;


public class Scene_GameMenu extends Scene {

	Scene_Map current_map;
	boolean dirty;
	
	//Der Index des aktuell gewählten Befehls
	private int cursor;
	//Zähler, um die Eingabe zu verzögern und einfacher zu machen
	int wait_counter;
	//Die vorhandenen Befehle
	private ArrayList<String> commands;
	
	Scene_GameMenu(Game g, Scene_Map m) {
		super(g);
		current_map = m;
		//Zeichne das Menü auf den Bildschirm
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
		print("Menü starten!");
		cursor = 0;
		commands = new ArrayList<String>();
		commands.add("    Inventar   ");
		commands.add("     Status    ");
		commands.add("    Optionen   ");
		commands.add("Spiel speichern");
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
		}
		if (cursor < 0) cursor = commands.size() - 1;
		if (cursor >= commands.size()) cursor = 0;
	}
	
	private void updateScreen() {
		if (dirty) {
			clear_screen();
			if (cursor == 0) {
				print(">>> " + commands.get(0)+" <<<");
			}
			else {
				print("    " + commands.get(0));
			}
			if (cursor == 1) {
				print(">>> " + commands.get(1)+" <<<");
			}
			else {
				print("    " + commands.get(1));
			}
			if (cursor == 2) {
				print(">>> " + commands.get(2)+" <<<");
			}
			else {
				print("    " + commands.get(2));
			}
			if (cursor == 3) {
				print(">>> " + commands.get(3)+" <<<");
			}
			else {
				print("    " + commands.get(3));
			}
			dirty = false;
		}
		//Befehle auf den Bildschrim zeichnen
		//Rahmen um den Befehl nummer 'cursor' zeichnen
	}
	
	private void print(String s) {
		System.out.println(s);
	}
	
	private void clear_screen() {
		print("\n\n\n\n\n\n\n\n\n\n\n\n\n");
	}
	
}
