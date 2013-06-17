import java.io.File;
import java.util.Iterator;
import java.util.LinkedHashMap;


public class Object_LevelData {
	
	File dir;
	File[] files;
	String add = null;
	int size = 0;
	int index = 0;	

	LinkedHashMap tmx = new LinkedHashMap();    
	LinkedHashMap csv = new LinkedHashMap();    
	Object_RoomData[] Room;


	public Object_LevelData(String address){
	
		File dir = new File(address);
		File[] files = dir.listFiles();
	
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
					}
					if(ArrayName[1].equals("csv")){
						csv.put(ArrayName[0],Path);
					}
					
				}
						
			}
						
		}
		
		size = tmx.size();
		Room = new Object_RoomData[size];
		for(int k=0;k<size;k++){
			Room[k]=new Object_RoomData();
		}
			
		/*
		System.out.println(tmx.entrySet());
		System.out.println(csv.entrySet());
		*/
		
		
		for (Iterator it =  tmx.keySet().iterator();it.hasNext();){
		    
			Object key = it.next();
		    System.out.println( key+"="+ tmx.get(key));		
		    add = (String) tmx.get(key); 
		    System.out.println(add);	
		    Object_MapData RoomMap;
			RoomMap = new Object_MapData(add);
			Room[index].setRoom(RoomMap.num);		
			System.out.println(index);
			index++;

		   }
		
		int[][][] test = Room[0].getRoom();
		System.out.println("weiduceshi ");
		System.out.println(test.length);
		
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

	


