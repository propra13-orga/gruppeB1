import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

class System_Render extends System_Component {
	protected Object_Screen screen;
	protected int[] screen_point;
	
	public System_Render(Abstract_Scene scene, Object_Screen screen) {
		super(scene,"sprite","camera");
		this.screen = screen;
		this.screen_point = new int[2];
		this.screen_point[0] = 0;
		this.screen_point[1] = 0;
	}
	
	@Override
	public void update() {
		// Spritekomponente updaten.
		for (Entity entity : this.getEntitiesByType("sprite")) {
			this.updateSprite(entity);
		}
		// Kamera updaten.
		this.updateScreenPoint();
		//Die gesamte Map wird als Bild berechnet
		BufferedImage map = new BufferedImage(
				this.getCurrentLevel().getWidth()*Object_Level.TILESIZE,
				this.getCurrentLevel().getHeight()*Object_Level.TILESIZE,
				BufferedImage.TYPE_INT_ARGB);
		this.getCurrentLevel().drawTiles(map, Object_TileSet.BELOW_SPRITE);
		this.getCurrentLevel().drawTiles(map, Object_TileSet.SAME_LEVEL_AS_SPRITE);
		
		//Sprites werden drauf gezeichnet
		drawSprites(map);
		this.getCurrentLevel().drawTiles(map, Object_TileSet.ABOVE_SPRITE);
		//Das sichtbare Feld um den Spieler wird ausgeschnitten...
		map = map.getSubimage(
				screen_point[0],
				screen_point[1],
				Object_Screen.SCREEN_W,
				Object_Screen.SCREEN_H);
		//...und angezeigt. Aber erst wird geschaut, ob der Spieler Schaden
		// genommen hat.
		
		if (!this.checkPlayerDMG()) {
			this.screen.getBuffer().getGraphics().drawImage(map,0,0,null);
		}
		this.displayStats();
	}
	
	
	/*
	 * Privates.
	 */
	
	/*
	 * Hat der Spieler Schaden genommen, so soll der Bildschirm einmal kurz
	 * rot aufblitzen.
	 */
	private boolean checkPlayerDMG() {
		if (!this.getEvents(EventType.PLAYERDMG).isEmpty()) {
			Graphics g = this.screen.getBuffer().getGraphics();
			g.clearRect(0, 0, Object_Screen.SCREEN_W, Object_Screen.SCREEN_H);
			g.setColor(new Color(153,0,0));
			g.fillRect(0, 0, Object_Screen.SCREEN_W, Object_Screen.SCREEN_H);
//			try {
//				Thread.sleep(30);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			return true;
		}
		return false;
	}
	
	private void displayStats() {
		Entity player = this.getScene().getPlayer();
		Component_Health compHealth = (Component_Health) player.getComponent("health");
		int hp = compHealth.getHP();
		float fraction = (float) hp / (float) compHealth.getMaxHP();
				
		String s = String.format("HP: %d", hp);
		Graphics g = this.screen.getBuffer().getGraphics();
		if (fraction < 0.3) g.setColor(new Color(200,0,0));
		else if (fraction > 0.7) g.setColor(new Color(0,200,0));
		else g.setColor(new Color(200,200,0));
		g.drawString(s,10,Object_Screen.SCREEN_H-10);
	}
	
	private Component_Sprite getSprite(Entity entity) { 
		return (Component_Sprite) entity.getComponent("sprite"); 
	}
	
