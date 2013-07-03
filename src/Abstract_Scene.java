import java.awt.Graphics;

/*
 * Abstract_Scene.java
 * 
 * HIER FINDET IHR DIE ERKLAERUNG ZUM VERBESSERTEN SZENENSYSTEM
 * 
 * Jede Scene erbt ab nun nicht mehr von der Klasse Scene, sondern von der Klasse
 * Abstract_Scene. Im Grunde ist das fuer euch letztendlich das selbe, nur hat die
 * Klasse jetzt von Anfang an etwas mehr zu bieten:
 * 
 * 1. Ihr habt jetzt nicht nur eine Referenz auf das Game Objekt sondern auch
 * auf den Screen (this.screen) und auf den keyhandler (this.keyhandler).
 * WICHTIG! this.screen ist kein BufferedImage sondern ein Graphics Objekt. Dadurch
 * koennt ihr sofort zeichnen, was ihr ja auch wollt z.B.
 * 
 * 				this.screen.drawString("Hallo, 100, 100);
 * 
 * aber merkt euch trotzdem, dass ihr an das tatsaechliche Screenobjekt nur ueber
 * game.getScreen() kommt. this.screen zeigt auf das Graphics Objekt vom Screen.
 * 
 * 2. Die Methode update() ist fuer euch jetzt nicht mehr aufrufbar (wurde als final)
 * deklariert). Dafuer habt ihr zwei neue Methoden updateData() und updateScreen() zur
 * Verfuegung. Das dient der besseren lesbarkeit des Programms und einer klaren Trennung
 * von beidem.
 * 
 * 3. Die Klasse Abstract_Scene implementiert ein Interface IScene, welches
 * nur fuer diese Klasse geschrieben wurde, das Interface selbst braucht euch also
 * absolut nicht zu kuemmern. Dies dient dazu, dass die in Abstract_Update definnierten
 * Methoden auch umgesetzt werden MUESSEN, es greift euch sozusagen ein bisschen unter
 * die Arme und "erinnert" euch daran, welche Methoden umgesetzt werden muessen.
 * 
 * Wenn ihr jetzt also eine neue Scene erstellt, lasst sie einfach von Abstract_Scene
 * erben, Eclipse meckert dann schon, dass die vorgegebenen Methoden implementiert
 * werden muessen und fuegt sie euch hinzu.
 * 
 * 
 * 
 * ERKLAERUNG ZU UNTERSZENEN (UPDATES)
 * 
 * Im Zuge dessen habe ich gleich auch sogenannte Updates erstellt. Das sind
 * "Scenes", welche innerhalb von anderen Scenes aufgerufen werden (z.B. ein
 * Menue in einer Szene). Der Unterschied besteht lediglich darin, dass Updates
 * keine einfache update() Methode besitzen, sondern nur updateData() und updateScreen().
 * Diese kOEnnen dann gesondert aufgerufen werden und ihr koennt deren Methoden
 * einfach in die jeweiligen Methoden eurer Scene integrieren. Schaut euch als
 * Beispiel einfach mal Scene_StartMenu an.
 * 
 */

abstract class Abstract_Scene implements IScene {
	
	protected Object_Game game;
	protected Graphics screen;
	protected Object_KeyHandler keyhandler;
	protected Object_SoundManager soundmanager;
	protected Object_AnimationManager animationmanager;
	
	Abstract_Scene(Object_Game game) {
		this.game = game;
		this.screen = game.getScreen().getBuffer().getGraphics();
		this.keyhandler = game.getKeyHandler();
		this.soundmanager = game.getSoundManager();
		this.animationmanager = game.getAnimationManager();
	}
	
	abstract public void onStart();
	abstract public void onExit();
	abstract public void updateData();
	abstract public void updateScreen();
	
	public void updateDataOnSwitching() {
		 //Eingabe und Datenverabrietung werden eingefroren
	 }
	
	public void updateScreenOnSwitching() {
		//Da updateData() nicht mehr aufgerufen wird, generiert updateScreen() immer das selbe
		//Bild, der Bildschirm wird also eingefroren
		this.updateScreen();
	}
	
	public final void update() {
		updateData();
		updateScreen();
	}

}
