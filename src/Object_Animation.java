import java.awt.image.BufferedImage;
import java.util.ArrayList;


public class Object_Animation extends Abstract_Update {

	private long								id;
	private int									x;
	private int									y;
	private int									delay;
	private int									tick;
	private int									frame_idx;
	private int									frame_len;
	private boolean								done;
	private ArrayList<ArrayList<String[]>>		frames;
	private ArrayList<String[]>					current_frame;
	private BufferedImage						set;
	
	Object_Animation(Object_Game game, long id, int x, int y, int delay, ArrayList<ArrayList<String[]>> frames, BufferedImage set) {
		super(game);
		this.id					= id;
		this.x					= x;
		this.y					= y;
		this.delay				= delay;
		this.tick				= 0;
		this.frame_idx			= 0;
		this.done				= false;
		this.frames				= frames;
		this.current_frame		= this.frames.get(0);
		this.set				= set;
		this.frame_len			= this.frames.size()-1;
	}

	@Override
	public void updateData() {
		this.tick++;
		if (this.tick == this.delay) {
			this.tick = 0;
			this.frame_idx++;
			if (this.frame_idx > this.frame_len) {
				this.done = true;
				return;
			}
			this.current_frame = this.frames.get(this.frame_idx);
		}
	}

	@Override
	public void updateScreen() {
		int id;
		int x;
		int y;
		int alpha;
		int set_x;
		int set_y;
		BufferedImage animation;
		for (String[] data : this.current_frame) {
			id		= Integer.parseInt(data[0]);
			x		= this.x + Integer.parseInt(data[1]);
			y		= this.y + Integer.parseInt(data[2]);
			alpha	= 255 - Integer.parseInt(data[3]);
			if (data.length == 5) {
				this.soundmanager.playSound(data[4]);
			}
			set_x	= (id%5)*96;
			set_y	= (id/5)*96;
			animation = this.set.getSubimage(set_x, set_y, 96, 96);
			Object_Screen.setTransparency(animation, alpha);
			this.screen.drawImage(
					animation,
					x,
					y,
					null);
		}
	}
	
	//public methods
	
	public boolean isDone() {
		return this.done;
	}
	
	public long getID() {
		return this.id;
	}

}
