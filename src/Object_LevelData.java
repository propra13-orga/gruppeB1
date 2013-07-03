import java.io.File;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;


public class Object_LevelData {
	
	File dir;
	File[] files;
	String add = null;
	int size_tmx = 0;
	int size_csv = 0;
	int index = 0;	

	LinkedHashMap tmx = new LinkedHashMap();    
	LinkedHashMap csv = new LinkedHashMap();    
	
	
	Object_RoomData[] Room;
	Object_EntityData[] Entity_Data;
	
	String address;


	public Object_LevelData(String address){
	
		this.address = address;
		File dir = new File(address);
		File[] files = dir.listFiles();
	
		//alle file auf der Weise von Tmx und Csv zuverteilen.
		if (files == null){
			return;
		}else{
			for(int i = 0; i < files.length; i++){
				if(files[i].isDirectory()){
					System.out.println("Es gibt noch Folder");
				}else{
					String Path = files[i].getPath();
					System.out.println(Path);
					String Name = files[i].getName();
					System.out.println(Name);
					String ArrayName[] = Name.split("\\.");
					System.out.println(ArrayName[1]);
					if(ArrayName[1].equals("tmx")){
						tmx.put(ArrayName[0],Path);
						//System.out.println(tmx.entrySet());
					}
					if(ArrayName[1].equals("csv")){
						csv.put(ArrayName[0],Path);
					}
					/*else{
						System.out.println("\nFile "+ArrayName[0]+"."+ArrayName[1]+" ist nicht *.csv oder *.tmx");
					}*/
					
				}
						
			}
						
		}
		
		//die Anzahle von Room sind size_tmx.
		//und dann alle Instanz von Room erzeugen.
		size_tmx = tmx.size();
		Room = new Object_RoomData[size_tmx];
		for(int k=0;k<size_tmx;k++){
			Room[k]=new Object_RoomData();
		}
		
		//alle Entity von dieser Room ins Room's Instanz hinzufugen.
		for (Iterator it =  csv.keySet().iterator();it.hasNext();){
			
			Object key = it.next();
		    String csv_add = (String) csv.get(key);		
			Object_EntityData Entity_Data = new Object_EntityData(csv_add);
			for(int i = 0; i < Entity_Data.Size_Entity; i++){
				Room[index].addEntityData(Entity_Data.Entity_List[i]);
			}

		index++;
 
		}
		
	    index = 0;
		/*
		System.out.println(tmx.entrySet());
		System.out.println(csv.entrySet());
		*/
		
	  //alle Daten von Map dieser Room ins Room's Instanz hinzufugen.
		for (Iterator it =  tmx.keySet().iterator();it.hasNext();){
		    
			Object key = it.next();	
		    add = (String) tmx.get(key); 	
		    Object_MapData RoomMap;
			RoomMap = new Object_MapData(add);
			Room[index].setRoom(RoomMap.num);		
			index++;

		   }
		
		
		/*
		for(int i = 0; i <Room[0].room.length; i++){
			for(int j = 0; j <Room[0].room[i].length; j++){
				for(int k=0; k<Room[0].room[i][j].length; k++){
					System.out.println(Room[0].room[i][j][k]);
				}
			}
		}
		*/
    }
	
}

	


