import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.List;


public class Object_DBReader {
	public static final String DIR = "res/db/";
	
	private Map<String,Map<String,String>> content;

	public Object_DBReader() {
		this.content = new HashMap<String,Map<String,String>>();
	}
	
	public Map<String,String> getProperties(String entityType) {
		if (!content.containsKey(entityType)) {
			content.put(entityType,this.getPropertiesFromFile(entityType+".et"));
			
		}
		return content.get(entityType);
	}
	
	private Map<String,String> getPropertiesFromFile(String fname) {
		Map<String,String> properties = new Object_EntityData();
		try {
			BufferedReader br = new BufferedReader(new FileReader(DIR+fname));
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
