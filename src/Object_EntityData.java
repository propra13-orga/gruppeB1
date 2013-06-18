import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;


public class Object_EntityData {

	private static final long serialVersionUID = -7494399952808211043L;
	
	int Size_Entity;
	LinkedHashMap[] Entity_List; 
	Object_DBBrowser db_Entity;
	
	public Object_EntityData(String path){
		
		db_Entity = new Object_DBBrowser(path);
		this.Size_Entity = db_Entity.entityTypes.size();
		list();
		
	}
	
	public void list(){
				
		Entity_List = new LinkedHashMap[Size_Entity];				
		
		for(int i = 0; i < Size_Entity; i++){
			
			int j = 0;

			Entity_List[i] = new LinkedHashMap();
			
			Iterator attribute = db_Entity.entityTypes.entrySet().iterator();
 
			for(Iterator header = db_Entity.headers.entrySet().iterator(); header.hasNext(); ){
				
				Map.Entry header_entry = (Map.Entry) header.next();
				Object key = header_entry.getKey();
				Entity_List[i].put(key, db_Entity.db[i][j]);
				j++;
				
			}
			
			System.out.println("\nEntity_List["+i+"]:\n"+Entity_List[i].entrySet()+"\n");
			
		}
		
	}	

}
