
public class Component_Equipment extends Abstract_Component {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6579517022715989440L;
	
	private Entity head;
	private Entity breast;
	private Entity arm_left;
	private Entity arm_right;
	private Entity legs;
	private Entity ring;
	private Entity necklace;

	public Component_Equipment(Entity entity,
			System_Component system) {
		super("equipment", entity, system);
	}

	public Component_Equipment(Abstract_Component comp) {
		super(comp);
	}
	
	/*
	 * Getter
	 */
	
	public Entity getHead() {
		return head;
	}

	public Entity getBreast() {
		return breast;
	}

	public Entity getArmLeft() {
		return arm_left;
	}

	public Entity getArmRight() {
		return arm_right;
	}

	public Entity getLegs() {
		return legs;
	}

	public Entity getRing() {
		return ring;
	}

	public Entity getNecklace() {
		return necklace;
	}
	
	public Entity[] getEquipment() {
		Entity[] equipment = {this.arm_left,this.arm_right,this.breast,this.head,this.legs,this.necklace,this.ring};
		return equipment;
	}
	
	/*
	 * Setter
	 */

	public void setHead(Entity head) {
		this.head = head;
	}

	public void setBreast(Entity breast) {
		this.breast = breast;
	}

	public void setArmLeft(Entity arm_left) {
		this.arm_left = arm_left;
	}

	public void setArmRight(Entity arm_right) {
		this.arm_right = arm_right;
	}

	public void setLegs(Entity legs) {
		this.legs = legs;
	}

	public void setRing(Entity ring) {
		this.ring = ring;
	}

	public void setNecklace(Entity necklace) {
		this.necklace = necklace;
	}

}