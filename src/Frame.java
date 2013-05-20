
import java.util.Date;


public class Frame extends Thread  {

    static int SLEEP_TIME = 10;
	
	public void run(){
		
		Game game = new Game();
		long time;
		long timeDiff;
		Date date = new Date();
		
		//Gameloop
		while (game.scene != null) {
			
			time = date.getTime();
			
			try {
				game.update();
			}
			catch (Exception e) {
				//Irgendeine unerwartete Exception
				e.printStackTrace();
			}
			
			timeDiff = date.getTime() - time;
			
			if (SLEEP_TIME-timeDiff > 0) {
				try {
					Thread.sleep(SLEEP_TIME-timeDiff);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		System.exit(0);
	}

}
