import java.applet.*;
import java.io.File;
import java.net.*;
import java.util.*;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequencer;

public class Object_SoundManager {
	
	Hashtable<String, AudioClip> sounds; // AudioClips werden in den HashTable gespeichert
	Vector<AudioClip> lClips;            // 
	
	public Object_SoundManager () {
		sounds = new Hashtable<String, AudioClip>();
		lClips = new Vector<AudioClip>();
	}
	
	public void loadSound(String name,String path) {
		
		if (sounds.containsKey(name)) {
			return;
		}
		
		URL sound_url = getClass().getClassLoader().getResource(path);
		sounds.put(name, (AudioClip) Applet.newAudioClip(sound_url));
		
	}

	@SuppressWarnings("deprecation")
	public void playSound2(String name) {
		File f = new File("res/sound/"+name+".wav");
		AudioClip sound;
		try {
			sound = Applet.newAudioClip( f.toURL() );
			sound.play();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	 public void playSound(String name) {
		 AudioClip audio = sounds.get(name);
		 audio.play();
	 }
	
	 public void loopSound(String name) {
		 AudioClip audio = sounds.get(name);
		 lClips.add(audio);
		 audio.loop();
	 }

	 
	 public void stopSound() {
		 for(AudioClip c:lClips) {
			 c.stop();
		 }
	 }
	 
	 public void playMidi(String filename) {
		 Sequencer sequencer;
		try {
			sequencer = MidiSystem.getSequencer();
			sequencer.open();
			//InputStream midiFile = ClassLoader.getSystemResourceAsStream( "res/music/"+filename+".mid" );
			File input = new File("res/music/"+filename+".mid");
			sequencer.setSequence( MidiSystem.getSequence(input) );
			sequencer.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }
}