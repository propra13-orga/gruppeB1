
/*
 * Scene.java
 * Superklasse aller Scenes. Es wird lediglich eine Referenz auf ein Game
 * Objekt erwartet und die Methode update, welche vom Gameobjekt aus aufgerufen wird.
 */

abstract class Scene {

	protected Object_Game game;
	
	Scene(Object_Game g) {
		game = g;
	}
	
	abstract public void update();

}
