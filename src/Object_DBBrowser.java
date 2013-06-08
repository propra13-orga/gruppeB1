import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/*
 * Object_DBBrowser.java
 * 
 * Diese Klasse liest eine Spieldatenbank gegeben im CSV-Format ein und 
 * ermöglicht es, Eigenschaften der Spielobjekte auszulesen. So gibt 
 * getEntry("Teleport","OnCollision") die Eigenschaft "trigger_levelchange"
 * zurück.
 * Alle Rückgaben sind Strings, müssen also ggf. noch in Integer, Boolean oder
 * andere Typen gecastet werden.
 */


public class Object_DBBrowser {
	String filename;
	HashMap<String,Integer> entityTypes;
	HashMap<String,Integer> headers;
	String[][] db;
	
	public Object_DBBrowser(String filename) {
		this.filename = filename;
		this.headers = this.getHeaders();		
		this.entityTypes = this.getEntityTypes();
		this.db = this.fillDB();
	}
	
	/*
	 * Gibt den Eintrag an der Stelle (entityType,attribute) zurück, wobei
	 * "entityType" der Zeilenname und "attribute" der Spaltenname ist.
	 */
	public String getEntry(String entityType, String attribute) {
		if (!this.entityTypes.containsKey(entityType)
				|| !this.headers.containsKey(attribute)) {
			System.err.println("Eintrag nicht vorhanden!");
			throw new IllegalArgumentException();
		}
		int row = this.entityTypes.get(entityType);
		int col = this.headers.get(attribute);
		String entry = db[row][col];
		if (!entry.equals(" ")) return entry;
		return null;
	}
	
	/*
	 * Gibt ein Set aller verfügbaren Attribute ("Überschriften") zurück.
	 */
	public Set<String> getAttributes() { return this.headers.keySet(); }
	
	
	/*
	 * Gibt eine liste aller Namen aller Attribute zurück, deren Werte in der 
	 * Datenbank für den Typen entityType nicht leer (also null) sind.
	 */
	public List<String> getNonEmptyAttributes(String entityType) {
		List<String> values = new LinkedList<String>();
		for (String attribute : this.getAttributes()) {
			if (this.getEntry(entityType, attribute) != null) {
				values.add(attribute);
			}
		}
		
		return values;
	}
	
	/*
	 * Privates
	 */
	
	/*
	 * 
	 */
	private String[][] fillDB() {
		int h = this.entityTypes.size();
		int w = this.headers.size();
		String[][] db = new String[h][w];
		try {
			BufferedReader br = new BufferedReader(new FileReader(this.filename));
			
			String line;
			String entries[];
			int i = 1;
			br.readLine();
			while ((line = br.readLine()) != null) {
				entries = line.split(";");
				for (int j=1;j<entries.length;j++) {
					db[i-1][j-1] = entries[j];
				}
				i++;
			}
			br.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return db;
	}
	
	private HashMap<String,Integer> getHeaders() {
		HashMap<String,Integer> headers = new HashMap<String,Integer>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(this.filename));
			
			String[] entries = br.readLine().split(";");
			for (int i=1;i<entries.length;i++) {
				headers.put(entries[i], i-1);
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return headers;
	}
	
	private HashMap<String,Integer> getEntityTypes() {
		HashMap<String,Integer> entityTypes = new HashMap<String,Integer>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(this.filename));
			String line;
			int i = 1;
			br.readLine();		// Kopfzeile überspringen.
			while ((line = br.readLine()) != null) {
				String[] entries = line.split(";");
				entityTypes.put(entries[0], i-1);
				i++;
			}
			br.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return entityTypes;
	}
}
