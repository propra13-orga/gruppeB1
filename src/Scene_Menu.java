import java.util.ArrayList;


public class Scene_Menu extends Scene {

	Scene_Map current_map;
	boolean dirty;
	
	//Der Index des aktuell gewählten Befehls
	private int cursor;
	//Die vorhandenen Befehle
	private ArrayList<String> commands;
	
	Scene_Menu(Game g, Scene_Map m) {
		super(g);
		current_map = m;
		//Zeichne das Menü auf den Bildschirm
		init_menu();
		dirty = true;
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
		commands.add("Neues Spiel");
		commands.add("Spiel laden");
		commands.add("Beenden");
		updateScreen();
	}
	
	private void updateLogic() {
		switch (game.getKeyHandler().get_last()) {
		case KeyHandler.DOWN:
			cursor++;
			dirty = true;
			break;
		case KeyHandler.UP:
			cursor--;
			dirty = true;
			break;
		case KeyHandler.ESCAPE:
			game.getKeyHandler().clear();
			game.scene = current_map;
		}
		if (cursor < 0) cursor = commands.size() - 1;
		if (cursor >= commands.size()) cursor = 0;
	}
	
	private void updateScreen() {
		if (dirty) {
			if (cursor == 0) {
				print(commands.get(0)+" <<<");
			}
			else {
				print(commands.get(0));
			}
			if (cursor == 1) {
				print(commands.get(1)+" <<<");
			}
			else {
				print(commands.get(1));
			}
			if (cursor == 2) {
				print(commands.get(2)+" <<<");
			}
			else {
				print(commands.get(2));
			}
			dirty = false;
		}
		//Befehle auf den Bildschrim zeichnen
		//Rahmen um den Befehl nummer 'cursor' zeichnen
	}
	
	private void print(String s) {
		System.out.println(s);
	}
	
}
