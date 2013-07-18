import java.util.LinkedList;
import java.util.List;


public class Scene_SkillMenu extends Abstract_Scene {
	
//	private Abstract_Scene parent;
	private Scene_Level current_level;
	private Entity adventurer;
	private Factory factory;
	
	private Window_Menu main_menu;
	private Window_Props current_message;
	private Window_Message message_skillPoints;
	
	private List<Object_Skill> skills;
	private List<Window_Props> messages;
	
	private int skillPoints;
	
	private final String[] skillnames = {"fireball","fastheal","earthfist",
			"eisblitz","fastmanaheal"};

	public Scene_SkillMenu(Object_Game game, Abstract_Scene parent,
			Scene_Level current_level, Entity player, Factory factory) {
		super(game);
//		this.parent = parent;
		this.current_level = current_level;
		this.adventurer = player;
		this.factory = factory;
		this.main_menu = new Window_Menu(game,"main");
		this.main_menu.topLeft();
		
		this.skills = new LinkedList<Object_Skill>();
		this.initSkills();
		
		this.messages = new LinkedList<Window_Props>();
		this.initMessages();
		
		this.skillPoints = this.retrieveSkillPoints();
		String msg = String.format("Skillpkt.: %d", this.skillPoints);
		this.message_skillPoints = new Window_Message(msg,Object_Screen.SCREEN_W-180,430,180,this.game);
		
		this.initMenu();
		
		this.main_menu.addCancelCommand("Beenden");
		Window_Menu.setMainMenu(this.main_menu);
		this.main_menu.setExitPossible(true);
	}

	@Override
	public void onStart() {
		//
	}

	@Override
	public void onExit() {
		Component_Skillbag compSkillbag = (Component_Skillbag) this.adventurer.getComponent("skillbag");
		compSkillbag.setSkillPoints(this.skillPoints);
	}

	@Override
	public void updateData() {
		this.main_menu.setupMenuPath();
		if (this.main_menu.isExecuted()) {
			this.main_menu.updateData();
			int cursor = this.main_menu.getCurrentCursor();
			if (cursor < this.skills.size()) {
				this.current_message = this.messages.get(cursor);
			}
			else this.current_message = null;
		}
		else {
			if (this.main_menu.isCanceled()) {
				this.keyhandler.clear();
				this.keyhandler.freeze(Object_KeyHandler.KEY_ESCAPE, 40);
				this.game.switchScene(this.current_level, true);
				return;
			}
			else {
				
				int cursor = this.main_menu.getCurrentCursor();
				if (cursor < this.skills.size() && this.main_menu.isEnabled(cursor)) {
					this.dealSkill(cursor);
					this.main_menu.getLastMenu().restart();
				}
				else {
					this.keyhandler.clear();
					this.keyhandler.freeze(Object_KeyHandler.KEY_ENTER, 40);
					this.game.switchScene(this.current_level, true);
					return;
				}
				
				
			}
		}

	}

	@Override
	public void updateScreen() {
		this.main_menu.updateScreen();
		this.message_skillPoints.updateScreen();
		if (this.current_message != null) {
			this.current_message.updateScreen();
		}

	}
	
	private void dealSkill(int pos) {
		if (this.skillPoints > 0) {
			this.skillPoints -= 1;
			Component_Skillbag compSkillbag = (Component_Skillbag) this.adventurer.getComponent("skillbag");
			compSkillbag.addSkill(this.skills.get(pos));
			this.main_menu.disableCommand(pos);
			this.updateMessageSkillPoints();
		}
	}
	
	private void initSkills() {
		Object_DBReader db_skl = this.factory.getDBSKL();
		for (String skillname : this.skillnames) {
			this.skills.add(new Object_Skill(db_skl.getProperties(skillname)));
		}
	}
	
	private void initMessages() {
		String s_head;
		String s_foot = "";
		for (Object_Skill skill : this.skills) {
			s_head = skill.getName();
			this.messages.add(new Window_Props(s_head,s_foot,Window_Props.intMapToString(skill.getProperties()),Object_Screen.SCREEN_W-300,0,this.factory,this.game));
		}
	}
	
	private void initMenu() {
		Component_Skillbag compSkillbag = (Component_Skillbag) this.adventurer.getComponent("skillbag");
		boolean hasSkill;;
		for (Object_Skill skill : this.skills) {
			hasSkill = compSkillbag.hasSkill(skill);
			this.main_menu.addReturnCommand(skill.getName(), hasSkill);
		}
	}
	
	private int retrieveSkillPoints() {
		Component_Skillbag compSkillbag = (Component_Skillbag) this.adventurer.getComponent("skillbag");
		return compSkillbag.getSkillPoints();
	}
	
	private void updateMessageSkillPoints() {
		String msg = String.format("Skillpkt.: %d", this.skillPoints);
		this.message_skillPoints.changeMessage(msg);
	}
}
