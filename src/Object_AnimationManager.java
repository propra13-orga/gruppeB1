import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;


public class Object_AnimationManager extends Abstract_Update {

	private long						counter;
	private int							fade_alpha;
	private int							fade_delta;
	private int							fade_delay;
	private int							fade_tick;
	private boolean						fading;
	private ArrayList<Object_Animation>	animations;
	
	Object_AnimationManager(Object_Game game) {
		super(game);
		this.counter	= 0;
		this.fade_alpha	= 0;
		this.fade_delta	= 0;
		this.fading		= false;
		this.animations	= new ArrayList<Object_Animation>();
	}

	@Override
	public void updateData() {
		if (this.fading) {
			this.fade_tick++;
			if (this.fade_tick >= this.fade_delay) {
				this.fade_tick = 0;
				this.fade_alpha += this.fade_delta;
				if (this.fade_alpha < 0) this.fade_alpha = 0;
				if (this.fade_alpha > 255) this.fade_alpha = 255;
				if (this.fade_alpha == 0 || this.fade_alpha == 255) {
					this.fading = false;
				}
			}
		}
		for (Object_Animation a : this.animations) {
			a.updateData();
		}
		for (int i=0; i<this.animations.size(); i++) {
			if (this.animations.get(i).isDone()) {
				this.animations.remove(i);
			}
		}
	}

	@Override
	public void updateScreen() {
		for (Object_Animation a : this.animations) {
			a.updateScreen();
		}
		this.screen.setColor(new Color(0,0,0,this.fade_alpha));
		this.screen.fillRect(0, 0, 640, 480);
	}
	
	public void playAnimation(String filename, int delay, int x, int y) {
		try {
			String							setname;
			String							tmp;
			BufferedImage					set;
			ArrayList<ArrayList<String[]>>	frames;
			ArrayList<String[]>				current_frame;
			
			FileReader						fr = new FileReader("res/animation/"+filename+".txt");
			BufferedReader					br = new BufferedReader(fr);
			
			//Loading animation set
			setname = br.readLine();
			set = new BufferedImage(480, 480, BufferedImage.TYPE_INT_ARGB);
			try {
				set.getGraphics().drawImage(ImageIO.read(new File("res/animationset/"+setname+".png")),0,0,null);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Object_Screen.makeTransparent(set);
			
			//Creating frames
			frames = new ArrayList<ArrayList<String[]>>();
			current_frame = new ArrayList<String[]>();
			tmp = br.readLine();
			while (!tmp.equals("!")) {
				while (tmp.length() != 0) {
					current_frame.add(tmp.split(" "));
					tmp = br.readLine();
				}
				frames.add(current_frame);
				current_frame = new ArrayList<String[]>();
				tmp = br.readLine();
			}
			
			this.counter++;
			Object_Animation ani = new Object_Animation(this.game, this.counter, x, y, delay, frames, set);
			this.animations.add(ani);
			
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void fadeIn(int delay, int delta) {
		this.fade_alpha = 255;
		this.fade_delta = (-1) * delta;
		this.fade_delay = delay;
		this.fade_tick = 0;
		this.fading = true;
	}
	
	public void fadeOut(int delay, int delta) {
		this.fade_alpha = 0;
		this.fade_delta = delta;
		this.fade_delay = delay;
		this.fade_tick = 0;
		this.fading = true;
	}
	
	public boolean isFading() {
		return this.fading;
	}
	
	public boolean isAnimationExecuted(long id) {
		if (this.animations.size() == 0) {
			return false;
		}
		for (Object_Animation animation : this.animations) {
			if (animation.getID() == id) {
				return true;
			}
		}
		return false;
	}

}
