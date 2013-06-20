import java.applet.*;
import java.io.File;
import java.net.*;
import java.util.*;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequencer;

public class Object_SoundManager {
	
	Hashtable<String, AudioClip> sounds;       // AudioClip wird in einer Hashtable gespeichert
	Vector<AudioClip> lClips;                  // AudioClip wird in einem Nutzungsvektor gespeichert
	
	public Object_SoundManager () {
		sounds = new Hashtable<String, AudioClip>();  // Werden an "sounds" und "lClips"
		lClips = new Vector<AudioClip>();             // attribuiert
	}
	
	public void loadSound(String name,String path) {           // Methode zum Laden der Sounds
		                                                       // in das spiel
		if (sounds.containsKey(name)) {
			return;
		}
		
		URL sound_url = getClass().getClassLoader().getResource(path); // Der Sound wird mit Hilfe
		sounds.put(name, (AudioClip) Applet.newAudioClip(sound_url)); // von sound_url lokalisiert
		                                                              // und mit sound.put zur
		                                                              // zu verfügung gestellt
	}

	@SuppressWarnings("deprecation")
	public void playSound2(String name) {             // beim benutzen der Methode playSound2
		File f = new File("res/sound/"+name+".wav");  // werden Sound-Wave-dateien abgespielt
		AudioClip sound;
		try {
			sound = Applet.newAudioClip( f.toURL() );
			sound.play();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	 public void playSound(String name) {      // Methode die den Sound einmal abspielt
		 AudioClip audio = sounds.get(name);
		 audio.play();
	 }
	
	 public void loopSound(String name) {        //Methode die den Sound in einer Schleife
		 AudioClip audio = sounds.get(name);     // wieder gibt
		 lClips.add(audio);
		 audio.loop();
	 }

	 
	 public void stopSound() {             // Methode die den Abspiel des Soundes abbricht
		 for(AudioClip c:lClips) {
			 c.stop();
		 }
	 }
	 
	 public void playMidi(String filename) {      //beim benutzen von der Methode playMidi
		 Sequencer sequencer;                     // werden Sound-Midi-dateien abgespielt
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