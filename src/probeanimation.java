import java.awt.Image;
import java.util.ArrayList;

public class probeanimation {

	  private ArrayList scenes;
	  private int sceneIndex;
	  private long movieTime;
	  private long totalTime;
	  
	 
	  // Konsktruktor
	  public probeanimation(){
		  scenes= new ArrayList();
		  totalTime = 0;
		  start ();
		 
	  }
	// Hinzufügen von neuen Szenen und Festlegen von der Dauer des Ablaufs einer Scene  
	public synchronized void addScene(Image i,long t) {
		
		totalTime += t;
		scenes.add(new Scene (i, totalTime));
	}

	
	//startet die Animation von neu.
	//synchronized macht es möglich, dass nicht zwei Scenen zu einem zeitpunkit laufen
	public synchronized void start() {
		movieTime = 0;
		sceneIndex = 0;
	}
		
	//wechseln von Scenen
	public synchronized void update (long timePassed){
		
		if(scenes.size()> 1){
			movieTime += timePassed;
			if (movieTime >= totalTime){
				movieTime = 0;
				sceneIndex = 0;
				
				}
				
	while (movieTime > get(sceneIndex).endTime){
			   sceneIndex++;
		
				
		}
	}
	
}

// get animation current scene (aka image)

private Object get(int sceneIndex2) {
		// TODO Auto-generated method stub
		return null;
	}
public synchronized Image getImage(){
	
   if (scenes.size()== 0){
	return null;
   }else{
	
	   return getScene(sceneIndex).pic;

  }
}


private Abstract_Scene getScene(int x){
	 return (Abstract_Scene) scenes.get (x);
	 
	 
	 
	 
	 private class Abstract_Scene {
		 Image pic;
		 long endTime;
		 
public  Abstract_Scene(Image pic, long endTime){
			 
			this.pic = pic;
			this.endTime= endTime;
			
			
		 }
	 }
}

}