import java.awt.*;

import javax.swing.ImageIcon;


public class feuer {
		
	private probeanimation animation;
	
	
	//ladet Bilder 
	
	public void loadPics(){
		
		Image fire1= new ImageIcon ("C:\\animationset\\fire1.jpg.").getImage();
	    Image fire2= new ImageIcon ("C:\\animationset\\fire2.jpg.").getImage();
	    Image fire3= new ImageIcon ("C:\\animationset\\fire3.jpg.").getImage();
	    Image fire4= new ImageIcon ("C:\\animationset\\fire4.jpg.").getImage();
	    Image fire5= new ImageIcon ("C:\\animationset\\fire5.jpg.").getImage();
	    Image fire6= new ImageIcon ("C:\\animationset\\fire6.jpg.").getImage();
	    Image fire7= new ImageIcon ("C:\\animationset\\fire7.jpg.").getImage();
	    Image fire8= new ImageIcon ("C:\\animationset\\fire8.jpg.").getImage();
	    Image fire9= new ImageIcon ("C:\\animationset\\fire9.jpg.").getImage();
	    Image fire10= new ImageIcon ("C:\\animationset\\fire10.jpg.").getImage();
	    Image fire11= new ImageIcon ("C:\\animationset\\fire11.jpg.").getImage();
	    Image fire12= new ImageIcon ("C:\\animationset\\fire12.jpg.").getImage();
	    Image fire13= new ImageIcon ("C:\\animationset\\fire13.jpg.").getImage();
	    Image fire14= new ImageIcon ("C:\\animationset\\fire14.jpg.").getImage();
	    Image fire15= new ImageIcon ("C:\\animationset\\fire15.jpg.").getImage();
	    Image fire16= new ImageIcon ("C:\\animationset\\fire16.jpg.").getImage();
	    Image fire17= new ImageIcon ("C:\\animationset\\fire17.jpg.").getImage();
	    Image fire18= new ImageIcon ("C:\\animationset\\fire18.jpg.").getImage();
	    Image fire19= new ImageIcon ("C:\\animationset\\fire19.jpg.").getImage();
	      
	      
	      
	      
	      
	      
	      
	      
      
	
	     
	     
	      animation = new probeanimation ();
         
	      animation.addScene(fire1, 250);
          animation.addScene(fire2, 250) ;
          animation.addScene(fire3, 250);
          animation.addScene(fire4, 250);
          animation.addScene(fire5, 250);
          animation.addScene(fire6, 250);
          animation.addScene(fire7, 250);
          animation.addScene(fire8, 250);
          animation.addScene(fire9, 250);
           animation.addScene(fire10, 250);
            animation.addScene(fire11, 250);
             animation.addScene(fire12, 250);
              animation.addScene(fire13, 250);
               animation.addScene(fire14, 250);
                animation.addScene(fire15, 250);
                 animation.addScene(fire16, 250);
                  animation.addScene(fire17, 250);
                   animation.addScene(fire18, 250);
                    animation.addScene(fire19, 250);
          }
          
          public void run (DisplayMode dm){
        	try{ 
        	loadPics();
		    movieLoop();
        	  } finally{
        		
        	  }
        	    
          }
          
          // main movie loop
          
          public void movieLoop(){
        	  long startingTime = System.currentTimeMillis();
        	  long cumTime = startingTime;
        	  
        	  
        	  
        	  
      while(cumTime - startingTime < 5000) {
    	  long curTime = 0;
		long timePassed= System.currentTimeMillis() - curTime;
    	   cumTime += timePassed;
    	   animation.update(timePassed);
    	  
    	
    	  try{
    		  Thread.sleep (20);
    		  
    	  }catch (Exception ex){}
    	  
    	  
    		  
    	}
    	  
      }
	
          
          //draw method
          
       public void draw (Graphics g ){ 
    g.drawImage(animation.getImage(),0,0, null);
          }
	}
          
	
