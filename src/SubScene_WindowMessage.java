import java.awt.FontMetrics;


public class SubScene_WindowMessage extends Abstract_SubScene {

	private SubScene_WindowBase window;
	
	SubScene_WindowMessage(String msg, int x, int y, Object_Game game) {
		super(game);
		window = new SubScene_WindowBase(x, y, 100, 100, game);
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
