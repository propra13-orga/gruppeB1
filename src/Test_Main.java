import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;


public class Test_Main {
	
	BufferedWriter bw;
	static int Romm_i;
	static int Layer_i;
	
	public Test_Main(){
		
	}
	
	public static void main(String[] args) {
		
		
		Test_Main Test = new Test_Main();
		
		Object_LevelData test = new Object_LevelData("res/");
		
		System.out.println("Die Anzahle von Room[]:\n"+test.size_tmx);
		
		for(Romm_i = 0; Romm_i < test.size_tmx; Romm_i++){
			
			List a = test.Room[Romm_i].getEntityData();
				
			Iterator it=a.iterator();
				 
			while(it.hasNext()){
				
				 System.out.println("Entity von Romm"+Romm_i+it.next());
				   
			}
			
		}
			
		
		for(Romm_i = 0; Romm_i < test.size_tmx; Romm_i++){
		
			for(Layer_i = 0; Layer_i < 3; Layer_i++){
				
				Test.output_2D(test.Room[Romm_i].room[Layer_i]);
				
			}
			
			Layer_i = 0;
			
		}
		

	    System.out.println(test.tmx.entrySet());
		System.out.println(test.csv.entrySet());
		
	}
	
	
	public void output_2D(int[][] Array){
		
		try {
			String romm_index = String.valueOf(Romm_i);
			String layer_index = String.valueOf(Layer_i);
			
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("res/Room"+romm_index+"_Layer"+layer_index+".txt")));
			String line = null;
			
			try {
				
				for(int i = 0; i < Array.length; i++){
					for(int j = 0; j < Array[i].length; j++){
						
							bw.write(String.valueOf(Array[i][j])+"\t");

							bw.flush();	
						
					}
					
					bw.write("\r\n");
					
				}
				

			} catch (IOException e) {
				e.printStackTrace();
			}  
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}

}
