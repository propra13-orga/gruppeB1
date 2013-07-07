import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/*
 * Object_Quest.java
 * 
 * Dies ist ein Quest-Objekt. Es ist im Prinzip eine Liste von Events, die
 * stattfinden muessen, bevor die Quest als erfüllt gilt. Die Events sind
 * dargestellt als Paare von Event-Typen (siehe Event.java) und Entitäts-Typen
 * gemaess der Datebank. Tritt nun ein "echtes" Event auf (siehe Event.java),
 * so wird ueberprüft, ob eine Entitaet, die eine Quest (im Questbag) besitzt,
 * das Event verursacht hat und ob der Typ der Entitaet, "auf" der das Event
 * verursacht wurde, mit dem Typ in der Quest uebereinstimmt.
 * 
 * Momentan müssen die Events noch konjunktiv auftreten, spaeter ggf. auch
 * disjunktiv (d.h. es muessen nur einige aus einer Auswahl erfuellt werden).
 * 
 * Weiter soll es spaeter ggf. noch Unterquests geben und Abhaengigkeiten unter
 * Quests.
 */

public class Object_Quest implements Comparable<Object_Quest>, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5779174461819933527L;
	
	public static final String DIR = "res/quests/";
	public static final String EXT = ".q";
	
	String fname;
	String name;
	String description;
	Map<EventType,List<String>> openEvents;
	Map<EventType,List<String>> closedEvents;
//	List<String> subquests;
//	List<String> reqQuests;	// Quests benoetigt, um Quest abzuschliessen.
//	Map<String,String> reqProperties; // Eigenschaften ----
	boolean accomplished;
	int xp;
	
	public Object_Quest(String questfname) {
		this.fname = questfname;
		this.description = "";
		this.openEvents = new HashMap<EventType,List<String>>();
		this.closedEvents = new HashMap<EventType,List<String>>();
		this.accomplished = false;
		this.readQuestFromFile();
	}
	
	/*
	 * Getters
	 */
	
	public String getFName() { return this.fname; }
	public String getName() { return this.name; }
	public String getDescription() { return this.description; }
	public List<String> getOpenEventsOfType(EventType eventType) { return this.openEvents.get(eventType); }
	public List<String> getClosedEventsOfType(EventType eventType) { return this.closedEvents.get(eventType); }
	public boolean isAccomplished() { return this.accomplished; }
	public boolean hasOpenEventOfType(EventType eventType) { return this.openEvents.containsKey(eventType); }
	public boolean hasClosedEventOfType(EventType eventType) { return this.closedEvents.containsKey(eventType); }
	public boolean noOpenEventsLeft() { return this.openEvents.isEmpty(); }
	public int getXP() { return this.xp; }
	
	
	
	/*
	 * Setters
	 */
	
	public void setAccomplished() { this.accomplished = true; }
	
	public void moveToClosedEvents(EventType eventType, int pos) {
		String entityType = this.getOpenEventsOfType(eventType).remove(pos);
		if (this.openEvents.get(eventType).isEmpty()) this.openEvents.remove(eventType);
		if (!this.closedEvents.containsKey(eventType)) this.closedEvents.put(eventType, new LinkedList<String>());
		this.closedEvents.get(eventType).add(entityType);
	}
	
	/*
	 * Privates
	 */
	
	/*
	 * Liest eine Quest aus einer Datei ein.
	 */
	private void readQuestFromFile() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(DIR+this.fname+EXT));
			String line;
			String[] entry;
			String entityType;
			String property;
			String value;
			EventType eventType;
			boolean withinBrackets = false;
			while ((line = br.readLine()) != null) {
				if (!withinBrackets) {
					entry = line.split(":");
					if (entry.length > 1) {
						property = entry[0].trim();
						if (property.equals("name")) this.name = entry[1].trim();
						else if (property.equals("xp")) this.xp = Integer.parseInt(entry[1].trim());
						else if (property.equals("event")) {
							entityType = null;
							eventType = EventType.valueOf(entry[1].trim().toUpperCase());
							if (entry.length > 2) entityType = entry[2].trim();
							if (!this.openEvents.containsKey(eventType)) {
								this.openEvents.put(eventType, new LinkedList<String>());
							}
							this.openEvents.get(eventType).add(entityType);
						}
						else if (property.equals("description")) {
							value = entry[1];
							if (value.matches(".*\\{.*")) {
								withinBrackets = true;
								value = value.replaceFirst("\\{", " ");
							}
							this.description += value.trim();
						}
					}
				}
				else {
					value = line;
					if (value.matches(".*\\}.*")) {
						value = value.replaceFirst("\\}", "");
						withinBrackets = false;
					}
					this.description += value.trim()+"\n";
				}
				entityType = null;
				property = null;
				value = null;
			}
			br.close();
		}
		catch (IOException e) { e.printStackTrace(); }
		
		this.description = this.description.replaceAll("[\\s\\n]+$", "");
	}

	@Override
	public int compareTo(Object_Quest q) {
		if (this.fname.equals(q.getFName())) return 0;
		return 1;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this.compareTo((Object_Quest) o) == 0) return true;
		return false;
	}

}
