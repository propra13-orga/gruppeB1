
import java.io.*;
import java.net.*;

public class TestServer extends Thread {

       private Socket client;
       
       public TestServer(Socket client) {
    	   this.client = client;
       }
       
       public void run() {
    	   BufferedReader in = null;
    	   PrintWriter out = null;
    	   
    	   try {
    		in = new BufferedReader(new InputStreamReader(client.getInputStream()));
    		out = new PrintWriter(client.getOutputStream(), true);
    		
    		out.println("Guten Tag, die ist der beantragte TestServer");
    		
    		String input;
    		input = in.readLine();
    		while(input != null) {
    			out.println(input);
       		} 
    	   }catch (IOException e) {
       			System.err.println(e);
             } 
       		
       		finally {
       			try {
       				if (client != null)
       					client.close();
       				if (in != null);
       				    in.close();
       				if (out != null);
       				  out.close();
       			} catch (IOException e){}
       		  }
       		}
    		
    	   
       
	public static void main(String[] args) {
		if(args.length != 1) {
			System.err.println("Bitte geben sie ein: java TestServer <port>");
			System.exit(1);
		}
		
		int port = 0;
		try {
			port = Integer.parseInt(args[0]);
		} catch (NumberFormatException e) {
			System.err.println("Ungueltige Portnummer");
			System.exit(1);
		}
		
		try {
			 ServerSocket server = new ServerSocket(port);
			 System.out.println("Der TestServer wird auf "+ port +" gestartet...");
			 while (true) {
				 
				 Socket client = server.accept();
				 new TestServer(client).start();
			 }
		} catch (IOException e) {
			System.err.println(e);
			
				 

			 }
			 
		}

	

}

