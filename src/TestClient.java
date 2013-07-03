
/* Habe ich mich mit zwei varianten bei der Netwerkprogrammierung entweder:
   1. Client/Server Methode : Client macht anfrage auf ein Socket(server) und er verarbeitet die
                              und gibt die zurück
                              
   oder
   
   2. Download Methode: Bei Jedem Spielstand kann sich der Zweite Spieler den Spielstand
                        vom anderem herunterladen und diese position wird angezeigt
 */

/* Dies ist die Client/Server Methode mit 2 Codes: TestClient.java +  TestServer.java`
   Hier habe ich es erstmal mit so ner art Chat-eingabe system versucht, ist auf jedenfall 
   ausbaufähig
 */


import java.io.*;
import java.net.*;

public class TestClient {
  public static void main(String[] args) {
	  if (args.length != 2) {
		  System.err.println("Bitte geben sie ein: java TestClient <host> <port>"); 
		  System.exit(1);
	  }
	  
	  String host = args[0];
	  int port = 0;
	  try {
		  port = Integer.parseInt(args[1]);
	  } catch (NumberFormatException e) {
		  System.err.println("Ungueltige Portnummer");
		  System.exit(1);
	  }
	  
	  try {
		  Socket socket1 = new Socket(host,port); // Aufbau der Verbindung zum Server
		  
		  BufferedReader in = new BufferedReader(new InputStreamReader(socket1.getInputStream()));
		  /* Hier wird der Eingabestrom erstellt"getInputStream"
		  für den spaeteren Socket1(Serverempfaenger)*/
		  
		  PrintWriter out = new PrintWriter(socket1.getOutputStream(),true);
		  /*Hier wird der Ausgabestrom erstellt "getOutputStream"
		  für den spaeteren Socket1(Serverempfaenger)*/
		  
		  System.out.println(in.readLine()); // Server begruesst
		  
		  BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		  
		  System.out.print("> ");                            // Hier werden die Ausgaben
		  String line;                                       // vom Socket1(Serverempfaenger)
		  line = input.readLine();                           // bei erfolgreichen antreffen
		  while (line != null){                              // der vom Client gestellten
			  if (line.length() == 0)                        // Anfrage am Terminal
				  break;                                     // koordiniert.......
			                                                 //...............
			  out.println(line);                             //...........
			  System.out.println("Antwort vom Server: ");    //........
			  System.out.println(in.readLine());             //.......
			  System.out.print("> ");                        //....
			 }
		  
		  socket1.close(); // Serverempfaenger wird geschlossen
		  in.close();      // Eingabestrom wird eingestellt
		  out.close();     // Ausgabestrom wird eingestellt	  
	    } catch (Exception e) {
	    	System.err.println(e);
	    }
  }
  


}
