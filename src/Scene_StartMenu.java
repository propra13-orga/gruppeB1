import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class Scene_StartMenu extends Abstract_Scene {

	Window_Menu menu;
	BufferedImage background;
	
	Scene_StartMenu(Object_Game game) {
		super(game);
		this.keyhandler.clear();
		menu = new Window_Menu(game, "main");
		
		Window_Menu test = new Window_Menu(game, "test");
		test.addReturnCommand("1");
		test.addReturnCommand("2");
		test.addReturnCommand("3");
		
		menu.addReturnCommand("Weiter");
		menu.addReturnCommand("Neues Spiel");
		menu.addReturnCommand("Kampfsystem starten");
		menu.addReturnCommand("Credits");
		menu.addReturnCommand("Spiel beenden");
		menu.addMenuCommand("Test", test);
		menu.center();
		Window_Menu.setMainMenu(menu);
		menu.setExitPossible(true);
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
		if (menu.isExecuted()) {
			menu.updateData();
		}
		else {
			menu.setupMenuPath();
			switch (menu.getCurrentCursor()){
			case 0: // Weiter
				//game.switchScene(new Scene_Level(game,true));
				this.animationmanager.playAnimation("test", 8, 100, 100);
				return;
			case 1: //Spiel starten
				game.switchScene(new Scene_Level(game,false));
				return;
			case 2:
				Object_BattleContext c1 = new Object_BattleContext();
				Scene_BattleSystem s = new Scene_BattleSystem(c1, null, this.game);
				Object_BattleActor b1 = new Object_BattleActor(s);
				Object_BattleActor b2 = new Object_BattleActor(s);
				Object_BattleActor b3 = new Object_BattleActor(s);
				Object_BattleActor e1 = new Object_BattleActor(s);
				Object_BattleActor e2 = new Object_BattleActor(s);
				
				b1.sprite = new Object_BattleSprite("battlechar-1", 1, 13, Object_BattleSprite.PLAYER, this.game, b1);
				b1.name = "Alex";
				b1.hp = 123;
				b1.maxHp = 300;
				b1.speed = 210;
				b1.maxSpeed = 350;

				b2.sprite = new Object_BattleSprite("battlechar-1", 2, 14, Object_BattleSprite.PLAYER, this.game, b2);
				b2.name = "Dagoberto";
				b2.hp = 70;
				b2.maxHp = 500;
				b2.speed = 230;
				b2.maxSpeed = 271;
				
				b3.sprite = new Object_BattleSprite("battlechar-1", 3, 13, Object_BattleSprite.PLAYER, this.game, b3);
				b3.name = "Victor";
				b3.speed = 160;
				b3.maxSpeed = 250;
				
				e1.name = "Hyojin";
				e1.side = BattleSide.ENEMY;
				e1.sprite = new Object_BattleSprite("enemy-2", 1, 14, Object_BattleSprite.ENEMY, this.game, e1);
				e1.speed = 180;
				e1.maxSpeed = 300;
				
				e2.name = "Elina";
				e2.side = BattleSide.ENEMY;
				e2.sprite = new Object_BattleSprite("enemy-2", 2, 13, Object_BattleSprite.ENEMY, this.game, e2);
				e2.speed = 210;
				

				c1.getActors().add(b1);
				c1.getActors().add(b3);
				c1.getActors().add(b2);
				c1.getActors().add(e1);
				c1.getActors().add(e2);
				
				s.setContext(c1);
				
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
				menu.reset();
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
