
import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;
import javax.swing.JApplet;


public class GL extends JApplet {
	
	public static AudioClip Hmusik;
	
	public void init() {
		
		Hmusik = Applet.newAudioClip(Get_Location("src/jack_in_the_box_fast-mike_koenig-1288903495.au"));
		Hmusik.play();
		
		}

	
	public URL Get_Location(String filename) {
		
		URL url = null;
		try {
			url = this.getClass().getResource(filename);
		} catch (Exception e) {}
		return url;
		}

	}


