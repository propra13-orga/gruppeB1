import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.List;


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
	
	public Map<String,String> getProperties(String fname) {
		if (!content.containsKey(fname)) {
			content.put(fname,this.getPropertiesFromFile(fname));
			
		}
		return content.get(fname);
	}
	
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
