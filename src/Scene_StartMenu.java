import java.awt.Font;
import javax.swing.JOptionPane;


public class Scene_StartMenu extends Abstract_Scene {

	Window_Selectable menu;
	
	Scene_StartMenu(Object_Game game) {
		super(game);
		this.keyhandler.clear();
		menu = new Window_Selectable(0,0,game);
		menu.EXIT_POSSIBLE = false;
		menu.addCommand("Spiel starten");
		menu.addCommand("Credits");
		menu.addCommand("Spiel beenden");
		menu.center();
		
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onExit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateData() {
		if (menu.EXECUTED) {
			menu.updateData();
		}
		else {
			switch (menu.cursor){
			case 0: //Spiel starten
				game.switchScene(new Scene_Level(game));
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
				game.quit();
			}
		}
	}

	@Override
	public void updateScreen() {
		this.game.getScreen().clear();
		menu.updateScreen();
	}
	
}
