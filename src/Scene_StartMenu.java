import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JOptionPane;


public class Scene_StartMenu extends Scene {

	Window_Selectable menu;
	Graphics g;
	
	Scene_StartMenu(Object_Game game) {
		super(game);
		g = game.getScreen().getBuffer().getGraphics();
		g.setFont(new Font("Arial", Font.PLAIN, 20));
		menu = new Window_Selectable(0,0,game);
		menu.EXIT_POSSIBLE = false;
		menu.addCommand("Spiel starten");
		menu.addCommand("Credits");
		menu.addCommand("Spiel beenden");
		menu.center();
		game.getKeyHandler().clear();
	}
	
	public void update() {
		g.clearRect(0, 0, Object_Screen.SCREEN_W, Object_Screen.SCREEN_H);
		if (menu.EXECUTED) menu.update();
		else {
			if (menu.CANCELED) {
				game.scene = null;
			}
			else {
				switch (menu.cursor){
				case 0: //Spiel starten
					game.scene = new Scene_Level(game);
					return;
				case 1: //Credits
					String text = "1. Meilenstein\n\n"+
								  "Programmierer:\n\n"  +
								  "Victor Persien\n"    +
								  "Bernard Darryl Oungouande\n" +
								  "Hyojin Lee\n"        +
								  "Elina Margamaeva\n"  +
								  "Alexander Schäfer\n" ;
					JOptionPane.showMessageDialog(
							null,
							text,
							"ProPra 13 - Erster Meilenstein",
							JOptionPane.OK_CANCEL_OPTION);
					menu.EXECUTED = true;
					break;
				case 2: //Spiel beenden
					game.scene = null;
				}
			}
		}
	}
	
}
