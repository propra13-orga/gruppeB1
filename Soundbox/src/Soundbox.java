/* Soundbox.java - Jukebox zum Spiel.Hier befinden sich die verschiedene Background-und Effectssound.
                   Die Methode "loadSound" bindet die ausgesuchte wave-datei an die gewünschte 
                   Funktion oder Stelle,die Methode "playSound" spielt die wave-datei einmal ab
                   an der bestimmten angesetzten stelle,die Methode "loopSound" spielt die bestimmte
                   wave-datei solange die Figur in der gegebenen Ebene ist und die Methode"stopSound"
                   beendet den Sound an der letzte Stelle der ebene bzw. beim betretetn der neuen Ebenen
 */


import java.applet.*;
import java.net.*;
import java.util.*;

public class Soundbox {
	
	Hashtable<String, AudioClip> sounds; // AudioClips werden in den HashTable gespeichert
	Vector<AudioClip> lClips;            // 
	
	public Soundbox () {
		sounds = new Hashtable<String, AudioClip>();
		lClips = new Vector<AudioClip>();
	}
	
	public void loadSound(String name,String path) {
		
		if (sounds.containsKey(name)) {
			return;
		}
		
		URL sound_url = getClass() .getClassLoader() .getResource(path);
		sounds.put(name, (AudioClip)Applet.newAudioClip(sound_url));
		
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
	 
	 
	 
	 
	public static void main(String[] args) {
		Soundbox soundbox;
		soundbox = new Soundbox();
		
		soundbox.loadSound("level_1", "src/xfiles.wav");            // Musik beim befinden bei den
		soundbox.loadSound("level_2", "src/forest.wav");           //   gegebenen level */
		soundbox.loadSound("level_3", "src/piratestheme1.wav");
		
		soundbox.loadSound("checkpoint", "src/End.wav"); // Musik beim erreichen eines Checkpoints
		
		soundbox.loadSound("item", "src/magic.wav");      /* Musik beim aufheben von items
		                                            und bei einahme von zauber*/
		
		soundbox.loadSound("gun_vs_enemy","src/punch.wav"); // Musik beim benutzen der Waffen gegen gegner;
		
		
		soundbox.loadSound("shop", "src/shop.mid");      // Musik beim betreten vom shop;
		
		soundbox.loadSound("boss_1+2", "src/boss12.wav");     // Musik zum Endgegner Level1 und Level2
		soundbox.loadSound("boss_3", "src/destroy.wav");             // Musik zum Endgener Level3
				
		soundbox.loadSound("trap", "src/Jump.wav"); // Musik beim berühren von Fallen
		
		soundbox.loadSound("end_game", "src/jpark.wav"); // Musik beim erfolgreichen beenden des spiels
		
	/* soundbox.loadSound("andere soundfaktoren","projektpfad");
	   soundbox.loadSound("...","...");*/

	}

}
/* Beim Abspielen vom sound in level 1/2/3 wir der in der StartGame klasse wird der Befehl:
  soundbox.loopSound("level_1/level_2/level_3"). Man beachte aber das man den sound beendet bevor 
  betreten der neuen ebenen mit: soundbox.stopSound().
  
 Genau den selben vorgang beim dem Shop,den Endgegnern und dem erfolgreichen Beenden des Spiels.
 
 Die anderen Sounds da sie nur einmal abgespielt werden also bei ereignissen werden mit dem Befehl:
 soundbox.playSound("trap/checkpoint/.../") in den jeweils Spielsituation abgespielt.
                                                                            */
 