	/*
	 * Aktualisiert alle Sprites (also Sprite-Komponenten) in Abhängigkeit von
	 * der Positions- und Bewegungsdaten der jeweiligen Entitäten.
	 */
	private void updateSprite(Entity entity) {
		Component_Movement compMovement = (Component_Movement) entity.getComponent("movement");
		Component_Sprite compSprite = this.getSprite(entity);
		// Ändere die Ausrichtung des Sprites entsprechend der aktuellen 
		// "tatsächlichen" Bewegungsrichtung.
		compSprite.setDirection(compMovement.getOrientation());
		
		/*
		 * Der folgende Abschnitt bewegt den Sprite auf dem Bildschirm in
		 * Abhängigkeit vom gewählten Delay. Aber nur, wenn sich der Spieler
		 * bewegt und der aktuelle Tick größer als 0 ist. Ansonsten wird die
		 * Anzeigeposition anhand der neuen Kachelposition bestimmt.
		 */
		if (compMovement.isMoving() && compMovement.getTick() > 1) {
			int newpos = 0;
			// Der Offset bestimmt die Anzahl an Pixeln, die der Sprite bewegt
			// werden soll. Die Pixelanzahl ist abhängig vom Delay und der
			// Kachelgröße.
			int offset = Object_Level.TILESIZE / compMovement.getDelay();
			switch(compMovement.getOrientation()) {
			case 1:
				compSprite.addToY(-offset);
				newpos = compSprite.getY();
				break;
			case 2:
				compSprite.addToY(offset);
				newpos = compSprite.getY();
				break;
			case 3:
				compSprite.addToX(-offset);
				newpos = compSprite.getX();
				break;
			case 4:
				compSprite.addToX(offset);
				newpos = compSprite.getX();
				break;
			}
			
			// newpos gibt jetzt den Abstand von der letzten Kachel an.
			newpos = Math.abs(newpos) % Object_Level.TILESIZE;
			
			// Hier wird entschieden, welche Animationsgrafik angezeigt wird.
			if (newpos < Object_Level.TILESIZE-16) {
				if (compSprite.getOldAnimation() == Component_Sprite.ANIMATION_LEFT) {
					compSprite.setAniRight();
				}
				else {
					compSprite.setAniLeft();
				}
			}
			else {
				compSprite.setAniMiddle();
			}
		}
		else {
			if (compMovement.getTick() == 1) {
				if (compSprite.getOldAnimation() == Component_Sprite.ANIMATION_LEFT) {
					compSprite.setOldAnimation(Component_Sprite.ANIMATION_RIGHT);
				}
				else compSprite.setOldAnimation(Component_Sprite.ANIMATION_LEFT);
			}
			compSprite.setAniMiddle();
			compSprite.setX(compMovement.getX());
			compSprite.setY(compMovement.getY());
		}	
	}
	
	
	private void updateScreenPoint() {
		Entity camera = this.getEntitiesByType("camera").get(0);

		Component_Sprite compSprite = this.getSprite(camera);		
		screen_point[0] = compSprite.getX() - (Object_Screen.SCREEN_W / 2);
		screen_point[1] = compSprite.getY() - (Object_Screen.SCREEN_H / 2) + 16;
		if (screen_point[0] < 0) screen_point[0] = 0;
		if (screen_point[0] > this.getCurrentLevel().getWidth()*Object_Map.TILESIZE-Object_Screen.SCREEN_W) {
			screen_point[0] = this.getCurrentLevel().getWidth()*Object_Map.TILESIZE-Object_Screen.SCREEN_W;
		}
		if (screen_point[1] < 0) screen_point[1] = 0;
		if (screen_point[1] > this.getCurrentLevel().getHeight()*Object_Map.TILESIZE-Object_Screen.SCREEN_H) {
			screen_point[1] = this.getCurrentLevel().getHeight()*Object_Map.TILESIZE-Object_Screen.SCREEN_H;
		}
	}

	private void drawSprites(BufferedImage screen) {
		for (Entity entity : this.getEntitiesByType("sprite")) {
			Component_Sprite compSprite = this.getSprite(entity);
			screen.getGraphics().drawImage(
					compSprite.getLowerHalf(),
					compSprite.getX(),
					compSprite.getY(),
					null);
		}
		
		for (Entity entity : this.getEntitiesByType("sprite")) {
			Component_Sprite compSprite = this.getSprite(entity);
			screen.getGraphics().drawImage(
					compSprite.getUpperHalf(),
					compSprite.getX(),
					compSprite.getY()-32,
					null);
		}
	}
	
	private Object_Level getCurrentLevel() {
		return ((Scene_Level) this.scene).getCurrentLevel();
	}
	
	
}