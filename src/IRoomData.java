import java.util.List;

/*
 * IRoomData.java
 * 
 * Das Interface IRoomData soll dazu dienen, die aktuelle Map und eine Liste
 * der sich darauf befindlichen Entitäten weiterzureichen. Die entsprechenden
 * Daten stammen aus XML-Dateien.
 * 
 */

public interface IRoomData {
	String getMap();
	
	List<IEntityData> getEntities();
}
