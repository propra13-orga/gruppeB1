import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Object_Quest implements Comparable<Object_Quest> {
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
