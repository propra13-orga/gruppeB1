import java.awt.FontMetrics;
import java.util.Arrays;

/*
 * Window_DialogBox.java
 * 
 * Dient zur Anzeige einer Textbox am unteren Bildschirmrand. Der Text ist 3
 * Zeilen lang und wird automatisch umgebrochen. Sollte der Text länger sein,
 * wird der Rest in der Variablen remainder gespeichert.
 */

public class Window_DialogBox extends Abstract_Update {

	private Window_Base window;
	private String[] message;
	private String remainder;
	private FontMetrics metrics;
	
	private static final int PADDING = 8;
	private static final int LINES = 4;
	private static final int HEIGHT = 110; //80
	private static final int BORDER = 8;
	
	Window_DialogBox(String msg, Object_Game game) {
		super(game);
		window = new Window_Base(game, BORDER,
				Object_Screen.SCREEN_H-HEIGHT-BORDER,
				Object_Screen.SCREEN_W-2*BORDER,
				HEIGHT);
		this.screen.setFont(Object_Game.FONT);
		this.metrics = this.screen.getFontMetrics();
		this.message = new String[LINES];
		this.remainder = "";
		this.prepareString(msg);
		
	}

	@Override
	public void updateData() { }

	@Override
	public void updateScreen() {
		this.window.updateScreen();
		this.drawMessage();
	}
	
	public String getRemainder() { return this.remainder; }
	
	private void prepareString(String message) {
		// Teile den Text entlang der Wortgrenzen.
		String[] split = message.split(" ");
		String line = "";
		int j = 0;			// Wortzähler.
		for (int i=0;i<LINES;i++) {		// Zeilenweise
			/*
			 * Betrachte das aktuelle Wort und schaue, ob es noch in die Zeile
			 * passt. Wenn ja, füge es der Zeile hinzu, ansonsten speichere
			 * die Zeile im Array message.
			 */
			for (int k=j;k<split.length;k++) {
				if (this.metrics.stringWidth(line+split[k]) < window.width-PADDING*2) {
					line += split[k]+" ";
					j++;
				}
				else {
					break;
				}
			}
			this.message[i] = line;
			line = "";
		}
		/*
		 * Wenn der Wortzähler nicht bis zum Ende gelaufen ist, gibt es noch
		 * verbleibenden Text. Speichere diesen in der Variablen remainder.
		 */
		if (j < split.length) {
			this.remainder = this.joinString(Arrays.copyOfRange(split, j, split.length));			
		}
	}
	
	/*
	 * Fügt ein String-Array zu einem String zusammen mit " " als Trennsymbol.
	 */
	private String joinString(String[] strarr) {
		String outstr = "";
		for (String s : strarr) outstr += s+" ";
		return outstr.trim();
	}
	
	
	private void drawMessage() {
		int text_x = window.x+PADDING;
		int text_y = window.y+metrics.getAscent()+PADDING;
		
		for (String s : this.message) {
			this.screen.drawString(s, text_x, text_y);
			text_y += metrics.getHeight();
		}

	}
}
