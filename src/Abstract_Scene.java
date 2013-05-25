import java.awt.Graphics;

/*
 * Abstract_Scene.java
 * 
 * HIER FINDET IHR DIE ERKLÄRUNG ZUM VERBESSERTEN SZENENSYSTEM
 * 
 * Jede Scene erbt ab nun nicht mehr von der Klasse Scene, sondern von der Klasse
 * Abstract_Scene. Im Grunde ist das für euch letztendlich das selbe, nur hat die
 * Klasse jetzt von Anfang an etwas mehr zu bieten:
 * 
 * 1. Ihr habt jetzt nicht nur eine Referenz auf das Game Objekt sondern auch
 * auf den Screen (this.screen) und auf den keyhandler (this.keyhandler).
 * WICHTIG! this.screen ist kein BufferedImage sondern ein Graphics Objekt. Dadurch
 * könnt ihr sofort zeichnen, was ihr ja auch wollt z.B.
 * 
 * 				this.screen.drawString("Hallo, 100, 100);
 * 
 * aber merkt euch trotzdem, dass ihr an das tatsächliche Screenobjekt nur über
 * game.getScreen() kommt.
 * 
 * 2. Die Methode update() ist für euch jetzt nicht mehr aufrufbar (wurde als final)
 * deklariert). Dafür habt ihr zwei neue Methoden updateData() und updateScreen() zur
 * Verfügung. Das dient der besseren lesbarkeit des Programms und einer klaren Trennung
 * von beidem.
 * 
 * 3. Die Klasse Abstract_Scene implementiert ein Interface Interface_Scene, welches
 * nur für diese Klasse geschrieben wurde, das Interface selbst braucht euch also
 * absolut nicht zu kümmern. Dies dient dazu, dass die in Abstract_Scene definnierten
 * Methoden auch umgesetzt werden MÜSSEN, es greift euch sozusagen ein bisschen unter
 * die Arme und "erinnert" euch daran, welche Methoden umgesetzt werden müssen.
 * 
 * 4. Es sind die Methoden onStart() und onExit() dazugekommen, da bin ich noch nicht
 * ganz fertig mit also kümmert euch gar nicht darum.
 * 
 * Wenn ihr jetzt also eine neue Scene erstellt, lasst sie einfach von Abstract_Scene
 * erben, Eclipse meckert dann schon, dass die vorgegebenen Methoden implementiert
 * werden müssen und fügt sie euch hinzu.
 * 
 * 
 * 
 * ERKLÄRUNG ZU UNTERSZENEN
 * 
 * Im Zuge dessen habe ich gleich auch sogenannte SubScenes erstellt. Das sind
 * "Scenes", welche innerhalb von anderen Scenes aufgerufen werden (z.B. ein
 * Menü in einer Szene). Der Unterschied besteht lediglich darin, dass SubScenes
 * keine update() Methode besitzen, sondern nur updateData() und updateScreen().
 * Diese können dann gesondert aufgerufen werden und ihr könnt deren Methoden
 * einfach in die jeweiligen Methoden eurer Scene integrieren. Schaut euch als
 * Beispiel einfach mal Scene_StartMenu an.
 * 
 * Bei Fragen einfach melden ;) und Falls ihr nicht ganz mitkommen solltet, nächste
 * Woche treffen wir uns alle zusammen und ich erkläre alles nochmal in Ruhe.
 * 
 */

abstract class Abstract_Scene implements Interface_Scene {
	
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
