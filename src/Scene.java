
/*
 * Scene.java
 * Superklasse aller Scenes. Es wird lediglich eine Referenz auf ein Game
 * Objekt erwartet und die Methode update, welche vom Gameobjekt aus aufgerufen wird.
 */

public class Scene {

	protected Game game;
	
	Scene(Game g) {
		game = g;
	}
	
	public void update() {};

}
