import java.awt.Graphics;

/*
 * Abstract_Scene.java
 * 
 * HIER FINDET IHR DIE ERKL�RUNG ZUM VERBESSERTEN SZENENSYSTEM
 * 
 * Jede Scene erbt ab nun nicht mehr von der Klasse Scene, sondern von der Klasse
 * Abstract_Scene. Im Grunde ist das f�r euch letztendlich das selbe, nur hat die
 * Klasse jetzt von Anfang an etwas mehr zu bieten:
 * 
 * 1. Ihr habt jetzt nicht nur eine Referenz auf das Game Objekt sondern auch
 * auf den Screen (this.screen) und auf den keyhandler (this.keyhandler).
 * WICHTIG! this.screen ist kein BufferedImage sondern ein Graphics Objekt. Dadurch
 * k�nnt ihr sofort zeichnen, was ihr ja auch wollt z.B.
 * 
 * 				this.screen.drawString("Hallo, 100, 100);
 * 
 * aber merkt euch trotzdem, dass ihr an das tats�chliche Screenobjekt nur �ber
 * game.getScreen() kommt.
 * 
 * 2. Die Methode update() ist f�r euch jetzt nicht mehr aufrufbar (wurde als final)
 * deklariert). Daf�r habt ihr zwei neue Methoden updateData() und updateScreen() zur
 * Verf�gung. Das dient der besseren lesbarkeit des Programms und einer klaren Trennung
 * von beidem.
 * 
 * 3. Die Klasse Abstract_Scene implementiert ein Interface Interface_Scene, welches
 * nur f�r diese Klasse geschrieben wurde, das Interface selbst braucht euch also
 * absolut nicht zu k�mmern. Dies dient dazu, dass die in Abstract_Scene definnierten
 * Methoden auch umgesetzt werden M�SSEN, es greift euch sozusagen ein bisschen unter
 * die Arme und "erinnert" euch daran, welche Methoden umgesetzt werden m�ssen.
 * 
 * 4. Es sind die Methoden onStart() und onExit() dazugekommen, da bin ich noch nicht
 * ganz fertig mit also k�mmert euch gar nicht darum.
 * 
 * Wenn ihr jetzt also eine neue Scene erstellt, lasst sie einfach von Abstract_Scene
 * erben, Eclipse meckert dann schon, dass die vorgegebenen Methoden implementiert
 * werden m�ssen und f�gt sie euch hinzu.
 * 
 * 
 * 
 * ERKL�RUNG ZU UNTERSZENEN
 * 
 * Im Zuge dessen habe ich gleich auch sogenannte SubScenes erstellt. Das sind
 * "Scenes", welche innerhalb von anderen Scenes aufgerufen werden (z.B. ein
 * Men� in einer Szene). Der Unterschied besteht lediglich darin, dass SubScenes
 * keine update() Methode besitzen, sondern nur updateData() und updateScreen().
 * Diese k�nnen dann gesondert aufgerufen werden und ihr k�nnt deren Methoden
 * einfach in die jeweiligen Methoden eurer Scene integrieren. Schaut euch als
 * Beispiel einfach mal Scene_StartMenu an.
 * 
 * Bei Fragen einfach melden ;) und Falls ihr nicht ganz mitkommen solltet, n�chste
 * Woche treffen wir uns alle zusammen und ich erkl�re alles nochmal in Ruhe.
 * 
 */

abstract class Abstract_Scene implements IScene {
	
	protected Object_Game game;
	protected Graphics screen;
	protected Object_KeyHandler keyhandler;
	
	Abstract_Scene(Object_Game game) {
		this.game = game;
		this.screen = game.getScreen().getBuffer().getGraphics();
		this.keyhandler = game.getKeyHandler();
	}
	
	abstract public void onStart();
	abstract public void onExit();
	abstract public void updateData();
	abstract public void updateScreen();
	
	public final void update() {
		updateData();
		updateScreen();
	}

}
