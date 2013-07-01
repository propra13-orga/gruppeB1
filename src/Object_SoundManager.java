import java.io.File;
import java.util.Hashtable;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequencer;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

public class Object_SoundManager {
	
	public static final boolean				MUTED = false;
	
	private Hashtable<String, Clip>	sounds;
	
	Object_SoundManager() {
		this.sounds = new Hashtable<String, Clip>();
		loadSound("cancel");
		loadSound("cursor");
		loadSound("decision");
		loadSound("invalid");
		loadSound("animation-sound");
	}

	public void loadSound(String name) {
		try{
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("res/sound/"+name+".wav"));
            AudioFormat af     = audioInputStream.getFormat();
            int size      = (int) (af.getFrameSize() * audioInputStream.getFrameLength());
            byte[] audio       = new byte[size];
            DataLine.Info info      = new DataLine.Info(Clip.class, af, size);
            audioInputStream.read(audio, 0, size);
            
           // for(int i=0; i < 32; i++) {
                Clip clip = (Clip) AudioSystem.getLine(info);
                clip.open(af, audio, 0, size);
                this.sounds.put(name, clip);
           // }
        }catch(Exception e){ e.printStackTrace(); }
	}
	
	public void playSound(String name) {
		if (!MUTED) {
			if (this.sounds.containsKey(name)) {
				this.sounds.get(name).start();
				this.sounds.get(name).setFramePosition(0);
			}
		}
	}

	public void playMidi(String filename) {
		Sequencer sequencer;
		try {
			sequencer = MidiSystem.getSequencer();
			sequencer.open();
			File input = new File("res/music/"+filename+".mid");
			sequencer.setSequence( MidiSystem.getSequence(input) );
			sequencer.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}