class Component_Battle extends Abstract_Component {
	
	private int ap;
	private int hp,mp;
	private int maxhp,maxmp;
	
	public Component_Battle(Entity entity, System_Component system, 
			int maxhp, int maxmp, int ap) {
		super("health",entity,system);
		this.maxhp = maxhp;
		this.hp = maxhp;
		this.maxmp = maxmp;
		this.mp = maxmp;
		this.ap = ap;
	}
	
	// Getters
	
	public int getMaxHP() { return this.maxhp; }
	public int getHP() { return this.hp; }
	public int getMaxMP() { return this.maxmp; }
	public int getMP() { return this.mp; }
	public int getAP() { return this.ap; }
	
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
	public void setMaxMP(int mp) { this.maxmp = mp; }
	public void addMP(int mp) { 
		this.mp += mp;
		if (this.mp > this.maxmp) this.mp = this.maxmp;
	}
	public void discountMP(int mp) { 
		this.mp -= mp;
		if (this.mp < 0) this.mp = 0;
	}
	public void setAP(int ap) { this.ap = ap; }
}
