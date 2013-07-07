import java.util.LinkedList;
import java.util.List;


public class Scene_QuestMenu extends Abstract_Scene {
	
	private Scene_Level parent;
	private Window_Menu main_menu;
	private Window_Message current_message;
	
	private Entity dealer;
	private Entity adventurer;
	
	private List<Object_Quest> quests;
	private List<Window_Message> messages;

	public Scene_QuestMenu(Object_Game game, Scene_Level parent,
			Entity dealer, Entity adventurer) {
		super(game);
		this.parent = parent;
		this.dealer = dealer;
		this.adventurer = adventurer;
		
		this.main_menu = new Window_Menu(game,"main");
		this.main_menu.topLeft();
		
		this.messages = new LinkedList<Window_Message>();
		
		Component_Questdealer compQuestdealer = (Component_Questdealer) this.dealer.getComponent("questdealer");
		Component_Questbag compQuestbag = (Component_Questbag) this.adventurer.getComponent("questbag");
		
		this.quests = compQuestdealer.getQuests();
		
		boolean isNew;
		String description;
		for (Object_Quest quest : this.quests) {
			isNew = false;
			if (!compQuestbag.hasQuest(quest)) isNew = true;
			this.main_menu.addReturnCommand(quest.getName(), !isNew);
			
			description = this.prepareDescription(quest);
//			this.messages.add(new Window_Message(description,Object_Screen.SCREEN_W,Object_Screen.SCREEN_H,this.game));
			this.messages.add(new Window_Message(description,Object_Screen.SCREEN_W-Window_Message.WIDTH,0,this.game));
			
		}
		
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
		//
	}

	@Override
	public void updateData() {
		if (this.main_menu.isExecuted()) {
			main_menu.updateData();
			this.main_menu.setupMenuPath();
			int cursor = this.main_menu.getCurrentCursor();
			if (cursor < this.quests.size()) {
				this.current_message = this.messages.get(cursor);
			}
			else this.current_message = null;
		}
		else {
			if (this.main_menu.isCanceled()) {
				this.keyhandler.clear();
				this.keyhandler.freeze(Object_KeyHandler.KEY_ESCAPE, 40);
				this.game.switchScene(this.parent, true);
				return;
			}
			else {
				this.main_menu.setupMenuPath();
				int cursor = this.main_menu.getCurrentCursor();
				if (cursor < this.quests.size() && this.main_menu.isEnabled(cursor)) {
					this.dealQuest(cursor);
					this.main_menu.disableCommand(cursor);
				}
				else {
					this.keyhandler.freeze(Object_KeyHandler.KEY_ESCAPE, 40);
					this.game.switchScene(this.parent, true);
					return;
				}
				
				
				this.main_menu.getLastMenu().restart();
			}
		}
	}

	@Override
	public void updateScreen() {
		this.main_menu.updateScreen();
		if (this.current_message != null) {
			this.current_message.updateScreen();
		}
	}
	
	
	private void dealQuest(int id) {
		Component_Questbag compQuestbag = (Component_Questbag) this.adventurer.getComponent("questbag");
		compQuestbag.addQuest(this.quests.get(id));
	}
	
	private String prepareDescription(Object_Quest quest) {
		String d = String.format("%s\n\n%s\n\nXP: %d", quest.getName(),quest.getDescription(),quest.getXP());
		return d;
	}
}
