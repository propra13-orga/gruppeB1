import java.util.HashMap;
import java.util.Map;

/**
 * 
 * Diese Komponente stellt das Equipment (die Ausrüstung) eines Spielers dar,
 * das (die) gerade angelegt ist. Jedes Item ist dabei einem Slot zugewiesen,
 * wobei die Slots den Körperteilen der Entität entsprechen.
 * 
 * @author Victor Persien
 *
 */
public class Component_Equipment extends Abstract_Component {

	private static final long serialVersionUID = -6579517022715989440L;
	
	public final static String[] SLOTS = {"slot_arm_left", "slot_arm_right",
			"slot_breast", "slot_legs", "slot_head", "slot_ring",
			"slot_necklace"};
	
	private Map<String,Entity> equipment;

	/**
	 * Konstruktur. Alle diese Komponenten sind vom Typ "equipment".
	 * 
	 * @param entity		Entität, der die Komponente gehört.
	 * @param system		Zugehöriges Komponentensystem.
	 */
	public Component_Equipment(Entity entity,
			System_Component system) {
		super("equipment", entity, system);
		this.equipment = new HashMap<String,Entity>();
	}

	public Component_Equipment(Abstract_Component comp) {
		super(comp);
	}
	
	/*
	 * Getter
	 */
	
	public Entity getHead() {
		return this.equipment.get("slot_head");
	}

	public Entity getBreast() {
		return this.equipment.get("slot_breast");
	}

	public Entity getArmLeft() {
		return this.equipment.get("slot_arm_left");
	}

	public Entity getArmRight() {
		return this.equipment.get("slot_arm_right");
	}

	public Entity getLegs() {
		return this.equipment.get("slot_legs");
	}

	public Entity getRing() {
		return this.equipment.get("slot_ring");
	}

	public Entity getNecklace() {
		return this.equipment.get("slot_necklace");
	}
	
	public Map<String,Entity> getEquipment() {
		return this.equipment;
	}
	
	public Entity getEquipment(String slot) {
		if (this.equipment.containsKey(slot)) {
			return this.equipment.get(slot);
		}
		return null;
	}
	
	/*
	 * Setter
	 */

	public void setHead(Entity head) {
		this.equipment.put("slot_head", head);
	}

	public void setBreast(Entity breast) {
		this.equipment.put("slot_breast", breast);
	}

	public void setArmLeft(Entity arm_left) {
		this.equipment.put("slot_arm_left", arm_left);
	}

	public void setArmRight(Entity arm_right) {
		this.equipment.put("slot_arm_right", arm_right);
	}

	public void setLegs(Entity legs) {
		this.equipment.put("slot_legs", legs);
	}

	public void setRing(Entity ring) {
		this.equipment.put("slot_ring", ring);
	}

	public void setNecklace(Entity necklace) {
		this.equipment.put("slot_necklace", necklace);
	}
	
	public void setEquipment(String slot, Entity item) {
		this.equipment.put(slot, item);
	}

}