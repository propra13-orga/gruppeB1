
public class Object_DamageCalculator {
	private static final double BONUS = 2.0;
	private static final double MALUS = 0.5;

	public Object_DamageCalculator() {
		
	}
	
	
	public int calcDMG(Object_BattleActor actor_attacker, Object_BattleActor actor_defender, Object_Skill skill) {
		Entity attacker = actor_attacker.getEntity();
		Entity defender = actor_defender.getEntity();
		Component_Equipment compEquipment_a = (Component_Equipment) attacker.getComponent("equipment");
		Component_Equipment compEquipment_d = (Component_Equipment) defender.getComponent("equipment");
		
		int dmg = this.calcElementDMG("dmg", null, compEquipment_a, skill);
		int dmg_ice = this.calcElementDMG("dmg", "ice", compEquipment_a, skill);
		int dmg_fire = this.calcElementDMG("dmg", "fire", compEquipment_a, skill);
		int dmg_earth = this.calcElementDMG("dmg", "earth", compEquipment_a, skill);
		
		int armor = this.calcElementDMG("armor", null, compEquipment_d, null);
		int armor_ice = this.calcElementDMG("armor", "ice", compEquipment_d, null);
		int armor_fire = this.calcElementDMG("armor", "fire", compEquipment_d, null);
		int armor_earth = this.calcElementDMG("armor", "earth", compEquipment_d, null);
		
		double outdmg = 0.0;
		
		
		
		return 0;
	}
	
	private int calcElementDMG(String armordmg, String element, Component_Equipment compEquipment, Object_Skill skill) {
		int value = 0;
		Integer dvalue;
		String prop = armordmg;	// armordmg ist entweder "dmg" oder "armor".
		if (element != null) {
			prop += "_"+element;		
		}
		if (skill == null) {
			Entity[] equipment = compEquipment.getEquipment();
			
			for (Entity item : equipment) {
				if (item != null) {
					Component_Item compItem = (Component_Item) item.getComponent("item");
					dvalue = compItem.getEffectValue(prop);
					if (dvalue != null) value += dvalue;
				}
			}			
		}
		else if (skill.hasProperty(prop)) {
			value += skill.getProperty(prop);
		}
		
		return value;
	}
	
	private double dmgVsArmor(double dmg, double armor) {
		double out = dmg-armor;
		if (out < 0.0) return 0.0;
		return out;
	}
	
}
