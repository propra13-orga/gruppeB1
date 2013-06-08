class Component_Battle extends Abstract_Component {
	
	private int atk, def, dex, spd, maxspd;
	private int hp,mp;
	private int maxhp,maxmp;
	private String sprite;
	
	public Component_Battle(Entity entity, System_Component system, 
			int maxhp, int maxmp, int atk, int def, int maxspd,
			int dex, String sprite) {
		super("battle",entity,system);
		this.maxhp = maxhp;
		this.hp = maxhp;
		this.maxmp = maxmp;
		this.mp = maxmp;
		this.atk = atk;
		this.def = def;
		this.maxspd = maxspd;
		this.spd = maxspd;
		this.dex = dex;
		this.sprite = sprite;
	}
	
	public Component_Battle(Component_Battle compBattle) {
		super(compBattle);
		this.maxhp = compBattle.maxhp;
		this.hp = compBattle.hp;
		this.maxmp = compBattle.maxmp;
		this.mp = compBattle.mp;
		this.atk = compBattle.atk;
		this.def = compBattle.def;
		this.maxspd = compBattle.def;
		this.spd = compBattle.spd;
		this.dex = compBattle.dex;
		this.sprite = compBattle.sprite;
	}
	
	public Component_Battle(Abstract_Component comp, Entity entity, System_Component system) {
		super(comp.getType(),entity,system);
		Component_Battle compBattle = (Component_Battle) comp;
		this.maxhp = compBattle.maxhp;
		this.hp = compBattle.hp;
		this.maxmp = compBattle.maxmp;
		this.mp = compBattle.mp;
		this.atk = compBattle.atk;
		this.def = compBattle.def;
		this.maxspd = compBattle.def;
		this.spd = compBattle.spd;
		this.dex = compBattle.dex;
		this.sprite = compBattle.sprite;
	}
	
	// Getters
	
	public int getMaxHP() { return this.maxhp; }
	public int getHP() { return this.hp; }
	public int getMaxMP() { return this.maxmp; }
	public int getMP() { return this.mp; }
	public int getATK() { return this.atk; }
	public int getDEF() { return this.def; }
	public int getMaxSPD() { return this.maxspd; }
	public int getSPD() { return this.spd; }
	public int getDEX() { return this.dex; }
	public String getSprite() { return this.sprite; }
	
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
	public void setATK(int atk) { this.atk = atk; }
	public void setDEF(int def) { this.def = def; }
	public void setDEX(int dex) { this.dex = dex; }
}
