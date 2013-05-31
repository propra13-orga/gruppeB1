import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Hashtable;


public interface IBattleActor {

	//Alle Statusvariablen des BattleActors
	
	public String getName();
	public int getHP();
	public int getMaxHP();
	public int getMP();
	public int getMaxMP();
	public int getATK();
	public int getDEF();
	public int getSpeed();
	public int getMaxSpeed();
	public int getActionCost(); //Wie viel "Speed" eine Aktion den Actor kostet. Sollte bei 0.3*Speed bis 0.5*Speed liegen
	public int getIQ();
	
	//Liste von Items, Zaubern und Waffen
	
	public ArrayList<Entity> getItems();
	public ArrayList<Entity> getSkills();
	
	//Die Ausrüstung soll so verwaltet werden, dass jeder Spieler immer nur eine Waffe,
	//eine Rüstung, etc. tragen kann.
	//Dann kann man auf die Ausrüstung einfach mit den Strings "weapon", "armor", "shield", etc.
	//zugreifen.
	public Hashtable<String, Entity> getEquipment();
	
	//BattleSprite
	public BufferedImage getBattleSprite();
	
	//Die Daten im BattleActor werden während des Kampfes genutzt und verändert.
	//writeBack() überträgt alle diese Daten auf die tatsächliche Spielerentität
	//Damit die Änderungen auch nach dem Kapmf wirksam sind
	public void writeBack();
	
	void reduceSpeed();
	void resetSpeed();
	
}
