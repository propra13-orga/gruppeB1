import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Das Windowsystem bietet eine einfache Schnittstelle zum Anzeigen von Nachrichten, Menues und Dialogen im Spiel.
 * Window_Base stellt dabei die wichtigsten Grundfunktionen zur Verfuegung und sollte nicht selbst instanziiert werden.
 * 
 * @author Alexander
 * 
 * @param WINDOWSYSTEM		Gibt den Dateinamen der Grafik an, welche zum generieren des Fesnters genutzt wird
 * @param x					x und y bilden die Koordinaten des linken oberen Randes des Fensters
 * @param x					x und y bilden die Koordinaten des linken oberen Randes des Fensters
 * @param width				Gibt die gesamte Breite des Fenster an (ggf. auch mit Rand)
 * @param height			Gibt die gesamte Hoehe des Fensters an (ggf. auch mit Rand)
 * @param box				Da die Fenstergrafik nur einmal generiert werden muss, wird sie anschliessend hier gespeichert
 * @param set				Speichert die genutzte Windowset Grafik
 *
 */

public class Window_Base extends Abstract_Update {

	public static final String		WINDOWSYSTEM = "window-1";
	
	protected int					x;
	protected int					y;
	protected int					width;
	protected int					height;
	
	private BufferedImage			box;
	private BufferedImage			set;
	
	/**
	 *
	 * @param game		
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	
	Window_Base(Object_Game game, int x, int y, int width, int height) {
		super(game);
		if (width < 0) width = 0;
		if (height < 0) height = 0;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		//Windowset laden
		
		String path = "res/windowset/"+WINDOWSYSTEM+".png";
		this.set = new BufferedImage(96, 96, BufferedImage.TYPE_INT_ARGB);
		try {
			this.set.getGraphics().drawImage(ImageIO.read(new File(path)),0,0,null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Object_Screen.makeTransparent(this.set);
		
		refresh_box();
	}

	@Override
	public void updateData() {}

	@Override
	public void updateScreen() {
		this.screen.drawImage(this.box, this.x, this.y, null);
	}
	
	/**
	 * Sollten sich Position, Form oder Windowset aendern, kann ueber refresh_box() die Fenstergrafik neu generiert werden
	 */
	
	public void refresh_box() {
		//Box zeichnen
		
				if (this.width <= 0 || this.height <= 0) {
					return;
				}
				int i;
				int j;
				this.box = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_ARGB);
				for (j=Object_Map.TILESIZE; j<=this.height-Object_Map.TILESIZE; j+=Object_Map.TILESIZE) {
					for (i=Object_Map.TILESIZE; i<=this.width-Object_Map.TILESIZE; i+=Object_Map.TILESIZE) {
						this.box.getGraphics().drawImage(
								this.set.getSubimage(
										Object_Map.TILESIZE,
										Object_Map.TILESIZE,
										Object_Map.TILESIZE,
										Object_Map.TILESIZE),i,j,null);						//Hintergrund
					}
				}
				for (i=Object_Map.TILESIZE; i<=this.width-Object_Map.TILESIZE; i+=Object_Map.TILESIZE) {
					this.box.getGraphics().drawImage(
							this.set.getSubimage(Object_Map.TILESIZE, 0, Object_Map.TILESIZE, Object_Map.TILESIZE),i,0,null);							//Top Leiste
					this.box.getGraphics().drawImage(
							this.set.getSubimage(Object_Map.TILESIZE, (2*Object_Map.TILESIZE), Object_Map.TILESIZE, Object_Map.TILESIZE),i,this.height-Object_Map.TILESIZE,null);			//Top Leiste
				}
				for (i=Object_Map.TILESIZE; i<=this.height-Object_Map.TILESIZE; i+=Object_Map.TILESIZE) {
					this.box.getGraphics().drawImage(
							this.set.getSubimage(0, Object_Map.TILESIZE, Object_Map.TILESIZE, Object_Map.TILESIZE),0,i,null);							//Top Leiste
					this.box.getGraphics().drawImage(
							this.set.getSubimage((2*Object_Map.TILESIZE), Object_Map.TILESIZE, Object_Map.TILESIZE, Object_Map.TILESIZE),this.width-Object_Map.TILESIZE,i,null);				//Top Leiste
				}
				this.box.getGraphics().drawImage(
						this.set.getSubimage(0, 0, Object_Map.TILESIZE, Object_Map.TILESIZE),0,0,null);								//TopLeft Rahmen
				this.box.getGraphics().drawImage(
						this.set.getSubimage((2*Object_Map.TILESIZE), 0, Object_Map.TILESIZE, Object_Map.TILESIZE),this.width-Object_Map.TILESIZE,0,null);					//TopRight Rahmen
				this.box.getGraphics().drawImage(
						this.set.getSubimage(0, (2*Object_Map.TILESIZE), Object_Map.TILESIZE, Object_Map.TILESIZE),0,this.height-Object_Map.TILESIZE,null);					//BottomLeft Rahmen
				this.box.getGraphics().drawImage(
						this.set.getSubimage((2*Object_Map.TILESIZE), (2*Object_Map.TILESIZE), Object_Map.TILESIZE, Object_Map.TILESIZE),this.width-Object_Map.TILESIZE,this.height-Object_Map.TILESIZE,null);	//BottomRight Rahmen
	}

}
