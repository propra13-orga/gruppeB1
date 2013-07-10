import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class Scene_GameOver extends Abstract_Scene {
	
	Window_Menu menu;
	BufferedImage background;

	Scene_GameOver(Object_Game game) { // Scene_GameOver erbt die Eigentschafte von Object_Game
		super(game);
		
		String path = "res/pictures/gameover.png";
		try {
			BufferedImage img = ImageIO.read( new File(path));
			background = new BufferedImage(
					img.getWidth(),
					img.getHeight(),
					BufferedImage.TYPE_INT_ARGB);
			background.getGraphics().drawImage(img, 0, 0, null);
		} catch (IOException e) {
			this.game.exitOnError("Gameoverbild konnte nicht geladen werden");
			return;
		}

	
	menu=new Window_Menu(game, "Auswahl"); // ein neues Objekt der Klasse Window_Menu
	menu.addReturnCommand("Spiel neu starten");     // Button "Spiel neu starten"
	menu.addReturnCommand("Zurueck ins Hauptmenu"); // Button "Zurueck ins Hauptmenu"
	menu.addReturnCommand("Spiel beenden"); // Button "Spiel beenden"
	menu.center(); // die Position (X-Achse) des Menufensters
	menu.y += 100; // die Position (Y-Achse) des Menufensters 
	Window_Menu.setMainMenu(menu);
	
	}

	@Override
	public void onStart() {
		
		
	}

	@Override
	public void onExit() {
		
		
	}

	@Override
	public void updateData() {
		if(menu.isExecuted()){       // Wenn das Menu ausgeführt soll das Menu immer neu aktualisiert werden,
           menu.updateData();       // Das passiert so schnell, dass man das mit bloßem Auge nicht wahrnimmt.
		}

		else {                     // Sonst soll einer der drei folgenden Fälle ausgeführt werden.
			menu.setupMenuPath();
			switch(menu.getCurrentCursor()){ // Je nach dem wo der Cursor sich befindet, soll in die entsperechende Scene gewechselt werden.
			
			case 0:
				game.switchScene(new Scene_Level(game), true);
				
				break;
			case 1:
				this.game.switchScene(new Scene_StartMenu(this.game), true);
				break;
			case 2:
				this.game.quit();
				break;
				
	}
			}
	}

	@Override //Abbildung des Hintergrundbildes über das ganze Fenster
	public void updateScreen() {
    this.screen.drawImage(this.background,0,0, null);
    
		menu.updateScreen();{
			
		}
	}

}