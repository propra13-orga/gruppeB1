import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class Scene_GameOver extends Abstract_Scene {
	
	Window_Menu menu;
	BufferedImage background;

	Scene_GameOver(Object_Game game) {
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

	
	menu=new Window_Menu(game, "Auswahl");
	menu.addReturnCommand("Spiel neu starten");
	menu.addReturnCommand("Zurueck ins Hauptmenue");
	menu.addReturnCommand("Spiel beenden");
	menu.center();
	menu.y += 120;
	Window_Menu.setMainMenu(menu);
	
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
		if(menu.isExecuted()){
			menu.updateData();
		}
		
		else {
			menu.setupMenuPath();
			switch(menu.getCurrentCursor()){
			
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

	@Override
	public void updateScreen() {
    this.screen.drawImage(this.background,0,0, null);
    
		menu.updateScreen();{
			
		}
	}

}