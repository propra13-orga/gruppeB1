import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;


public class Object_AnimationManager extends Abstract_Update {

	private long					counter;
	private ArrayList<Animation>	animations;
	
	Object_AnimationManager(Object_Game game) {
		super(game);
		this.counter	= 0;
		this.animations	= new ArrayList<Animation>();
	}

	@Override
	public void updateData() {
		for (Animation a : this.animations) {
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
		for (Animation a : this.animations) {
			a.updateScreen();
		}
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
			Animation ani = new Animation(this.game, this.counter, x, y, delay, frames, set);
			this.animations.add(ani);
			
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean isAnimationExecuted(long id) {
		if (this.animations.size() == 0) {
			return false;
		}
		for (Animation animation : this.animations) {
			if (animation.getID() == id) {
				return true;
			}
		}
		return false;
	}

}
