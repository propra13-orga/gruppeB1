
public class Test_Main {
	
	public Test_Main(){
		
	}
	
	public static void main(String[] args) {
		
		/*
		fuer test
		*/
		
		Object_LevelData test = new Object_LevelData("res/");
		
		System.out.println(test.Room[0]);
		int[][][] array = test.Room[0].room;
		
		
		for(int i = 0; i < test.Room[1].room.length; i++){
			for(int j = 0; j < test.Room[1].room[i].length; j++){
				for(int k=0; k<test.Room[1].room[i][j].length; k++){
					System.out.println(test.Room[1].room[i][j][k]);
				}
			}
		}
		
		System.out.println(test.tmx.entrySet());
		System.out.println(test.csv.entrySet());
		
		
	}

}
