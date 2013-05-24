class Component_Health extends Component {
	
	private int hp;
	private int maxhp;
	
	public Component_Health(Entity entity, System_Component system, int maxhp) {
		super("health",entity,system);
		this.maxhp = maxhp;
		this.hp = maxhp;
	}
	
	// Getters
	
	public int getMaxHP() { return this.maxhp; }
	public int getHP() { return this.hp; }
	
	// Setters
	
	public void setMaxHP(int hp) { this.maxhp = hp; }
	public void addHP(int hp) { 
		this.hp += hp;
		if (this.hp > this.maxhp) this.hp = this.maxhp;
	}
	public void discountHP(int hp) { 
		this.hp -= hp;
		if (this.hp < 0) this.hp = 0;
	}
}
