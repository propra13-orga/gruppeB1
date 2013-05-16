import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLabel;
import javax.swing.JPanel;

/*
 * Screen.java
 * 
 * Hier befindet sich alles, was auf den Bildschirm und Grafiken zugreift.
 * Der Screen besitzt eine Liste von allen Sprites, die er in jedem Durchlauf aktualisiert.
 * Außerdem besitzt er eine Map, die auch regelmäßig aktualisiert wird.
 * Um später soetwas wie einen Titelbildschirm oder ein Menü zu bauen, könnte man
 * diese auch ganz einfach als Map gestalten, eben mit ein paar anderen Eigenschaften aber
 * das ist nicht weiter schwer (siehe Map.getLowMapImage)
 * 'buffer' ist ein BufferedImage, darauf wird letztendlich alles zuerst gezeichnet und wenn
 * alles fertig ist, wird das Bild neu angezeigt
 */

public class Screen extends JFrame {
	
	static int SCREEN_W = 640;
	static int SCREEN_H = 480;
	static int VISIBLE_TILES_X = SCREEN_W / Map.TILESIZE;
	static int VISIBLE_TILES_Y = SCREEN_H / Map.TILESIZE;
	
	private BufferedImage buffer;
	private JPanel board;
	
	Screen() {
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
}
