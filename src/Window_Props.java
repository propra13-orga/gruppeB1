import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class Window_Props extends Abstract_Update {
	
	private Window_Message head;
	private Window_Message body;
	private Window_Message foot;
	
//	private Factory factory;
	
	private String s_head;
	private String s_body;
	private String s_foot;

	public Window_Props(String s_head, String s_foot, Map<String,String> properties,
			int x, int y, Factory factory, Object_Game game) {
		this(s_head,s_foot,propertiesToString(properties,".*",factory),x,y,factory,game);
	}
	
	public Window_Props(String s_head, String s_foot, String s_body,
			int x, int y, Factory factory, Object_Game game) {
		super(game);
		this.screen.setFont(Object_Game.FONT);
//		this.factory = factory;
		
		this.s_head = s_head;
		this.s_foot = s_foot;
		this.s_body = s_body;
		
		this.head = new Window_Message(this.s_head,x,y,game);
		this.body = new Window_Message(this.s_body,x,y+this.head.height,game);
		this.foot = new Window_Message(this.s_foot,x,y+this.head.height+this.body.height,game);
	}
	
	@Override
	public void updateData() {
		//
	}

	@Override
	public void updateScreen() {
		this.head.updateScreen();
		this.body.updateScreen();
		this.foot.updateScreen();
	}

	
	private static String propertiesToString(Map<String,String> properties, String regex, Factory factory) {
		String s_body = "";
		Iterator<String> iterator = properties.keySet().iterator();
		String attr_name;
		Map<String,String> names = factory.getDBDAT().getProperties("names");
		while (iterator.hasNext()) {
			String attr = iterator.next();
			if (attr.matches(regex)) {
				if (names.containsKey(attr)) attr_name = names.get(attr);
				else attr_name = attr;
				s_body += String.format("%s: %s", attr_name, properties.get(attr));
				if (iterator.hasNext()) {
					s_body += "\n";
				}
			}
		}
		return s_body;
	}
	
	public static Map<String,String> intMapToString(Map<String,Integer> properties) {
		Map<String,String> newprops = new HashMap<String,String>();
		for (String key : properties.keySet()) {
			newprops.put(key, String.format("%d", properties.get(key)));
		}
		return newprops;
	}
}
