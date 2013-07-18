import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * 
 * Dient dem Auslesen von Dateien der Typen .et (Entitäten), .q (Quests),
 * .skl (Skills) oder .dat (sonstige Daten). Kann instantiiert werden und buffert
 * dann einmal bereits gelesene Daten. Jeder Instanz von Object_DBReader ist 
 * ein Dateityp zugeordnet und nur solche Dateien lassen sich damit auslesen.
 * 
 * @author Victor Persien
 *
 */
public class Object_DBReader {
	public static final String DIR_ET = "res/db/";
	public static final String EXT_ET = ".et";
	public static final String DIR_Q = "res/quests/";
	public static final String EXT_Q = ".q";
	public static final String DIR_SKL = "res/skills/";
	public static final String EXT_SKL = ".skl";
	public static final String DIR_DAT = "res/data/";
	public static final String EXT_DAT = ".dat";
	
	private final String ext;
	private final String dir;
	
	
	private Map<String,Map<String,String>> content;
	
	/**
	 * Konstruktor. "type" bestimmt den Typ, wobei 
	 * <ul>
	 * <li> "entity": Dateien vom Typ .et,
	 * <li> "skill": Dateien vom Typ .skl,
	 * <li> "quest": Dateien vom Typ .q,
	 * <li> "data": Dateien vom Typ .dat.
	 * </ul>
	 * @param type		Der Typ, den der Reader behandelt.
	 */
	public Object_DBReader(String type) {
		this.content = new HashMap<String,Map<String,String>>();
		switch(type) {
		case "entity":
			this.ext = EXT_ET;
			this.dir = DIR_ET;
			break;
		case "skill":
			this.ext = EXT_SKL;
			this.dir = DIR_SKL;
			break;
		case "quest":
			this.ext = EXT_Q;
			this.dir = DIR_Q;
			break;
		case "data":
		default:
			this.ext = EXT_DAT;
			this.dir = DIR_DAT;
			break;
		}
		
	}
	
	/**
	 * Liest die Daten aus der Datei oder dem Puffer.
	 * 
	 * @param fname		Dateiname (ohne Endung).
	 * @return			Tabelle der Eigenschaften.
	 */
	public Map<String,String> getProperties(String fname) {
		if (!content.containsKey(fname)) {
			content.put(fname,this.getPropertiesFromFile(fname));
			
		}
		return content.get(fname);
	}
	
	/**
	 * Parst Dateien des Typs, für den die Klasse instantiiert wurde aus der
	 * angegebenen Datei im angegebenen Pfad.
	 * 
	 * @param fname		Dateiname (ohne Endung).
	 * @return			Tabelle der Eigenschaften.
	 */
	private Map<String,String> getPropertiesFromFile(String fname) {
		Map<String,String> properties = new HashMap<String,String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(this.dir+fname+this.ext));
			String line;
			String[] entry;
			while ((line = br.readLine()) != null) {
				entry = line.split(":");
				if (entry.length > 1) {
					properties.put(entry[0].trim(), entry[1].trim());
				}
			}
			br.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		return properties;
	}
	

}
