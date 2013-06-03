import java.awt.FontMetrics;


public class Window_Message extends Abstract_Update {

	private Window_Base window;
	
	Window_Message(String msg, int x, int y, Object_Game game) {
		super(game);
		window = new Window_Base(x, y, 100, 100, game);
	}

	@Override
	public void updateData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateScreen() {
		// TODO Auto-generated method stub
		
	}

}
