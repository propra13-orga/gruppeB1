import java.util.ArrayList;

public class Window_Selectable extends Window_Base {
	
	private ArrayList<String> commands;
	private int cursor;

	Window_Selectable(int x, int y, int width, int height, Game game) {
		super(x, y, width, height, game);
		commands = new ArrayList<String>();
		cursor = 0;
	}
	
	public void addCommand(String command) {
		commands.add(command);
	}
	
	public void update() {
		updateLogic();
		draw();
		
	}
	
	private void updateLogic() {
		
	}

<<<<<<< HEAD
}
=======
}
>>>>>>> 85c8cee54e8b7f323cca4d4494bfbc1927ca60ca
