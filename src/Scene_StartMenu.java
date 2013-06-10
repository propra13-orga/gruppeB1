import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class Scene_StartMenu extends Abstract_Scene {

	Window_Selectable menu;
	BufferedImage background;
	
	Scene_StartMenu(Object_Game game) {
		super(game);
		this.keyhandler.clear();
		menu = new Window_Selectable(0,0,game);
		menu.EXIT_POSSIBLE = false;
		menu.addCommand("Weiter");
		menu.addCommand("Neues Spiel");
		menu.addCommand("Kampfsystem starten");
		menu.addCommand("Credits");
		menu.addCommand("Spiel beenden");
		menu.center();
		//menu.setY(300);
		
		this.background = new BufferedImage(
				Object_Screen.SCREEN_W,
				Object_Screen.SCREEN_H,
				BufferedImage.TYPE_INT_ARGB);
		try {
			this.background.getGraphics().drawImage(ImageIO.read(new File("res/background/castle.png")),0,0,null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
			case 0: // Weiter
				game.switchScene(new Scene_Level(game,true));
				return;
			case 1: //Spiel starten
				game.switchScene(new Scene_Level(game,false));
				return;
			case 2:
				Object_BattleActor b1 = new Object_BattleActor();
				Object_BattleActor b2 = new Object_BattleActor();
				Object_BattleActor b3 = new Object_BattleActor();
				Object_BattleActor e1 = new Object_BattleActor();
				Object_BattleActor e2 = new Object_BattleActor();
				b1.sprite = new Object_BattleSprite("battlechar-1", 1, 13, BattleSide.PLAYER, this.game);
				b2.sprite = new Object_BattleSprite("battlechar-1", 2, 14, BattleSide.PLAYER, this.game);
				b3.sprite = new Object_BattleSprite("battlechar-1", 3, 13, BattleSide.PLAYER, this.game);
				e1.side = BattleSide.ENEMY;
				e2.side = BattleSide.ENEMY;
				e1.sprite = new Object_BattleSprite("enemy-2", 1, 14, BattleSide.ENEMY, this.game);
				e2.sprite = new Object_BattleSprite("enemy-2", 2, 13, BattleSide.ENEMY, this.game);
				Object_BattleContext c1 = new Object_BattleContext();
				c1.actors.add(b1);
				c1.actors.add(b3);
				c1.players.add(b1);
				c1.players.add(b2);
				c1.players.add(b3);
				c1.actors.add(b2);
				c1.actors.add(e1);
				c1.actors.add(e2);
				c1.enemies.add(e1);
				c1.enemies.add(e2);
				
				b1.name = "Alex";
				b1.hp = 123;
				b1.maxHp = 300;
				b2.name = "Peter";
				b3.name = "Jakob";
				
				this.game.switchScene(new Scene_BattleSystem(c1, null, this.game));
				break;
			case 3: //Credits
				String text = "1. Meilenstein\n\n"+
							  "Programmierer:\n\n"  +
							  "Victor Persien\n"    +
							  "Bernard Darryl Oungouande\n" +
							  "Hyojin Lee\n"        +
							  "Elina Margamaeva\n"  +
							  "Alexander Sch�fer\n" ;
				JOptionPane.showMessageDialog(
						null,
						text,
						"ProPra 13 - Erster Meilenstein",
						JOptionPane.OK_CANCEL_OPTION);
				menu.EXECUTED = true;
				break;
			case 4: //Spiel beenden
				game.quit();
			}
		}
	}

	@Override
	public void updateScreen() {
		this.game.getScreen().clear();
		this.screen.drawImage(this.background, 0, 0, null);
		menu.updateScreen();
	}
	
}
