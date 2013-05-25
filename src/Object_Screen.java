import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

/*
 * Screen.java
 * Die Screenklasse definiert größtenteils nur konstanten und stellt über ein
 * JFrame den Bildschirm zur Verfügung.
 * Über getBuffer können alle Scenen auf den internen Bildschirmpuffer zugreifen
 * um den Bildschirm innerhalb eines Frames zu verändern und zu aktualisieren.
 * Die Methode update wird nur vom Game Objekt aufgerufen, NACHDEM scene.update
 * aufgerufen wurde.
 * Dies garantiert, dass alle Bearbeitungen am Bildschirm abgeschlossen sind, wenn
 * er (für den Benutzer sichtbar) aktualisiert wird.
 * 
 * Die statische Methode makeTransparent ersetzt in einem BufferedImage jedes Pixel
 * mit dem RGB Wert (255, 0, 255) bzw. 0xFF00FF durch ein trasparentes Pixel. Dadurch
 * ist es möglich, die Char- und Tilesets mit einer Hintergrundfarbe zu versehen!!!
 */

public class Object_Screen extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	static int SCREEN_W = 640;
	static int SCREEN_H = 480;
	static int VISIBLE_TILES_X = SCREEN_W / Object_Map.TILESIZE;
	static int VISIBLE_TILES_Y = SCREEN_H / Object_Map.TILESIZE;
	static Color TRANSPARENT = new Color(255, 0, 255, 255);
	
	private BufferedImage buffer;
	private JPanel board;
	
	Object_Screen() {
		//JFrame initialisieren
		super();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		board = new JPanel();
		board.setPreferredSize(new Dimension(SCREEN_W, SCREEN_H));
		getContentPane().add(board);
		pack();
		//Rest initialisieren
		buffer = new BufferedImage(SCREEN_W, SCREEN_H, BufferedImage.TYPE_INT_ARGB);
		buffer.getGraphics().setFont(new Font("Arial", Font.PLAIN, 32));
	}
	
	public void update() {
		//Screen.update wird als letzte Methode im Frameloop aufgerufen
		//Die aktuelle Scene hat den buffer im aktuellen Frame also vollständig angepasst.
		//Jetzt kann er auch neu auf den Screen gezeichnet werden
		board.getGraphics().drawImage(buffer, 0, 0, this);
	}
	
	public BufferedImage getBuffer() {
		return buffer;
	}
	
	public JPanel getBoard() {
		return board;
	}
	
	public void clear() {
		buffer.getGraphics().clearRect(0, 0, SCREEN_W, SCREEN_H);
	}
	
	public static void makeTransparent(BufferedImage b) {
		int width = b.getWidth();
		int height = b.getHeight();
		for (int y=0; y<height; y++) {
			for (int x=0; x<width; x++) {
				int rgb = b.getRGB(x, y) & 0x00FFFFFF;
				if (rgb == 0xFF00FF) {
					b.setRGB(x, y, 0);
				}
			}
		}
	}
}
