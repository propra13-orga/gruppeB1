/*
 * IEntityData.java
 * 
 * das Interface IEntityData soll dazu dienen, entitäts-bezogene Daten weiter-
 * zureichen, die aus der Spieldatenbank oder einer anderen Datei (XML) geladen
 * wurden.
 * 
 */

public interface IEntityData {
	
	String getName();
	String getSprite();
	
	int getHP();
	int getCurrentHP();
	int getMP();
	int getCurrentMP();
	int getAP();
	
	int getX();
	int getY();
	int getOrientation();
	int getSpeed();
		
	boolean isControllable();
	boolean isWalkable();
	boolean isCollidable();
	boolean hasAI();
	
	// Trigger-Spezifika
	
	String onEvent(EventType type);
	
	int toLevel();
	int toX();
	int toY();
	int getATK();
	int getDEF();
	int getDEX();
	int getSPD();
	int getCurrentSPD();
}
