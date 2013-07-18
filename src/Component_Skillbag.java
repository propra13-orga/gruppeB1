import java.util.LinkedList;
import java.util.List;


/**
 * 
 * Entitäten, die diese Komponente besitzen, sind in der Lage, Skills
 * (Fähigkeiten) zu erlernen.
 * 
 * @author Victor Persien
 *
 */
public class Component_Skillbag extends Abstract_Component {

	private static final long serialVersionUID = 6694489409150291391L;
	
	private List<Object_Skill> skills;
	private int skillPoints;

	/**
	 * Konstruktur. Alle diese Komponenten sind vom Typ "skillbag".
	 * 
	 * @param entity		Entität, der die Komponente gehört.
	 * @param system		Zugehöriges Komponentensystem.
	 */
	public Component_Skillbag(Entity entity,
			System_Component system) {
		super("skillbag", entity, system);
		this.skillPoints = 1;
		this.skills = new LinkedList<Object_Skill>();
	}

	public Component_Skillbag(Abstract_Component comp) {
		super(comp);
	}
	
	/*
	 * Getter
	 */
	
	public List<Object_Skill> getSkills() { return this.skills; }
	public int getSkillPoints() { return this.skillPoints; }
	public boolean hasSkill(Object_Skill skill) { return this.skills.contains(skill); }
	
	/*
	 * Setter
	 */
	
	public void addSkill(Object_Skill skill) { this.skills.add(skill); }
	public void setSkillPoints(int skillPoints) { this.skillPoints = skillPoints; }
	public void addToSkillPoints(int d) { this.skillPoints += d; }
	
	/*
	 * Privates
	 */

}
