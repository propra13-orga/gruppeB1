
public class Static_DamageCalculator {
//	private static final double BONUS = 2.0;
//	private static final double MALUS = 0.5;

	
	/*
	 * Berechnet den Schaden, den actor_defender erhalten soll.
	 */
	public static int calcDMG(Object_BattleActor actor_attacker, Object_BattleActor actor_defender, Object_Skill skill) {
		Entity attacker = actor_attacker.getEntity();
		Entity defender = actor_defender.getEntity();
		Component_Equipment compEquipment_a = (Component_Equipment) attacker.getComponent("equipment");
		Component_Equipment compEquipment_d = (Component_Equipment) defender.getComponent("equipment");
		
		int dmg = calcElementDMG("dmg", null, compEquipment_a, skill);
		int dmg_ice = calcElementDMG("dmg", "ice", compEquipment_a, skill);
		int dmg_fire = calcElementDMG("dmg", "fire", compEquipment_a, skill);
		int dmg_earth = calcElementDMG("dmg", "earth", compEquipment_a, skill);
		
		int armor = calcElementDMG("armor", null, compEquipment_d, null);
		int armor_ice = calcElementDMG("armor", "ice", compEquipment_d, null);
		int armor_fire = calcElementDMG("armor", "fire", compEquipment_d, null);
		int armor_earth = calcElementDMG("armor", "earth", compEquipment_d, null);
		
		double outdmg = 0.0;
		
		outdmg += dmgVsArmor(dmg, armor) + dmgVsArmor(dmg_ice, armor_ice) + dmgVsArmor(dmg_fire, armor_fire) + dmgVsArmor(dmg_earth, armor_earth);
		
		
		return (int) outdmg;
	}
	
	
	/*
	 * Berechnet Schadens- oder Ruestungswert anhand des Equipments.
	 */
	private static int calcElementDMG(String armordmg, String element, Component_Equipment compEquipment, Object_Skill skill) {
		int value = 0;
		Integer dvalue;
		String prop = armordmg;	// armordmg ist entweder "dmg" oder "armor".
		if (element != null) {
			prop += "_"+element;		
		}
		if (skill == null) {
			
			for (Entity item : compEquipment.getEquipment().values()) {
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
	
	/*
	 * Zieht vom Schadens- den Ruestungswert ab.
	 */
	private static double dmgVsArmor(double dmg, double armor) {
		double out = dmg-armor;
		if (out < 0.0) return 0.0;
		return out;
	}
	
}
