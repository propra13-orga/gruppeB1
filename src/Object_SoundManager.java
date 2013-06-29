import java.applet.*;
import java.io.File;
import java.net.*;
import java.util.Hashtable;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequencer;

public class Object_SoundManager {
	
	public static final boolean				MUTED = false;
	
	private Hashtable<String, AudioClip>	sounds;
	
	Object_SoundManager() {
		this.sounds = new Hashtable<String, AudioClip>();
		loadSound("cancel");
		loadSound("cursor");
		loadSound("decision");
		loadSound("invalid");
		loadSound("animation-sound");
	}
	
	@SuppressWarnings("deprecation")
	private void loadSound(String name) {
		File f = new File("res/sound/"+name+".wav");
		AudioClip sound;
		try {
			sound = Applet.newAudioClip( f.toURL() );
			this.sounds.put(name, sound);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void playSound(String name) {
		if (!MUTED) {
			if (this.sounds.containsKey(name)) {
				this.sounds.get(name).play();
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