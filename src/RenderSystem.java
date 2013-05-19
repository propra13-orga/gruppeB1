class RenderSystem extends ComponentSystem {
	protected Screen screen;
	protected int[] screenPoint;
	
	public RenderSystem(Scene scene, Screen screen) {
		super(scene,"renderable","sprite","camera");
		this.screen = screen;
		this.screenPoint = new int[2];
		this.screenPoint[0] = 0;
		this.screenPoint[1] = 0;
	}
	
	@Override
	public void update() {
		screen.getBuffer().getGraphics().clearRect(0, 0, screen.SCREEN_W, screen.SCREEN_H);
		this.drawLowMap();
		this.animations();
		this.check_scrolling();
		this.drawSprites();
	}
	
	
	
	/*
	 * Privates.
	 */
	
	private void animations() {
		for (Entity entity : this.getEntitiesByType("sprite")) {
			CompSprite compSprite = (CompSprite) entity.getComponent("sprite");
			CompMovement compMovement = (CompMovement) entity.getComponent("movement");
			
			if (compMovement.moving) compSprite.movecounter += compSprite.move_distance;
			if (compSprite.movecounter >= Map.TILESIZE) {
				compSprite.movecounter = 0;
				if (compSprite.old_animation == CompSprite.ANIMATION_LEFT) {
					compSprite.old_animation = CompSprite.ANIMATION_RIGHT;
				}
				else {
					compSprite.old_animation = CompSprite.ANIMATION_LEFT;
				}
				/*
				 * Setze die Movement-Komponente wieder auf bewegbar.
				 * Sollte hier eigentlich nicht stehen, sondern im
				 * MovementSystem! Könnte man beheben, indem man den Movecounter
				 * in die Bewegungskomponente setzt, womit man auch gleichzeitig
				 * die Bewegungsgeschwindigkeit umsetzen könnte. Die Animation
				 * würde dann in Abhängigkeit von dieser angezeigt werden.
				 */
				compMovement.setMoveable();
			}
		}
	}
	
	
	private void check_scrolling() {
		//Berechnet, ob die Karte gescrollt werden muss
		Entity focus = this.getEntitiesByType("camera").get(0);
		CompMovement compMovement = (CompMovement) focus.getComponent("movement");
		
		Map map = ((Scene_Level) this.scene).getCurrentLevel();
		
		boolean scrolling = false;
		
		// TODO Scrollt aus irgendeinem Grund viel zu weit.
		if (compMovement.isMoving()) {
			
			switch(compMovement.orientation) {
			case 1: //UP
				if (map.getHeight()-compMovement.getY()-1 <= Screen.VISIBLE_TILES_Y/2) break;
				if (compMovement.getY() >= Screen.VISIBLE_TILES_Y/2) {
					scrolling = true;
					this.screenPoint[1]--;
				}
				break;
			case 2: //DOWN
				if (compMovement.getY() <= Screen.VISIBLE_TILES_Y/2) break;
				if (map.getHeight()-compMovement.getY() > Screen.VISIBLE_TILES_Y/2) {
					scrolling = true;
					screenPoint[1]++;
				}
				break;
			case 3: //LEFT
				if (map.getWidth()-compMovement.getX() <= Screen.VISIBLE_TILES_X/2) break;
				if (compMovement.getX() >= Screen.VISIBLE_TILES_X/2) {
					scrolling = true;
					screenPoint[0]--;
				}
				break;
			case 4: //RIGHT
				if (compMovement.getX() <= Screen.VISIBLE_TILES_X/2) break;
				if (map.getWidth()-compMovement.getX() >= Screen.VISIBLE_TILES_X/2) {
					scrolling = true;
					screenPoint[0]++;
				}
				break;
			}
		}
		map.scrolling = scrolling;
	}
	
	private void drawLowMap() {
		Entity focus = this.getEntitiesByType("camera").get(0);
		CompMovement compMovement = (CompMovement) focus.getComponent("movement");
		CompSprite compSprite = (CompSprite) focus.getComponent("sprite");
		
		Map map = ((Scene_Level) this.scene).getCurrentLevel();
		//Zeichnet die LowMap (siehe Map.drawLowMapImage)
		//Hier wird jedoch scrolling miteinbezogen, das fertige Bild
		//wird also nur noch korrekt ausgerichtet
		int map_x = -this.screenPoint[0]*Map.TILESIZE;
		int map_y = -this.screenPoint[1]*Map.TILESIZE;
		
		if (compMovement.isMoving() && map.scrolling){
			switch (compMovement.orientation) {
			case 1: //UP
				map_y -= Map.TILESIZE - compSprite.movecounter;
				break;
			case 2: //DOWN
				map_y += Map.TILESIZE - compSprite.movecounter;
				break;
			case 3: //LEFT
				map_x -= Map.TILESIZE - compSprite.movecounter;
				break;
			case 4: //RIGHT
				map_x += Map.TILESIZE - compSprite.movecounter;
				break;
			}
		}
		this.screen.getBuffer().getGraphics().drawImage(
				map.getLowMapImage(),
				map_x,
				map_y,
				this.screen);
	}
	
	private void drawSprites() {
		Map map = ((Scene_Level) this.scene).getCurrentLevel();
		for (Entity entity : this.getEntitiesByType("sprite")) {
			CompSprite compSprite = (CompSprite) entity.getComponent("sprite");
			CompMovement compMovement = (CompMovement) entity.getComponent("movement");
			if (compSprite.movecounter > 0) {
				//ANIMATION
				if (compSprite.movecounter < Map.TILESIZE-10) {
					if (compSprite.old_animation == Sprite.ANIMATION_LEFT) compSprite.animation = Sprite.ANIMATION_RIGHT;
					else compSprite.animation = Sprite.ANIMATION_LEFT;
				}
				else {
					compSprite.animation = Sprite.ANIMATION_MIDDLE;
				}
			}
			
			int new_x = -100;
			int new_y = -100;
			
			if (map.scrolling) {
				switch (compMovement.orientation) {
				case 2: //DOWN
				case 1: //UP
					new_x = (compMovement.getOldX()-this.screenPoint[0])*Map.TILESIZE;
					new_y = (Screen.VISIBLE_TILES_Y/2)*Map.TILESIZE - Map.TILESIZE;
					break;
				case 3: //LEFT
				case 4: //RIGHT
					new_x = (Screen.VISIBLE_TILES_X/2)*Map.TILESIZE;
					new_y = (compMovement.getOldY()-this.screenPoint[1])*Map.TILESIZE - Map.TILESIZE;
					break;
				}
			}
			else {
				new_x = (compMovement.getOldX()-this.screenPoint[0])*Map.TILESIZE;
				new_y = (compMovement.getOldY()-this.screenPoint[1])*Map.TILESIZE - Map.TILESIZE;
				//Falls der Character sich nicht bewegt ist movecounter 0 und nichts
				//�ndert sich hier!
				switch (compMovement.orientation) {
				case 1: //UP
					new_y -= compSprite.movecounter;
					break;
				case 2: //DOWN
					new_y += compSprite.movecounter;
					break;
				case 3: //LEFT
					new_x -= compSprite.movecounter;
					break;
				case 4: //RIGHT
					new_x += compSprite.movecounter;
				}
			}
			this.screen.getBuffer().getGraphics().drawImage(
					compSprite.getImage(compMovement.orientation),
					new_x,
					new_y,
					this.screen);
		}
	}
}